package com.willy.strutsActionForm;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class IndexActionForm extends ActionForm{
	private String userName;
	private String passWord;
	private String[] multiboxlist;
	
	public String[] getMultiboxlist() {
		return multiboxlist;
	}

	public void setMultiboxlist(String[] multiboxlist) {
		this.multiboxlist = multiboxlist;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
		ActionErrors errors=new ActionErrors();
		if(this.getUserName().trim().length()<1 || this.getPassWord().trim().length()<1){
			errors.add("userName", new ActionMessage("username.empty"));
			errors.add("passWord", new ActionMessage("password.empty"));
		}
		return errors;
	}


}
