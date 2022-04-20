package com.li;

import org.apache.tomcat.jni.User;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

public class LoginAction extends ActionSupport implements Action, ModelDriven<User>{
	private static final long serialVersionUID = 1L;
//	private String userName;
//	private String password;
	private User user =new User();
	@Override
	public String execute() throws Exception {

		ActionContext ac = ActionContext.getContext();

		if ("zhangsan".equals("abc") && "123456".equals("cde")) {
			ac.put("success", "123");
			return Action.SUCCESS;
		} else {
			ac.put("error", "錯誤");
			return Action.ERROR;
		}
	}
	@Override
	public User getModel() {
		// TODO Auto-generated method stub
		return null;
	}

//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//
//	}


	
}
