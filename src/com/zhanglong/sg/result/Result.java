package com.zhanglong.sg.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.zhanglong.sg.dao.BaseItemDao;
import com.zhanglong.sg.dao.BaseStoryDao;
import com.zhanglong.sg.dao.HeroDao;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.entity2.BaseDailyTask;
import com.zhanglong.sg.entity2.BaseItem;
import com.zhanglong.sg.entity2.BaseMission;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.utils.SpringContextUtils;

public class Result {

	private HashMap<String, Object> map = new HashMap<String, Object>();

    private List<Object> generalList;
    private List<Object> itemList;

    private List<BaseMission> missionList;
    
    private List<BaseAchievement> achievements;

    private ArrayList<BaseDailyTask> dailyTaskList;

    private ArrayList<int[]> copyList = new ArrayList<int[]>();
    private ArrayList<int[]> heroCopyList = new ArrayList<int[]>();

    private ArrayList<int[]> random_result = new ArrayList<int[]>();

    private HashMap<String, Integer> money;
    private HashMap<String, Integer> team;
    private long[] ap;
    
    private int addAp = 0;

    private Integer arena_money = null;

    public Result() {
    }

    public void setArenaMoney(int money) {
        this.arena_money = money;
    }

    public void addAp(int ap) {
        this.addAp += ap;
    }

    public void setTeam(int exp) {

        int level = Role.toLevel(exp);
        int index = level - 2;

        if (index >= 0) {
            exp -= Role.EXP[index];
        }

  //      this.addPhysicalStrength += addPhysicalStrength;
        
        this.team = new HashMap<String, Integer>();
        this.team.put("exp", exp);
        this.team.put("level", level);
        this.team.put("max_level", HeroDao.MAX_LEVEL[level - 1]);
 //       this.team.put("add_mp", addPhysicalStrength);
    }

    public void setMoney(int coin, int gold) {

        this.money = new HashMap<String, Integer>();
        this.money.put("coin", coin);
        this.money.put("gold", gold);
    }





    public void addMission(BaseMission mission) {
        if (this.missionList == null) {
            this.missionList = new ArrayList<BaseMission>();
        }
        this.missionList.add(mission);
    }

//    public void setDailyTask(ArrayList<DailyTask> dailyTaskList) {
//        this.dailyTaskList = dailyTaskList;
//    }

    public void addDailyTask(BaseDailyTask dailyTask) {
        if (this.dailyTaskList == null) {
            this.dailyTaskList = new ArrayList<BaseDailyTask>();
        }
        this.dailyTaskList.add(dailyTask);
    }

    public void setAp(int ap, int coolTime) {
        this.ap = new long[]{(long)ap, (long)coolTime * 1000l};
    }

    public void setCopyList(ArrayList<int[]> copyList) {
        this.copyList = copyList;
    }

    public void setHeroCopyList(ArrayList<int[]> heroCopyList) {
        this.heroCopyList = heroCopyList;
    }

    public void addItem(Item item) throws Exception {

        if (this.itemList == null) {
            this.itemList = new ArrayList<Object>();
        }

        BaseItemDao baseItemDao = (BaseItemDao) SpringContextUtils.getBean(BaseItemDao.class);
        BaseItem baseItem = baseItemDao.findOne(item.getItemId());

        int[] itemData = new int[5];
        itemData[0] = item.getItemId();
        itemData[1] = item.getNum();
        itemData[2] = item.getId();
        itemData[3] = baseItem.getSellCoin();
        itemData[4] = baseItem.getExp();

        this.itemList.add(itemData);
    }

    public Object[] addHero(Hero hero) throws Exception {

        if (this.generalList == null) {
            this.generalList = new ArrayList<Object>();
        }

        this.generalList.add(hero.toArray());

        return hero.toArray();
    }

    public void addRandomItem(int[] item) {
        this.random_result.add(item);
    }

    public void addCopy(Story story) throws Exception {
        
        story.init();

        if (story.getType() == BaseStory.COPY_TYPE) {

        	BaseStoryDao baseStoryDao = (BaseStoryDao) SpringContextUtils.getBean(BaseStoryDao.class);
            BaseStory baseStory = baseStoryDao.findOne(story.getStoryId(), BaseStory.COPY_TYPE);

            if (baseStory == null) {
            	throw new Exception("不存在的关卡 , 普通[" + story.getStoryId() + "]");
            }

            this.copyList.add(new int[]{story.getStoryId() , story.getStar() , 0 , baseStory.getTeamExp() , 0 , 0});
        } else if (story.getType() == BaseStory.HERO_COPY_TYPE) {

            int num = 3 - story.getNum();
            if (num < 0) {
                num = 0;
            }

        	BaseStoryDao baseStoryDao = (BaseStoryDao) SpringContextUtils.getBean(BaseStoryDao.class);
            BaseStory baseStory = baseStoryDao.findOne(story.getStoryId(), BaseStory.HERO_COPY_TYPE);

            if (baseStory == null) {
            	throw new Exception("不存在的关卡 , 精英[" + story.getStoryId() + "]");
            }

            this.heroCopyList.add(new int[]{story.getStoryId() , story.getStar() , num , baseStory.getTeamExp() , story.needGold() , story.getBuyNum()});
        }
    }

    public void addAchievement(BaseAchievement ach) {
        if (this.achievements == null) {
            this.achievements = new ArrayList<BaseAchievement>();
        }
    	this.achievements.add(ach);
    }

    public void setValue(String key, Object value) {
    	this.map.put(key, value);
    }

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<String, Object>();
        if (this.money != null) {
            result.put("money", this.money);
        }
        if (this.itemList != null) {
            result.put("item", this.itemList);
        }
        if (this.generalList != null) {
            result.put("general", this.generalList);
        }
        if (this.ap != null) {
            result.put("mp", this.ap);
        }

        if (this.missionList != null) {
            result.put("task", this.missionList);
        }

        if (this.achievements != null) {

        	List<Object> achs = new ArrayList<Object>();
        	for (BaseAchievement ach : this.achievements) {
        		achs.add(new Object[]{ach.getId() , ach.getName() , ach.getDesc() , ach.getType() , ach.getNum() ,  ach.getGoal() , ach.getMoneyType() , ach.getMoney() , ach.getReward() , ach.getOther()});
			}

            result.put("ach", achs);
        }

        if (this.dailyTaskList != null) {
            result.put("daily_task", this.dailyTaskList);
        }

        if (this.team != null) {
            this.team.put("add_mp", this.addAp);
            result.put("team", this.team);
        }

        if (this.copyList.size() != 0) {
            result.put("copy", this.copyList);
        }

        if (this.heroCopyList.size() != 0) {
            result.put("hero_copy", this.heroCopyList);
        }

        if (this.random_result.size() != 0) {
            result.put("random_result", this.random_result);
        }

        if (this.arena_money != null) {
            result.put("arena_money", this.arena_money);
        }

        if (this.map.size() > 0) {

            for (Iterator<Map.Entry<String, Object>> iter = this.map.entrySet().iterator(); iter.hasNext();) {
                Map.Entry<String, Object> entry = iter.next();

                result.put(entry.getKey(), entry.getValue());
            }
        }

        result.put("time", System.currentTimeMillis());
        return result;
    }
}
