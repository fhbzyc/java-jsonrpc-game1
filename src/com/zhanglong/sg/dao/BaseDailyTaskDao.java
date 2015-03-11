package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseDailyTask;

@Repository
public class BaseDailyTaskDao extends BaseDao {

	private static List<BaseDailyTask> list;

	@SuppressWarnings("unchecked")
	public List<BaseDailyTask> findAll() {

		if (BaseDailyTaskDao.list == null) {
			Session session = this.sessionFactory.getCurrentSession();
			BaseDailyTaskDao.list = session.createCriteria(BaseDailyTask.class).list();
		}

		return BaseDailyTaskDao.list;
	}
}
