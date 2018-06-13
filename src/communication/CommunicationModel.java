package communication;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mysql.fabric.xmlrpc.base.Param;

import infomation.BaseInfo;
import json.UploadInfoModel;

public class CommunicationModel {

	/**
	 * 上传信息
	 * 
	 * @param uploadInfoModel
	 *            信息模型
	 */
	public static void uploadInfo(UploadInfoModel uploadInfoModel) {

		StringBuffer params = new StringBuffer();
		params.append("macAddress=" + uploadInfoModel.getMacAddress());
		params.append("&calculation_setting=" + uploadInfoModel.getInfoSetting().getCalculation_setting());
		params.append("&disk_setting=" + uploadInfoModel.getInfoSetting().getDisk_setting());
		params.append("&bandwidth_setting=" + uploadInfoModel.getInfoSetting().getBandwidth_setting());
		params.append("&calculation_actual=" + uploadInfoModel.getInfoActual().getCalculation_actual());
		params.append("&disk_actual=" + uploadInfoModel.getInfoActual().getDisk_actual());
		params.append("&bandwidth_actual=" + uploadInfoModel.getInfoActual().getBandwidth_actual());
		params.append("&onlineTime=" + uploadInfoModel.getInfoActual().getOnlineTime());

		System.out.println("post请求的参数: " + params.toString());

		try {
			URL url = new URL("http://opencell.amongthecrowd.cn/submit/");

			byte[] response = httpPost(url, params.toString());
			if (response == null) {
				System.out.println("请求失败，返回值为空！");
				return;
			}

			String result = new String(response);
			System.out.println("post请求返回的结果: " + result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 手动更新到区块链
	 */
	public static void updateByman() {
		try {
			URL url = new URL("http://opencell.amongthecrowd.cn/update");

			byte[] response = httpGet(url);
			String result = new String(response);
			System.out.println("手动更新到区块链！结果的：" +result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询积分情况
	 */
	public static void acquirePoint() {
		//String params = "id=" + BaseInfo.getInstance().getMacAddress();
		String params = "id=kong" ;

		try {
			URL url = new URL("http://opencell.amongthecrowd.cn/acquire/");

			byte[] response = httpPost(url, params);
			String result = new String(response);
			System.out.println("查询积分的结果：" + result);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * http get请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 字节数组
	 * @throws IOException
	 */
	private static byte[] httpGet(URL url) throws IOException {

		// 开启http连接
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

		// 响应码是200
		InputStream inputStream;
		if (httpURLConnection.getResponseCode() == 200) {
			// 获取connection对象对应的输入流
			inputStream = httpURLConnection.getInputStream();
		} else {
			inputStream = httpURLConnection.getErrorStream();
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// 每次读取1024个字节
		byte[] buffer = new byte[1024];
		int len = 0;

		while ((len = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
			byteArrayOutputStream.flush();
		}

		// 关闭对象的流操作
		byteArrayOutputStream.close();
		inputStream.close();

		// 返回字节数组
		return byteArrayOutputStream.toByteArray();

	}

	/**
	 * http POST请求，返回字节数组
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            post参数
	 * @return 字节数组
	 * @throws IOException
	 */
	private static byte[] httpPost(URL url, String params) throws IOException {

		// 开启http连接
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

		// 设置http连接类型
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("ContentType", "text/xml;charset=utf-8");
		httpURLConnection.setRequestProperty("charset", "utf-8");
		// 发送poet请求必须设置以下两行
		httpURLConnection.setDoOutput(true);
		httpURLConnection.setDoInput(true);
		// 获取connection对象对应的输出流（写参数出去）
		PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
		// 想输入流中写入post请求参数
		printWriter.write(params);
		printWriter.flush();
		// 获取connection对象对应的输入流（读返回内容回来）
		BufferedInputStream bufferedInputStream;
		if (httpURLConnection.getResponseCode() == 200) {
			bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
		} else {
			bufferedInputStream = new BufferedInputStream(httpURLConnection.getErrorStream());
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		// 每次读取1024个字节
		int len = 0;
		byte[] buffer = new byte[1024];
		while ((len = bufferedInputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, len);
			byteArrayOutputStream.flush();
		}

		// 关闭流操作
		byteArrayOutputStream.close();
		bufferedInputStream.close();

		// 返回字节数组
		return byteArrayOutputStream.toByteArray();
	}
}
