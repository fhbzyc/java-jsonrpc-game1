package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Hero;
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
		return criteria.add(Restrictions.eq("roleId", roleId)).add(Restrictions.eq("status", Order.STATUS_SUCCESS)).list();
	}

	public int sum(int roleId) {

		String sql = "SELECT SUM(order_money) FROM role_orders WHERE role_id = ? AND order_status = ?";
		
		Session session = this.getSessionFactory().getCurrentSession();
		
		Query query = session.createQuery(sql);

		query.setParameter(1, roleId);
		query.setParameter(2, Order.STATUS_SUCCESS);

		return ((Integer) query.list().iterator().next()).intValue();
	}

	public int after6(int roleId) {

		String sql = "SELECT count(*) FROM role_orders WHERE role_id = ? AND order_status = ? AND order_gold >= ?";

		Session session = this.getSessionFactory().getCurrentSession();

		Query query = session.createQuery(sql);

		query.setParameter(1, roleId);
		query.setParameter(2, Order.STATUS_SUCCESS);
		query.setParameter(3, 60);

		return ((Integer) query.list().iterator().next()).intValue();
	}
	
	public void create(Order order) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(order);
	}

	public void update(Order order) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(order);
	}
}
