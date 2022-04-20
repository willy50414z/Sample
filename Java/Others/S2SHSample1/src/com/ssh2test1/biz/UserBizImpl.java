package com.ssh2test1.biz;

import java.util.List;

import com.ssh2test1.dao.UserDAO;
import com.ssh2test1.entity.Users;

public class UserBizImpl implements UserBiz {

	UserDAO userDAO;	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public List login(Users condition) {
		// TODO Auto-generated method stub
		return userDAO.search(condition);
	}
	public boolean isExist(String username) {	
		boolean bExist = false;
		Users condition = new Users();
		condition.setLoginName(username); 
		List list = userDAO.search(condition);
		if (list==null || list.size()==0){
			bExist = false;
		}else{
			bExist = true;
		}
		return bExist;

	}	
}
