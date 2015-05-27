package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseActivity;

@Repository
public class BaseActivityDao extends BaseDao {

	public List<BaseActivity> findAll(int serverId) throws CloneNotSupportedException {

	//	String sql = "SELECT * FROM base_activity WHERE activity_enable = TRUE AND NOW() BETWEEN activity_begin_time AND activity_end_time AND (server_id = 0 OR server_id = ?) ORDER BY activity_order";

		long time = System.currentTimeMillis();

		Session session = this.sessionFactory.getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BaseActivity> list = session.createCriteria(BaseActivity.class).
				add(Restrictions.eq("enable", true)).
				add(Restrictions.lt("beginTime", new Timestamp(time))).
				add(Restrictions.gt("endTime", new Timestamp(time))).
				add(Restrictions.or(Restrictions.eq("serverId", 0), Restrictions.eq("serverId", serverId))).
				addOrder(Order.desc("order")).list();

		List<BaseActivity> result = new ArrayList<BaseActivity>();
		for (BaseActivity baseActivity : list) {
			result.add(baseActivity.clone());
		}

		return result;
	}
}
