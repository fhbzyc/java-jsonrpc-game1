package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseActivity;

@Repository
public class BaseActivityDao extends BaseDao {

	public List<BaseActivity> findAll(int serverId) {

		Session session = this.sessionFactory.getCurrentSession();
		return session.createCriteria(BaseActivity.class).
		add(Restrictions.or(Restrictions.eq("serverId", 0), Restrictions.eq("serverId", serverId)))
		.addOrder(Order.desc("order")).list();
	}
}
