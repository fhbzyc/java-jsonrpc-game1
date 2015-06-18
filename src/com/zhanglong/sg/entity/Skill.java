package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.zhanglong.sg.dao.BaseSkillDao;
import com.zhanglong.sg.entity2.BaseSkill;
import com.zhanglong.sg.utils.SpringContextUtils;

@Entity
@IdClass(SkillPK.class)
@Table(name = "role_skills")
public class Skill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "role_id")
    public int aRoleId;

	@Id
	@Column(name = "skill_id")
    public int skillId;

	@Column(name = "skill_level" , nullable = false , columnDefinition = "int default 0")
    public int level;

	public Skill() {
	}

	public int getARoleId() {
		return aRoleId;
	}

	public void setARoleId(int aRoleId) {
		this.aRoleId = aRoleId;
	}

	public int getSkillId() {
		return skillId;
	}

	public void setSkillId(int skillId) {
		this.skillId = skillId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int[] toArray() throws Exception {

		BaseSkillDao baseSkillDao = (BaseSkillDao) SpringContextUtils.getBean(BaseSkillDao.class);
		BaseSkill skill = baseSkillDao.findOne(this.skillId);

		return new int[]{this.skillId , this.level , skill.getMaxLevel() , this.levelNum()};
	}

	public int levelNum() throws Exception {

		BaseSkillDao baseSkillDao = (BaseSkillDao) SpringContextUtils.getBean(BaseSkillDao.class);

		BaseSkill skill = baseSkillDao.findOne(this.skillId);
		if (this.level >= skill.getMaxLevel()) {
			return 0;
		}
		return skill.getBaseCoin() + (this.level - 1) * skill.getLevelupCoin();
	}
}
