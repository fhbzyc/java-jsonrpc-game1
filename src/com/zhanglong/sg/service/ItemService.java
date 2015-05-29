package com.zhanglong.sg.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhanglong.sg.dao.BaseMakeItemDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseItem;
import com.zhanglong.sg.entity2.BaseMakeItem;
import com.zhanglong.sg.result.Result;

@Service
public class ItemService extends BaseService {

	@Resource
	private BaseMakeItemDao baseMakeItemDao;

    /**
     * 合成装备
     * @param itemBaseId
     * @return Object
     * @throws Exception
     */
    public Object makeItem(int itemId) throws Exception {

    	int roleId = this.roleId();

        BaseItem baseItem = this.baseItemDao.findOne(itemId);
        if (baseItem == null) {
            return this.returnError(this.lineNum(), "出错,不存在此道具");
        }

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        if (role.getCoin() < baseItem.getMakeCoin()) {
        	return returnError(lineNum(), "铜钱不足");
        } else {
        	this.roleDao.subCoin(role, baseItem.getMakeCoin(), "合成装备<" + baseItem.getName() + ">", FinanceLog.STATUS_EQUIP_MAKE, result);
        	// roleDao.update(role, result);
        }

        List<BaseMakeItem> items = this.baseMakeItemDao.findByItemId(itemId);

        boolean find = false;
        for (BaseMakeItem baseMakeItem : items) {
			if (baseMakeItem.getPk().getBaseItem().getBaseId() == itemId) {

				find = true;

				int materialId = baseMakeItem.getPk().getMaterial().getBaseId();

				Item item = this.itemDao.findOneByItemId(roleId, materialId);
				if (item == null) {
					return this.returnError(this.lineNum(), "缺少合成材料:" + this.baseItemDao.findOne(materialId).getName());
				} else if (item.getNum() < baseMakeItem.getNum()) {
					return this.returnError(this.lineNum(), "合成材料:" + this.baseItemDao.findOne(materialId).getName() + " , 数量不足");
				} else {
					this.itemDao.subItem(item, baseMakeItem.getNum(), result);
				}
			}
		}

        if (!find) {
        	return this.returnError(this.lineNum(), "这个道具没有合成配方");
        }

        this.itemDao.addItem(roleId, itemId, 1, result);

        return this.success(result.toMap());
    }

    /**
     * 出售道具
     * @param itemId
     * @param num
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object sellItem(int id, int num) throws Throwable {

    	int roleId = this.roleId();

        if (num < 1) {
            return this.returnError(this.lineNum(), "参数出错,数量最少为1");
        }

        Item item = this.itemDao.findOne(id);
        if (item == null) {
            return this.returnError(this.lineNum(), "出错,你没有此道具");
        }

        if (item.getNum() < num) {
            return this.returnError(this.lineNum(), "参数出错,卖出道具数量超过拥有的数量");
        }

        BaseItem baseItem = this.baseItemDao.findOne(item.getItemId());
        int coin = baseItem.getSellCoin() * num;

        Result result = new Result();
        this.itemDao.subItem(item, num, result);

        Role role = this.roleDao.findOne(roleId);
        this.roleDao.addCoin(role, coin, "卖出道具<" + baseItem.getName() + "> 数量<" + num + ">", FinanceLog.STATUS_ITEM_SELL, result);
        this.roleDao.update(role, result);

        return this.success(result.toMap());
    }

    /**
     * 批量出售
     * @param itemIds
     * @param nums
     * @return
     * @throws Throwable
     */
    public Object sellItems(int[]itemIds, int[]nums) throws Throwable {

    	int roleId = this.roleId();

    	Result result = new Result();

    	int coin = 0;
    	String descString = "";

    	Role role = this.roleDao.findOne(roleId);
        for (int i = 0  ; i < itemIds.length ; i++) {

        	Item item = this.itemDao.findOne(itemIds[i]);

        	int num = nums[i];
        	if (item.getNum() < num) {
        		return this.returnError(this.lineNum(), "参数出错,卖出数量超过拥有的数量");
        	}

        	this.itemDao.subItem(item, num, result);

            BaseItem baseItem = this.baseItemDao.findOne(item.getItemId());
            coin += baseItem.getSellCoin() * num;
            
            descString += baseItem.getName() + "x" + num + ",";
		}

        this.roleDao.addCoin(role, coin, descString, FinanceLog.STATUS_ITEM_SELL, result);
        this.roleDao.update(role, result);

        return this.success(result.toMap());
    }

    /**
     * 使用经验书
     * @param heroId
     * @param itemId
     * @param num
     * @return
     * @throws Throwable
     */
    public Object useExpBook(int heroId, int id, int num) throws Throwable {

    	int roleId = this.roleId();

        Item item = this.itemDao.findOne(id);
        if (item == null) {
            return this.returnError(this.lineNum(), "出错,你没有此道具");
        }

        Hero hero = heroDao.findOne(roleId, heroId);
        if (hero == null) {
            return this.returnError(this.lineNum(), "出错,你没有此武将");
        }

        int baseId = item.getItemId();
        int exp = 0;
        if (baseId == 4200) {
        	exp = 60;
        } else if (baseId == 4201) {
        	exp = 300;
        } else if (baseId == 4202) {
        	exp = 1500;
        } else {
        	return this.returnError(this.lineNum(), "出错,此道具不是经验书");
        }

        Result result = new Result();

        Role role = roleDao.findOne(roleId);
        int maxExp = heroDao.maxExp(role.level());
        if (hero.getExp() >= maxExp) {
        	return this.returnError(this.lineNum(), "经验已经最大,不能再加了");
        } else {
        	int j = num;
        	for (int i = 1; i <= num; i++) {
        		hero.setExp(hero.getExp() + exp);
        		if (hero.getExp() >= maxExp) {
        			hero.setExp(maxExp);
        			j = i;
        			break;
        		}
        	}
        	hero.setLevel(hero.level());
        	heroDao.update(hero, result);
        	this.itemDao.subItem(item, j, result);
        }

        return this.success(result.toMap());
    }
}
