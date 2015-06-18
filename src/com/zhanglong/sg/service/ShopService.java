package com.zhanglong.sg.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.BaseItemDao;
import com.zhanglong.sg.dao.ItemShopDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseItemShop;
import com.zhanglong.sg.model.ItemShopModel;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;

@Service
public class ShopService extends BaseService {

    @Resource
    private ItemShopDao itemShopDao;

    @Resource
    private BaseItemDao baseItemDao;

	/**
     * 普通商店商品列表
     * @return
     * @throws Throwable
     */
    public Object defaultShop() throws Exception {

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

        Result result = new Result();

        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
        result.setValue("discount", itemShopModel.getDiscount());
        return this.success(result.toMap());
    }

    /**
     * 竞技场商店
     * @return
     * @throws Exception
     */
    public Object arenaShop() throws Exception {

    	int roleId = this.roleId();

    	Role role = this.roleDao.findOne(roleId);
        if (role.level() < 10) {
        	return this.returnError(this.lineNum(), "10级才能开竞技场商店");
        }

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

        Result result = new Result();
        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
        result.setValue("discount", itemShopModel.getDiscount());
        result.setValue("money3", role.getMoney3());
        result.setValue("arena_money", role.getMoney3());
        return this.success(result.toMap());
    }

    /**
     * 讨伐商店
     * @return
     * @throws Exception
     */
    public Object tftxShop() throws Exception {

    	int roleId = this.roleId();

    	Role role = this.roleDao.findOne(roleId);
        if (role.level() < 10) {
        	return this.returnError(this.lineNum(), "10级才能开竞技场商店");
        }

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_4);

