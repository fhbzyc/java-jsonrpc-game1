package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.BaseMakeItemDao;
import com.zhanglong.sg.entity.BaseItem;
import com.zhanglong.sg.entity.BaseMakeItem;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.ItemTable;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/item")
public class ItemService extends BaseClass {

	@Resource
	private BaseMakeItemDao baseMakeItemDao;
	
	/**
	 * 道具列表
	 * @param tokenS
	 * @return
	 * @throws Throwable
	 */
    public HashMap<String, Object> list() throws Throwable {

        List<ItemTable> queryList = this.itemDao.findAll(this.roleId());

        Result result = new Result();
        
        for (ItemTable itemTable : queryList) {
            result.addItem(itemTable);
        }

        return result.toMap();
    }

    /**
     * 合成装备
     * @param tokenS
     * @param itemBaseId
     * @return HashMap<String, Object>
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object makeItem(int itemBaseId) throws Throwable {

    	int roleId = this.roleId();

        BaseItem baseItem = this.baseItemDao.findOne(itemBaseId);
        if (baseItem == null) {
            throw new Throwable("出错,不存在此道具");
        }

        Result result = new Result();

        Role role = roleDao.findOne(roleId);
        if (role.getCoin() < baseItem.getMakeCoin()) {
        	return returnError(lineNum(), "铜钱不足");
        } else {
        	roleDao.subCoin(role, baseItem.getMakeCoin(), "合成装备<" + baseItem.getName() + ">", FinanceLog.STATUS_EQUIP_MAKE);
        	roleDao.update(role, result);
        }

        List<BaseMakeItem> items = this.baseMakeItemDao.findByItemId(itemBaseId);

        boolean find = false;
        for (BaseMakeItem baseMakeItem : items) {
			if (baseMakeItem.getPk().getBaseItem().getBaseId() == itemBaseId) {

				find = true;

				int materialId = baseMakeItem.getPk().getMaterial().getBaseId();

				ItemTable item = this.itemDao.findOneByItemId(roleId, materialId);
				if (item == null) {
					throw new Throwable("缺少合成材料:" + this.baseItemDao.findOne(materialId).getName());
				} else if (item.getNum() < baseMakeItem.getNum()) {
					throw new Throwable("合成材料:" + this.baseItemDao.findOne(materialId).getName() + " , 数量不足");
				} else {
					this.itemDao.subItem(item, baseMakeItem.getNum(), result);
				}
			}
		}

        if (!find) {
        	throw new Throwable("这个道具没有合成配方");
        }

        this.itemDao.addItem(roleId, itemBaseId, 1, result);

        return result.toMap();
    }

    /**
     * 出售道具
     * @param tokenS
     * @param itemId
     * @param num
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public HashMap<String, Object> sellItem(int id, int num) throws Throwable {

    	int roleId = this.roleId();

        if (num < 1) {
            throw new Throwable("参数出错,数量最少为1");
        }

        ItemTable item = this.itemDao.findOne(id);
        if (item == null) {
            throw new Throwable("出错,你没有此道具");
        }

        if (item.getNum() < num) {
            throw new Throwable("参数出错,卖出道具数量超过拥有的数量");
        }

        BaseItem baseItem = this.baseItemDao.findOne(item.getItemId());
        int coin = baseItem.getSellCoin() * num;

        Result result = new Result();
        this.itemDao.subItem(item, num, result);

        Role role = this.roleDao.findOne(roleId);
        this.roleDao.addCoin(role, coin, "卖出道具<" + baseItem.getName() + "> 数量<" + num + ">", FinanceLog.STATUS_ITEM_SELL, result);
        this.roleDao.update(role, result);

        return result.toMap();
    }

    /**
     * 使用经验书
     * @param tokenS
     * @param generalId
     * @param itemId
     * @param num
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> useExpBook(int generalId, int id, int num) throws Throwable {

    	int roleId = this.roleId();

        ItemTable item = this.itemDao.findOne(id);
        if (item == null) {
            throw new Throwable("出错,你没有此道具");
        }

        Hero hero = heroDao.findOne(roleId, generalId);
        if (hero == null) {
            throw new Throwable("出错,你没有此武将");
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
        	throw new Throwable("出错,此道具不是经验书");
        }

        Result result = new Result();

        Role role = roleDao.findOne(roleId);
        int maxExp = heroDao.maxExp(role.level());
        if (hero.getExp() >= maxExp) {
        	throw new Throwable("经验已经最大,不能再加了");
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
        	heroDao.update(hero, result);
        	this.itemDao.subItem(item, j, result);
        }

        return result.toMap();
    }
}
