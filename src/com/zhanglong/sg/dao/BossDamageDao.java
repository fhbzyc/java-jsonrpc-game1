package com.zhanglong.sg.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BossDamage;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BossDamageDao extends BaseDao {

	public BossDamage findOne(int roleId) {
		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossDamage> list = session.createCriteria(BossDamage.class)
				.add(Restrictions.eq("date", Integer.valueOf(Utils.date())))
				.add(Restrictions.eq("roleId", roleId))
				.list();

		if (list.size() != 0) {
			return list.get(0);
		}
		return null;
	}

	public List<BossDamage> findAll(int date, int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossDamage> list = session.createCriteria(BossDamage.class)
				.add(Restrictions.eq("date", date))
				.add(Restrictions.eq("serverId", serverId))
				.addOrder(Order.desc("damage")).list();

		return list;
	}

	public List<String> top3(int serverId) {

		int day = Integer.valueOf(Utils.date());
		
		Date date = new Date();
		int hour = date.getHours();
		if (hour < 20) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = simpleDateFormat.format(new Date(System.currentTimeMillis() - 86400l * 1000l));
			day = Integer.valueOf(str);
		}

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossDamage> list = session.createCriteria(BossDamage.class)
				.add(Restrictions.eq("date", day))
				.add(Restrictions.eq("serverId", serverId))
				.addOrder(Order.desc("damage")).setMaxResults(3).list();

		List<String> names = new ArrayList<String>();
		for (BossDamage damage : list) {
			names.add(damage.getName());
		}
		return names;
	}

	public BossDamage findOne(int roleId, int date) {
		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossDamage> list = session.createCriteria(BossDamage.class)
				.add(Restrictions.eq("date", date))
				.add(Restrictions.eq("roleId", roleId)).list();

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public int countAfter(int damage, int serverId) {

		String sql = "SELECT COUNT(*) AS n FROM role_boss_damages WHERE boss_damage > ? AND server_id = ?";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, damage);
		query.setParameter(1, serverId);

		return ((BigInteger) query.list().iterator().next()).intValue();
	}

	public void save(BossDamage bossDamage) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(bossDamage);
	}

	public void update(BossDamage bossDamage) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.update(bossDamage);
	}
}
