package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Skill;
import com.zhanglong.sg.entity2.BaseSkill;

@Repository
public class SkillDao extends BaseDao {

	@Resource
	private BaseSkillDao baseSkillDao;

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

    public List<Object> comboSkills(int roleId) throws Exception {

    	List<Skill> skills = this.findAll(roleId);

		List<BaseSkill> baseSkills = this.baseSkillDao.findAll();
		for (BaseSkill baseSkill : baseSkills) {
			if (baseSkill.getCombo()) {
				boolean find = false;
				for (Skill skill : skills) {
					if ((int)skill.getSkillId() == (int)baseSkill.getId()) {
						find = true;
						break;
					}
				}
				if (!find) {
					Skill skill = new Skill();
					skill.setARoleId(roleId);
					skill.setLevel(1);
					skill.setSkillId(baseSkill.getId());
					skills.add(skill);
				}
			}
		}

		List<Object> list = new ArrayList<Object>();
		for (Skill skill : skills) {
			list.add(skill.toArray());
		}

		return list;
    }
}
