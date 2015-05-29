package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Server;

@Repository
public class ServerDao extends BaseDao {

	public List<Server> findAll() {
		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Server> list = session.createCriteria(Server.class).list();
		return list;
	}

	public Server findOne(int serverId) {
		Session session = this.getSessionFactory().getCurrentSession();
		Object object = session.get(Server.class, serverId);
		if (object == null) {
			return null;
		}
		return (Server)object;
	}
}
