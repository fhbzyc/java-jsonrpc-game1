package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.result.*;
import com.zhanglong.sg.dao.AchievementDao;
import com.zhanglong.sg.dao.BaseStoryDao;
import com.zhanglong.sg.dao.BattleLogDao;
import com.zhanglong.sg.dao.CopyStarDao;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.dao.StoryDao;
import com.zhanglong.sg.entity.BattleLog;
import com.zhanglong.sg.entity.CopyStar;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.model.CopyStarModel;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class StoryService extends BaseService {

	@Resource
	private AchievementDao achievementDao;

	@Resource
	private StoryDao storyDao;

	@Resource
	private BaseStoryDao baseStoryDao;

	@Resource
	private BattleLogDao battleLogDao;

	@Resource
	private CopyStarDao copyStarDao;

    @Resource
    private PowerDao powerDao;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object list() throws Exception {

        int roleId = this.roleId();

        List<Story> queryList = this.storyDao.findAll(roleId);

        Result result = new Result();

        for (Story story : queryList) {
            result.addCopy(story);
        }

        return this.success(result.toMap());
    }

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
    public Object items() throws Exception {

    	int roleId = this.roleId();

    	List<BaseStory> list1 = this.baseStoryDao.copys();
    	List<BaseStory> list2 = this.baseStoryDao.heroCopys();

    	ArrayList<Object> copyList = new ArrayList<Object>();

    	for (int i = 0 ; i < list1.size() ; i++) {

    		int[][] item = this.baseStoryDao.itemIds(list1.get(i));

    		int[] ite = new int[item.length];

    		for (int j = 0; j < item.length; j++) {
    			ite[j] = item[j][0];
			}

    		copyList.add(ite);
		}

    	ArrayList<Object> heroCopyList = new ArrayList<Object>();

    	for (int i = 0 ; i < list2.size() ; i++) {
    		int[][] item = this.baseStoryDao.itemIds(list2.get(i));
    		
    		int[] ite = new int[item.length];
    		
    		for (int j = 0; j < item.length; j++) {
    			ite[j] = item[j][0];
			}

    		heroCopyList.add(ite);
		}

    	List<ArrayList<Object>> star1 = new ArrayList<ArrayList<Object>>();
    	List<ArrayList<Object>> star2 = new ArrayList<ArrayList<Object>>();

    	Result result = new Result();

    	List<CopyStarModel> stars = this.copyStarDao.configs();

    	for (CopyStarModel copyStar : stars) {
			if (copyStar.getType() == 1) {
				if (star1.size() < copyStar.getChapter()) {
					star1.add(new ArrayList<Object>());
				}
				ArrayList<Object> arrayList = star1.get(star1.size() - 1);
				arrayList.add(copyStar.getItems());
			} else {
				if (star2.size() < copyStar.getChapter()) {
					star2.add(new ArrayList<Object>());
				}
				ArrayList<Object> arrayList = star2.get(star2.size() - 1);
				arrayList.add(copyStar.getItems());
			}
		}

    	List<ArrayList<Boolean>> get1 = new ArrayList<ArrayList<Boolean>>();
    	List<ArrayList<Boolean>> get2 = new ArrayList<ArrayList<Boolean>>();

    	List<CopyStar> list = this.copyStarDao.findAll(roleId);

    	for (int i = 0 ; i < star1.size() ; i++) {
    		if (get1.size() <= i) {
    			get1.add(new ArrayList<Boolean>());
    		}
			ArrayList<Boolean> array = get1.get(i);
			
			boolean find1 = false;
			boolean find2 = false;
			boolean find3 = false;

			for (CopyStar copyStar : list) {
				if (copyStar.getType() == 1 && copyStar.getChapter() == i + 1) {
					if (copyStar.getStar() == 18) {
						find1 = true;
					}
					if (copyStar.getStar() == 36) {
						find2 = true;
					}
					if (copyStar.getStar() == 54) {
						find3 = true;
					}
				}
			}
			array.add(find1);
			array.add(find2);
			array.add(find3);
    	}

    	for (int i = 0 ; i < star2.size() ; i++) {
    		if (get2.size() <= i) {
    			get2.add(new ArrayList<Boolean>());
    		}
			ArrayList<Boolean> array = get2.get(i);
			
			boolean find1 = false;
			boolean find2 = false;
			boolean find3 = false;

			for (CopyStar copyStar : list) {
				if (copyStar.getType() == 2 && copyStar.getChapter() == i + 1) {
					if (copyStar.getStar() == 6) {
						find1 = true;
					}
					if (copyStar.getStar() == 12) {
						find2 = true;
					}
					if (copyStar.getStar() == 18) {
						find3 = true;
					}
				}
			}
			array.add(find1);
			array.add(find2);
			array.add(find3);
    	}

    	result.setValue("story", copyList);
    	result.setValue("hero_story", heroCopyList);
    	result.setValue("star1", star1);
    	result.setValue("star2", star2);
    	result.setValue("get1", get1);
    	result.setValue("get2", get2);

    	return this.success(result.toMap());
    }

    /**
     * 
     * @param storyId
     * @param storyType
     * @param heroId1
     * @param heroId2
     * @param heroId3
     * @param heroId4
     * @param power
     * @return
     * @throws Exception
     */
    public Object beginBattle(int storyId, int storyType, int heroId1, int heroId2, int heroId3, int heroId4, int power) throws Exception {

    	int roleId = this.roleId();

        if (storyId <= 0) {
            return this.returnError(this.lineNum(), "参数出错");
        }

        if (storyType != Story.COPY_TYPE && storyType != Story.HERO_COPY_TYPE) {
            return this.returnError(this.lineNum(), "参数出错");
        }

//        List<BaseStory> baseStoryList;
//        if (storyType == Story.COPY_TYPE) {
//            baseStoryList = this.baseStoryDao.copys();
//        } else {
//            baseStoryList = this.baseStoryDao.heroCopys();
//        }

        BaseStory baseStory = this.baseStoryDao.findOne(storyId, storyType);
        if (baseStory == null) {
            return this.returnError(this.lineNum(), "没有这个关卡");
        }

        List<Hero> heros = this.heroDao.findAll(roleId);
        List<Hero> hero2 = new ArrayList<Hero>();

        boolean find1 = false;
        boolean find2 = false;
        boolean find3 = false;
        boolean find4 = false;
        for (Hero hero : heros) {
            if (heroId1 != 0 && (int)hero.getHeroId() == heroId1) {
                find1 = true;
                hero2.add(hero);
            }
            if (heroId2 != 0 && (int)hero.getHeroId() == heroId2) {
                find2 = true;
                hero2.add(hero);
            }
            if (heroId3 != 0 && (int)hero.getHeroId() == heroId3) {
                find3 = true;
                hero2.add(hero);
            }
            if (heroId4 != 0 && (int)hero.getHeroId() == heroId4) {
                find4 = true;
                hero2.add(hero);
            }
        }

        if (heroId1 != 0 && !find1 || heroId2 != 0 && !find2 || heroId3 != 0 && !find3 || heroId4 != 0 && !find4) {
            return this.returnError(this.lineNum(), "未拥有的武将 ");
        }

        Role role = roleDao.findOne(roleId);

        this.powerDao.save(role, power, hero2);

        if (storyId == 1 && storyType == BaseStory.COPY_TYPE) {
            // 无条件攻打
        } else if (storyId == 1) {
            Story story = this.storyDao.findOne(roleId, storyId, storyType - 1);
            if(story == null) {
                return this.returnError(this.lineNum(), "出错,你还不能攻打精英关卡");
            }
            if (story.getStar() <= 0) {
                return this.returnError(this.lineNum(), "出错,你还不能攻打精英关卡");
            }
        } else {
            Story story = this.storyDao.findOne(roleId, storyId, storyType);
            if(story == null) {
                return this.returnError(this.lineNum(), "出错,你还不能攻打这个关卡");
            }
        }

        if (storyType == BaseStory.HERO_COPY_TYPE) {
            Story story = this.storyDao.findOne(roleId, storyId, storyType);
            if (story.getNum() >= 3) {
                return this.returnError(this.lineNum(), "挑战次数不足");
            }
        }

        int physicalStrength = role.ap();
        if (physicalStrength < baseStory.getTeamExp()) {
            return this.returnError(this.lineNum(), "体力不足,不能攻打");
        }

        int unixTime = (int)(System.currentTimeMillis() / 1000);

        Result result = new Result();

        BattleLog battleLogTable = new BattleLog();
        battleLogTable.setRoleId(roleId);
        battleLogTable.setBattleResult(BattleLog.BATTLE_LOG_INIT);
        battleLogTable.setBeginTime(unixTime);
        battleLogTable.setEndTime(unixTime);
        battleLogTable.setStoryId(storyId);
        battleLogTable.setStoryType(storyType);
        battleLogTable.setHeroId1(heroId1);
        battleLogTable.setHeroId2(heroId2);
        battleLogTable.setHeroId3(heroId3);
        battleLogTable.setHeroId4(heroId4);
        battleLogTable.setData("");

        this.battleLogDao.create(battleLogTable);

        this.roleDao.update(role, result);

        result.setValue("battle_id", battleLogTable.getId());

        List<int[]> rand = this.baseStoryDao.randomItems(roleId, baseStory, 1, this.serverId()).get(0);
        List<int[]> items = new ArrayList<int[]>();
        for (int[] is : rand) {
            items.add(new int[]{is[0] , is[1] , 100});
        }
        
        if (items.size() == 0 && storyId == 1 && storyType == Story.COPY_TYPE) {
        	// 第一关至少掉一个道具
        	items.add(new int[]{3000 , 1 , 100});
        }

        result.setValue("items", items);
        result.setValue("coin", baseStory.getCoin());
        return this.success(result.toMap());
    }

    /**
     * 
     * @param battleId
     * @param win
     * @param coin
     * @param itemIds
     * @param itemNum
     * @param star
     * @return
     * @throws Exception
     */
    public Object endBattle(int battleId, boolean win, int coin, int[] itemIds, int[] itemNum, int star) throws Exception {

        int roleId = this.roleId();

        /********************************** 数据效验 **********************************/

        /********************************** 数据效验 **********************************/

        BattleLog battleLog = this.battleLogDao.findOne(battleId);
        if (battleLog == null) {
            return this.returnError(this.lineNum(), "参数出错,没有此战斗");
        }

        if (battleLog.getBattleResult() != 0) {

            	ObjectMapper objectMapper = new ObjectMapper();
            	HashMap<String, Object> object = objectMapper.readValue(battleLog.getData(), new TypeReference<Map<String, Object>>(){});
            	return this.success(object);
			
        }

        int battleResult = BattleLog.BATTLE_LOG_LOST;
        if (win) {
            battleResult = BattleLog.BATTLE_LOG_WIN;
        }

        battleLog.setBattleResult(battleResult);
        battleLog.setEndTime((int)(System.currentTimeMillis() / 1000));

        int storyId = battleLog.getStoryId();
        int storyType = battleLog.getStoryType();

        BaseStory baseStory = this.baseStoryDao.findOne(storyId, storyType);

        Role role = this.roleDao.findOne(roleId);

        Result result = new Result();
        if (!win) {

        	this.roleDao.subAp(role, 1, result);

        	this.roleDao.update(role, result);
      
            Object object = result.toMap();
            ObjectMapper objectMapper = new ObjectMapper();
            battleLog.setData(objectMapper.writeValueAsString(object));
            this.battleLogDao.update(battleLog);

            return this.success(object);
        } else {
        	this.battleLogDao.update(battleLog);
        }

        int teamExp = baseStory.getTeamExp();

        // 先扣体力
        this.roleDao.subAp(role, teamExp, result);
        this.roleDao.addExp(role, teamExp, result);

        this.roleDao.addCoin(role, coin, "关卡<" + storyId + ">掉落金币", FinanceLog.STATUS_STORY_GET_COIN, result);
        this.roleDao.update(role, result);

        // 上阵武将发经验
        List<Hero> battleGeneralList = new ArrayList<Hero>();
        if (battleLog.getHeroId1() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getHeroId1()));
        }
        if (battleLog.getHeroId2() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getHeroId2()));
        }
        if (battleLog.getHeroId3() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getHeroId3()));
        }
        if (battleLog.getHeroId4() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getHeroId4()));
        }

        int exp = baseStory.getExp() / battleGeneralList.size();
        if (exp < 1) {
            exp = 1;
        }

        for (Hero general : battleGeneralList) {
        	this.heroDao.addExp(general, role.level(), exp, result);
        }

        if (itemIds != null) {
            for(int i = 0 ; i < itemIds.length ; i++) {
                int itemBaseId = itemIds[i];
                int num = itemNum[i];
                this.itemDao.addItem(roleId, itemBaseId, num, result);
            }
        }

        Story story = this.storyDao.findOne(roleId, storyId, storyType);

        boolean update = false;
        if (story == null) {
            story = new Story();
            story.setARoleId(roleId);
            story.setStar(star);
            story.setStoryId(storyId);
            story.setType(storyType);
            story.setBuyNum(0);
            story.setDate(Integer.valueOf(Utils.date()));
            story.setNum(0);

            update = true;
            
        } else if (story.getStar() < star) {
            story.setStar(star);
            update = true;
        }

        story.init();
        this.storyDao.addNum(role, story, 1, result);

        if (update) {

        	List<Story> storys = this.storyDao.findAll(roleId);
        	int n = 0;
        	 for (Story story2 : storys) {
				if (story2.getType() == BaseStory.HERO_COPY_TYPE) {
					n += story2.getStar();
				}
			}
    		// 成就
    		this.achievementDao.setNum(roleId, BaseAchievement.TYPE_STAR, 0, n, result);
        }

        BaseStory next = this.baseStoryDao.findOne(storyId + 1, storyType);
        // 是否开启下一关卡
