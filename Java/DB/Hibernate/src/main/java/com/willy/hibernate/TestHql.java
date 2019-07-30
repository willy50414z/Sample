package com.willy.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.willy.hibernate.entity.Publishers;
import com.willy.hibernate.entity.Users;


public class TestHql {
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		Configuration config=new Configuration().configure();
		//2.讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		SessionFactory sessionFactory=config.buildSessionFactory();
		//3.開啟session
		Session session=sessionFactory.openSession();
		System.out.println(" --- 1 基礎查詢 -- ");
		String hql="from Users u";
		Query query=session.createQuery(hql);
		List list=query.list();
		for(Object user : list) {
			Users users=(Users) user;
			System.out.println(users.getLoginName());
		}
		System.out.println("---- 2 加條件--------");
//		hql="from Publishers where PublisherId=?";
		hql="from Publishers P where P.PublisherId=:iddd";
		query=session.createQuery(hql);
//		query.setInteger(0, 2);//以位置塞參數
		query.setInteger("iddd", 2);//以名稱塞參數
		list=query.list();
		for(Object user : list) {
			Publishers users=(Publishers) user;
			System.out.println(users.getBookname());
		}
		System.out.println("---- 3只撈取部分欄位 --------");
		hql="select u.id,u.loginName,u.loginPwd from Users u";//可以select部分欄位，但不可轉換成類別
		query=session.createQuery(hql);
		list=query.list();
	    Iterator it=list.iterator();
	    while(it.hasNext()) {
	    	Object[] ob=(Object[])it.next();
	    	System.out.println(ob[0]+" -- "+ob[1]+" -- "+ob[2]);
	    }
	    System.out.println("---- 4分頁 --------");
		hql="from Users u";
		query=session.createQuery(hql);
		query.setFirstResult(0);//第幾頁
		query.setMaxResults(2);//每頁個數
		list=query.list();
		for(Object user : list) {
			Users users=(Users) user;
			System.out.println(users.getLoginName());
		}
	}
}
