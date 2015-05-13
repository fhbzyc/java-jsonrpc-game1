package com.zhanglong.sg.dao;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.zhanglong.sg.entity.FinanceLog;

public class FinanceLogDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void create(FinanceLog financeLog) {
		Session session = this.getSessionFactory().getCurrentSession();
		financeLog.setTime(new Timestamp(System.currentTimeMillis()));
		session.save(financeLog);
	}
}
