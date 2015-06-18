package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseSkill;

@Repository
public class BaseSkillDao extends BaseDao2 {

	private static List<BaseSkill> skills;

	public List<BaseSkill> findAll() {

		if (BaseSkillDao.skills == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			@SuppressWarnings("unchecked")
			List<BaseSkill> list = session.createCriteria(BaseSkill.class).list();
			BaseSkillDao.skills = list;
		}
		return BaseSkillDao.skills;
    }

	public BaseSkill findOne(int skillId) throws Exception {
		for (BaseSkill baseSkill : BaseSkillDao.skills) {
			if (baseSkill.getId() == skillId) {
				return baseSkill;
			}
		}
		throw new Exception("不存在的技能[:" + skillId + "]");
	}
}
