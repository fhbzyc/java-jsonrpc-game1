package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseMission;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.model.MissionModel;
import com.zhanglong.sg.result.Result;

@Repository
public class MissionDao extends BaseDao {

	@Resource
	private RedisTemplate<String, MissionModel> redisTemplate;

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

    private static String RedisKey = "MISSION_MAP1_";

    public List<BaseMission> findAll(Role role) throws Exception {

        MissionModel missionModel = (MissionModel) this.redisTemplate.opsForHash().get(RedisKey, role.getRoleId());

        List<BaseMission> list = new ArrayList<BaseMission>();
        
        if (missionModel == null) {

        	this.checkNew(role, list, new Result());

        } else {

            HashMap<Integer, Integer> map = missionModel.getMissionMap();

            for (Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<Integer, Integer> entry = iter.next();

                Integer missionId = entry.getKey();
                int num = entry.getValue();

                BaseMission mission = this.baseMissionDao.findOne(missionId);
                if (num < 0) {
                	mission.setComplete(true);
                	mission.setNum(mission.getGoal());
                } else {
                	mission.setNum(num);
                }

                list.add(mission);
            }
        }

        return list;
    }

    public void save(int roleId, List<BaseMission> missions) {

    	HashMap<Integer, Integer> missionMap = new HashMap<Integer, Integer>();

    	for (BaseMission baseMission : missions) {
    		
    		int num = baseMission.getNum();
    		if (baseMission.getComplete()) {
    			num = -1;
    		}

    		missionMap.put(baseMission.getId(), num);
		}

    	MissionModel missionModel = new MissionModel();
    	missionModel.setMissionMap(missionMap);

    	this.redisTemplate.opsForHash().put(RedisKey, roleId, missionModel);
    }

    private void check(Role role, String type, int target, int num, Result result) throws Exception {

    	List<BaseMission> list = this.findAll(role);

    	boolean find = false;
    	
        for (BaseMission mission : list) {
        	if (!mission.getComplete() && mission.getType().equals(type)) {
        		if (mission.getTarget() == target) {
        			mission.setNum(mission.getNum() + num);
        			result.addMission(mission);
        			find = true;
        		}
        	}
        }

        if (find) {
        	this.save(role.getRoleId(), list);
        }
    }


    private void check(Role role, String type, int num, Result result) throws Exception {

    	List<BaseMission> list = this.findAll(role);

    	boolean find = false;

        for (BaseMission mission : list) {
        	if (mission.getType().equals(type)) {
    			mission.setNum(mission.getNum() + num);
    			result.addMission(mission);
    			find = true;
        	}
        }

        if (find) {
        	this.save(role.getRoleId(), list);
        }
    }

    public void checkHeroNum(Role role, int num, Result result) throws Exception {
        this.check(role, TYPE_HERO_NUM, num, result);
    }

    public void checkLevel(Role role, Result result) throws Throwable {

    	List<BaseMission> list = this.findAll(role);

    	boolean find = false;

        for (BaseMission mission : list) {
            if (!mission.getComplete() && mission.getType().equals(TYPE_LEVELUP)) {

            	find = true;

            	mission.setNum(role.level());
                result.addMission(mission);
            }
        }

        if (find) {
        	this.save(role.getRoleId(), list);
        }
    }

    public void checkStory(Role role, int storyId, int num, Result result) throws Throwable {
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

    	List<BaseMission> list = this.findAll(role);

    	for (BaseMission mission : list) {
            if (mission.getId() == missionId) {
            // 完成的任务  status 设置为 -1
            	mission.setComplete(true);
            }
        }

    	this.save(role.getRoleId(), list);
    }

    public void newMission(Role role, Result result) throws Throwable {

    	this.checkNew(role, this.findAll(role), result);
    }

    private void checkNew(Role role, List<BaseMission> missions, Result result) throws CloneNotSupportedException {

         for (BaseMission temp : this.baseMissionDao.findAll()) {

        	 BaseMission baseMission = temp.clone();
        	 baseMission.setComplete(false);

             if (role.level() >= baseMission.getLevel()) {

             	boolean allreadyHave = false;
             	for (BaseMission mission : missions) {
 					if ((int)mission.getId() == (int)baseMission.getId()) {
 						allreadyHave = true;
 						break;
 					}
 				}

             	if (!allreadyHave) {

             		boolean find2 = false;
                 	if (baseMission.getParentId() != 0) {

                     	for (BaseMission mission : missions) {
         					if ((int)mission.getId() == (int)baseMission.getParentId() && mission.getComplete()) {
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

             			missions.add(baseMission);
             			result.addMission(baseMission);
                 	}
             	}
             }
         }

         this.save(role.getRoleId(), missions);
    }
}
