package com.willy.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.willy.test.Imp.UsersImp;
import com.willy.test.inter.UsersInter;


public class TestClass {
	public static void main(String[] args) {
		ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
//		SessionFactory sf=(SessionFactory) ac.getBean("sessionFactory");
//		System.out.println(((Users)sf.openSession().get(Users.class, 2)).getLoginName());
		UsersInter ui=(UsersInter) ac.getBean("usersImp");
		Users us=new Users("name11","pwd11");
		ui.load(us);
	}
	
}
