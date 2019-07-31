package com.willy.main.H2DBSample;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.h2.tools.DeleteDbFiles;

public class SimpleSample {
	private static String driverName = "org.h2.Driver";
	
	// 在 D:/ 建立一 sample 的fileDB
	private static String connectionString = "jdbc:h2:file:D:/sample";
	
	//設定帳號密碼 AES加密 123文件鎖 456帳號鎖 
	//private static String connectionString = "jdbc:h2:file:D:/sample;CIPHER=AES;user=sa;password=123 456;";
	
	//記憶體內名為test_mem的DB
	//private static String connectionString = "jdbc:h2:mem:test_mem";
	
	//啟動時即執行schema.sql及data.sql中語法
	//private static String connectionString = "jdbc:h2:file:D:/sample;INIT=RUNSCRIPT FROM 'classpath:schema.sql'\\;RUNSCRIPT FROM 'classpath:data.sql'";
	
	private static String userName = "";
	private static String password = "";
	private static PreparedStatement ps;

	public static void main(String[] args) throws SQLException, FileNotFoundException {
		// TODO Auto-generated method stub
		// 刪除在D:/名為sample的FileDB,將信息不顯示在console
		DeleteDbFiles.execute("D:/", "sample", true);
		
		// 取得連線
		Connection con = getDBConnection();
		
		// 建立Table
		System.out.println("---建立Table---");
		createTable(con);

		// 塞資料
		System.out.println("---塞資料---");
		insertData(con);
		
		
		// 查資料
		System.out.println("---查資料---");
		queryData(con);
		
		con.close();
		

	}

	private static Connection getDBConnection() {
		Connection dbConnection = null;
		try {
			Class.forName(driverName);
			dbConnection = DriverManager.getConnection(connectionString, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	private static void createTable(Connection con) {
		String CreateQuery = "CREATE TABLE PERSON(id int primary key, name varchar(255))";
		try {
		ps = con.prepareStatement(CreateQuery);
		ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void insertData(Connection con) {
		String InsertQuery = "INSERT INTO PERSON" + "(id, name) values" + "(?,?)";
		try {
			ps = con.prepareStatement(InsertQuery);
			ps.setInt(1, 1);
			ps.setString(2, "Jose");
			ps.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private static void queryData(Connection con) {
		String SelectQuery = "select * from PERSON";
		try {
			ps = con.prepareStatement(SelectQuery);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println("Id " + rs.getInt("id") + " Name " + rs.getString("name"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
