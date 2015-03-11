package com.zhanglong.sg.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseSpecialCopy;

@Repository
public class BaseSpecialCopyDao extends BaseDao {

	public List<BaseSpecialCopy> findTodayAll() {

	    Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int week =  c.get(Calendar.DAY_OF_WEEK) - 1;
        
        Session session = this.getSessionFactory().getCurrentSession();
        return session.createCriteria(BaseSpecialCopy.class).add(Restrictions.eq("week", week)).list();
	}
}
