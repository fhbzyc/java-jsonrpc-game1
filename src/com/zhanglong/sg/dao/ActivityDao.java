package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Activity;

@Repository
public class ActivityDao extends BaseDao {

	public List<Activity> findAll(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Activity> list = session.createCriteria(Activity.class).add(Restrictions.eq("roleId", roleId)).list();
		return list;
    }

	public Activity findOne(int roleId, int actId, int key) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Activity> list = session.createCriteria(Activity.class)
				.add(Restrictions.eq("roleId", roleId))
				.add(Restrictions.eq("actId", actId))
				.add(Restrictions.eq("key", key)).list();
		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	public void create(Activity act) {

		act.setTime(new Timestamp(System.currentTimeMillis()));

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(act);
    }

}
