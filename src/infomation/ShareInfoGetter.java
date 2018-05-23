package infomation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.Random;

/**
 * 获取linux下某进程的信息，该进程为共享程序
 * 
 * @author Kevin-
 *
 */
public class ShareInfoGetter {

	// 获取进程ID
	// 获取进程内存%和CPU%
	public static void acquireProcBase() throws IOException, InterruptedException {
		ShareInfo shareInfo = ShareInfo.getInstance();

		Process pro = null;
		Runtime runtime = Runtime.getRuntime();
		// 这个命令需要自行输入
		String command = "ps -aux | grep ipfs | grep -v grep | awk '{print($1\" \"$2\" \"$3\" \"$4\" \"$11);}'";

		pro = runtime.exec(command);
		String line = null;
		BufferedReader bReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

		while ((line = bReader.readLine()) != null) {
			line = line.trim();
			String[] temp = line.split("\\s+");
			// 设置PID，第2位是进程PID
			shareInfo.setPID(Integer.parseInt(temp[1]));
			// 设置CPU使用率，第3位是CPU%
			shareInfo.setCpuUsage(Float.parseFloat(temp[2]));
			// 设置内存使用率，第4位是内存%
			shareInfo.setMemUsage(Float.parseFloat(temp[3]));
		}

		bReader.close();
	}

	// 设置进程共享的磁盘容量（默认为10G）
	public static void setDiskSize(long shareDiskSize) {
		ShareInfo shareInfo = ShareInfo.getInstance();
		shareInfo.setdiskSize(shareDiskSize);
	}

	// 获取上行字节数（暂无办法）
	public static void acquireProcUploadBytes() {

	}

	// 测试函数
	public static void initialize() {
		Random random = new Random();
		ShareInfo shareInfo = ShareInfo.getInstance();
		shareInfo.setPID(10098 + random.nextInt(1000));
		shareInfo.setCpuUsage(10 + random.nextInt(10) + random.nextFloat());
		shareInfo.setMemUsage(17 + random.nextInt(10) + random.nextFloat());
		shareInfo.setdiskSize(500 + random.nextInt(100));
		shareInfo.setUploadBytes(4455666 + random.nextInt(10000));
	}

}
