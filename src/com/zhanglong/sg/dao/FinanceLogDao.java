package com.zhanglong.sg.dao;

import java.math.BigInteger;
import java.sql.Timestamp;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.FinanceLog;

@Repository
public class FinanceLogDao extends BaseDao {

	public void create(FinanceLog financeLog) {
		Session session = this.getSessionFactory().getCurrentSession();
		financeLog.setTime(new Timestamp(System.currentTimeMillis()));
		session.save(financeLog);
	}

	public int count(int roleId, int stauts) {

		String sql = "SELECT COUNT(*) FROM role_finance_log WHERE finance_status = ? AND role_id = ? ";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, stauts);
		query.setParameter(1, roleId);

		return ((BigInteger) query.list().iterator().next()).intValue();
	}
}
