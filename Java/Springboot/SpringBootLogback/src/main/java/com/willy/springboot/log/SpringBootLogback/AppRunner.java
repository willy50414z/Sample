package com.willy.springboot.log.SpringBootLogback;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class AppRunner implements CommandLineRunner{
	private static PreparedStatement ps;
	 private static final Logger LOGGER = LoggerFactory.getLogger(AppRunner.class);
	@Override
	public void run(String... args) throws Exception {
		//建立DB
		Class.forName("org.h2.Driver");
		Connection con = DriverManager.getConnection("jdbc:h2:file:D:/SpringBootLogBack", "", "");
		ps = con.prepareStatement("CREATE TABLE logging_event (   timestmp BIGINT NOT NULL,   formatted_message LONGVARCHAR NOT NULL,   logger_name VARCHAR(256) NOT NULL,   level_string VARCHAR(256) NOT NULL,   thread_name VARCHAR(256),   reference_flag SMALLINT,   arg0 VARCHAR(256),   arg1 VARCHAR(256),   arg2 VARCHAR(256),   arg3 VARCHAR(256),   caller_filename VARCHAR(256),    caller_class VARCHAR(256),    caller_method VARCHAR(256),    caller_line CHAR(4),   event_id IDENTITY NOT NULL);   CREATE TABLE logging_event_property (   event_id BIGINT NOT NULL,   mapped_key  VARCHAR(254) NOT NULL,   mapped_value LONGVARCHAR,   PRIMARY KEY(event_id, mapped_key),   FOREIGN KEY (event_id) REFERENCES logging_event(event_id));  CREATE TABLE logging_event_exception (   event_id BIGINT NOT NULL,   i SMALLINT NOT NULL,   trace_line VARCHAR(256) NOT NULL,   PRIMARY KEY(event_id, i),   FOREIGN KEY (event_id) REFERENCES logging_event(event_id));");
		ps.execute();
		// TODO Auto-generated method stub
		LOGGER.info("InfoLog");
		LOGGER.debug("DebugLog");
		LOGGER.error("ERRLog");
		try {
			
			ps = con.prepareStatement("select * from logging_event");
			ResultSet rs = ps.executeQuery();
			int colCount = rs.getMetaData().getColumnCount();
			while (rs.next()) {
				for(int i=1;i<=colCount;i++) {
					System.out.println(rs.getString(i)+"\t");					
				}
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
