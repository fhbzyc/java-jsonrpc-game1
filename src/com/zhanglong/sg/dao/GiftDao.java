package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.GiftCode;
import com.zhanglong.sg.entity.GiftLog;
import com.zhanglong.sg.entity.GiftTemplate;
import com.zhanglong.sg.entity.Role;

@Repository
public class GiftDao extends BaseDao {

	public GiftCode findOne(String code) {

		Session session = this.getSessionFactory().getCurrentSession();
		return (GiftCode) session.get(GiftCode.class, code);
	}

	public GiftTemplate findOne(int giftId) {

		Session session = this.getSessionFactory().getCurrentSession();
		return (GiftTemplate) session.get(GiftTemplate.class, giftId);
	}

	public void delete(GiftCode gift) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(gift);
	}

	public long countLogs(int roleId, int giftId) {

		Session session = this.getSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<Long> list = session.createCriteria(GiftLog.class)
		.add(Restrictions.eq("roleId", roleId))
		.add(Restrictions.eq("giftId", giftId))
		.setProjection(Projections.projectionList().add(Projections.count("id")))
		.list();

		return list.get(0);
	}

	public void insertLog(Role role, GiftTemplate giftTemplate, String code) {

		Session session = this.getSessionFactory().getCurrentSession();

		GiftLog giftLog = new GiftLog();
		giftLog.setRoleId(role.getRoleId());
		giftLog.setRoleName(role.getName());
		giftLog.setGiftId(giftTemplate.getId());
		giftLog.setCode(code);
		giftLog.setTime(new Timestamp(System.currentTimeMillis()));

		session.save(giftLog);
	}
}
