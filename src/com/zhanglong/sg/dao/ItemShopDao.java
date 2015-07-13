package com.zhanglong.sg.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity2.BaseItemShop;
import com.zhanglong.sg.model.ItemShopModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class ItemShopDao extends BaseDao {

    private static String RedisKey = "ITEM_SHOP_";

    @Resource
    private BaseItemShopDao baseItemShopDao;

    @Resource
    private JedisConnectionFactory jedisConnectionFactory;

    public ItemShopDao() {
    }

    public ItemShopModel getShopByType(int roleId, int type, boolean refresh) throws Exception {

        JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
        String json = jedisConnection.getNativeConnection().get(ItemShopDao.RedisKey + type + roleId);
        jedisConnection.close();

        ItemShopModel itemShopModel = null;

        if (json != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            itemShopModel = objectMapper.readValue(json, ItemShopModel.class);
        }

        if (itemShopModel == null || (refresh && System.currentTimeMillis() >= itemShopModel.getRefreshTime())) {

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
            
            this.save(roleId, type, itemShopModel, true);
        }

        return itemShopModel;
    }

    public ItemShopModel buyItem(int roleId, int type, int index) throws Exception {

        ItemShopModel itemShopModel = this.getShopByType(roleId, type, false);
        BaseItemShop baseItemShop = itemShopModel.getItemList().get(index - 1);
        baseItemShop.setSold(true);

        this.save(roleId, type, itemShopModel, false);
        return itemShopModel;
    }

    public ItemShopModel refresh(int roleId, int type) throws Exception {

        int num = 8;

        int discount = this.baseItemShopDao.getDiscount(type);

        BaseItemShop[] items = this.ref(type, num, discount);

        List<BaseItemShop> itemList = new ArrayList<BaseItemShop>();
        for (BaseItemShop baseItemShop : items) {
            itemList.add(baseItemShop);
        }

        ItemShopModel itemShopModel = this.getShopByType(roleId, type, true);

        itemShopModel.setItemList(itemList);
        itemShopModel.setRefreshNum(itemShopModel.getRefreshNum() + 1);
        itemShopModel.setDiscount(discount);

        this.save(roleId, type, itemShopModel, true);
        return itemShopModel;
    }

    private BaseItemShop[] ref(int type, int num, int discount) throws Exception {

        List<BaseItemShop> shopItemList = this.baseItemShopDao.findByType(type);

        if (shopItemList.size() <= num) {
            throw new Exception("商店配置出错,无法刷新");
        }

        int[] tempList = new int[num];
        BaseItemShop[] result = new BaseItemShop[num];

        int begin = 0;

        if (type == BaseItemShop.MONEY_TYPE_3) {
            List<BaseItemShop> delIdList = new ArrayList<BaseItemShop>();
            for (int i = 0 ; i < shopItemList.size() ; i++) {
                BaseItemShop item = shopItemList.get(i);
                int itemId = item.getItemId();
                if (itemId == 4017 || itemId == 4020) {
                    // 前两个固定灵魂石

                    result[begin] = item;
 
                    begin++;
                    delIdList.add(item);
                    continue;
                }
            }

            for (BaseItemShop delId : delIdList) {
                shopItemList.remove(delId);
            }
        } else if (type == BaseItemShop.SHOP_TYPE_4) {
            List<BaseItemShop> delIdList = new ArrayList<BaseItemShop>();
            for (int i = 0 ; i < shopItemList.size() ; i++) {
                BaseItemShop item = shopItemList.get(i);
                int itemId = item.getItemId();
                if (itemId == 4005 || itemId == 4010) {

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

    private void save(int roleId, int type, ItemShopModel itemShopModel, boolean updateTime) throws ParseException, JsonProcessingException {

        if (updateTime) {
            long nowUnixTime = System.currentTimeMillis();

            Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());
            long todayBenginTime = date.getTime();

            long time1 = todayBenginTime + 21l * 3600l * 1000l;
            long time2 = todayBenginTime + 21l * 3600l * 1000l;
            long time3 = todayBenginTime + 21l * 3600l * 1000l;
            long time4 = todayBenginTime + 21l * 3600l * 1000l;

            if (type == BaseItemShop.SHOP_TYPE_1) {
                time1 = todayBenginTime + 9l * 3600l * 1000l;
                time2 = todayBenginTime + 12l * 3600l * 1000l;
                time3 = todayBenginTime + 18l * 3600l * 1000l;
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
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(itemShopModel);

        JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
        jedisConnection.getNativeConnection().set(ItemShopDao.RedisKey  + type + roleId, json);
        jedisConnection.close();
    }
}