        Result result = new Result();
        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
        result.setValue("discount", itemShopModel.getDiscount());
        result.setValue("money4", role.getMoney4());
        return this.success(result.toMap());
    }

    /**
     * 刷新普通商店
     * @return
     * @throws Exception
     */
	public Object refreshShop() throws Exception {

         ItemShopModel itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

         Role role = this.roleDao.findOne(this.roleId());

         int gold = this.gold(itemShopModel.getRefreshNum());
         if (role.getGold() < gold) {
        	 return this.returnError(2, ErrorResult.NotEnoughGold);
         }

         Result result = new Result();
         this.roleDao.subGold(role, gold, "第<" + (itemShopModel.getRefreshNum() + 1) + ">次刷新普通商城", FinanceLog.STATUS_SHOP_REFRESH, result);
        // this.roleDao.update(role, result);
  
         this.itemShopDao.refresh(this.roleId(), BaseItemShop.SHOP_TYPE_1);
         itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

         result.setValue("items", itemShopModel.getItemList());
         result.setValue("refresh_times", itemShopModel.getRefreshNum());
         result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
         result.setValue("discount", itemShopModel.getDiscount());
         return this.success(result.toMap());
     }

    /**
     * 刷新竞技场商店
     * @return
     * @throws Exception
     */
    public Object refreshArenaShop() throws Exception {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_3);

        Role role = this.roleDao.findOne(roleId);

        int gold = this.gold(itemShopModel.getRefreshNum());
        if (role.getGold() < gold) {
        	return this.returnError(2, ErrorResult.NotEnoughGold);
        }

        Result result = new Result();
        this.roleDao.subGold(role, gold, "第<" + (itemShopModel.getRefreshNum() + 1) + ">次刷新竞技场商城", FinanceLog.STATUS_SHOP_REFRESH, result);
       // this.roleDao.update(role, result);
 
        this.itemShopDao.refresh(roleId, BaseItemShop.SHOP_TYPE_3);
        itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_3);

        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
        result.setValue("discount", itemShopModel.getDiscount());
        result.setValue("money3", role.getMoney3());
        result.setValue("arena_money", role.getMoney3());

        return this.success(result.toMap());
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object refreshTftxShop() throws Exception {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_4);

        Role role = this.roleDao.findOne(roleId);

        int gold = this.gold(itemShopModel.getRefreshNum());
        if (role.getGold() < gold) {
        	return this.returnError(2, ErrorResult.NotEnoughGold);
        }

        Result result = new Result();
        this.roleDao.subGold(role, gold, "第<" + (itemShopModel.getRefreshNum() + 1) + ">次刷新讨伐商城", FinanceLog.STATUS_SHOP_REFRESH, result);
        // this.roleDao.update(role, result);
 
        this.itemShopDao.refresh(roleId, BaseItemShop.SHOP_TYPE_4);
        itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_4);

        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.gold(itemShopModel.getRefreshNum()));
        result.setValue("discount", itemShopModel.getDiscount());
        result.setValue("money4", role.getMoney4());
        return this.success(result.toMap());
    }

    /**
     * 竞技场商城购买道具
     * @param index
     * @return
     * @throws Exception
     */
    public Object buyDefaultShopItem(int index) throws Exception {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

        List<BaseItemShop> shopItemList = itemShopModel.getItemList();

        if (index < 1 || index > shopItemList.size()) {
        	return this.returnError(this.lineNum(), "参数错误");
        }

        BaseItemShop shopItem = shopItemList.get(index - 1);

        if (shopItem.getSold()) {
            return this.returnError(this.lineNum(), "这个道具已售罄");
        }

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);

        int unit = shopItem.getMoneyType();

        if (unit == BaseItemShop.MONEY_TYPE_COIN) {
            if(role.getCoin() < shopItem.getPrice()) {
            	return this.returnError(this.lineNum(), "铜币不足");
            } else {
            	this.roleDao.subCoin(role, shopItem.getPrice(), "普通商店购买道具<" + this.baseItemDao.findOne(shopItem.getItemId()).getName() + ">", FinanceLog.STATUS_SHOP_BUY_COIN, result);
            //	this.roleDao.update(role, result);
            }

        } else if (unit == BaseItemShop.MONEY_TYPE_GOLD) {
            if(role.getGold() < shopItem.getPrice()) {
            	return this.returnError(2, ErrorResult.NotEnoughGold);
            } else {

            	this.roleDao.subGold(role, shopItem.getPrice(), "普通商店购买道具<" + this.baseItemDao.findOne(shopItem.getItemId()).getName() + ">", FinanceLog.STATUS_SHOP_BUY_GOLD, result);
            //	this.roleDao.update(role, result);
            }
        } else {
        	return this.returnError(this.lineNum(), "配置出错");
        }

        int itemId = shopItem.getItemId();
        int num = shopItem.getNum();

        this.itemDao.addItem(roleId, itemId, num, result);

        this.itemShopDao.buyItem(roleId, BaseItemShop.SHOP_TYPE_1, index);
        itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

        shopItemList = itemShopModel.getItemList();
        result.setValue("items", new BaseItemShop[]{shopItemList.get(index - 1)});
        return this.success(result.toMap());
    }

    /**
     * 普通商城购买道具
     * @param index
     * @return
     * @throws Exception
     */
    public Object buyArenaShopItem(int index) throws Exception {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

        List<BaseItemShop> shopItemList = itemShopModel.getItemList();

        if (index < 1 || index > shopItemList.size()) {
        	return this.returnError(this.lineNum(), "参数错误");
        }

        BaseItemShop shopItem = shopItemList.get(index - 1);

        if (shopItem.getSold()) {
            return this.returnError(this.lineNum(), "这个道具已售罄");
        }

        Result result = new Result();

        int itemId = shopItem.getItemId();
        int num = shopItem.getNum();

        Role role = this.roleDao.findOne(roleId);

        if(role.getMoney3() < shopItem.getPrice()) {
        	return this.returnError(this.lineNum(), "竞技币不足");
        } else {
        	this.roleDao.subMoney3(role, shopItem.getPrice(), "商店购买<" + baseItemDao.findOne(itemId).getName() + ">", FinanceLog.STATUS_SHOP_BUY_MONEY3);
        	this.roleDao.update(role, result);
        }

        this.itemDao.addItem(roleId, itemId, num, result);

        this.itemShopDao.buyItem(roleId, BaseItemShop.SHOP_TYPE_3, index);
        itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

        shopItemList = itemShopModel.getItemList();
        result.setValue("items", new BaseItemShop[]{shopItemList.get(index - 1)});
        result.setValue("money3", role.getMoney3());
        result.setValue("arena_money", role.getMoney3());
        return this.success(result.toMap());
    }

    /**
     * 
     * @param index
     * @return
     * @throws Exception
     */
    public Object buyTftxShop(int index) throws Exception {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_4);

        List<BaseItemShop> shopItemList = itemShopModel.getItemList();

        if (index < 1 || index > shopItemList.size()) {
        	return this.returnError(this.lineNum(), "参数错误");
        }

        BaseItemShop shopItem = shopItemList.get(index - 1);

        if (shopItem.getSold()) {
            return this.returnError(this.lineNum(), "这个道具已售罄");
        }

        Result result = new Result();

        int itemId = shopItem.getItemId();
        int num = shopItem.getNum();

        Role role = this.roleDao.findOne(roleId);

        if(role.getMoney4() < shopItem.getPrice()) {
        	return this.returnError(this.lineNum(), "竞技币不足");
        } else {
        	this.roleDao.subMoney4(role, shopItem.getPrice(), "商店购买<" + this.baseItemDao.findOne(itemId).getName() + ">", FinanceLog.STATUS_SHOP_BUY_MONEY4);
        	this.roleDao.update(role, result);
        }

        this.itemDao.addItem(roleId, itemId, num, result);

        this.itemShopDao.buyItem(roleId, BaseItemShop.SHOP_TYPE_4, index);
        itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_4);

        shopItemList = itemShopModel.getItemList();
        result.setValue("items", new BaseItemShop[]{shopItemList.get(index - 1)});
        result.setValue("money4", role.getMoney4());
        return this.success(result.toMap());
    }

    private int gold(int num) {
    	int n = num / 2;
    	int gold = (int)(50 * Math.pow(2, n));
    	if (gold > 800) {
    		gold = 800;
    	}
    	return gold;
    }
}
