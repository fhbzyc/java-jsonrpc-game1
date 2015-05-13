package com.zhanglong.sg.dao;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;

@Repository
public class RewardDao extends BaseDao {

	@Resource
	private ItemDao itemDao;

	@Resource
	private RoleDao roleDao;
	
    public RewardDao() {
    }

    public void get(Role role, Reward reward, String desc, int financeStatus, Result result) throws Throwable {

        int[] items = reward.getItem_id();
        if (items != null) {

        	int[] itemNums = reward.getItem_num();
            for (int i = 0 ; i < items.length ; i++) {
                int itemId = items[i];
                int num = itemNums[i];
                this.itemDao.addItem(role.getRoleId(), itemId, num, result);
            }
        }

        boolean set = false;
        Integer exp = (Integer)reward.getExp();
        if (exp != null) {
        	this.roleDao.addExp(role, exp, result);
        	set = true;
        }

        Integer coin = (Integer)reward.getCoin();
        if (coin != null) {
        	this.roleDao.addCoin(role, coin, desc, financeStatus, result);
        	set = true;
        }

        Integer gold = (Integer)reward.getGold();
        if (gold != null) {
        	this.roleDao.addGold(role, gold, desc, financeStatus, result);
        	set = true;
        }

        if (set) {
        	this.roleDao.update(role, result);
        }

        Integer money3 = (Integer)reward.getMoney3();
        if (money3 != null) {
//        	ArenaModel arenaModel = new ArenaModel(this.roleId);
//        	arenaModel.addMoney(arena_money, result);
        }

        Integer money4 = (Integer)reward.getMoney4();
        if (money4 != null) {
//        	ArenaModel arenaModel = new ArenaModel(this.roleId);
//        	arenaModel.addMoney(arena_money, result);
        }

        
    }
//
//    public String getJson(int[] itemId, int[]itemNum, Integer coin, Integer gold, Integer heroId, Integer arena_money) throws JsonProcessingException  {
//
//    	Reward reward = new Reward();
//    	
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        if (itemId != null && itemId.length > 0) {
//            map.put("item_id", itemId);
//            map.put("item_num", itemNum);
//        }
//        if (coin != null && coin > 0) {
//            map.put("coin", coin);
//        }
//        if (gold != null && gold > 0) {
//            map.put("gold", gold);
//        }
//        if (heroId != null && heroId > 0) {
//            map.put("hero", heroId);
//        }
//
//        if (arena_money != null && arena_money > 0) {
//            map.put("arena_money", arena_money);
//        }
//
//        HashMap<String, Object> reward = new HashMap<String, Object>();
//        reward.put("reward", map);
//
//        ObjectMapper mapper = new ObjectMapper();
//        return mapper.writeValueAsString(reward);
//    }
}
