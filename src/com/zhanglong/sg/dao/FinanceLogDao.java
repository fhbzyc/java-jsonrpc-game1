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

	public int sum(int roleId, int moneyType, Timestamp beginTime) {

		String sql = "SELECT SUM(finance_old_money - finance_new_money) FROM role_finance_log WHERE finance_money_type = ? AND role_id = ? AND finance_time > ? AND finance_old_money > finance_new_money";
		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter(0, moneyType);
		query.setParameter(1, roleId);
		query.setParameter(2, beginTime);

		Object sum = query.list().get(0);
		if (sum != null) {
			return ((BigInteger) sum).intValue();
		}

		return 0;
	}

}
