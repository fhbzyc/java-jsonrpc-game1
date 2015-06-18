package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.WhiteList;

@Repository
public class WhiteListDao extends BaseDao2 {

	public List<WhiteList> findAll() {

		Session session = this.getBaseSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(WhiteList.class);
		@SuppressWarnings("unchecked")
		List<WhiteList> list = criteria.list();
		return list;
    }

	public boolean find(String imei) {
		Session session = this.getBaseSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(WhiteList.class);
		@SuppressWarnings("unchecked")
		List<WhiteList> list = criteria.add(Restrictions.eq("imei", imei)).list();
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
