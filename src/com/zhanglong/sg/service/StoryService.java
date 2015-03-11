package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.result.*;
import com.zhanglong.sg.dao.BaseStoryDao;
import com.zhanglong.sg.dao.BattleLogDao;
import com.zhanglong.sg.dao.ItemDao;
import com.zhanglong.sg.dao.StoryDao;
import com.zhanglong.sg.entity.BaseStory;
import com.zhanglong.sg.entity.BattleLog;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.ItemTable;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.utils.Utils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;

@Service
@JsonRpcService("/story")
public class StoryService extends BaseClass {

	@Resource
	private StoryDao storyDao;

	@Resource
	private BaseStoryDao baseStoryDao;

	@Resource
	private BattleLogDao battleLogDao;

	public HashMap<String, Object> list() throws Throwable {

        int roleId = this.roleId();

        List<Story> queryList = this.storyDao.findAll(roleId);

        Result result = new Result();

        for (Story story : queryList) {
            result.addCopy(story);
        }

        return result.toMap();
    }

    public Object items() throws Throwable {

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

    	Result result = new Result();
    	HashMap<String, Object> r = result.toMap();

    	r.put("story", copyList);
    	r.put("hero_story", heroCopyList);

    	return r;
    }

    /**
     * 战斗开始
     * @param tokenS
     * @param storyId
     * @param storyType
     * @param generalIds
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> battleBegin(int storyId, int storyType, int[] generalIds, int power) throws Throwable {

    	int roleId = this.roleId();

        if (storyId <= 0) {
            throw new Throwable("参数出错,关卡ID无效");
        }

        if (storyType != Story.COPY_TYPE && storyType != Story.HERO_COPY_TYPE) {
            throw new Throwable("参数出错,关卡类型无效");
        }

        List<BaseStory> baseStoryList;
        if (storyType == Story.COPY_TYPE) {
            baseStoryList = this.baseStoryDao.copys();
        } else {
            baseStoryList = this.baseStoryDao.heroCopys();
        }

        if (storyId >= baseStoryList.size() + 1) {
            throw new Throwable("参数出错,关卡ID超过最大值");
        }

        Role role = roleDao.findOne(roleId);

        if (storyId == 1 && storyType == BaseStory.COPY_TYPE) {
            // 无条件攻打
        } else if (storyId == 1) {
            Story story = this.storyDao.findOne(roleId, storyId, storyType - 1);
            if(story == null) {
                throw new Throwable("出错,你还不能攻打精英关卡");
            }
            if (story.getStar() <= 0) {
                throw new Throwable("出错,你还不能攻打精英关卡");
            }
        } else {
            Story story = this.storyDao.findOne(roleId, storyId - 1, storyType);
            if(story == null) {
                throw new Throwable("出错,你还不能攻打这个关卡");
            }
        }

        if (storyType == BaseStory.HERO_COPY_TYPE) {
            Story story = this.storyDao.findOne(roleId, storyId, storyType);
            if (story.getNum() >= 3) {
                throw new Throwable("挑战次数不足");
            }
        }

        BaseStory baseStory = baseStoryList.get(storyId - 1);
        
        int physicalStrength = role.getPhysicalStrength();
        if (physicalStrength < baseStory.getTeamExp()) {
            throw new Throwable("体力不足,不能攻打");
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
        battleLogTable.setGeneralBaseId1(generalIds[0]);
        battleLogTable.setGeneralBaseId2(generalIds[1]);
        battleLogTable.setGeneralBaseId3(generalIds[2]);
        battleLogTable.setGeneralBaseId4(generalIds[3]);
        battleLogTable.setData("");

        this.battleLogDao.create(battleLogTable);

        this.roleDao.update(role, result);

        result.setValue("battle_id", battleLogTable.getId());

        List<int[]> rand = this.baseStoryDao.randomItems(roleId, baseStory, 1).get(0);
        List<int[]> items = new ArrayList<int[]>();
        for (int[] is : rand) {
            items.add(new int[]{is[0],is[1],100});
        }
        result.setValue("items", items);
        result.setValue("coin", baseStory.getCoin());
        return result.toMap();
    }

    /**
     * 提交战斗结果
     * @param tokenS
     * @param battleId
     * @param win
     * @param coin
     * @param itemBaseIds
     * @param itemNum
     * @param sign
     * @return
     * @throws Throwable
     */
    public Object battleEnd(int battleId, boolean win, int coin, int[] itemBaseIds, int[] itemNum, String sign) throws Throwable {

        int roleId = this.roleId();

        /********************************** 数据效验 **********************************/
//        String string = tokenS + battleId + String.valueOf(win) + coin;
//
//        if (itemBaseIds == null) {
//            itemBaseIds = new int[]{};
//        }
//        for (int i : itemBaseIds) {
//            string += i;
//        }
//
//        if (itemNum == null) {
//            itemNum = new int[]{};
//        }
//        for (int i : itemNum) {
//            string += i;
//        }

//        if (!MD5.digest(string + "cd538900-2494-4833-9a31-8f00fdb4e582").equals(sign)) {
//            throw new Throwable("战斗数据效验失败!");
//        }
        /********************************** 数据效验 **********************************/

        BattleLog battleLog = battleLogDao.findOne(battleId);
        if (battleLog == null) {
            throw new Throwable("参数出错,没有此战斗");
        }

        if (battleLog.getBattleResult() != 0) {
            if (battleLog.getData().equals("")) {
            	throw new Throwable("战斗已提交");
            } else {
            	ObjectMapper objectMapper = new ObjectMapper();
            	HashMap<String, Object> object = objectMapper.readValue(battleLog.getData(), new TypeReference<Map<String, Object>>(){});
            	return object;
			}
        }

        int battleResult = BattleLog.BATTLE_LOG_LOST;
        if (win) {
            battleResult = BattleLog.BATTLE_LOG_WIN;
        }

        battleLog.setBattleResult(battleResult);
        battleLog.setEndTime((int)(System.currentTimeMillis() / 1000));

        int storyId = battleLog.getStoryId();
        int storyType = battleLog.getStoryType();

        List<BaseStory> baseStoryList;
        if (storyType == Story.COPY_TYPE) {
            baseStoryList = baseStoryDao.copys();
        } else if (storyType == Story.HERO_COPY_TYPE) {
            baseStoryList = baseStoryDao.heroCopys();
        } else {
            throw new Throwable("出错,战斗记录异常");
        }

        BaseStory baseStory = baseStoryList.get(storyId - 1);

        Role role = roleDao.findOne(roleId);

        Result result = new Result();
        if (!win) {

            role.setPhysicalStrength(role.getPhysicalStrength() - 1);

            roleDao.update(role, result);
      
            Object object = result.toMap();
            ObjectMapper objectMapper = new ObjectMapper();
            battleLog.setData(objectMapper.writeValueAsString(object));
            battleLogDao.update(battleLog);

            return object;
        } else {
        	battleLogDao.update(battleLog);
        }

        int teamExp = baseStory.getTeamExp();

        // 先扣体力
        role.setPhysicalStrength(role.getPhysicalStrength() - teamExp);

        roleDao.addExp(role, teamExp, result);

        roleDao.addCoin(role, coin, "关卡<" + storyId + ">掉落金币", FinanceLog.STATUS_STORY_GET_COIN, result);
        roleDao.update(role, result);

        // 上阵武将发经验
        List<Hero> battleGeneralList = new ArrayList<Hero>();
        if (battleLog.getGeneralBaseId1() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getGeneralBaseId1()));
        }
        if (battleLog.getGeneralBaseId2() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getGeneralBaseId2()));
        }
        if (battleLog.getGeneralBaseId3() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getGeneralBaseId3()));
        }
        if (battleLog.getGeneralBaseId4() != 0) {
            battleGeneralList.add(this.heroDao.findOne(roleId, battleLog.getGeneralBaseId4()));
        }

        int exp = baseStory.getExp() / battleGeneralList.size();
        if (exp < 1) {
            exp = 1;
        }

        for (Hero general : battleGeneralList) {
        	this.heroDao.addExp(general, role.level(), exp, result);
        }

        ItemDao Item = new ItemDao();
        for(int i = 0 ; i < itemBaseIds.length ; i++) {
            int itemBaseId = itemBaseIds[i];
            int num = itemNum[i];
            Item.addItem(roleId, itemBaseId, num, result);
        }

        int star = 1;
        int battleTime = battleLog.getEndTime() - battleLog.getBeginTime();
        if (battleTime <= 100) {
            star = 3;
        } else if (battleTime <= 160) {
            star = 2;
        }

        Story story = this.storyDao.findOne(roleId, storyId, storyType);

        if (story == null) {
            story = new Story();
            story.setARoleId(roleId);
            story.setStar(star);
            story.setStoryId(storyId);
            story.setType(storyType);
            story.setBuyNum(0);
            story.setDate(Integer.valueOf(Utils.date()));
            story.setNum(0);
        } else if (story.getStar() < star) {
            story.setStar(star);
        }

        story.init();
        this.storyDao.addNum(role, story, 1, result);

        BaseStory next = this.baseStoryDao.findOne(storyId + 1, storyType);
        // 是否开启下一关卡
