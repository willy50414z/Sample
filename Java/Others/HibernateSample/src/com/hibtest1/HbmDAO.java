package com.hibtest1;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HbmDAO {
	Session session;
	SessionFactory sessionFactory;
	Configuration config;
	Transaction tran;
	public HbmDAO() {
		super();
		//1.起始化,讀取組態檔hibernate.cfg.xml
		config=new Configuration().configure();
		//2.讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		sessionFactory=config.buildSessionFactory();System.out.println("aa");
		//3.開啟session
		session=sessionFactory.openSession();System.out.println("bb");
	}
	public Object get(Class cla,Serializable id) {
		Object ob=session.get(cla, id);
		return ob;
	}
	public void add(Object ob) {
		tran=session.beginTransaction();
		try {
		session.save(ob);
		tran.commit();
		}catch(Exception e) {
			tran.rollback();
		}finally {
		
		}
	}
	public void delete(Object ob) {
		tran=session.beginTransaction();
		try {
		session.delete(ob);
		tran.commit();
		}catch(Exception e) {
			tran.rollback();
		}
	}	
	public void update(Object ob) {
		tran=session.beginTransaction();
		try {
		session.update(ob);
		tran.commit();
		}catch(Exception e) {
			tran.rollback();
		}
	}	
}
