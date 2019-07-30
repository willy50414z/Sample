package com.willy.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.willy.hibernate.entity.Users;

public class TestDelete {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestDelete().testDelete();
	}
	private void testDelete(){
		//起始化,讀取組態檔hibernate.cfg.xml
		Configuration config=new Configuration().configure();
		//讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		SessionFactory sessionFactory=config.buildSessionFactory();
		//開啟session
		Session session=sessionFactory.openSession();
		Transaction tx=null;
		//載入要移除的資料
		Users user=(Users)session.get(Users.class, new Integer(1));
		try {
			tx=session.beginTransaction();    //開始一個交易			
			session.delete(user);             //執行移除	
			tx.commit();                      //傳送交易
		} catch (Exception e) {
			if(tx!=null){
				tx.rollback();       //交易返回				
			}
			e.printStackTrace();
		}finally{
			session.close();   //關閉session
		}	
		
	}

}
