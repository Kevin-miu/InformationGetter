package upload;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import communication.CommunicationModel;
import database.DBConnection;
import json.JsonUtil;
import json.UploadInfoModel;
import main.InfoPadding;

public class Upload {

	// 定时填充上传函数
	public static void upload(DBConnection db) {
		Calendar calendar = Calendar.getInstance();
		// 设置首次执行的时间
		// calendar.set(Calendar.HOUR_OF_DAY, 21);
		// calendar.set(Calendar.MINUTE, 36);
		// calendar.set(Calendar.SECOND, 0);

		// 不设置则默认是当前时间
		java.util.Date time = calendar.getTime();
		System.out.println("首次开始任务的时间是：" + time);

		Timer timer = new Timer();

		// 间隔是1min
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				myTask(db);
			}
		}, time, 1000 * 60);

	}

	// 上传任务
	public static void myTask(DBConnection db) {
		// 1.填充待上传数据
		UploadInfoModel uploadInfoModel = InfoPadding.paddingInfo(db);

		// 2.生成json(没用)
		String jsonStr = JsonUtil.transformToJson(uploadInfoModel);

		// System.out.println("上传 "+jsonStr);
		System.out.println("上传数据 ");

		// 3.执行上传函数
		CommunicationModel.uploadInfo(uploadInfoModel);

		// 4.手动更新到区块链
		CommunicationModel.updateByman();

		// 5.更新后查询（可能会出问题）
		//CommunicationModel.acquirePoint();
	}
}
