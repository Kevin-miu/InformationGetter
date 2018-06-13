package main;

import java.util.Random;

import database.DBConnection;
import database.DBObject;
import database.DBUtil;
import infomation.BaseInfo;
import infomation.ShareInfo;
import infomation.ShareInfoGetter;

public class Collect {

	// 信息收集上传函数（需要增加随机性）
	public static void collection() {
		DBConnection db = new DBConnection();

		while (true) {
			try {
				DBObject dbObject = new DBObject();
				Random random = new Random();
				//dbObject.setMacAddress(String.valueOf(21118));
				dbObject.setMacAddress(BaseInfo.getInstance().getMacAddress());
				dbObject.setOnlineTime(10 + random.nextInt(10));

				ShareInfoGetter.initialize();
				ShareInfo shareInfo = ShareInfo.getInstance();
				dbObject.setCpuUsage(shareInfo.getCpuUsage());
				dbObject.setMemUsage(shareInfo.getMemUsage());
				dbObject.setDiskSize(shareInfo.getdiskSize());
				dbObject.setBandwidth(shareInfo.getUploadBytes());

				DBUtil.insertData(db, dbObject);

				Thread.sleep(10000);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
