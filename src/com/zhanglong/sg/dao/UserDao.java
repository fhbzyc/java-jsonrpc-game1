package com.zhanglong.sg.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	user.setTime(simpleDateFormat.format(new Date()));
    	session.save(user);
    }
    
    public User getByUsername(int platformId, String username) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	List<User> list = session.createCriteria(User.class).add(Restrictions.eq("platformId", platformId)).add(Restrictions.eq("name", username)).setMaxResults(1).list();
    	if (list.size() == 0) {
    		return null;
    	}

    	return list.get(0);
    }
}
