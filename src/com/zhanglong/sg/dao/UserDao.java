package com.zhanglong.sg.dao;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.User;

@Repository
public class UserDao extends BaseDao {

    public User findOne(int userId) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	return (User) session.get(User.class, userId);
    }

    public void create(User user) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	user.setTime(timestamp);
    	user.setSigninTime(timestamp);
    	session.save(user);
    }

    public void update(User user) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	user.setSigninTime(timestamp);
    	session.update(user);
    }

    public User getByUsername(String username) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	@SuppressWarnings("unchecked")
		List<User> list = session.createCriteria(User.class).add(Restrictions.eq("userName", username)).setMaxResults(1).list();

    	if (list.size() == 0) {
    		return null;
    	}

    	return list.get(0);
    }
    
    public User getByUsername(int platformId, String username) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
		List<User> list = session.createCriteria(User.class).add(Restrictions.eq("platformId", platformId)).add(Restrictions.eq("userName", username)).setMaxResults(1).list();
    	if (list.size() == 0) {
    		return null;
    	}

    	return list.get(0);
    }

    public User getByImei(String imei) {

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
		List<User> list = session.createCriteria(User.class).add(Restrictions.eq("imei", imei)).add(Restrictions.eq("registerType", User.QUICK_REG)).setMaxResults(1).list();
    	if (list.size() == 0) {
    		return null;
    	}

    	return list.get(0);
    }
}
