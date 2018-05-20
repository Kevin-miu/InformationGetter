package infomation;

/**
 * 设备共享信息的数据结构
 * 
 * @author Kevin-
 *
 */

public class ShareInfo {

	private int PID;// 共享程序的进程ID
	private float cpuUsage;// 共享程序的cpu占比%
	private float memUsage;// 共享程序的内存占比%
	private float diskSize;// 共享程序的磁盘使用情况(默认为10GB)
	private long uploadBytes;// 共享程序的上行带宽（暂时不确定如何获取）

	private static ShareInfo instance;

	private ShareInfo() {

	}

	public static ShareInfo getInstance() {// 单例模式
		if (instance == null) {
			instance = new ShareInfo();
		}
		return instance;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
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

	public float getdiskSize() {
		return diskSize;
	}

	public void setdiskSize(float diskSize) {
		this.diskSize = diskSize;
	}

	public long getUploadBytes() {
		return uploadBytes;
	}

	public void setUploadBytes(long uploadBytes) {
		this.uploadBytes = uploadBytes;
	}

	@Override
	public String toString() {
		String output = " PID: " + getPID() + "\n cpuUsage: " + getCpuUsage() + "\n memUsage: " + getMemUsage()
				+ "\n diskUsage: " + getdiskSize() + "\n uploadBytes: " + getUploadBytes() + "\n";

		return output;
	}

}
