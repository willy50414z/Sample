package com.hibtest1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.hibtest1.entity.Users;

public class TestLoad {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start=System.currentTimeMillis();
		new TestLoad().testLoad();
		System.out.println("共花了 : "+(System.currentTimeMillis()-start)+"毫秒");
	}
	
	private void testLoad(){
		//1.起始化,讀取組態檔hibernate.cfg.xml
		Configuration config=new Configuration().configure();
		//2.讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		SessionFactory sessionFactory=config.buildSessionFactory();
		//3.開啟session
		Session session=sessionFactory.openSession();
		//4.載入資料,PK值為2
		Users users=(Users)session.get(Users.class, 2);
		//在主控台輸出使用者名稱和密碼
		if(users==null) {
			System.out.println("沒有資料");
		}else{
			System.out.println(users.getLoginName()+" "+users.getLoginPwd());
		}
		
	}

}
