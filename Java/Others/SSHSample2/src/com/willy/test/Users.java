package com.willy.test;

public class Users {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	private int id;
	private String loginName;
	private String loginPwd;
	public Users(String loginName, String loginPwd) {
		super();
		this.loginName = loginName;
		this.loginPwd = loginPwd;
	}
	public Users() {
		super();
	}
	
}
