package com.willy.hibernate.entity;

import java.io.Serializable;

public class Users implements Serializable {
	private Integer id;         //編號
	private String loginName;   //使用者名稱
	private String loginPwd;    //密碼
	//預設建構方法
	public Users() {		
	}
	//屬性id的get方法
	public Integer getId() {    
		return id;
	}
	//屬性id的set方法
	public void setId(Integer id) {   
		this.id = id;
	}
	//屬性loginName的get方法
	public String getLoginName() {    
		return loginName;
	}
	//屬性loginName的set方法
	public void setLoginName(String loginName) {   
		this.loginName = loginName;
	}
	//屬性loginPwd的get方法
	public String getLoginPwd() {
		return loginPwd;
	}
	//屬性loginPwd的set方法
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

}
