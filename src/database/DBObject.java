package database;

/**
 * 数据库info表的字段
 * 
 * @author Kevin-
 *
 */
public class DBObject {

	private String macAddress;// mac地址，主键
	private float cpuUsage;// 本进程cpu占用率，单位是%
	private float memUsage;// 本进程内存占用率，单位是%
	private long diskSize;// 本进程磁盘使用情况，单位是Mb
	private float bandwidth;// 本进程上行带宽使用情况，单位是Mbps
	private float onlineTime;// 本设备在线时长，单位是h

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public float getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(float cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public float getMemUsage() {
		return memUsage;
	}

	public void setMemUsage(float memUsage) {
		this.memUsage = memUsage;
	}

	public long getDiskSize() {
		return diskSize;
	}

	public void setDiskSize(long diskSize) {
		this.diskSize = diskSize;
	}

	public float getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(float bandwidth) {
		this.bandwidth = bandwidth;
	}

	public float getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(float onlineTime) {
		this.onlineTime = onlineTime;
	}

	@Override
	public String toString() {
		String output = " macAddress: " + getMacAddress() + " cpuUsage: " + getCpuUsage() + " menUsage: "
				+ getMemUsage() + " diskSize: " + getDiskSize() + " bandwidth: " + getBandwidth() + " onlineTime: "
				+ getOnlineTime();

		return output;
	}

}
