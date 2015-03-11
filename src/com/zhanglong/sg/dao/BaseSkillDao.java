package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseSkill;

@Repository
public class BaseSkillDao {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<BaseSkill> findAll() {

		Session session = this.getSessionFactory().getCurrentSession();
		return session.createCriteria(BaseSkill.class).list();
    }
}
