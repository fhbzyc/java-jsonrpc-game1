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

	@Resource
	private DailyTaskDao dailyTaskDao;

	@Resource
	private BaseItemDao baseItemDao;

    public RewardDao() {
    }

    public void get(Role role, Reward reward, String desc, int financeStatus, Result result) throws Exception {

        int[] items = reward.getItem_id();
        if (items != null) {

        	int[] itemNums = reward.getItem_num();
            for (int i = 0 ; i < items.length ; i++) {
                int itemId = items[i];
                int num = itemNums[i];
                this.itemDao.addItem(role.getRoleId(), itemId, num, result);

                desc += ",";
        		desc += this.baseItemDao.findOne(itemId).getName();
        		desc += "x";
        		desc += num;
            }
        }

        boolean set = false;
        Integer exp = reward.getExp();
        if (exp != null) {
        	this.roleDao.addExp(role, exp, result);
        	set = true;
        }

        Integer coin = reward.getCoin();
        if (coin != null) {
        	this.roleDao.addCoin(role, coin, desc, financeStatus, result);
        	set = true;
        }

        Integer gold = reward.getGold();
        if (gold != null) {
        	this.roleDao.addGold(role, gold, desc, financeStatus, result);
        	set = true;
        }

        Integer money3 = reward.getMoney3();
        if (money3 != null) {
        	this.roleDao.addMoney3(role, gold, desc, financeStatus, result);
        	set = true;
        }

        Integer money4 = reward.getMoney4();
        if (money4 != null) {
        	this.roleDao.addMoney4(role, gold, desc, financeStatus, result);
        	set = true;
        }

        Integer ap = reward.getAp();
        if (ap != null) {
        	this.roleDao.addAp(role, ap, result);
        	set = true;
        }

        Integer vip = reward.getVip();
        if (vip != null && vip > role.vip) {
    		role.vip = vip;
    		set = true;

    		this.dailyTaskDao.addVip(role, result);

            result.setValue("vip", role.vip());
        }

        Integer card = reward.getCard();
        if (card != null) {
    		role.cardTime = (int)(System.currentTimeMillis() / 1000l) + 86400 * 30;
    		set = true;
    		this.dailyTaskDao.addMoonCard(role, result);
        }

        if (set) {
        	this.roleDao.update(role, result);
        }
    }
}
