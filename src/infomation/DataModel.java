package infomation;

public class DataModel {

	private String macAddress;
	private BaseInfo baseInfo;
	private ShareInfo shareInfo;

	public DataModel(String maString_p, BaseInfo baseInfo_p, ShareInfo shareInfo_p) {
		this.macAddress = maString_p;
		this.baseInfo = baseInfo_p;
		this.shareInfo = shareInfo_p;
	}

	@Override
	public String toString() {
		String output1 = "{" + "\n macAddress: " + macAddress + "\n baseInfo:{" + "\n cpuInfo: " + baseInfo.getCpuInfo()
				+ "\n memoryInfo: " + baseInfo.getMemoryInfo() + "\n netInfo: " + baseInfo.getNetInfo() + "\n ioInfo: "
				+ baseInfo.getIoInfo() + "\n diskInfo: " + baseInfo.getDiskInfo() + "\n onlineTime: "
				+ baseInfo.getOnlineTime() + " \n}\n";

		String output2 = " shareInfo:{ " + "\n PID: " + shareInfo.getPID() + "\n cpuUsage%: " + shareInfo.getCpuUsage()
				+ "\n memUsage%: " + shareInfo.getMemUsage() + "\n diskUsage: " + shareInfo.getdiskSize()
				+ "\n uploadBytes: " + shareInfo.getUploadBytes() + " \n}\n" + "\n}\n";

		String output = output1 + output2;

		return output;
	}
}
