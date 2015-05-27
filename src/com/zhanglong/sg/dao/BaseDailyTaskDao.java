package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseDailyTask;

@Repository
public class BaseDailyTaskDao extends BaseDao2 {

	private static List<BaseDailyTask> list;

	@SuppressWarnings("unchecked")
	public List<BaseDailyTask> findAll() {

		if (BaseDailyTaskDao.list == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			BaseDailyTaskDao.list = session.createCriteria(BaseDailyTask.class).list();
		}

		List<BaseDailyTask> list = new ArrayList<BaseDailyTask>();
		for (BaseDailyTask baseDailyTask : BaseDailyTaskDao.list) {
			try {
				list.add(baseDailyTask.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}
}