//        if (baseStory.getId() < baseStoryList.size()) {
        if (next != null) {

            if (role.level() >= next.getUnlockLevel()) {
                story = this.storyDao.findOne(roleId, next.getId(), next.getType());
                if (story == null) {

                	result.addCopy(this.storyDao.create(roleId, next.getId(), next.getType()));
                }
            }
        }

        result.setValue("general_exp", exp);
        result.setValue("star", star);

        ObjectMapper objectMapper = new ObjectMapper();

        battleLog.setData(objectMapper.writeValueAsString(result.toMap()));
        this.battleLogDao.update(battleLog);

        return this.success(result.toMap());
    }

    /**
     * 扫荡
     * @param tokenS
     * @param storyId
     * @param type
     * @param times
     * @return
     * @throws Exception
     */
    public Object wipeOut(int storyId, int type, int times) throws Exception {

        if (times != 1 && times != 10 && times != 3 && times != 2) {
            return this.returnError(this.lineNum(), "Illegal operation !");
        }

        int roleId = this.roleId();

//        ArrayList<BaseStory> list = null;
//        if (type == Story.COPY_TYPE) {
//            list = BaseStoryInstance.getBaseStoryType1List();
//        } else if (type == Story.HERO_COPY_TYPE) {
//            list = BaseStoryInstance.getBaseStoryType2List();
//        }
//
//        if (list == null) {
//            return this.returnError(this.lineNum(), "参数出错");
//        }

        Story story = this.storyDao.findOne(roleId, storyId, type);

        if (story == null || story.getStar() < 3) {
            return this.returnError(this.lineNum(), "不是三星不能扫荡");
        }

        Role role = roleDao.findOne(roleId);

        if (type == BaseStory.HERO_COPY_TYPE) {
            if (3 - story.getNum() < times) {
                return this.returnError(this.lineNum(), "今日可用扫荡次数不足");
            }
        }

        if (role.getVip() < 4 && times == 10) {
            return returnError(2, "升级到VIP4可立即开启【扫荡10次】功能，前去升级？");
        } else if (role.getVip() < 4 && times > 1) {
        	 return returnError(2, "升级到VIP4可立即开启【扫荡3次】功能，前去升级？");
        }

        Result result = new Result();

        BaseStory baseStory = this.baseStoryDao.findOne(storyId, type);

        int exp = baseStory.getTeamExp() * times;
        if (role.ap() < exp) {
            return this.returnError(this.lineNum(), "体力不足");
        } else {
        	this.roleDao.subAp(role, exp, result);
        }

        List<Item> itemList = this.itemDao.findAll(roleId);
        Item item = null;
        for (Item itemTable : itemList) {
            if (itemTable.getItemId() == 4208) {
                // 有剿匪令
                item = itemTable;
                break;
            }
        }

        if (item != null && item.getNum() >= times) {
        	this.itemDao.subItem(item, times, result);
        } else {

            if (role.getVip() < 1) {
            	return this.returnError(2, "【剿匪令】数量不足，要开启元宝扫荡，请升级到VIP1用户，前去充值？");
            }

            if (role.getGold() < times) {
            	return this.returnError(2, ErrorResult.NotEnoughGold);
            } else {
            	this.roleDao.subGold(role, times, "扫荡<" + baseStory.getName() + "><" + times + ">次", FinanceLog.STATUS_GOLD_WIPE_OUT, result);
            }
        }

        this.storyDao.addNum(role, story, times, result);

        int[][][] item_result = new int[times][][];

        ArrayList<ArrayList<int[]>> aItemList = this.baseStoryDao.randomItems(roleId, baseStory, times, this.serverId());

        for(int i = 0 ; i < aItemList.size() ; i++) {
            ArrayList<int[]> aItems = aItemList.get(i);
            item_result[i] = new int[aItems.size()][];
            for (int j = 0 ; j < aItems.size() ; j++) {
                item_result[i][j] = aItems.get(j);
            }
        }

        String typeString = "普通关卡";
        if (type == BaseStory.HERO_COPY_TYPE) {
            typeString = "精英关卡";
        }

        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

        String desc = "扫荡" + typeString + "<" + storyId + "><" + times + ">次掉落<";
        for (int[][] items : item_result) {
            for (int[] is : items) {
                desc += baseItemDao.findOne(is[0]).getName() + ",";
                Integer n = map.get(is[0]);
                if (n == null) {
                    n = 0;
                }
                map.put(is[0], n + is[1]);
            }
        }
        desc += ">";

        this.roleDao.addExp(role, exp, result);

        this.roleDao.addCoin(role, baseStory.getCoin() * times, desc, FinanceLog.STATUS_WIPE_OUT_GET, result);
        // this.roleDao.update(role, result);

        for (Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<Integer, Integer> entry = iter.next();
            this.itemDao.addItem(roleId, entry.getKey(), entry.getValue(), result);
        }

        int[][] expBook = this.baseStoryDao.decodeWipeOutItems(baseStory);
        int[][] expRes = new int[expBook.length][];
        for (int i = 0 ; i < expBook.length ; i++) {
            expRes[i] = new int[]{expBook[i][0] , expBook[i][1] * times};
            this.itemDao.addItem(roleId, expRes[i][0], expRes[i][1], result);
        }

        result.setValue("coin", baseStory.getCoin() * times);
        result.setValue("random_item", item_result);
        result.setValue("other_item", expRes);
        if (type == BaseStory.HERO_COPY_TYPE) {

            int n = 3;
            result.setValue("wipeout_times", n > 0 ? n : 0);
        }

        return this.success(result.toMap());
    }

    /**
     * 购买体力
     * @param tokenS
     */
    public Object buyMp() throws Throwable {

        int roleId = this.roleId();

        Role role = roleDao.findOne(roleId);

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

        if (dateNumModel.getBuyApNum() >= role.maxBuyPsNum()) {
            return this.returnError(2, "购买体力次数已用完，提升VIP等级，可增加购买体力次数，前去充值？");
        }

        int gold = dateNumModel.buyApNeedGold();

        Result result = new Result();

        if (role.getGold() < gold) {
        	return this.returnError(2, ErrorResult.NotEnoughGold);
        } else {
        	this.roleDao.addAp(role, 120, result);

        	dateNumModel.setBuyApNum(dateNumModel.getBuyApNum() + 1);
        	
        	this.roleDao.subGold(role, gold, "购买体力第<" + dateNumModel.getBuyApNum()  + ">次", FinanceLog.STATUS_BUY_PHYSICAL_STRENGTHP, result);
        	// this.roleDao.update(role, result);
        }

        this.dateNumDao.save(roleId, dateNumModel);

        result.setValue("buy_mp", new int[]{dateNumModel.getBuyApNum(), role.maxBuyPsNum(), dateNumModel.buyApNeedGold()});
        return this.success(result.toMap());
    }

    /**
     * 购买精英关卡挑战次数
     * @param tokenS
     * @param storyId
     * @return
     * @throws Exception
     */
    public Object buyHeroCopyTimes(int storyId) throws Exception {

        int roleId = this.roleId();

        Story story = this.storyDao.findOne(roleId, storyId, BaseStory.HERO_COPY_TYPE);
        if (story == null) {
        	return this.returnError(this.lineNum(), "未开启这个关卡");
        }

        int dayNum = story.getBuyNum();

    	int gold = story.needGold();

        Role role = this.roleDao.findOne(roleId);

        if (role.getVip() < dayNum + 2) {
            return returnError(2, "购买次数不足");
        }

        Result result = new Result();

        if (role.getGold() < gold) {
        	return this.returnError(2, ErrorResult.NotEnoughGold);
        } else {
        	this.roleDao.subGold(role, gold, "购买扫荡次数,第<" + storyId + ">关," + this.baseStoryDao.findOne(storyId, BaseStory.HERO_COPY_TYPE).getName(), FinanceLog.STATUS_BUY_WIPE_OUT, result);
        	// this.roleDao.update(role, result);
        }

        story.setNum(0);
        story.setBuyNum(story.getBuyNum() + 1);
        this.storyDao.update(story, result);

        return this.success(result.toMap());
    }
    

    /**
     * 
     * @param type
     * @param chapter
     * @param star
     * @return
     * @throws Exception
     */
    public Object starReward(int type, int chapter, int star) throws Exception {

    	if (chapter < 0) {
    		return this.returnError(this.lineNum(), "参数错误");
    	}

    	int n = 0;
    	if (type == BaseStory.COPY_TYPE) {

    		if (star != 18 && star != 36 && star != 54) {
    			return this.returnError(this.lineNum(), "参数错误");
    		}

    		n = 18;
    	} else if (type == BaseStory.HERO_COPY_TYPE) {
    		
    		if (star != 6 && star != 12 && star != 18) {
    			return this.returnError(this.lineNum(), "参数错误");
    		}

    		n = 6;
    	} else {
    		return this.returnError(this.lineNum(), "参数错误");
    	}

		int start = (chapter - 1) * n + 1;
		int end = chapter * n;

    	int roleId = this.roleId();

    	List<Story> list = this.storyDao.findAll(roleId);

    	int countStar = 0;

    	for (Story story : list) {
			if (story.getType() == type && story.getStoryId() >= start && story.getStoryId() <= end) {
				countStar += story.getStar();
			}
		}

    	if (countStar < star) {
    		return this.returnError(this.lineNum(), "星星不够不能领奖");
    	}

    	List<CopyStar> copyStars = copyStarDao.findAll(roleId);
    	for (CopyStar copyStar : copyStars) {
			if (copyStar.getChapter() == chapter && copyStar.getType() == type && copyStar.getStar() == star) {
				return this.returnError(this.lineNum(), "奖励已领取");
			}
		}
    	
    	CopyStar copyStar = new CopyStar();
    	copyStar.setChapter(chapter);
    	copyStar.setRoleId(roleId);
    	copyStar.setStar(star);
    	copyStar.setType(type);
    	
    	this.copyStarDao.save(copyStar);

    	copyStars.add(copyStar);


    	List<CopyStarModel> stars = this.copyStarDao.configs();

    	Result result = new Result();
    	
    	for (CopyStarModel copyStar2 : stars) {
			if (copyStar2.getType() == type && copyStar2.getChapter() == chapter && copyStar2.getStar() == star) {

				for (int[] baseItem : copyStar2.getItems()) {
					this.itemDao.addItem(roleId, baseItem[0], baseItem[1], result);
				}
			}
		}

    	return this.success(result.toMap());
    }
}
