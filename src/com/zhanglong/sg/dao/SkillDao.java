package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Skill;

@Repository
public class SkillDao extends BaseDao {

	public List<Skill> findAll(int roleId) {
		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Skill.class);
		@SuppressWarnings("unchecked")
		List<Skill> list = criteria.add(Restrictions.eq("aRoleId", roleId)).list();
		return list;
	}

    public Skill findOne(int roleId, int skillId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Skill.class);

		@SuppressWarnings("unchecked")
		List<Skill> list = criteria.add(Restrictions.eq("aRoleId", roleId)).add(Restrictions.eq("skillId", skillId)).setMaxResults(1).list();
		if (list.size() == 0) {
			return null;
		}

		return list.get(0);
    }

    public Skill save(Skill skill) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(skill);
		return skill;
    }

    public void update(Skill skill) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.update(skill);
    }
}
