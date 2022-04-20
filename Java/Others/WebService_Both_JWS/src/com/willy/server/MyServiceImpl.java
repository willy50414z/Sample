package com.willy.server;

import javax.jws.WebService;

import org.zttc.service.User;

@WebService(endpointInterface="com.willy.server.IMyService")
public class MyServiceImpl implements IMyService {

	@Override
	public int add(int a, int b) {
		System.out.println(a+"+"+b+"="+(a+b));
		return a+b;
	}

	@Override
	public int minus(int a, int b) {
		System.out.println(a+"-"+b+"="+(a-b));
		return a-b;
	}

	@Override
	public User login(String username, String password) {
		System.out.println(username+" is logining");
		User user = new User();
		user.setId(1);
		user.setUserName(username);
		user.setPassword(password);
		return user;
	}

}