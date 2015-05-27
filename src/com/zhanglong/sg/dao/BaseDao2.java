package com.zhanglong.sg.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BaseDao2 {

	@Autowired
	protected SessionFactory baseSessionFactory;

	public SessionFactory getBaseSessionFactory() {
		return baseSessionFactory;
	}

	public void setBaseSessionFactory(SessionFactory sessionFactory) {
		this.baseSessionFactory = sessionFactory;
	}
}
