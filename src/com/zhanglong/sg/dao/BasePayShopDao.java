package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BasePayShop;

@Repository
public class BasePayShopDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public List<BasePayShop> findAll() {

		Session session = this.sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(BasePayShop.class);

		return criteria.addOrder(Order.desc("order")).list();
	}

	public BasePayShop findOne(int id) {

		Session session = this.sessionFactory.getCurrentSession();
		return (BasePayShop) session.get(BasePayShop.class, id);
	}
}
