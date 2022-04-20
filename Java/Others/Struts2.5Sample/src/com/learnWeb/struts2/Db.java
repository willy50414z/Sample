package com.learnWeb.struts2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Db {


	
	public ResultSet getKeyWord(String key){
		Context ctx;
		ResultSet rs = null;
		
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/GMS");
			Connection con = ds.getConnection();
			Statement stmt = con.createStatement();
		String sql = "select cfgkey from gms_config";
		
		rs = stmt.executeQuery(sql);
		}catch(Exception e) {
			
		}
		return rs;
	}

}
