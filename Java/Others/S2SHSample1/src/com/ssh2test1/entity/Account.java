package com.ssh2test1.entity;

/**
 * Account entity. @author MyEclipse Persistence Tools
 */

public class Account implements java.io.Serializable {

	// Fields

	private Integer id;
	private String accountNo;
	private Long balance;

	// Constructors

	/** default constructor */
	public Account() {
	}

	/** full constructor */
	public Account(String accountNo, Long balance) {
		this.accountNo = accountNo;
		this.balance = balance;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Long getBalance() {
		return this.balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

}