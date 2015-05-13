package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Mail;

@Repository
public class MailDao extends BaseDao {

	public void create(Mail mail) {

		mail.setStatus(0);
		mail.setSendTime(new Timestamp(System.currentTimeMillis()));

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

	@SuppressWarnings("unchecked")
	public List<Mail> findAll(int roleId, int page) {

        Session session = this.getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(Mail.class);
        criteria.add(Restrictions.eq("roleId", roleId));
        criteria.addOrder(Order.desc("id"));
        criteria.setFirstResult((page - 1) * 40);
        criteria.setMaxResults(40);

        return criteria.list();
	}

	public Mail findOne(int mailId) {
		Session session = this.getSessionFactory().getCurrentSession();
		return (Mail) session.get(Mail.class, mailId);
	}

	public void del(int days) {

		SessionFactory sessionFactory = this.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        String sql = "DELETE FROM role_mail WHERE mail_status = " + Mail.READ + " AND mail_time < now() - interval '" + days + " days'";

        session.createSQLQuery(sql).executeUpdate();
        transaction.commit();
        session.close();
	}
}
