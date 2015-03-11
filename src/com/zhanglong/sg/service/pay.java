package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.DailyTaskDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Order;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;


public class pay extends BaseClass {
//
//    private static String PAY_STORE = "pay_store";
//
//    /**
//     * 充值商店
//     * @param tokenS
//     * @return
//     * @throws Throwable
//     */
//    public List<Item> list(String tokenS) throws Throwable {
//
//        String roleId = this.verifyToken(tokenS);
//
//        SgpJpaRepository<Item, String> jpaRepository = this.context.getSgpJpaRepository(Item.class);
//
//        Item where1 = new Item();
//        where1.setStatus(Item.ENABLED);
//        where1.setStoreId(PAY_STORE);
//
//        List<Item> goodsList = jpaRepository.findAll(SgpSpecification.create(this.context.getEntityManager(), where1));
//
//        OrderDao orderDao = new OrderDao();
//        List<OrderTable> successOrderList = orderDao.findComplateList(roleId);
////        SgpJpaRepository<OrderTable, String> jpaRepository2 = this.context.getSgpJpaRepository(OrderTable.class);
////        OrderTable where2 = new OrderTable();
////        where2.setRoleId(roleId);
////        where2.setStatus(OrderTable.STATUS_SUCCESS);
////        List<OrderTable> successOrderList = jpaRepository2.findAll(SgpSpecification.create(this.context.getEntityManager(), where2));
//
//    	for (Item item : goodsList) {
//			if (item.getType().equals("moon_card")) {
//
//		        DailyTaskDao DailyTask = new DailyTaskDao(roleId);
//		        long vipTime = DailyTask.getVipTime();
//		        long time = vipTime - System.currentTimeMillis();
//
//		        if (time > 0) {
//					if (item.getType().equals("moon_card")) {
//						item.setDescription("月卡生效中，剩余 " + (int)Math.ceil(time / (86400d * 1000d)) + " 天");
//					}
//		        }
//			} else {
//
//				boolean find = false;
//				for (OrderTable orderTable : successOrderList) {
//					
//					if ((int)orderTable.getGold() == (int)item.getAmount()) {
//						find = true;
//						break;
//					}
//				}
//				if (!find) {
//					item.setDescription("另赠" + (int)item.getAmount() + "元宝(限赠1次)");
//				}
//			}
//		}
//
//        return goodsList;
//    }
//
//    /**
//     * 下订单
//     * @param tokenS
//     * @param index
//     * @return
//     * @throws Throwable
//     */
//    public Order order(String tokenS, int index) throws Throwable {
// 
//        String roleId = this.verifyToken(tokenS);
//
//        if (index <= 0) {
//            throw new Throwable("Illegal request !");
//        }
//
//        List<Item> list = this.list(tokenS);
//
//        if (index > list.size()) {
//            throw new Throwable("Illegal request !");
//        }
//
//        Item item = list.get(index - 1);
//
//        if (item.getType().equals("moon_card")) {
//	        DailyTaskDao DailyTask = new DailyTaskDao(roleId);
//	        long vipTime = DailyTask.getVipTime();
//	        long time = vipTime - System.currentTimeMillis();
//
//	        if ((int)Math.ceil(time / (86400d * 1000d)) > 3) {
//	        	throw new Throwable("月卡生效中,倒数第三天后可购买");
//	        }
//        }
//
//        // 统计
//        HashMap<String, String> eventData = new HashMap<String, String>();
//        eventData.put("xiaDingDan", String.valueOf(item.getPrice()));
//        Event event = new Event();
//    	event.setEventId("zhiFu");
//    	event.setEventData(eventData);
//    	event.setSgpPlayerId(roleId);
//    	event.start();
//
//        Token T = TokenInstance.getToken(this.context.getRedisHandle());
//
//        DelegateDidServiceImpl delegateDidServiceImpl = this.context.getSgpService(DelegateDidServiceImpl.class);
//        String did = delegateDidServiceImpl.createDid(T.getServerId(tokenS), T.getUid(tokenS), roleId);
//
//        OrderTable order = new OrderTable();
//        order.setRoleId(roleId);
//        order.setType(item.getType());
//        order.setDid(did);
//        order.setGold(item.getAmount());
//        order.setMoney(item.getPrice());
//        order.setAddGold(item.getQuality());
//        order.setStatus(OrderTable.STATUS_CREATE);
//
//        OrderDao orderDao = new OrderDao();
//        orderDao.create(order);
//
//        Order o = new Order();
//        o.setId(order.getId());
//        o.setDid(did);
//        o.setPlayerId(roleId);
//        return o;
//    }
//
//    /**
//     * 支付成功领取元宝
//     * @param tokenS
//     * @param orderId
//     * @return
//     * @throws Throwable
//     */
//    @Transactional(rollbackFor = Throwable.class)
//    public HashMap<String, Object> complete(String tokenS, int orderId) throws Throwable {
//
//        String roleId = this.verifyToken(tokenS);
//
////        SgpJpaRepository<OrderTable, String> jpaRepository = this.context.getSgpJpaRepository(OrderTable.class);
////        
////        OrderTable where = new OrderTable();
////        where.setId(orderId);
////        OrderTable order = jpaRepository.findOne(SgpSpecification.create(this.context.getEntityManager(), where));
//
//        OrderDao orderDao = new OrderDao();
//        OrderTable order = orderDao.findOne(orderId);
//
//        if (order == null || !order.getRoleId().equals(roleId)) {
//            throw new Throwable("错误,订单不存在");
//        }
//
//        List<OrderTable> list = orderDao.findComplateList(roleId);
//
// //       int money = 0;
//        int countGold = 0;
//
//        for (OrderTable o : list) {
//            if (o.getId() == orderId) {
//                throw new Throwable("订单已完成");
//            }
//            countGold += o.getGold();
// //           money += o.getMoney();
//        }
//
//        boolean isDouble = false;
//
//        if (!order.getType().equals("moon_card")) {
//        	boolean find = false;
//            for (OrderTable o : list) {
//                if ((int)o.getGold() == (int)order.getGold()) {
//                	find = true;
//                }
//            }
//            if (!find) {
//            	isDouble = true;
//            }
//        }
//
//        // 支付完成统计
//        HashMap<String, String> eventData = new HashMap<String, String>();
//        eventData.put("wanCheng", String.valueOf(order.getMoney()));
//        Utils.addCustomEvent("zhiFu", eventData, roleId);
//
//        DelegateDidServiceImpl delegateDidServiceImpl = this.context.getSgpService(DelegateDidServiceImpl.class);
//        ExternalCallbackBean callback = delegateDidServiceImpl.queryByDid(order.getDid());
//
//        Result result = new Result();
//
//        eventData = new HashMap<String, String>();
//
//        RoleModel Role = new RoleModel(roleId);
//
//        boolean success = false;
//        if (callback != null && callback.getUpdateTime() != null) {
//
//            success = true;
//            try {
//                ObjectMapper mapper = new ObjectMapper();
//                HashMap<String, Object> map = mapper.readValue(callback.getContent(), new TypeReference<Map<String, Object>>(){});
//
//                int realMoney = (Integer)map.get("trade_fee");
//                if (order.getMoney() != realMoney) {
//                	order.setMoney(realMoney);
//                	order.setGold(realMoney / 10);
//                	order.setAddGold(0);
//                }
//
//            } catch (Exception e) {
//                throw new Throwable("回调数据解析出错");
//            }
//
//            order.setStatus(OrderTable.STATUS_SUCCESS);
//            orderDao.update(order);
//
//            int gold = order.getGold();
//
//            countGold += gold;
//
//            // 月卡不加元宝
//            
//            if (isDouble) {
//            	gold *= 2;
//            } else {
//                gold += order.getAddGold();
//            }
//
//            Role.addGold(gold, "充值成功获得<" + gold + "元宝>", FinanceLog.STATUS_PAY_GET_GOLD, result);
//
//            int newVip = Role.getVipLevelupGold(countGold)[0];
//
//            int oldVip = Role.vip;
//
//            if (newVip > oldVip) {
//                Role.vip = newVip;
//
//                // VIP统计
//                HashMap<String, String> eventData2 = new HashMap<String, String>();
//                eventData2.put("vip" + newVip, "vip" + newVip);
//                Event event = new Event();
//            	event.setEventId("vip");
//            	event.setEventData(eventData2);
//            	event.setSgpPlayerId(roleId);
//            	event.start();
//            }
//
//            Role.save(result);
//
//            // 月卡每日任务加1
//            if (order.getType().equals("moon_card")) {
//                DailyTaskDao dailyTaskModel = new DailyTaskDao(roleId);
//                dailyTaskModel.addMoonCard(result);
//            } else {
//
//            	if (order.getGold() >= 250) {
//        	        DailyTaskDao DailyTask = new DailyTaskDao(roleId);
//        	        long vipTime = DailyTask.getVipTime();
//        	        long time = vipTime - System.currentTimeMillis();
//
//        	        if (time <= 0) {
//                        DailyTaskDao dailyTaskModel = new DailyTaskDao(roleId);
//                        dailyTaskModel.addMoonCard(result);
//        	        }
//            	}
//            }
//
//            // 成为VIP任务完成
//            if (newVip > 0 && oldVip <= 0) {
//                DailyTaskDao dailyTaskModel = new DailyTaskDao(roleId);
//                dailyTaskModel.addVip(result);
//            }
//
//            // 支付成功发货统计
//            eventData = new HashMap<String, String>();
//            eventData.put("chengGongFaHuo", String.valueOf(order.getMoney()));
//            Utils.addCustomEvent("zhiFu", eventData, roleId);
//
////            try {
////
////            	money += order.getMoney();
////
////                SgpPlayerServiceImpl playerService = this.context.getSgpService(SgpPlayerServiceImpl.class);
////                SgpPlayer player = playerService.getSgpPlayerById(roleId);
////                player.setLevel(Role.getLevel());
////                player.setVip(newVip);
////                playerService.update(player);
////
////            } catch (Exception e) {
////                
////            }
//        }
//
//        HashMap<String, Object> r = result.returnMap();
//        r.put("success", success);
//        r.put("callback", callback);
//        r.put("vip", new int[]{Role.vip , countGold, Role.getVipLevelupGold(countGold)[1]});
//
//        return r;
//    }
//    
//
////    @Transactional(rollbackFor = Throwable.class)
////    public HashMap<String, Object> repair(String password, String did) throws Throwable {
////
////        if (!MD5.digest(did + "speed").equals(password)) {
////        	throw new Throwable("Permission denied");
////        }
////
////        SgpJpaRepository<OrderTable, String> jpaRepository = this.context.getSgpJpaRepository(OrderTable.class);
////        OrderTable where = new OrderTable();
////        where.setDid(did);
////
////        OrderTable order = jpaRepository.findOne(SgpSpecification.create(this.context.getEntityManager(), where));
////        
////        if (order == null) {
////        	throw new Throwable("订单号错误");
////        }
////
////        if (order.getStatus() == OrderTable.STATUS_SUCCESS) {
////        	throw new Throwable("订单已完成");
////        }
////
////        String roleId = order.getRoleId();
////
////        where = new OrderTable();
////        where.setRoleId(roleId);
////        where.setStatus(OrderTable.STATUS_SUCCESS);
////        List<OrderTable> list = jpaRepository.findAll(SgpSpecification.create(this.context.getEntityManager(), where));
////
////        int money = 0;
////        int countGold = 0;
////
////        boolean isFirst = true;
////
////        for (OrderTable o : list) {
////
////            countGold += o.getGold();
////            money += o.getMoney();
////            
////            if (!o.getType().equals("moon_card")) {
////            	isFirst = false;
////            }
////        }
////
////        // 支付完成统计
////        HashMap<String, String> eventData = new HashMap<String, String>();
////        eventData.put("wanCheng", String.valueOf(order.getMoney()));
////        Utils.addCustomEvent("zhiFu", eventData, roleId);
////
////        Result result = new Result();
////
////        eventData = new HashMap<String, String>();
////
////        RoleModel Role = new RoleModel(roleId);
////
////        int vip = 0;
////        boolean success = false;
////
////            success = true;
////
////            order.setStatus(OrderTable.STATUS_SUCCESS);
////            jpaRepository.save(order);
////
////            int gold = order.getGold();
////
////            countGold += gold;
////
////            // 月卡不加元宝
////            if (!order.getType().equals("moon_card")) {
////                // 首冲双倍的 发双倍
////                if (isFirst) {
////                	gold *= 2;
////                } else if (order.getType().equals("add")) {
////                    gold += order.getAddGold();
////                }
////
////                Role.addGold(gold, "充值成功获得<" + gold + "元宝>", FinanceLog.STATUS_PAY_GET_GOLD, result);
////            } 
////
////            int newVip = Role.getVipLevelupGold(countGold)[0];
////
////            int oldVip = Role.vip;
////
////            if (newVip > oldVip) {
////                Role.vip = newVip;
////                vip = Role.vip;
////  
////                // VIP统计
////                HashMap<String, String> eventData2 = new HashMap<String, String>();
////                eventData2.put("vip" + newVip, "vip" + newVip);
////                Event event = new Event();
////            	event.setEventId("vip");
////            	event.setEventData(eventData2);
////            	event.setSgpPlayerId(roleId);
////            	event.start();
////            }
////
////            Role.save(result);
////
////            // 月卡每日任务加1
////            if (order.getType().equals("moon_card")) {
////                DailyTaskModel dailyTaskModel = new DailyTaskModel(roleId);
////                dailyTaskModel.addMoonCard(result);
////            }
////
////            // 成为VIP任务完成
////            if (newVip > 0 && oldVip <= 0) {
////                DailyTaskModel dailyTaskModel = new DailyTaskModel(roleId);
////                dailyTaskModel.addVip(result);
////            }
////
////            // 支付成功发货统计
////            eventData = new HashMap<String, String>();
////            eventData.put("chengGongFaHuo", String.valueOf(order.getMoney()));
////            Utils.addCustomEvent("zhiFu", eventData, roleId);
////
////            try {
////
////            	money += order.getMoney();
////
////                SgpPlayerServiceImpl playerService = this.context.getSgpService(SgpPlayerServiceImpl.class);
////                SgpPlayer player = playerService.getSgpPlayerById(roleId);
////                player.setLevel(Role.getLevel());
////                player.setVip(newVip);
////                player.setMoney((double)money);
////                playerService.update(player);
////
////            } catch (Exception e) {
////                
////            }
////
////        HashMap<String, Object> r = result.returnMap();
////        r.put("success", success);
////
////        if (vip > 0) {
////            r.put("vip", new int[]{vip , countGold, Role.getVipLevelupGold(countGold)[1]});
////        }
////
////        return r;
////    }
//
//    
//    public Object firstIcon(String tokenS) throws Throwable {
//
//    	String roleId = this.verifyToken(tokenS);
//
//    	OrderDao orderDao = new OrderDao();
//    	int count = orderDao.after6(roleId);
//    	
//    	boolean bool = true;
//    	if (count > 0) {
//    		bool = false;
//    	}
//    	
//    	HashMap<String, Boolean> map = new HashMap<String, Boolean>();
//    	map.put("first", bool);
//    	return map;
//    }
}
