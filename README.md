# linux设备硬件信息的获取

- **摘要：** 本文主要分析了在linux设备上采集相关的硬件信息，并给出了java实现的方法。然后模拟了“玩客云”的场景，摘取了部分关键信息，上传至服务器。
- **关键技术：** linux命令行返回值的解析、MySQL数据的连接及相关操作、HTTP通信
- **实现语言：** java
- **参考：** http://www.jb51.net/LINUXjishu/65741.html   https://blog.csdn.net/blue_jjw/article/details/8741000
- **github：** https://github.com/Kevin-miu/InformationGetter

## 信息采集

### 0.重要数据结构

以下就是准备收集的硬件信息，集中保存在BaseInfo对象。

![name][01]
[01]:http://39.108.133.122/Images/file/infoTree.png '信息树'

![](http://39.108.133.122/Images/file/infoTree.png)

基本信息类：

```java
public class BaseInfo {

	private String macAddress;// mac地址（可以去掉，放在总结构上）
	private MemoryInfo memoryInfo;// 内存信息
	private CPUInfo cpuInfo;// cpu信息
	private NetInfo netInfo;// 网络带宽信息
	private IOInfo ioInfo;// 磁盘IO信息
	private DiskInfo diskInfo;// 磁盘使用情况
	private float onlineTime;// 在线时长
}
```

其他详细信息：

```java
/**
	 * memTotal:总容量； memFree：空闲容量 ； swapTotal：交换空间总容量； swapFree：交换空间空闲容量；
	 */
	public class MemoryInfo {
		private long memTotal;
		private long memFree;
		private long swapTotal;
		private long swapFree;
    }

    /**
	 * user:系统启动至今，用户态的CPU时间。1jiffies=0.01s； nice：系统启动至今，nice值为负的进程所占用的CPU时间；
	 * system：系统启用至今，核心CPU时间 ； idle：系统启动至今，除IO等待时间以外的其他等待时间； 计算cpu的使用率：
	 */
	public class CPUInfo {
		private long user;
		private long nice;
		private long system;
		private long idle;
    }

    /**
	 * receBytes:接受的字节数，可计算下载带宽 ； sendBytes：发送的字节数，可计算上行带宽；
	 * currentDownloadSpeed：当前下载速度 ； currentUploadSpeed：当前上传速度；
	 *
	 */
	public class NetInfo {
		private long receBytes;
		private long sendBytes;
		private String currentDownloadSpeed;
		private String currentUploadSpeed;
    }

    /**
	 * util:一秒中有百分之多少的时间用于I/O操作；
	 */
	public class IOInfo {
		private float util;
    }

    /**
	 * totalSize:磁盘总容量 ； usedSize：磁盘已使用大小 ； availSize：磁盘剩余容量；
	 *
	 */
	public class DiskInfo {
		private String totalSize;
		private String usedSize;
		private String availSize;
    }

```

### 1.CPU

#### 背景知识介绍

proc文件系统：proc文件系统是一个伪文件系统，它只存在内存当中，而不占用外存空间。它以文件系统的方式为访问系统内核数据的操作提供接口。用户和应用程序可以通过proc得到系统的信息，并可以改变内核的某些参数。

从/proc文件系统获取cpu使用情况：    cat /proc/stat
该文件包含了所有CPU活动的信息，所有值都是从系统启动开始累计到当前时刻

执行结果如下所示

```sh
kevin@ubuntu:~$ cat /proc/stat
cpu  947 0 2492 45071 2008 0 101 0 0 0
cpu0 536 0 1242 22405 726 0 47 0 0 0
cpu1 411 0 1249 22666 1281 0 54 0 0 0
intr 63204 57 64 0 1 152 0 2 0 1 0 0 0 1668 0 0 0 1556 10529 61 350 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 320 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0
ctxt 183833
btime 1527075275
processes 2914
procs_running 3
procs_blocked 0
softirq 48101 1 17338 51 585 11210 0 109 7723 11 11073
```

输出解释：
cpu0和cpu1代表为2核，cpu每一列的参数意思为：

- user (947) 从系统启动开始累计到当前时刻，用户态的CPU时间（单位：jiffies） ，不包含 nice值为负进程。1jiffies=0.01秒
- nice (0) 从系统启动开始累计到当前时刻，nice值为负的进程所占用的CPU时间（单位：jiffies） 
- system (2492) 从系统启动开始累计到当前时刻，核心时间（单位：jiffies） 。
- idle (45017) 从系统启动开始累计到当前时刻，除硬盘IO等待时间以外其它等待时间（单位：jiffies） 
- iowait (2008) 从系统启动开始累计到当前时刻，硬盘IO等待时间（单位：jiffies） 。
- irq (0) 从系统启动开始累计到当前时刻，硬中断时间（单位：jiffies） 。
- softirq (101) 从系统启动开始累计到当前时刻，软中断时间（单位：jiffies）。

CPU时间=user+system+nice+idle+iowait+irq+softirq

- intr：这行给出中断的信息，第一个为自系统启动以来，发生的所有的中断的次数；然后每个数对应一个特定的中断自系统启动以来所发生的次数。
- ctxt：给出了自系统启动以来CPU发生的上下文交换的次数。
- btime：给出了从系统启动到现在为止的时间，单位为秒。
- processes (total_forks) ：自系统启动以来所创建的任务的个数目。
- procs_running：当前运行队列的任务的数目。
- procs_blocked：当前被阻塞的任务的数目。

CPU利用率的计算方法：可以使用取两个采样点，计算其差值的办法。
CPU利用率 = 1- (idle2-idle1)/(cpu2-cpu1)

注：在Linux的内核中，有一个全局变量：Jiffies。 Jiffies代表时间。它的单位随硬件平台的不同而不同。系统里定义了一个常数HZ，代表每秒种最小时间间隔的数目。这样jiffies的单位就是 1/HZ。Intel平台jiffies的单位是1/100秒，这就是系统所能分辨的最小时间间隔了。每个CPU时间片，Jiffies都要加1。

#### 代码实现

```java
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
```
----

### 2.内存

#### 背景知识介绍

从/proc文件系统获取内存使用情况：   cat /proc/meminfo
执行结果如下所示

```sh
kevin@ubuntu:~$ cat /proc/meminfo 
MemTotal:        4041076 kB   //所有可用内存RAM大小（即物理内存减去一些预留位和内核的二进制代码大小）
MemFree:         3060956 kB   //未使用的内存大小
Buffers:           53972 kB   //用来给文件做缓冲的大小
Cached:           418960 kB   //被高速缓冲存储器（cache memory）用的内存的大小
SwapCached:            0 kB   //swap 缓存的大小，很少使用swap的，经常为0
Active:           504068 kB   //在活跃使用中的缓冲或高速缓冲存储器页面文件的大小
Inactive:         330164 kB   //在不经常使用中的缓冲或高速缓冲存储器页面文件的大小
Active(anon):     362300 kB
Inactive(anon):     9396 kB
Active(file):     141768 kB
Inactive(file):   320768 kB
Unevictable:           4 kB
Mlocked:               4 kB
SwapTotal:       4191228 kB   //交换空间的总大小
SwapFree:        4191228 kB   //未被使用交换空间的大小
Dirty:                24 kB
Writeback:             0 kB
AnonPages:        361340 kB
Mapped:           119652 kB
Shmem:             10400 kB
Slab:              56796 kB
//以下省略
....

```

输出解释：
在上述注释里

内存使用率 = 1 - MemFree/MemTotal

#### 代码实现

```java
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
```

----

### 3.磁盘IO

#### 背景知识介绍

使用命令    iostat -d -x
执行结果如下所示

```sh
kevin@ubuntu:~$ iostat -d -x
Linux 3.13.0-147-generic (ubuntu) 	2018年05月23日 	_x86_64_	(2 CPU)

Device:         rrqm/s   wrqm/s     r/s     w/s    rkB/s    wkB/s avgrq-sz avgqu-sz   await r_await w_await  svctm  %util
sda               6.00     4.46   21.19    2.17   466.30   142.82    52.14     0.66   28.37   21.05   99.83   1.93   4.52

```

输出解释：

- rrqm/s:每秒进行merge的读操作数目。即delta(rmerge)/s 。
- wrqm/s:每秒进行merge的写操作数目。即delta(wmerge)/s 。
- r/s:每秒完成的读I/O设备次数。即delta(rio)/s。
- w/s:每秒完成的写I/0设备次数。即delta(wio)/s 。
- rsec/s:每秒读扇区数。即delta(rsect)/s 。
- wsec/s:每秒写扇区数。即delta(wsect)/s 。
- rKB/s:每秒读K字节数。是rsec/s的一半，因为每扇区大小为512字节 。

- wKB/s:每秒写K字节数。是wsec/s的一半 。
- **avgrq-sz:** 平均每次设备I/O操作的数据大小(扇区)。即delta(rsect+wsect)/delta(rio+wio)。 
- **avgqu-sz:** 平均I/O队列长度。即delta(aveq)/s/1000(因为aveq的单位为毫秒) 。
- **await:** 平均每次设备I/O操作的等待时间(毫秒)。即delta(ruse+wuse)/delta(rio+wio) 。
- **svctm:** 平均每次设备I/O操作的服务时间(毫秒)。即delta(use)/delta(rio+wio) 。
- **%util:** 一秒中有百分之多少的时间用于I/O操作,或者说一秒中有多少时间I/O队列是非空的。即delta(usr)/s/1000(因为use的单位为毫秒)。 

如果%util接近100%,说明产生的I/O请求太多,I/O系统已经满负载,该磁盘可能存在瓶颈。

svctm一般要小于await(因为同时等待的请求的等待时间被重复计算了),svctm的大小一般和磁盘性能有关,CPU/内存的负荷也会对其有影响，请求过多也会间接导致svctm的增加。await的大小一般取决于服务时间(svctm)以及I/O队列的长度和I/O请求的发出模式。如果svctm比较接近await,说明I/O几乎没有等待时间;如果await远大于svctm,说明I/O队列太长，应用得到的响应时间变慢,如果响应时间超过了用户可以容许的范围,这时可以考虑更换更快的磁盘,调整内核elevator算法,优化应用,或者升级CPU

#### 代码实现

代码略

----

### 4.磁盘空间使用情况

#### 背景知识介绍

使用命令   df -hl /home/  （home可以替换成任意磁盘名称）
执行结果如下所示

```sh
kevin@ubuntu:~$ df -hl /home/
Filesystem      Size  Used Avail Use% Mounted on
/dev/sda1        95G  9.8G   80G  11% /
```

输出解释：

- Size: 磁盘总可用空间
- Used: 磁盘已用空间
- Avail: 磁盘未用空间
- Use%: 磁盘空间使用率

#### 代码实现

代码略

----

### 5.网络带宽

#### 背景知识介绍

从/proc文件系统获取网络使用情况：   cat /proc/net/dev
执行结果如下所示

```sh
kevin@ubuntu:~$ cat /proc/net/dev
Inter-|   Receive                                                |  Transmit
 face |bytes    packets errs drop fifo frame compressed multicast|bytes    packets errs drop fifo colls carrier compressed
  eth0: 6956040    6082    0    0    0     0          0         0   111234    1102    0    0    0     0       0          0
    lo:   29766     218    0    0    0     0          0         0    29766     218    0    0    0     0       0          0

```

统计一段时间内Receive（下载）和Tramsmit（上行）的bytes数的变化，即可获得网口传输速率，再除以网口的带宽就得到带宽的使用率。
计算上传下载速率：先采集一段时间内的Receive（下载）和Tramsmit（上行）的bytes数的变化
下载速率：(receBytes2 - receBytes1) / 1024 / 时间间隔
上传速率：(sendBytes2 - sendBytes1) / 1024 / 时间间隔

#### 代码实现

略

----

### 6.mac地址

#### 背景知识介绍

使用命令    ifconfig eth0  (eth0可以换成其他网卡名)
执行结果如下所示  

```sh
eth0      Link encap:Ethernet  HWaddr 00:0c:29:c9:e9:2e  
          inet addr:192.168.129.135  Bcast:192.168.129.255  Mask:255.255.255.0
          inet6 addr: fe80::20c:29ff:fec9:e92e/64 Scope:Link
          UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
          RX packets:6301 errors:0 dropped:0 overruns:0 frame:0
          TX packets:1104 errors:0 dropped:0 overruns:0 carrier:0
          collisions:0 txqueuelen:1000 
          RX bytes:6969645 (6.9 MB)  TX bytes:111636 (111.6 KB)

//其中HWaddr就是硬件mac地址
```

#### 代码实现

代码略

----

### 7.在线时长

#### 背景知识介绍

使用命令   cat /proc/uptime
执行结果如下所示

```sh
kevin@ubuntu:~$ cat /proc/uptime
3015.07 5866.02
```

输出解释：
- 参数1：代表从系统启动到现在的时间(以秒为单位)。
- 参数2：第二个参数是代表系统空闲的时间(以秒为单位)。

计算空闲率：参数2/参数1

1. 空闲率高低并不意味着，它做的工作很多，还有跟设备的配置和性能有很大的关系，这台设备有这么低的空闲率，或者说这么高的利用率，是因为它的配置比较低。
2. 系统空闲的时间有时会是系统运行时间的几倍，这是怎么回事呢？因为系统空闲时间的计算，是把SMP算进去的，就是所你有几个逻辑的CPU（包括超线程）.

#### 代码实现

代码略

## 模拟玩客云场景

### 基本流程

![name][01]
[01]:http://39.108.133.122/Images/file/flow1.png '基本流程'

![](http://39.108.133.122/Images/file/flow1.png)

本项目只展示了**客户端模块**,服务器模块**coming soon**。

----

### 数据库

#### 表

数据库采用MySQL数据。
数据库仅包含一张表，该表不设主键，用来存储每个1min采集的硬件信息，24h后处理这些信息得到待上传数据，且清空表。
表名为 info

![name][01]
[01]:http://39.108.133.122/Images/file/db1.png 'info表'

![](http://39.108.133.122/Images/file/db1.png)

#### 数据库连接

使用JDBC连接数据库，下载JDBC连接器：https://www.mysql.com/products/connector/

1. 初始化：

```java

DBDriver = "com.mysql.jdbc.Driver";//设置驱动
DBURL = "jdbc:mysql://localhost:3306/hardware";//设置数据库链接（此处是本地数据库，根据自身情况设置）
DBUser = "root";//设置数据库用户账号(根据自身情况设置)
DBPassword = "xxxx";//设置该账号的登录密码(根据自身情况设置)
```

2. 执行：

```java
Class.forName(DBDriver);// 加载JDBC驱动
connection = DriverManager.getConnection(DBURL, DBUser, DBPassword);// 取得连接
statement = connection.createStatement();// 取得SQL语句对象
```

3. 关闭：

```java
statement.close();//关闭SQL语句对象
connection.close();//关闭连接
```

#### 数据库操作

**增删改查**操作，这里只分析**获取磁盘使用情况**

```java
// 获取磁盘使用情况
	public static long calDiskSize(DBConnection db) {
		long diskSize = 0;
		ResultSet resultSet = null;

		String sql = "SELECT AVG(diskSize) FROM info";

		try {
			resultSet = db.getStatement().executeQuery(sql);//执行语句

			while (resultSet.next()) {//注意即使只有一个值也必须这样访问
				diskSize = resultSet.getInt("AVG(diskSize)");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("磁盘：" + diskSize);
		return diskSize;
	}
```

----

### 填充和生成json

#### 填充对象

使用GSON包来生成和解析json字符串需要填充对象来协助，本项目的填充对象是UploadInfoModel对象：

```java
public class UploadInfoModel {

	private String macAddress; // mac地址
	private InfoSetting infoSetting;
	private InfoActual infoActual;
}

//用户设置的值
public class InfoSetting {

	private float calculation_setting;// 计算力
	private long disk_setting;// 存储
	private float bandwidth_setting;// 带宽
}

//设备实际使用的值
public class InfoActual {

	private float calculation_actual;// 计算力
	private long disk_actual;// 存储
	private float bandwidth_actual;// 带宽
	private float onlineTime;// 实际在线时间
}
```

#### json工具

使用GSON包来生成和解析json，GSON包下载：http://repo1.maven.org/maven2/com/google/code/gson/gson/2.7/
下载jar即可

```java
//传入填充对象生成json字符串
	public static String transformToJson(Object object) {
		Gson gson = new Gson();
		String jsonStr = gson.toJson(object);

		return jsonStr;
	}
	
	//将json字符串解析成指定类对象（填充对象）
	public static <T> T getObjectByJson(String jsonData, Class<T> classOfT) {

		// 将json字符串解析成object对象
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(jsonData).getAsJsonObject();

		// 使用gson工具解析成指定的类对象（该对象的结果必须与json格式严格一致）
		Gson gson = new Gson();

		return gson.fromJson(object, classOfT);
	}
```

#### json格式

```json
{
	"macAddress": "kong",
	"infoSetting": {
		"calculation_setting": 100.0,
		"disk_setting": 1000,
		"bandwidth_setting": 5.0
	},
	"infoActual": {
		"calculation_actual": 1795.0757,
		"disk_actual": 548,
		"bandwidth_actual": 4.465156,
		"onlineTime": 19.0
	}
}
```

----

### 通信模块

coming soon