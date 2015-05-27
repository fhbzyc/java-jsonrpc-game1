package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Version;

@Repository
public class VersionDao extends BaseDao {

	public List<Version> findAll(int code) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Version> list = session.createCriteria(Version.class).add(Restrictions.gt("versionCode", code)).list();
		return list;
    }
}
