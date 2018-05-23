package json;

import upload.InfoActual;
import upload.InfoSetting;

/**
 * 将本类对象生成json，或将Json解析成本类对象。（ 本类仅包含待上传的设备信息）
 * 
 * @author Kevin-
 *
 */
public class UploadInfoModel {
	private String macAddress;
	private InfoSetting infoSetting;
	private InfoActual infoActual;

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public InfoSetting getInfoSetting() {
		return infoSetting;
	}

	public void setInfoSetting(InfoSetting infoSetting) {
		this.infoSetting = infoSetting;
	}

	public InfoActual getInfoActual() {
		return infoActual;
	}

	public void setInfoActual(InfoActual infoActual) {
		this.infoActual = infoActual;
	}

	@Override
	public String toString() {
		String output = "{\r\n" + 
				"    \"macAddress\":" +getMacAddress()+",\r\n" + 
				"    \"infoSetting\": {\r\n" + 
				"        \"calculation_setting\": "+infoSetting.getCalculation_setting()+",\r\n" + 
				"        \"disk_setting\":"+infoSetting.getDisk_setting()+",\r\n" + 
				"        \"bandwidth_setting\": "+infoSetting.getBandwidth_setting()+"\r\n" + 
				"    },\r\n" + 
				"    \"infoActual\": {\r\n" + 
				"        \"calculation_actual\": "+infoActual.getCalculation_actual()+",\r\n" + 
				"        \"disk_actual\": "+infoActual.getDisk_actual()+",\r\n" + 
				"        \"bandwidth_actual\": "+infoActual.getBandwidth_actual()+",\r\n" + 
				"        \"onlineTime\": "+infoActual.getOnlineTime()+"\r\n" + 
				"    }\r\n" + 
				"}";

		return output;
	}
}