//        if (baseStory.getId() < baseStoryList.size()) {
        if (next != null) {

            if (role.level() >= next.getUnlockLevel()) {
                story = this.storyDao.findOne(roleId, next.getId(), next.getType());
                if (story == null) {

                	this.storyDao.create(roleId, next.getId(), next.getType());
                }
            }
        }

        result.setValue("general_exp", exp);
        result.setValue("star", star);

        ObjectMapper objectMapper = new ObjectMapper();

        battleLog.setData(objectMapper.writeValueAsString(result.toMap()));
        this.battleLogDao.update(battleLog);

        return result.toMap();
    }

    /**
     * 扫荡
     * @param tokenS
     * @param storyId
     * @param type
     * @param times
     * @return
     * @throws Throwable
     */
    public Object wipeOut(int storyId, int type, int times) throws Throwable {

        if (times != 1 && times != 10 && times != 3 && times != 2) {
            throw new Throwable("Illegal operation !");
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
//            throw new Throwable("参数出错");
//        }

        Story story = this.storyDao.findOne(roleId, storyId, type);

        if (story == null || story.getStar() < 3) {
            throw new Throwable("不是三星不能扫荡");
        }

        Role role = roleDao.findOne(roleId);

        if (type == BaseStory.HERO_COPY_TYPE) {
            if (3 - story.getNum() < times) {
                throw new Throwable("今日可用扫荡次数不足");
            }
        }

        if (role.getVip() < 4 && times == 10) {
            return new ErrorResult(new com.zhanglong.sg.result.Error(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM, "升级到VIP4可立即开启【扫荡10次】功能，前去升级？"));
        } else if (role.getVip() < 4 && times > 1) {
        	 return new ErrorResult(new com.zhanglong.sg.result.Error(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM, "升级到VIP4可立即开启【扫荡3次】功能，前去升级？"));
        }

        Result result = new Result();

        BaseStory baseStory = this.baseStoryDao.findOne(storyId, type);

        int exp = baseStory.getTeamExp() * times;
        if (role.getPhysicalStrength() < exp) {
            throw new Throwable("体力不足");
        } else {
        	role.setPhysicalStrength(role.getPhysicalStrength() - exp);
        }

        ItemDao Item = new ItemDao();

        List<ItemTable> itemList = Item.findAll(roleId);
        ItemTable item = null;
        for (ItemTable itemTable : itemList) {
            if (itemTable.getItemId() == 4208) {
                // 有剿匪令
                item = itemTable;
                break;
            }
        }

        if (item != null && item.getNum() >= times) {
            Item.subItem(item, times, result);
        } else {

            if (role.getVip() < 1) {
                return new ErrorResult(new com.zhanglong.sg.result.Error(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM, "【剿匪令】数量不足，要开启元宝扫荡，请升级到VIP1用户，前去充值？"));
            }

            if (role.getGold() < times) {
                return ErrorResult.NotEnoughGold;
            } else {
            	this.roleDao.subGold(role, times, "扫荡<" + baseStory.getName() + "><" + times + ">次", FinanceLog.STATUS_GOLD_WIPE_OUT);
            }
        }

        this.storyDao.addNum(role, story, times, result);

        int[][][] item_result = new int[times][][];

        ArrayList<ArrayList<int[]>> aItemList = this.baseStoryDao.randomItems(roleId, baseStory, times);

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

        roleDao.addExp(role, exp, result);

        roleDao.addCoin(role, baseStory.getCoin() * times, desc, FinanceLog.STATUS_WIPE_OUT_GET, result);
        roleDao.update(role, result);

        for (Iterator<Map.Entry<Integer, Integer>> iter = map.entrySet().iterator(); iter.hasNext();) {
            Map.Entry<Integer, Integer> entry = iter.next();
            Item.addItem(roleId, entry.getKey(), entry.getValue(), result);
        }

        int[][] expBook = this.baseStoryDao.decodeWipeOutItems(baseStory);
        int[][] expRes = new int[expBook.length][];
        for (int i = 0 ; i < expBook.length ; i++) {
            expRes[i] = new int[]{expBook[i][0] , expBook[i][1] * times};
            Item.addItem(roleId, expRes[i][0], expRes[i][1], result);
        }

        HashMap<String, Object> r = result.toMap();
        r.put("coin", baseStory.getCoin() * times);
        r.put("random_item", item_result);
        r.put("other_item", expRes);
        if (type == BaseStory.HERO_COPY_TYPE) {

            int n = 3;
            r.put("wipeout_times", n > 0 ? n : 0);
        }

        return r;
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
            com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
            error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
            error.setMessage("购买体力次数已用完，提升VIP等级，可增加购买体力次数，前去充值？");
            ErrorResult result = new ErrorResult(error);
            return result;
        }

        int gold = dateNumModel.buyApNeedGold();

        Result result = new Result();

        if (role.getGold() < gold) {
            return ErrorResult.NotEnoughGold;
        } else {
        	role.setPhysicalStrength(role.getPhysicalStrength() + 120);
        	
        	dateNumModel.setBuyApNum(dateNumModel.getBuyApNum() + 1);
        	
        	roleDao.subGold(role, gold, "购买体力第<" + dateNumModel.getBuyApNum()  + ">次", FinanceLog.STATUS_BUY_PHYSICAL_STRENGTHP);
        	roleDao.update(role, result);
        }

        this.dateNumDao.save(roleId, dateNumModel);

        HashMap<String, Object> r = result.toMap();
        r.put("buy_mp", new int[]{dateNumModel.getBuyApNum(), role.maxBuyPsNum(), dateNumModel.buyApNeedGold()});
        return r;
    }

    /**
     * 购买精英关卡挑战次数
     * @param tokenS
     * @param storyId
     * @return
     * @throws Throwable
     */
    public Object buyHeroCopyTimes(int storyId) throws Throwable {

        int roleId = this.roleId();

        Story story = this.storyDao.findOne(roleId, storyId, BaseStory.HERO_COPY_TYPE);
        if (story == null) {
        	throw new Throwable("未开启这个关卡");
        }

        int dayNum = story.getBuyNum();

    	int gold = story.needGold();

        Role role = roleDao.findOne(roleId);

        if (role.getVip() < dayNum + 2) {
            return new ErrorResult(new com.zhanglong.sg.result.Error(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM, "购买次数不足"));
        }

        Result result = new Result();

        if (role.getGold() < gold) {
        	return ErrorResult.NotEnoughGold;
        } else {
        	roleDao.subGold(role, gold, "购买扫荡次数,第<" + storyId + ">关," + this.baseStoryDao.findOne(storyId, BaseStory.HERO_COPY_TYPE).getName(), FinanceLog.STATUS_BUY_WIPE_OUT);
        	roleDao.update(role, result);
        }

        story.setNum(0);
        story.setBuyNum(story.getBuyNum() + 1);
        this.storyDao.update(story, result);

        return result.toMap();
    }
}
