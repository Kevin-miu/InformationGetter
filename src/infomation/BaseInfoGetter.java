package infomation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import infomation.BaseInfo.CPUInfo;
import infomation.BaseInfo.DiskInfo;
import infomation.BaseInfo.IOInfo;
import infomation.BaseInfo.MemoryInfo;
import infomation.BaseInfo.NetInfo;

/**
 * 获取linux下的硬件和带宽的基本信息
 * 
 * @author Kevin-
 */
public final class BaseInfoGetter {

	/**
	 * 获取设备的在线时长
	 * 
	 * @return 在线时间长onlineTime
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static float acquireOnlineTime() throws IOException, InterruptedException {
		// 获取存储在线时长的数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();

		float onlineTime = 0.0f;

		// 此文件保存了linux内核信息，可以查看在线时长
		File file = new File("/proc/uptime");
		// 封装成buffer，便于使用readline方法
		BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = null;
		// 读取文件，每次读取一行
		while ((line = bReader.readLine()) != null) {
			// 去掉首尾空白键
			line = line.trim();
			if (line != null) {
				// 分片
				String[] temp = line.split("\\s+");
				// 第一个子字符串是在线时长，第二个子字符串是空闲时长，我们获取第一个
				onlineTime = Float.parseFloat(temp[0]);
				// 存储数据结构
				baseInfo.setOnlineTime(Float.parseFloat(temp[0]));
			}
		}
		return onlineTime;
	}

	/**
	 * 获取设备的mac地址
	 * 
	 * @return MAC地址字符串
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String acquireMacAddress() throws IOException, InterruptedException {
		// 获取存储mac地址的数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();

		String mac = null;
		// 定义process实例，可用来控制本进程并获取相关信息
		Process pro = null;
		// 定义并获取runtime实例，可与运行环境相连接
		Runtime runtime = Runtime.getRuntime();
		// 指定命令，该命令可获取eth0网卡信息
		String command = "ifconfig eth0";
		// 执行命令，获取process实例
		pro = runtime.exec(command);

		BufferedReader bReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		String line = null;
		// 读取返回的字符流
		while ((line = bReader.readLine()) != null) {
			// 去掉首尾的空白键
			line = line.trim();
			// 找到eth0的信息
			if (line.startsWith("eth0")) {
				// 分片
				String[] temp = line.split("\\s+");
				// System.out.println(temp[4]);
				// 第4个子字符串就是mac地址
				mac = temp[4];
				// 存入数据结构
				baseInfo.setMacAddress(temp[4]);
				break;
			}
		}
		return mac;
	}

	/**
	 * 获取内存信息
	 * 
	 * @return memory:总容量+空闲容量+总交换空间+空闲交换空间
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static MemoryInfo acquireMemInfo() throws IOException, InterruptedException {
		// 定义存储数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();
		MemoryInfo memory = baseInfo.new MemoryInfo();

		// 此文件存储了linux内核的内存信息
		File file = new File("/proc/meminfo");
		// 封装成buffer，便于使用readline方法
		BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		String line = null;

		while ((line = bReader.readLine()) != null) {
			String[] temp = line.split("\\s+");

			if (temp[0].startsWith("MemTotal:")) {
				// 内存总量
				memory.setMemTotal(Long.parseLong(temp[1]));
			} else if (temp[0].startsWith("MemFree:")) {
				// 空闲内存量
				memory.setMemFree(Long.parseLong(temp[1]));
			} else if (temp[0].startsWith("SwapTotal:")) {
				// 交换空间总量
				memory.setSwapTotal(Long.parseLong(temp[1]));
			} else if (temp[0].startsWith("SwapFree:")) {
				// 空闲交换空间量
				memory.setSwapFree(Long.parseLong(temp[1]));
			}
		}

		bReader.close();
		baseInfo.setMemoryInfo(memory);
		// 返回内存信息
		return memory;
	}

	/**
	 * 获取CPU信息
	 * 
	 * @return cpu:user+nice+syst+idle
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static CPUInfo acquireCPUInfo() throws IOException, InterruptedException {
		// 定义CPU数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();
		CPUInfo cpu = baseInfo.new CPUInfo();

		// 此文件存储了linux内核信息
		File file = new File("/proc/stat");
		// 封装成buffer，便于使用readline方法
		BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		// StringTokenizer可以分解字符串，将字符串分解成一个个子字符串
		StringTokenizer token = new StringTokenizer(bReader.readLine());

		// 检查是否获取到空数据
		if (!token.hasMoreTokens()) {
			System.out.println("没有获取到CPU信息");
			return null;
		}

		// 此token是字符串cpu
		token.nextToken();

		// 第一次采集
		cpu.setUser(Integer.parseInt(token.nextToken()));
		cpu.setNice(Integer.parseInt(token.nextToken()));
		cpu.setSystem(Integer.parseInt(token.nextToken()));
		cpu.setIdle(Integer.parseInt(token.nextToken()));

		// 关闭buffer
		bReader.close();
		baseInfo.setCpuInfo(cpu);

		return cpu;
	}

	/**
	 * 获取网络带宽信息
	 * 
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static NetInfo acquireNetInfo() throws IOException, InterruptedException {
		// 定义网络带宽数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();
		NetInfo netInfo = baseInfo.new NetInfo();

		// 此文件存储了linux内核信息
		File file = new File("/proc/net/dev");
		// 记录起始时间
		long startTime = System.currentTimeMillis();
		// 封装成buffer，便于使用readline方法
		BufferedReader bReader1 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		String line1 = null;
		long receBytes1 = 0, sendBytes1 = 0;
		// 第一次采集
		while ((line1 = bReader1.readLine()) != null) {
			line1 = line1.trim();
			if (line1.startsWith("eth0")) {
				// System.out.println("eth0网段的信息：" + line1);
				String[] temp = line1.split("\\s+");
				// System.out.println("网络信息rece："+temp[1]);
				// System.out.println("网络信息send："+temp[9]);
				// 获取receive字节数
				receBytes1 = Long.parseLong(temp[1]);
				// 获取send字节数
				sendBytes1 = Long.parseLong(temp[9]);
				break;
			}
		}

		bReader1.close();
		// 等待1s
		Thread.sleep(1000);

		// 第二次采集
		long endTime = System.currentTimeMillis();
		BufferedReader bReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		String line2 = null;
		long receBytes2 = 0, sendBytes2 = 0;

		while ((line2 = bReader2.readLine()) != null) {
			line2 = line2.trim();
			if (line2.startsWith("eth0")) {
				String[] temp = line2.split("\\s+");
				receBytes2 = Long.parseLong(temp[1]);
				sendBytes2 = Long.parseLong(temp[9]);
				break;
			}
		}

		bReader2.close();

		netInfo.setReceBytes(receBytes2);
		netInfo.setSendBytes(sendBytes2);

		// 计算上传下载速率
		if (receBytes1 != 0 && receBytes2 != 0 && sendBytes1 != 0 && sendBytes2 != 0) {
			float interval = (float) (endTime - startTime) / 1000;

			float downloadSpeed = (float) ((float) (receBytes2 - receBytes1) / 1024 / interval);
			float uploadSpeed = (float) ((float) (sendBytes2 - sendBytes1) / 1024 / interval);

			// 指定存储格式，字符串
			DecimalFormat df = new DecimalFormat("0.00");
			String dls, uls;

			if ((float) (downloadSpeed / 1024) >= 1) {
				downloadSpeed = (float) (downloadSpeed / 1024);
				dls = df.format(downloadSpeed) + "Mb/s";
			} else {
				dls = df.format(downloadSpeed) + "Kb/s";
			}

			if ((float) (uploadSpeed / 1024) >= 1) {
				uploadSpeed = (float) (uploadSpeed / 1024);
				uls = df.format(uploadSpeed) + "Mb/s";
			} else {
				uls = df.format(uploadSpeed) + "Kb/s";
			}

			netInfo.setCurrentDownloadSpeed(dls);
			netInfo.setCurrentUploadSpeed(uls);

		}

		return netInfo;

	}
	/**
	 * 获取磁盘的使用率 执行iostat -d -x命令可以获取相关信息。其中util字段表示使用率
	 * 
	 * @return io:磁盘使用率util
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static IOInfo acquireIOInfo() throws IOException, InterruptedException {
		// 定义磁盘IO存储数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();
		IOInfo io = baseInfo.new IOInfo();
		// 定义process实例，可用来控制本进程并获取相关信息
		Process pro = null;
		// 定义并获取runtime实例，使程序可以与其运行环境相连接
		Runtime runtime = Runtime.getRuntime();
		// 执行命令
		String command = "iostat -d -x";
		// 执行命令，创建process实例
		pro = runtime.exec(command);
		// 封装成buffer，便于使用readline方法
		BufferedReader bReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));

		String line = null;
		int count = 0;

		while ((line = bReader.readLine()) != null) {
			// 获取第4行才有关键信息
			if (++count >= 4) {
				// System.out.println("检查IO的信息： " + line);
				// 正则表达式切分该行字符串
				String[] temp = line.split("\\s+");

				if (temp.length > 1) {
					// 获取最后一个子字符串，即是util字段
					io.setUtil(Float.parseFloat(temp[temp.length - 1]));
				}
			}
		}

		bReader.close();
		pro.destroy();

		baseInfo.setIoInfo(io);

		return io;
	}

	/**
	 * 获取磁盘使用情况
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static DiskInfo acquireDiskInfo() throws IOException, InterruptedException {
		// 定义磁盘使用情况数据结构
		BaseInfo baseInfo = BaseInfo.getInstance();
		DiskInfo disk = baseInfo.new DiskInfo();

		// 定义process实例，可用来控制本进程并获取相关信息
		Process pro = null;
		// 定义并获取runtime示例，使程序可以与其运行环境相连接
		Runtime runtime = Runtime.getRuntime();

		// 指定命令
		String command = "df -hl /home/";
		// 执行命令，创建process实例
		pro = runtime.exec(command);

		BufferedReader bReader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
		String line = null;
		int count = 0;
		// 读取每二行
		// 行内容字符串分片
		// 获取想要的size/used/avail字段，分别在2,3,4
		while ((line = bReader.readLine()) != null) {
			line = line.trim();
			String[] temp = line.split("\\s+");
			if (++count >= 2) {
				disk.setTotalSize(temp[1]);
				disk.setUsedSize(temp[2]);
				disk.setAvailSize(temp[3]);

				break;
			}
		}

		bReader.close();
		baseInfo.setDiskInfo(disk);

		return disk;
	}

}
