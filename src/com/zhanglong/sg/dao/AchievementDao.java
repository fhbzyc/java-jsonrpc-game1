package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Achievement;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.result.Result;

@Repository
public class AchievementDao extends BaseDao {


	@Resource
	private HeroDao heroDao;

	@Resource
	private StoryDao storyDao;

	@Resource
	private BaseAchievementDao baseAchievementDao;

	public AchievementDao() {
	}

    public List<Achievement> findAll(int roleId)  {

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
    	List<Achievement> achievements = session.createCriteria(Achievement.class).add(Restrictions.eq("aRoleId", roleId)).list();

        if (achievements.size() == 0) {
        	List<BaseAchievement> baseAchs = this.baseAchievementDao.findAll();
        	for (BaseAchievement baseAch : baseAchs) {

     			Achievement achievement = new Achievement();
     			achievement.setAchievementId(baseAch.getId());
     			achievement.setARoleId(roleId);
     			achievement.setType(baseAch.getType());
     			this.save(achievement);

     			achievements.add(achievement);
			}
        }

        return achievements;
    }

    public void save(Achievement achievement) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	session.save(achievement);
    }
    
    public void update(Achievement achievement) {

    	Session session = this.getSessionFactory().getCurrentSession();
    	session.update(achievement);
    }

    public void setNum(int roleId, String type, int target, int num, Result result) {
		List<Achievement> list = this.findAll(roleId);
		List<BaseAchievement> achievements = this.baseAchievementDao.findAll();

		for (BaseAchievement baseAch : achievements) {

			if (!baseAch.getType().equals(type) || baseAch.getTarget() != target) {
				continue;
			}

			boolean find = false;
			for (Achievement achievement : list) {
				if (achievement.getAchievementId() == baseAch.getId() && achievement.getComplete()) {
					find = true;
					break;
				}
			}
			if (find) {
				continue;
			}
			baseAch.setNum(num);
			result.addAchievement(baseAch);
		}
    }
}
