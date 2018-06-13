package main;

import java.io.IOException;

import database.DBConnection;
import infomation.BaseInfoGetter;
import json.UploadInfoModel;
import upload.Upload;

public class Main {

	public static void main(String[] args) {
		// 测试函数
		// test();

		// 1.收集信息并存入数据库
		// mac地址只需要收集一次
		try {
			String mac = BaseInfoGetter.acquireMacAddress();
			System.out.println("本机的mac地址是：" + mac);

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// 新线程执行，因为无限循环执行
		DBConnection db = new DBConnection();
		new Thread(new Runnable() {

			@Override
			public void run() {
				Collect.collection();

			}
		}).start();

		// 2.定时函数（模拟定时1min），计算出填充值 -> 生成json文件 -> 上传服务器
		Upload.upload(db);
	}

}
