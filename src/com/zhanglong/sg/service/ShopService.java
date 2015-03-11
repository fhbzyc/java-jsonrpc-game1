package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.BaseItemDao;
import com.zhanglong.sg.dao.ItemShopDao;
import com.zhanglong.sg.entity.BaseItemShop;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.ItemShopModel;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("shop")
public class ShopService extends BaseClass {

    @Resource
    private ItemShopDao itemShopDao;

    @Resource
    private BaseItemDao baseItemDao;

	/**
     * 普通商店商品列表
     * @param tokenS
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> defaultShop() throws Throwable {

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

        Result result = new Result();

        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.getRefreshGold(itemShopModel.getRefreshNum()));
        result.setValue("discount", 100);
        return result.toMap();
    }

    /**
     * 竞技场商店
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> arenaShop() throws Throwable {

    	int roleId = this.roleId();

    	Role role = this.roleDao.findOne(roleId);
        if (role.level() < 10) {
        	throw new Throwable("10级才能开竞技场商店");
        }

        ItemShopModel itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

        Result result = new Result();
        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.getRefreshGold(itemShopModel.getRefreshNum()));
        result.setValue("discount", 100);
        result.setValue("money3", role.getMoney3());
        return result.toMap();
    }

    /**
     * 刷新普通商店
     * @param tokenS
     * @return
     * @throws Throwable
     */
	public Object refreshShop() throws Throwable {

         ItemShopModel itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

         Role role = this.roleDao.findOne(this.roleId());

         int gold = this.getRefreshGold(itemShopModel.getRefreshNum());
         if (role.getGold() < gold) {
        	 return ErrorResult.NotEnoughGold;
         }

         Result result = new Result();
         this.roleDao.subGold(role, gold, "第<" + (itemShopModel.getRefreshNum() + 1) + ">次刷新普通商城", FinanceLog.STATUS_SHOP_REFRESH);
         this.roleDao.update(role, result);
  
         this.itemShopDao.refresh(this.roleId(), BaseItemShop.SHOP_TYPE_1);
         itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

         result.setValue("items", itemShopModel.getItemList());
         result.setValue("refresh_times", itemShopModel.getRefreshNum());
         result.setValue("refresh_money", this.getRefreshGold(itemShopModel.getRefreshNum()));
         result.setValue("discount", 100);
         return result.toMap();
     }

    /**
     * 刷新竞技场商店
     * @param tokenS
     * @return
     * @throws Throwable
     */
    public Object refreshArenaShop() throws Throwable {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_3);

        Role role = this.roleDao.findOne(roleId);

        int gold = this.getRefreshGold(itemShopModel.getRefreshNum());
        if (role.getGold() < gold) {
       	 return ErrorResult.NotEnoughGold;
        }

        Result result = new Result();
        this.roleDao.subGold(role, gold, "第<" + (itemShopModel.getRefreshNum() + 1) + ">次刷新竞技场商城", FinanceLog.STATUS_SHOP_REFRESH);
        this.roleDao.update(role, result);
 
        this.itemShopDao.refresh(roleId, BaseItemShop.SHOP_TYPE_3);
        itemShopModel = this.itemShopDao.getShopByType(roleId, BaseItemShop.SHOP_TYPE_3);

