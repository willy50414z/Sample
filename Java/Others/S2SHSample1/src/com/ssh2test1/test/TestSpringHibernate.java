package com.ssh2test1.test;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ssh2test1.biz.UserBiz;
import com.ssh2test1.entity.Users;

public class TestSpringHibernate {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 //載入applicationContext.xml組態
		ApplicationContext context=new ClassPathXmlApplicationContext("/applicationContext.xml");
		//取得組態中的案例
		UserBiz userBiz =(UserBiz)context.getBean("userBiz");
		Users conditon=new Users();
		conditon.setLoginName("admin");
		conditon.setLoginPwd("123456");
		List list=userBiz.login(conditon);
		if(list.size()>0){
			System.out.println("登陸成功");
		}else{
			System.out.println("登陸失敗");
		}		
	}
}
