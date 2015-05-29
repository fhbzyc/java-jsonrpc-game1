package com.zhanglong.sg.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Order;

@Repository
public class OrderDao extends BaseDao {

	public Order findOne(int orderId) {
		Session session = this.getSessionFactory().getCurrentSession();
		Object object = session.get(Order.class, orderId);
		if (object == null) {
			return null;
		}
		return (Order)object;
	}

	public List<Order> findComplateList(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Order.class);
		@SuppressWarnings("unchecked")
		List<Order> list = criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("status", Order.STATUS_SUCCESS)).list();
		return list;
	}

	public int sum(int roleId) {

		int s = 0;
		List<Order> list = this.findComplateList(roleId);
		for (Order order : list) {
			s += order.getMoney();
		}
		return s;
	}

	public int after6(int roleId) {

		String sql = "SELECT COUNT(*) FROM role_orders WHERE role_id = ? AND order_status = ? AND order_gold >= ?";

		Session session = this.getSessionFactory().getCurrentSession();

		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter(0, roleId);
		query.setParameter(1, Order.STATUS_SUCCESS);
		query.setParameter(2, 60);

		BigInteger bigInteger = ((BigInteger) query.list().iterator().next());
		return bigInteger.intValue();
	}

	public void create(Order order) {
		order.setTime(new Timestamp(System.currentTimeMillis()));
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(order);
	}

	public void update(Order order) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(order);
	}
}
