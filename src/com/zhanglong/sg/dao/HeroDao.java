package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.result.Result;

@Repository
public class HeroDao extends BaseDao {

	public static int[] MAX_LEVEL = new int[]{10,10,10,10,15,15,15,15,15,15,15,16,16,17,17,18,18,19,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90};

	@Resource
	private AchievementDao achievementDao;

	@Resource
	private BaseHeroDao baseHeroDao;

	@Resource
	private MissionDao missonDao;

	public List<Hero> findAll(int roleId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Hero.class);
		@SuppressWarnings("unchecked")
		List<Hero> list = criteria.add(Restrictions.eq("aRoleId", roleId)).list();
		return list;
	}

    public Hero findOne(int roleId, int heroId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Hero.class);
		
		@SuppressWarnings("unchecked")
		List<Hero> list = criteria.add(Restrictions.eq("aRoleId", roleId)).add(Restrictions.eq("heroId", heroId)).setMaxResults(1).list();
		if (list.size() == 0) {
			return null;
		}

		return list.get(0);
    }

	public void update(Hero hero, Result result) throws Exception {

		Session session = this.getSessionFactory().getCurrentSession();
		session.update(hero);
		result.addHero(hero);
	}

	public void delete(Hero hero) {

		Session session = this.getSessionFactory().getCurrentSession();
		session.delete(hero);
	}

    public Hero create(Role role, int heroId, Result result) throws Exception {

        Hero hero = new Hero();
        hero.setARoleId(role.getRoleId());
        hero.setHeroId(heroId);
        hero.setStar(this.baseHeroDao.findOne(heroId).getStar());
//        hero.setExp(0);
//        hero.setStr(0);
//        hero.setINT(0);
//        hero.setDex(0);
//        hero.setCLASS(0);
//        hero.setIsBattle(false);
        hero.setSkill1Level(1);
        hero.setSkill2Level(1);
        hero.setSkill3Level(1);
        hero.setSkill4Level(1);
        hero.setLevel(1);

        if (hero.getHeroId() == 10002) {
        	hero.setPosition(1);
        	hero.setIsBattle(true);
        }

        this.missonDao.checkHeroId(role, heroId, 1, result);
        this.missonDao.checkHeroNum(role, 1, result);

        result.addRandomItem(new int[]{heroId, 1});

        Session session = this.getSessionFactory().getCurrentSession();
        session.save(hero);

        // 刷新成就
		this.achievementDao.setNum(role.getRoleId(), BaseAchievement.TYPE_HERO_Id, heroId, 1, result);

        result.addHero(hero);
        return hero;
    }

    public int maxExp(int rolelevel) {
    	int maxLevel = MAX_LEVEL[rolelevel - 1];
    	return Hero.EXP[maxLevel - 1];
    }

    public int soulNumByStar(int heroId) throws Exception {

    	int star = this.baseHeroDao.findOne(heroId).getStar();

		if (star == 2) {
			return 18;
		} else if (star == 3) {
			return 30;
		} else {
			return 7;
		}
    }

    public void addExp(Hero hero, int rolelevel, int exp, Result result) throws Exception {

    	int maxExp = this.maxExp(rolelevel);

    	if (hero.getExp() < maxExp) {
        	hero.setExp(hero.getExp() + exp);
        	if (hero.getExp() > maxExp) {
        		hero.setExp(maxExp);
        	}
        	hero.setLevel(hero.level());
    	}

        result.addHero(hero);
    }
}
