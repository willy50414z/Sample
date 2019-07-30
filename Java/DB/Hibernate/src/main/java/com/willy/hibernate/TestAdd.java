package com.willy.hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.willy.hibernate.entity.Users;


public class TestAdd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestAdd().addUser();
	}	
	private void addUser(){
		//建立持久化物件
		Users user=new Users();
//		user.setId(2);
		user.setLoginName("zhangsan");
		user.setLoginPwd("123456");
		//1.起始化,讀取組態檔hibernate.cfg.xml
		Configuration config=new Configuration().configure();
		//2.讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		SessionFactory sessionFactory=config.buildSessionFactory();
		//3.開啟session
		Session session=sessionFactory.openSession();
		Transaction tx=null;
		try {			
			tx=session.beginTransaction();    //4.開始一個交易			
			session.save(user);		 //5.持久化動作	
			tx.commit();    //6.傳送交易
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();  //交易返回				
			}	
			e.printStackTrace();
		}finally{
			session.close();   //7.關閉session
		}
		
	}
}
