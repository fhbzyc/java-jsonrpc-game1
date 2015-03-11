package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Mail;;

@Repository
public class MailDao extends BaseDao {

	public void create(Mail mail) {
		mail.setSendTime(System.currentTimeMillis());
		Session session = this.getSessionFactory().getCurrentSession();
		session.save(mail);
	}

	public void update(Mail mail) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(mail);
	}

	public void delete(Mail mail) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(mail);
	}

	public List<Mail> findAll(int roleId, int page) {

		long time = System.currentTimeMillis();
		String sql = "SELECT * FROM role_mail WHERE role_id = ? AND status != -1 AND ((attchment != '') OR (mail_time > ?)) ORDER BY id DESC LIMIT ? OFFSET ?";

        Session session = this.getSessionFactory().getCurrentSession();
        SQLQuery sqlQuery = session.createSQLQuery(sql);

        int pageNum = 40;

        sqlQuery.setParameter(1, roleId);
        sqlQuery.setParameter(2, time - (7l * 86400l * 1000l));
        sqlQuery.setParameter(3, pageNum);
        sqlQuery.setParameter(4, (page - 1) * pageNum);

        return sqlQuery.list();
	}

	public Mail findOne(int mailId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return (Mail) session.get(Mail.class, mailId);
	}
}
