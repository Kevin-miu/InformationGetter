package database;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库基本操作（增删改查等）
 * 
 * @author Kevin-
 *
 */
public class DBUtil {

	// 查询所有数据，以字符串返回
	public static String queryAll(DBConnection db) {
		String output = null;
		ResultSet resultSet = null;

		try {
			String sql = "SELECT address,cpuUsage,memUsage,diskSize,bandwidth,onlineTime FROM info";
			resultSet = db.getStatement().executeQuery(sql);

			while (resultSet.next()) {
				if (output == null || output.length() < 0) {
					output = resultSet.getString("address") + " " + resultSet.getFloat("cpuUsage") + " "
							+ resultSet.getFloat("memUsage") + " " + resultSet.getLong("diskSize") + " "
							+ resultSet.getFloat("bandwidth") + " " + resultSet.getLong("onlineTime") + "\n";
				} else {
					output = resultSet.getString("address") + " " + resultSet.getFloat("cpuUsage") + " "
							+ resultSet.getFloat("memUsage") + " " + resultSet.getLong("diskSize") + " "
							+ resultSet.getFloat("bandwidth") + " " + resultSet.getLong("onlineTime") + "\n";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return output;
	}

	// 插入一条数据
	public static void insertData(DBConnection db, DBObject dbObject) {
		try {
			String macAddressGBK = new String(dbObject.getMacAddress().getBytes(), "GBK");
			String sql = "INSERT INTO info(address,cpuUsage,memUsage,diskSize,bandwidth,onlineTime) VALUES ('"
					+ macAddressGBK + "'," + dbObject.getCpuUsage() + "," + dbObject.getMemUsage() + ","
					+ dbObject.getDiskSize() + "," + dbObject.getBandwidth() + "," + dbObject.getOnlineTime() + ")";
			System.out.println("insertData的语句: ");

			db.getStatement().executeUpdate(sql);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 删除所有数据
	public static void deleteAll(DBConnection db) {
		try {
			String sql = "DELETE FROM info";
			System.out.println("delete的语句:" + sql);

			db.getStatement().executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 根据cpu占用率和内存占用率，计算出计算力参数
	public static float calCalculation(DBConnection db) {
		float cpuUsage = 0;
		float memUsage = 0;
		float calculation = 0.0f;
		ResultSet resultSet = null;

		try {
			String sql = "SELECT AVG(cpuUsage),AVG(memUsage) FROM info";

			resultSet = db.getStatement().executeQuery(sql);

			while (resultSet.next()) {
				cpuUsage = resultSet.getFloat("AVG(cpuUsage)");
				memUsage = resultSet.getFloat("AVG(memUsage)");
			}

			calculation = (float) ((cpuUsage * 100 * 0.6) + (memUsage * 100 * 0.4));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("计算力：" + calculation);
		return calculation;
	}

	// 获取磁盘使用情况
	public static long calDiskSize(DBConnection db) {
		long diskSize = 0;
		ResultSet resultSet = null;

		String sql = "SELECT AVG(diskSize) FROM info";

		try {
			resultSet = db.getStatement().executeQuery(sql);

			while (resultSet.next()) {
				diskSize = resultSet.getInt("AVG(diskSize)");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("磁盘：" + diskSize);
		return diskSize;
	}

	// 获取上行带宽使用情况
	public static float calBandwidth(DBConnection db) {
		float bandwidth = 0;
		ResultSet resultSet = null;

		String sql = "SELECT MAX(bandwidth) FROM info";

		try {
			resultSet = db.getStatement().executeQuery(sql);

			while (resultSet.next()) {
				bandwidth = resultSet.getFloat("MAX(bandwidth)");
			}

			bandwidth /= 1000 * 1000;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("带宽：" + bandwidth);
		return bandwidth;
	}

	// 获取在线时长（暂时无法判断重启的情况）
	public static float calOnlineTime(DBConnection db) {
		float onlineTime = 0;
		ResultSet resultSet = null;

		String sql = "SELECT MAX(onlineTime) FROM info";

		try {
			resultSet = db.getStatement().executeQuery(sql);

			while (resultSet.next()) {
				onlineTime = resultSet.getFloat("MAX(onlineTime)");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		//System.out.println("在线时长：" + onlineTime);
		return onlineTime;
	}

}
