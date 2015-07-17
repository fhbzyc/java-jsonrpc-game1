package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.KillRank;
import com.zhanglong.sg.entity.Role;

@Repository
public class KillRankDao extends BaseDao {

	public KillRankDao() {
	}

	public KillRank findOne(Role role) {

		Session session = this.getSessionFactory().getCurrentSession();
		KillRank killRank = (KillRank) session.get(KillRank.class, role.getRoleId());
		if (killRank == null) {

			killRank = new KillRank();
			killRank.setRoleId(role.getRoleId());
			killRank.setAvatar(role.getAvatar());
			killRank.setLevel(role.getLevel());
			killRank.setName(role.getName());
			killRank.setNum(0);
			killRank.setServerId(role.getServerId());
			killRank.setTime(new Timestamp(System.currentTimeMillis()));
			session.save(killRank);
		}
		return killRank;
	}
	
	public void update(Role role, int num) {

		Session session = this.getSessionFactory().getCurrentSession();
		KillRank killRank = this.findOne(role);

		killRank.setAvatar(role.getAvatar());
		killRank.setLevel(role.getLevel());
		killRank.setName(role.getName());
		killRank.setNum(num);
		killRank.setServerId(role.getServerId());
		killRank.setTime(new Timestamp(System.currentTimeMillis()));

		session.update(killRank);
	}

	public List<KillRank> top20(int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<KillRank> list = session.createCriteria(KillRank.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.gt("num", 0))
				.addOrder(Order.desc("num"))
				.addOrder(Order.asc("time"))
				.setMaxResults(20).list();
		return list;
	}

	public long countAfter(int killNum, int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Long> list = session.createCriteria(KillRank.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.gt("num", killNum))
				.setProjection(Projections.projectionList().add(Projections.count("roleId")))
				.list();
		return list.get(0);
	}
}
