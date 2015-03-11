package com.zhanglong.sg.dao;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		financeLog.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		session.save(financeLog);
	}
}
