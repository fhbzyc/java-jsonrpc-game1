package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Checkin;
import com.zhanglong.sg.entity.LjCheckin;
import com.zhanglong.sg.utils.Utils;

@Repository
public class CheckinDao extends BaseDao {

	public List<Checkin> findAll(int roleId) {

		int month = month();

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Checkin.class);
		@SuppressWarnings("unchecked")
		List<Checkin> list = criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("month", month)).list();
		return list;
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

	public LjCheckin findLj(int roleId) {

		int month = month();

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(LjCheckin.class);
		@SuppressWarnings("unchecked")
		List<LjCheckin> list = criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("month", month)).list();
		
		if(list.size() != 0) {
			return list.get(0);
		} else {
			LjCheckin ljCheckin = new LjCheckin();
			ljCheckin.setRoleId(roleId);
			ljCheckin.setMonth(month);
			session.save(ljCheckin);
			return ljCheckin;
		}
	}

	public void updateLj(LjCheckin ljCheckin) {
		this.sessionFactory.getCurrentSession().update(ljCheckin);
	}
}
