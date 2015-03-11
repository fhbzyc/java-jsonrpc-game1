package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Checkin;
import com.zhanglong.sg.entity.ItemTable;
import com.zhanglong.sg.utils.Utils;

@Repository
public class CheckinDao extends BaseDao {

	public List<Checkin> findAll(int roleId) {
		
		int month = month();

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Checkin.class);
		return criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("month", month)).list();
	}

	public int month() {
		return Integer.valueOf(Utils.date().substring(0, 6));
	}

	public void create(int roleId, Checkin checkin) {
		this.sessionFactory.getCurrentSession().save(checkin);
	}

	public void update(int roleId, Checkin checkin) {
		this.sessionFactory.getCurrentSession().update(checkin);
	}
}
