package com.zhanglong.sg.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BossKiller;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BossKillerDao extends BaseDao {

	public BossKiller findOne(int serverId) {

		int day = Integer.valueOf(Utils.date());

		Date date = new Date();
		int hour = date.getHours();
		if (hour < 20 || (hour == 20 && date.getMinutes() < 30)) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = simpleDateFormat.format(new Date(System.currentTimeMillis() - 86400l * 1000l));
			day = Integer.valueOf(str);
		}

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<BossKiller> list = session.createCriteria(BossKiller.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.eq("date", day))
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
