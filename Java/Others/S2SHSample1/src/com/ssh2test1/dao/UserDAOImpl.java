package com.ssh2test1.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ssh2test1.entity.Users;

public class UserDAOImpl extends HibernateDaoSupport implements UserDAO {

	public List search(final Users condition) {
		return super.getHibernateTemplate().executeFind(new HibernateCallback(){
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria c=session.createCriteria(Users.class);
				if(condition!=null){
					if(condition.getLoginName()!=null && !condition.getLoginName().equals("")){
						c.add(Restrictions.eq("loginName", condition.getLoginName()));
					}
					if(condition.getLoginPwd()!=null && !condition.getLoginPwd().equals("")){
						c.add(Restrictions.eq("loginPwd", condition.getLoginPwd()));
					}
				}
				return c.list();
			}
		});		
	}

	public void updateUsers() {
		Users u1=(Users)super.getHibernateTemplate().get(Users.class, new Integer(8));
		u1.setLoginPwd("1");
		super.getHibernateTemplate().saveOrUpdate(u1);
		Users u2=(Users)super.getHibernateTemplate().get(Users.class, new Integer(9));
		u2.setLoginPwd("2");		
		super.getHibernateTemplate().saveOrUpdate(u2);
	}	

}
