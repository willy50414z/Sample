package com.learnWeb.struts2;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class DwrAjax extends ActionSupport {
	private static final long serialVersionUID = 1L;

	public List getName(String key) throws SQLException {
		Db db = new Db();
		ResultSet rs = db.getKeyWord(key);
		List list = new ArrayList();
		while (rs.next()) { 
			list.add(rs.getString(1));
		}
		return list;
	}
}
