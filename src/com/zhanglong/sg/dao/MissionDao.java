package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Mission;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseMission;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.result.Result;

@Repository
public class MissionDao extends BaseDao {

	public static ThreadLocal<List<Mission>> cache = new ThreadLocal<List<Mission>>(); 

	@Resource
	private HeroDao heroDao;

	@Resource
	private StoryDao storyDao;

	@Resource
	private BaseMissionDao baseMissionDao;

	public MissionDao() {
	}

	public static String TYPE_COPY = "copy";
	public static String TYPE_HERO_COPY = "hero_copy";
	public static String TYPE_LEVELUP = "levelup";
	public static String TYPE_HERO_NUM = "general_num";
	public static String TYPE_ITEM_NUM = "item_num";
	public static String TYPE_HERO_CLASS = "general_class";
	public static String TYPE_CALL_HERO = "call_hero";

    public List<Mission> findAll(Role role) throws Exception {

    	List<Mission> missions2 = cache.get();
    	if (missions2 != null) {
    		return missions2;
    	}

    	Session session = this.getSessionFactory().getCurrentSession();

    	@SuppressWarnings("unchecked")
    	List<Mission> missions = session.createCriteria(Mission.class).add(Restrictions.eq("aRoleId", role.getRoleId())).list();

        if (missions.size() == 0) {
        	this.checkNew(role, missions, new Result());
        }

        cache.set(missions);
        return missions;
    }

    private void check(Role role, String type, int target, int num, Result result) throws Exception {

    	List<Mission> list = this.findAll(role);

        for (Mission mission : list) {
        	if (!mission.getComplete() && mission.getType().equals(type)) {
        		
        		BaseMission baseMission = this.baseMissionDao.findOne(mission.getMissionId());
        		
        		if (baseMission.getTarget() == target) {
        			mission.setNum(mission.getNum() + num);
        			baseMission.setNum(mission.getNum());
        			result.addMission(baseMission);
        		}
        	}
        }
    }

    private void check(Role role, String type, int num, Result result) throws Exception {

    	List<Mission> list = this.findAll(role);

        for (Mission mission : list) {
        	if (!mission.getComplete() && mission.getType().equals(type)) {
    			mission.setNum(mission.getNum() + num);

    			BaseMission baseMission = this.baseMissionDao.findOne(mission.getMissionId());
    			baseMission.setNum(mission.getNum());
    			result.addMission(baseMission);
        	}
        }
    }

    public void checkHeroNum(Role role, int num, Result result) throws Exception {
        this.check(role, TYPE_HERO_NUM, num, result);
    }

    public void checkLevel(Role role, Result result) throws Throwable {

    	List<Mission> list = this.findAll(role);

        for (Mission mission : list) {
            if (!mission.getComplete() && mission.getType().equals(TYPE_LEVELUP)) {

            	mission.setNum(role.level());
            	
    			BaseMission baseMission = this.baseMissionDao.findOne(mission.getMissionId());
    			baseMission.setNum(mission.getNum());
                result.addMission(baseMission);
            }
        }
    }

    public void checkStory(Role role, int storyId, int num, Result result) throws Exception {
        this.check(role, TYPE_COPY, storyId, num, result);
    }


    public void checkHeroStory(Role role, int storyId, int num, Result result) throws Exception {
    	this.check(role, TYPE_HERO_COPY, storyId, num, result);
    }


    public void checkHeroClass(Role role, int CLASS, int num, Result result) throws Exception {
    	this.check(role, TYPE_HERO_CLASS, CLASS, num, result);
    }

    public void checkHeroId(Role role, int heroId, int num, Result result) throws Exception {
        check(role, TYPE_CALL_HERO, heroId, num, result);
    }

    public void complete(Role role, int missionId, Result result) throws Exception {

    	List<Mission> list = this.findAll(role);

    	for (Mission mission : list) {
            if (mission.getMissionId() == missionId) {
            	mission.setComplete(true);
            	this.getSessionFactory().getCurrentSession().update(mission);
            }
        }
    }

    public void newMission(Role role, Result result) throws Exception {

    	this.checkNew(role, this.findAll(role), result);
    }

    private void checkNew(Role role, List<Mission> missions, Result result) throws CloneNotSupportedException {

         for (BaseMission temp : this.baseMissionDao.findAll()) {

        	 BaseMission baseMission = temp.clone();
        	 baseMission.setComplete(false);

             if (role.level() >= baseMission.getLevel()) {

             	boolean allreadyHave = false;
             	for (Mission mission : missions) {
 					if ((int)mission.getMissionId() == (int)baseMission.getId()) {
 						allreadyHave = true;
 						break;
 					}
 				}

             	if (!allreadyHave) {

             		boolean find2 = false;
                 	if (baseMission.getParentId() != 0) {

                     	for (Mission mission : missions) {
         					if ((int)mission.getMissionId() == (int)baseMission.getParentId() && mission.getComplete()) {
         						find2 = true;
         						break;
         					}
         				}

                 	} else {
                 		find2 = true;
                 	}

                 	if (find2) {
             			if (baseMission.getType().equals(TYPE_LEVELUP)) {

                 			baseMission.setNum(role.level());

                        } else if (baseMission.getType().equals(TYPE_HERO_NUM)) {

                         	baseMission.setNum(this.heroDao.findAll(role.getRoleId()).size());

                        } else if (baseMission.getType().equals(TYPE_HERO_CLASS)) {

                         	List<Hero> gs = this.heroDao.findAll(role.getRoleId());

                         	int n = 0;
                         	for (Hero generalTable : gs) {
 								if ((int)generalTable.getCLASS() >= (int)baseMission.getTarget()) {
 									n++;
 								}
 							}

                         	baseMission.setNum(n);
                        } else if (baseMission.getType().equals(TYPE_COPY)) {

                         	Story story = this.storyDao.findOne(role.getRoleId(), baseMission.getTarget(), BaseStory.COPY_TYPE);

                         	if (story != null && story.getStar() > 0) {
                         		baseMission.setNum(1);
                         	}
                        } else if (baseMission.getType().equals(TYPE_HERO_COPY)) {

                         	Story story = this.storyDao.findOne(role.getRoleId(), baseMission.getTarget(), BaseStory.HERO_COPY_TYPE);

                         	if (story != null && story.getStar() > 0) {
                         		baseMission.setNum(1);
                         	}
                        }

             			Mission mission = new Mission();
             			mission.setMissionId(baseMission.getId());
             			mission.setARoleId(role.getRoleId());
             			mission.setNum(baseMission.getNum());
             			mission.setType(baseMission.getType());
             			Session session = this.getSessionFactory().getCurrentSession();
             			session.save(mission);

             			missions.add(mission);

             			result.addMission(baseMission);
                 	}
             	}
             }
         }
    }
}
