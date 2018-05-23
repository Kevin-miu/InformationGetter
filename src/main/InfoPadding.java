package main;

import database.DBConnection;
import database.DBUtil;
import json.UploadInfoModel;
import upload.InfoActual;
import upload.InfoSetting;

/**
 * 按照上传数据的json格式要求来填充数据
 * 
 * @author Kevin-
 *
 */
public class InfoPadding {

	public static UploadInfoModel paddingInfo(DBConnection db) {
		
		InfoSetting infoSetting = new InfoSetting();
		// 此处为默认值，暂不接受用户自行设置
		infoSetting.setCalculation_setting(100);
		infoSetting.setDisk_setting(1000);
		infoSetting.setBandwidth_setting(5);

		InfoActual infoActual = new InfoActual();
		// 此处检测该进程的实际值
		infoActual.setCalculation_actual(DBUtil.calCalculation(db));
		infoActual.setDisk_actual(DBUtil.calDiskSize(db));
		infoActual.setBandwidth_actual(DBUtil.calBandwidth(db));
		infoActual.setOnlineTime(DBUtil.calOnlineTime(db));

		UploadInfoModel uploadInfoModel = new UploadInfoModel();
		// 按要求填充信息
		uploadInfoModel.setInfoSetting(infoSetting);
		uploadInfoModel.setInfoActual(infoActual);
		uploadInfoModel.setMacAddress("kong");
		
		return uploadInfoModel;
	}
}
