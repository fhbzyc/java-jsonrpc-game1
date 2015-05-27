package com.zhanglong.sg.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseItemShop;
import com.zhanglong.sg.model.ItemShopModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class ItemShopDao extends BaseDao {

	private static String RedisKey = "ITEM_SHOP1_";

	@Resource
	private BaseItemShopDao baseItemShopDao;

	@Resource
	private RedisTemplate<String, ItemShopModel> redisTemplate;

	public ItemShopDao() {
	}

//	public static ItemShopDao instance(int roleId, int type) {
//		@SuppressWarnings("resource")
//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
//		ItemShopDao itemShopDao = (ItemShopDao) applicationContext.getBean("itemShopDao");
//		itemShopDao.setRoleId(roleId);
//		itemShopDao.setType(type);
//		return itemShopDao;
//	}

	public ItemShopModel getShopByType(int roleId, int type) throws Throwable {

		ItemShopModel itemShopModel = (ItemShopModel) this.redisTemplate.opsForHash().get(RedisKey + type, roleId);

		if (itemShopModel == null || System.currentTimeMillis() >= itemShopModel.getRefreshTime()) {

			int num = 8;

	        int discount = this.baseItemShopDao.getDiscount(type);

			BaseItemShop[] items = this.ref(type, num, discount);

			List<BaseItemShop> itemList = new ArrayList<BaseItemShop>();
			for (BaseItemShop baseItemShop : items) {
				itemList.add(baseItemShop);
			}

			itemShopModel = new ItemShopModel();
			itemShopModel.setItemList(itemList);
			itemShopModel.setDiscount(discount);
			
			this.save(roleId, type, itemShopModel);
		}

		return itemShopModel;
	}

    public void buyItem(int roleId, int type, int index) throws Throwable {

    	ItemShopModel itemShopModel = this.getShopByType(roleId, type);
    	BaseItemShop baseItemShop = itemShopModel.getItemList().get(index - 1);
    	baseItemShop.setSold(true);

    	this.save(roleId, type, itemShopModel);
    }

    public void refresh(int roleId, int type) throws Throwable {

		int num = 8;

        int discount = this.baseItemShopDao.getDiscount(type);

		BaseItemShop[] items = this.ref(type, num, discount);

		List<BaseItemShop> itemList = new ArrayList<BaseItemShop>();
		for (BaseItemShop baseItemShop : items) {
			itemList.add(baseItemShop);
		}

		ItemShopModel itemShopModel = this.getShopByType(roleId, type);
		
		itemShopModel.setItemList(itemList);
		itemShopModel.setRefreshNum(itemShopModel.getRefreshNum() + 1);
		itemShopModel.setDiscount(discount);

    	this.save(roleId, type, itemShopModel);
    }

    private BaseItemShop[] ref(int type, int num, int discount) throws Throwable {

    	List<BaseItemShop> shopItemList = this.baseItemShopDao.findByType(type);

        if (shopItemList.size() <= num) {
        	throw new Throwable("商店配置出错,无法刷新");
        }

        int[] tempList = new int[num];
        BaseItemShop[] result = new BaseItemShop[num];

        int begin = 0;

        if (type == BaseItemShop.SHOP_TYPE_3) {
        	ArrayList<BaseItemShop> delIdList = new ArrayList<BaseItemShop>();
        	for (int i = 0 ; i < shopItemList.size() ; i++) {
        		BaseItemShop item = shopItemList.get(i);
            	int itemId = item.getItemId();
            	if (itemId == 4016 || itemId == 4017) {
            		// 固定给4016 大乔魂石 和 4017 孟获魂石
            		
//            		item.setPrice(item.getPrice() * discount / 100);
//            		item.setQuality(discount / 10);
            		result[begin] = item;
 
            		begin++;
            		delIdList.add(item);
            		continue;
            	}
        	}

            for (BaseItemShop delId : delIdList) {
            	shopItemList.remove(delId);
			}
        }

        Random random = new Random();
        
        Sign:
        for (int i = begin ; i < num ; i++) {
            int r = random.nextInt(shopItemList.size());
            int id = shopItemList.get(r).getId();
            for (int j : tempList) {
                if (j == id && id != 0) {
                    i--;
                    continue Sign;
                }
            }
            tempList[i] = id;
            result[i] = shopItemList.get(r);
        }

        for (int i = 0 ; i < result.length ; i++) {
            result[i].setPrice(result[i].getPrice() * discount / 10);
		}

        return result;
    }

    private void save(int roleId, int type, ItemShopModel itemShopModel) throws ParseException {

        long nowUnixTime = System.currentTimeMillis();

        Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());
        long todayBenginTime = date.getTime();

        long time1 = todayBenginTime + 9l * 3600l * 1000l;
        long time2 = todayBenginTime + 12l * 3600l * 1000l;
        long time3 = todayBenginTime + 18l * 3600l * 1000l;
        long time4 = todayBenginTime + 21l * 3600l * 1000l;

        if (type == BaseItemShop.SHOP_TYPE_3) {
            time1 = todayBenginTime + 21l * 3600l * 1000l;
            time2 = todayBenginTime + 21l * 3600l * 1000l;
            time3 = todayBenginTime + 21l * 3600l * 1000l;
            time4 = todayBenginTime + 21l * 3600l * 1000l;
        }

        long setTime;
        if (nowUnixTime < time1) {
            setTime = time1;
        } else if (nowUnixTime < time2) {
            setTime = time2;
        } else if (nowUnixTime < time3) {
            setTime = time3;
        } else if (nowUnixTime < time4) {
            setTime = time4;
        } else {
            setTime = time1 + 86400 * 1000; // 明天9点
        }

        itemShopModel.setRefreshTime(setTime);

    	this.redisTemplate.opsForHash().put(RedisKey + type, roleId, itemShopModel);
    }

}
