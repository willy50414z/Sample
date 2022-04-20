package com.ssh2test1.entity;

/**
 * Users entity. @author MyEclipse Persistence Tools
 */

public class Users implements java.io.Serializable {

	// Fields

	private Integer id;
	private String loginName;
	private String loginPwd;
	private String name;
	private String address;
	private String phone;
	private String mail;
	private Integer userRoleId;
	private Integer userStateId;

	// Constructors

	/** default constructor */
	public Users() {
	}

	/** full constructor */
	public Users(String loginName, String loginPwd, String name,
			String address, String phone, String mail, Integer userRoleId,
			Integer userStateId) {
		this.loginName = loginName;
		this.loginPwd = loginPwd;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.mail = mail;
		this.userRoleId = userRoleId;
		this.userStateId = userStateId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPwd() {
		return this.loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Integer getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public Integer getUserStateId() {
		return this.userStateId;
	}

	public void setUserStateId(Integer userStateId) {
		this.userStateId = userStateId;
	}

}