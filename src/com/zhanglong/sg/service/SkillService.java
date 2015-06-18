package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.BaseSkillDao;
import com.zhanglong.sg.dao.SkillDao;
import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.entity.Skill;
import com.zhanglong.sg.entity2.BaseSkill;
import com.zhanglong.sg.result.Result;

@Service
public class SkillService extends BaseService {

	@Resource
	private SkillDao skillDao;

	@Resource
	private BaseSkillDao baseSkillDao;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object list() throws Exception {

		int roleId = this.roleId();
		List<Skill> skills = this.skillDao.findAll(roleId);

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

		Result result = new Result();
		result.setValue("combo_skill", list);

		return this.success(result.toMap());
	}

	/**
	 * 升级
	 * @param skillId
	 * @return
	 * @throws Exception
	 */
	public Object levelUp(int skillId) throws Exception {

		int roleId = this.roleId();

		BaseSkill baseSkill = this.baseSkillDao.findOne(skillId);
		if (!baseSkill.getCombo()) {
			return returnError(this.lineNum(), "不是组合技不能升级");
		}

		int num = 0;
		Skill skill = this.skillDao.findOne(roleId, skillId);
		if (skill == null) {
			skill = new Skill();
			skill.setARoleId(roleId);
			skill.setLevel(1);
			skill.setSkillId(baseSkill.getId());
		}
		num = skill.levelNum();

		if (num == 0) {
			return returnError(this.lineNum(), "技能已经满级");
		}

		// 扣缘分石
		Item item = this.itemDao.findOneByItemId(roleId, 4231);

		Result result = new Result();

		if (item == null || item.getNum() < num) {
			return returnError(this.lineNum(), "缘分石数量不足");
		} else {
			this.itemDao.subItem(item, num, result);
		}

		skill.level += 1;

		if (skill.level == 2) {
			this.skillDao.save(skill);
		} else {
			this.skillDao.update(skill);
		}

		List<Object> list = new ArrayList<Object>();
		list.add(skill.toArray());

		result.setValue("combo_skill", list);
		return this.success(result.toMap());
	}
}
