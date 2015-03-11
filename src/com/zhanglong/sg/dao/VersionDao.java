package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.zhanglong.sg.entity.Version;

public class VersionDao extends BaseDao {

	public List<Version> findAll(int code) {

		Session session = this.getSessionFactory().getCurrentSession();
		return (List<Version>) session.createCriteria(Version.class).add(Restrictions.gt("versionCode", code));
    }
}
