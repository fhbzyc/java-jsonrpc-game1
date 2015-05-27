package com.zhanglong.sg.dao;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseCheckin;

@Repository
public class BaseCheckinDao extends BaseDao {

	public BaseCheckin findOne(int month) {

		Session session = this.getSessionFactory().getCurrentSession();
		return (BaseCheckin) session.get(BaseCheckin.class, month);
	}
}
