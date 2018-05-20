package infomation;

import java.io.IOException;

import com.google.gson.Gson;

import infomation.BaseInfo.CPUInfo;
import infomation.BaseInfo.DiskInfo;
import infomation.BaseInfo.IOInfo;
import infomation.BaseInfo.MemoryInfo;
import infomation.BaseInfo.NetInfo;

public class Main {

	public static void main(String[] args) {
		while (true) {
			try {
				System.out.println("----------------基本信息----------------------");
				// 测试在线时间地址获取函数
				float time = BaseInfoGetter.acquireOnlineTime();
				System.out.println("开机至今在线时间： " + time);

				// 测试mac地址获取函数
				String mac = BaseInfoGetter.acquireMacAddress();
				System.out.println("mac地址： " + mac);

				// 测试内存信息获取函数
				MemoryInfo memInfoObject = BaseInfoGetter.acquireMemInfo();
				System.out.println(memInfoObject.toString());

				// 测试CPU信息获取函数
				CPUInfo CPUInfoObject = BaseInfoGetter.acquireCPUInfo();
				System.out.println(CPUInfoObject.toString());

				// 测试网络带宽信息获取函数
				NetInfo netInfoObject = BaseInfoGetter.acquireNetInfo();
				System.out.println(netInfoObject.toString());

				// 测试磁盘IO信息获取函数
				IOInfo IOInfoObject = BaseInfoGetter.acquireIOInfo();
				System.out.println(IOInfoObject.toString());

				// 测试磁盘使用情况获取函数
				DiskInfo diskInfoObject = BaseInfoGetter.acquireDiskInfo();
				System.out.println(diskInfoObject.toString());

				System.out.println();
				System.out.println("----------------------共享信息--------------------");

				// 初始化测试共享信息的函数
				ShareInfo shareInfo = ShareInfo.getInstance();
				ShareInfoGetter.initialize();
				System.out.println(shareInfo.toString());

				System.out.println();
				System.out.println("**********************测试数据模型是否正确************************");

				DataModel dataModel = new DataModel(BaseInfo.getInstance().getMacAddress(), BaseInfo.getInstance(),
						ShareInfo.getInstance());

				System.out.println(dataModel.toString());

				System.out.println();
				System.out.println("**************************结束********************");

				String jsonStr = JsonUtil.transformToJson(dataModel);
				System.out.println("json格式的数据为：\n" + JsonFormatTool.formatJson(jsonStr));

				System.out.println();
				System.out.println("-------------------------------------------------------");

				Thread.sleep(3000);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		// TestObject object = new TestObject();
		// TestObject.Friend friend = object.new Friend("朋友小明", 20);
		// object.setAge(18);
		// object.setName("小凯文");
		// object.setFirends(friend);
		//
		// Gson gson = new Gson();
		// String jsonStr2 = gson.toJson(object);
		// System.out.println("json数据：\n" + JsonFormatTool.formatJson(jsonStr2));

	}

}
