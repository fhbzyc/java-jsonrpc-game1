package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Notice;

@Repository
public class NoticeDao extends BaseDao {

	public Notice findOne() {

        Session session = this.getSessionFactory().getCurrentSession();
        String sql = "SELECT * FROM base_notice WHERE NOW() BETWEEN notice_begin_time AND notice_end_time";

        @SuppressWarnings("unchecked")
		List<Notice> list = session.createSQLQuery(sql).addEntity(Notice.class).list();
        if (list.size() == 0) {
        	return null;
        }
        return (Notice) list.get(0);
    }
}