        result.setValue("items", itemShopModel.getItemList());
        result.setValue("refresh_times", itemShopModel.getRefreshNum());
        result.setValue("refresh_money", this.getRefreshGold(itemShopModel.getRefreshNum()));
        result.setValue("discount", 100);
        result.setValue("money3", role.getMoney3());
        return result.toMap();
    }

    /**
     * 竞技场商城购买道具
     * @param tokenS
     * @param index
     * @return
     * @throws Throwable
     */
    public Object buyDefaultShopItem(int index) throws Throwable {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

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
            	return this.returnError(this.lineNum(), "金币不足");
            } else {
            	this.roleDao.subCoin(role, shopItem.getPrice(), "默认商店购买道具<" + this.baseItemDao.findOne(shopItem.getItemId()).getName() + ">", FinanceLog.STATUS_SHOP_BUY_COIN);
            	this.roleDao.update(role, result);
            }

        } else if (unit == BaseItemShop.MONEY_TYPE_GOLD) {
            if(role.getCoin() < shopItem.getPrice()) {
            	return ErrorResult.NotEnoughGold;
            } else {

            	this.roleDao.subGold(role, shopItem.getPrice(), "默认商店购买道具<" + this.baseItemDao.findOne(shopItem.getItemId()).getName() + ">", FinanceLog.STATUS_SHOP_BUY_GOLD);
            	this.roleDao.update(role, result);
            }
        } else {
        	return this.returnError(this.lineNum(), "配置出错");
        }

        int itemId = shopItem.getItemId();
        int num = shopItem.getNum();

        this.itemDao.addItem(roleId, itemId, num, result);

        this.itemShopDao.buyItem(roleId, BaseItemShop.SHOP_TYPE_1, index);
        itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_1);

        result.setValue("items", new BaseItemShop[]{shopItemList.get(index - 1)});
        return result.toMap();
    }

    /**
     * 普通商城购买道具
     * @param index
     * @return
     * @throws Throwable
     */
    public Object buyArenaShopItem(int index) throws Throwable {

    	int roleId = this.roleId();

        ItemShopModel itemShopModel = itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

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

        if(role.getMoney3() < shopItem.getPrice()) {
        	return this.returnError(this.lineNum(), "竞技币不足");
        } else {
        	this.roleDao.subMoney3(role, shopItem.getPrice());
        	this.roleDao.update(role, result);
        }

        int itemId = shopItem.getItemId();
        int num = shopItem.getNum();

        this.itemDao.addItem(roleId, itemId, num, result);

        this.itemShopDao.buyItem(roleId, BaseItemShop.SHOP_TYPE_3, index);
        itemShopModel = this.itemShopDao.getShopByType(this.roleId(), BaseItemShop.SHOP_TYPE_3);

        result.setValue("items", new BaseItemShop[]{shopItemList.get(index - 1)});
        return result.toMap();
    }

