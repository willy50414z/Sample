package com.ssh2test1.dao;

import java.util.List;

import com.ssh2test1.entity.Account;

public interface AccountDAO {
	public List getAccountByAccountNo(String accountNo);
	public void transfer(Account a1,Account a2);

}
