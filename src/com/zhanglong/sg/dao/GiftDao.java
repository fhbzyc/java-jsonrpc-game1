package com.zhanglong.sg.dao;

import java.sql.Timestamp;

import org.hibernate.Session;
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
