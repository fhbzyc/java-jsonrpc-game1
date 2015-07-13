package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseAchievement;

@Repository
public class BaseAchievementDao extends BaseDao2 {

	private static long myTime = 0l;

	private static List<BaseAchievement> achievements;

	@SuppressWarnings("unchecked")
	public List<BaseAchievement> findAll() {

		long time = System.currentTimeMillis();

		if (achievements == null || time - BaseAchievementDao.myTime > 5l * 60l * 1000l) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			achievements = session.createCriteria(BaseAchievement.class).list();
			
			for (BaseAchievement baseAchievement : achievements) {
	
				if (baseAchievement.getType().equals(BaseAchievement.TYPE_CRUSADE)) {
	
					baseAchievement.setMoneyType(4);
				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_HERO_CLASS)) {

					baseAchievement.setMoneyType(2);

				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_HERO_Id)) {

					baseAchievement.setMoneyType(2);

				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_KILL_NUM)) {

					baseAchievement.setMoneyType(1);

				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_PK)) {

					baseAchievement.setMoneyType(3);

				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_VIP)) {

					baseAchievement.setMoneyType(2);

				} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_STAR)) {

					baseAchievement.setMoneyType(1);
				}
			}
		}
		return achievements;
    }

	public BaseAchievement findOne(int achievementId) throws Exception {
		for (BaseAchievement baseAchievement : this.findAll()) {
			if (baseAchievement.getId() == achievementId) {
				return baseAchievement.clone();
			}
		}
		throw new Exception("achievement-id not found[id:" + achievementId + "]");
	}
}
