package com.willy.struts.ActionForm;

import org.apache.struts.action.ActionForm;

public class LoginActionForm extends ActionForm{
public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
private String account;
private String password;
public LoginActionForm() {
	super();System.out.println("actionform");
}

}
