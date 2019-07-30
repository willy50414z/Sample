package com.willy.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.willy.hibernate.entity.Users;

public class TestCriteria {
	public static void main(String[] args) {
		test1();
	}
	public static void test1() {
		Configuration config=new Configuration().configure();
		//2.讀取並解析映射檔案(Users.hbm.xml)，建立sessionFactory
		SessionFactory sessionFactory=config.buildSessionFactory();
		//3.開啟session
		Session session=sessionFactory.openSession();
		Criteria cri=session.createCriteria(Users.class);
//		MatchMode.ANYWHERE  %*%
//		MatchMod.END %*
//		MatchMode.EXACT  *
//		MatchMode.START  *%
		cri.add(Restrictions.like("loginName", "po", MatchMode.ANYWHERE)).addOrder(Order.desc("id"));
		cri.setFirstResult(0);
		cri.setMaxResults(2);
		List list=cri.list();
		for(Object user : list) {
			Users users=(Users) user;
			System.out.println(users.getLoginName());
		}
	}
}
