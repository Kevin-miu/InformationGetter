package upload;

/**
 * 用户设备设计使用的硬件参数
 * 
 * @author Kevin-
 *
 */
public class InfoActual {
	private float calculation_actual;// 计算力
	private long disk_actual;// 存储
	private float bandwidth_actual;// 带宽

	private float onlineTime;// 实际在线时间

	public float getCalculation_actual() {
		return calculation_actual;
	}

	public void setCalculation_actual(float calculation_actual) {
		this.calculation_actual = calculation_actual;
	}

	public long getDisk_actual() {
		return disk_actual;
	}

	public void setDisk_actual(long disk_actual) {
		this.disk_actual = disk_actual;
	}

	public float getBandwidth_actual() {
		return bandwidth_actual;
	}

	public void setBandwidth_actual(float bandwidth_actual) {
		this.bandwidth_actual = bandwidth_actual;
	}

	public float getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(float onlineTime) {
		this.onlineTime = onlineTime;
	}

}
