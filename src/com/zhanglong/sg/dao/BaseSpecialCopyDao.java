package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseSpecialCopy;

@Repository
public class BaseSpecialCopyDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public List<BaseSpecialCopy> findTodayAll() {

        Session session = this.getSessionFactory().getCurrentSession();
        String sql = "SELECT * FROM base_special_copy WHERE week = (SELECT EXTRACT(DOW FROM NOW())) AND NOW() BETWEEN begin_time AND end_time";
        return session.createSQLQuery(sql).addEntity(BaseSpecialCopy.class).list();
	}
}
