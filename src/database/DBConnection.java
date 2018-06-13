package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 连接数据库
 * @author Kevin-
 *
 */

public class DBConnection {

	private String DBDriver;
	private String DBURL;
	private String DBUser;
	private String DBPassword;

	private Connection connection;
	private Statement statement;

	public DBConnection() {
		this.DBDriver = "com.mysql.jdbc.Driver";
		this.DBURL = "jdbc:mysql://localhost:3306/hardware";
		this.DBUser = "root";
		this.DBPassword = "123456";

		try {
			// 加载JDBC驱动
			Class.forName(DBDriver);

			// 取得连接
			connection = DriverManager.getConnection(DBURL, DBUser, DBPassword);

			// 取得SQL语句对象
			statement = connection.createStatement();

			System.out.println("连接数据库成功");

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public Statement getStatement() {
		return this.statement;
	}

	//关闭连接
	public void closeConnection() {
		try {
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		String output = "数据库驱动程序：" + DBDriver + "\n 数据库连接地址： " + DBURL + "\n 用户名： " + DBUser + "\n 密码： " + DBPassword;
		return output;
	}
}
