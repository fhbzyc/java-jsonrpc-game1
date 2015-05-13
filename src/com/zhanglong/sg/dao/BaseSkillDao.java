package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseItem;
import com.zhanglong.sg.entity.BaseSkill;

@Repository
public class BaseSkillDao extends BaseDao {

	private static List<BaseSkill> skills;

	public List<BaseSkill> findAll() {

		if (skills == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			skills = session.createCriteria(BaseSkill.class).list();
		}
		return skills;
    }
}
