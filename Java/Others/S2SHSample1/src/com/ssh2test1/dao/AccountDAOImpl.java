package com.ssh2test1.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.ssh2test1.entity.Account;

public class AccountDAOImpl extends HibernateDaoSupport implements AccountDAO {

	
	//轉賬
	public void transfer(Account a1, Account a2) {
		super.getHibernateTemplate().update(a1);
		super.getHibernateTemplate().update(a2);
	}

	//根據賬號取得賬戶物件
	public List getAccountByAccountNo(final String accountNo) {
		// TODO Auto-generated method stub
		return super.getHibernateTemplate().executeFind(new HibernateCallback() {			
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria c=session.createCriteria(Account.class);
				c.add(Restrictions.eq("accountNo", accountNo));
				return c.list();
			}
		});			
	}
}
