package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BossKiller;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BossKillerDao extends BaseDao {

	public BossKiller findOne(int serverId) {
		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossKiller> list = session.createCriteria(BossKiller.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.eq("date", Integer.valueOf(Utils.date())))
				.list();

		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public void save(BossKiller bossKiller) {
		bossKiller.setDate(Integer.valueOf(Utils.date()));
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(bossKiller);
	}
}
