package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseSkill;

@Repository
public class BaseSkillDao extends BaseDao2 {

	private static List<BaseSkill> skills;

	public List<BaseSkill> findAll() {

		if (skills == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			@SuppressWarnings("unchecked")
			List<BaseSkill> list = session.createCriteria(BaseSkill.class).list();
			skills = list;
		}
		return skills;
    }
}