//    private String[] getJson(int roleId, int type) {
//    	
//    	RedisTemplate<String, String> redisRemplate = Utils.getRedisTemplate();
//        String string = (String) redisRemplate.opsForHash().get(Redis_Key + type + "_", roleId);
//        if (string != null) {
//            String[] array = string.split(" ");
//            String json = array[1];
//            if (array.length > 2) {
//                for (int i = 2 ; i < array.length ; i++) {
//                    json += array[i];
//                }
//            }
//            String[] result = new String[2];
//            result[0] = array[0];
//            result[1] = json;
//            return result;
//        }
//        return null;
//    }
//
//    private Item[] jsonDecode(String json) {
//        try {
//            ObjectMapper obj = new ObjectMapper();
//            Item[] result = obj.readValue(json, Item[].class);
//            return result;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    private void saveData(int roleId, String type, Item[] data) throws Throwable {
//
//        long nowUnixTime = System.currentTimeMillis();
//
//        Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());
//        long todayBenginTime = date.getTime();
//
//        long time1 = todayBenginTime + 9l * 3600l * 1000l;
//        long time2 = todayBenginTime + 12l * 3600l * 1000l;
//        long time3 = todayBenginTime + 18l * 3600l * 1000l;
//        long time4 = todayBenginTime + 21l * 3600l * 1000l;
//
//        if (type.equals(Arena_Shop)) {
//            time1 = todayBenginTime + 21l * 3600l * 1000l;
//            time2 = todayBenginTime + 21l * 3600l * 1000l;
//            time3 = todayBenginTime + 21l * 3600l * 1000l;
//            time4 = todayBenginTime + 21l * 3600l * 1000l;
//        }
//
//        String setTime;
//        if (nowUnixTime < time1) {
//            setTime = String.valueOf(time1);
//        } else if (nowUnixTime < time2) {
//            setTime = String.valueOf(time2);
//        } else if (nowUnixTime < time3) {
//            setTime = String.valueOf(time3);
//        } else if (nowUnixTime < time4) {
//            setTime = String.valueOf(time4);
//        } else {
//            setTime = String.valueOf(time1 + 86400 * 1000); // 明天9点
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(data);  
//
//        this.redisSaveData(roleId, type, setTime + " " + json);
//    }
//
//    private void redisSaveData(int roleId, String type, String data) {
//    	RedisTemplate<String, String> redisRemplate = Utils.getRedisTemplate();
//        redisRemplate.opsForHash().put(Redis_Key + type + "_", roleId, data);
//    }
//
//    private Item[] refresh(String type, int num) throws Throwable {
//
//        Item where = new Item();
//        where.setStoreId(type); // 管理后台 普通商城 type 要配成 default_store
//        where.setStatus(Item.ENABLED);
//        List<Item> shopItemList = new ArrayList<Item>();
//	
//   //     List<Item> shopItemList = this.getBaseItemShopDao().findByType(Integer.valueOf(type));
//
//        if (shopItemList.size() <= num) {
//        	throw new Throwable("商店配置出错,无法刷新");
//        }
//
//        //******************************************** 获取折扣  begin ********************************************// 
//
//
//        Integer dis = 100;
//
//        int discount = dis;
//        //******************************************** 获取折扣  end   ********************************************//
//
//        int[] tempList = new int[num];
//        Item[] result = new Item[num];
//
//        int begin = 0;
//
//        if (type.equals(Arena_Shop)) {
//        	ArrayList<Item> delIdList = new ArrayList<Item>();
//        	for (int i = 0 ; i < shopItemList.size() ; i++) {
//        		Item item = shopItemList.get(i);
//            	int itemId = Integer.valueOf(item.getCustomId());
//            	if (itemId == 4016 || itemId == 4017) {
//            		// 固定给4016 大乔魂石 和 4017 孟获魂石
//            		
////            		item.setPrice(item.getPrice() * discount / 100);
////            		item.setQuality(discount / 10);
//            		result[begin] = item;
// 
//            		begin++;
//            		delIdList.add(item);
//            		continue;
//            	}
//        	}
//
//            for (Item delId : delIdList) {
//            	shopItemList.remove(delId);
//			}
//        }
//
//        Random random = new Random();
//        
//        Sign:
//        for (int i = begin ; i < num ; i++) {
//            int r = random.nextInt(shopItemList.size());
//            int id = shopItemList.get(r).getId();
//            for (int j : tempList) {
//                if (j == id && id != 0) {
//                    i--;
//                    continue Sign;
//                }
//            }
//            tempList[i] = id;
//            result[i] = shopItemList.get(r);
//        }
//
//        for (int i = 0 ; i < result.length ; i++) {
//            result[i].setPrice(result[i].getPrice() * discount / 100);
//            result[i].setQuality(discount / 10);
//		}
//        
//        return result;
//    }

    private int getRefreshGold(int times) {
    	int n = times / 2;
    	return (int)(50 * Math.pow(2, n));
    }

//    private int getRefreshTimes(int roleId, int type) {
//		String key = "TIMES_" + type + "_" + roleId;
//        String string = null;
//        if (string != null) {
//            String[] array = string.split(",");
//            String date = Utils.date();
//            if (date.equals(array[0])) {
//            	return Integer.valueOf(array[1]);
//            }
//        }
//        return 0;
//    }

//    private void setRefreshTimes(String roleId, int times, String type) {
//
//    	String key = "TIMES_" + type + "_" + roleId;
//    	RedisTemplate<String, String> redisRemplate = Utils.getRedisTemplate();
//        redisRemplate.opsForHash().put( "TIMES_" + type + "_", roleId, times);
//
//    }
}
