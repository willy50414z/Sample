package com.willy.test.Imp;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.willy.test.Users;
import com.willy.test.inter.UsersInter;

@Transactional
public class UsersImp implements UsersInter{
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private SessionFactory sessionFactory;
	
//	ApplicationContext ac=new ClassPathXmlApplicationContext("applicationContext.xml");
//	UsersImp usersImp=(UsersImp) ac.getBean("usersImp");
	Users us;
	@Override
	public void showUser(int id) {
		// TODO Auto-generated method stub
		Session session=sessionFactory.openSession();
		us=(Users) session.get(Users.class, 2);
		if(us==null) {
			System.out.println("沒有資料");
		}else{
			System.out.println(us.getLoginName()+" "+us.getLoginPwd());
		}
	}

	@Override
	public void addUser(Users us) {
		// TODO Auto-generated method stub
		
		
		//一般方法
//		Session session=sessionFactory.openSession();
//		session.save(us);
		
		
		//期望方法-->錯誤 configuration does not allow creation of non-transactional one here
		sessionFactory.getCurrentSession().save(us);
		
		
		
	}

	@Override
	public boolean checkUser(Users us) {
		// TODO Auto-generated method stub
		String hql="from Users where loginName=? and loginpwd=?";
		List<Users> list=(sessionFactory.getCurrentSession().createQuery(hql).setString(0, us.getLoginName()).setString(1, us.getLoginPwd())).list();
		if(list.size()==1) {
			return true;
		}else {
			return false;
		}
			
	}

	@Override
	public void load(Users us) {
		// TODO Auto-generated method stub
//		String hql="from Users where loginName='aa' and loginpwd='bb'";
//		Query query=sessionFactory.getCurrentSession().createQuery(hql);
//		query.setString(0, us.getLoginName());
//		query.setString(1, us.getLoginPwd());
		
		String hql="from Users where loginName=? and loginpwd=?";
		Query query=sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, "aa");
		query.setString(1, "bb");
		List list=query.list();
		for(Object user : list) {
			Users users=(Users) user;
			System.out.println(users.getLoginName());
		}
	}

}
