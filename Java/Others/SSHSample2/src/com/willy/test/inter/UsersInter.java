package com.willy.test.inter;

import com.willy.test.Users;

public interface UsersInter {
	public void showUser(int id);
	public void addUser(Users us);
	public boolean checkUser(Users us);
	public void load(Users us);
}
