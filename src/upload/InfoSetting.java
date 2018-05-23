package upload;

/**
 * 用户设备设定的硬件参数（默认值）
 * 
 * @author Kevin-
 *
 */
public class InfoSetting {

	private float calculation_setting;// 计算力
	private long disk_setting;// 存储
	private float bandwidth_setting;// 带宽

	public float getCalculation_setting() {
		return calculation_setting;
	}

	public void setCalculation_setting(float calculation_setting) {
		this.calculation_setting = calculation_setting;
	}

	public long getDisk_setting() {
		return disk_setting;
	}

	public void setDisk_setting(long disk_setting) {
		this.disk_setting = disk_setting;
	}

	public float getBandwidth_setting() {
		return bandwidth_setting;
	}

	public void setBandwidth_setting(float bandwidth_setting) {
		this.bandwidth_setting = bandwidth_setting;
	}

}
