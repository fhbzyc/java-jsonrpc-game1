package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.BasePayShopDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.entity.BasePayShop;
import com.zhanglong.sg.entity.Order;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.result.Result;

@Service
public class PayService extends BaseService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private BasePayShopDao basePayShopDao;
    
	public Object list() throws Exception {

		int roleId = this.roleId();

		List<BasePayShop> list = this.basePayShopDao.findAll();

		List<BasePayShop> list2 = new ArrayList<BasePayShop>();

		List<Integer> orders = this.orderDao.group(roleId);

		Role role = this.roleDao.findOne(roleId);

        boolean find1RMB = false;
        for (int money : orders) {
			if (money == 10) {
				find1RMB = true;
			}
		}

    	for (BasePayShop basePayShop : list) {

    		BasePayShop item = basePayShop.clone();

    		if (find1RMB) {
				if (item.getMoney() == 10) {
					continue;
				}
    		}

			if (item.getType() == BasePayShop.M_CARD) {

		        int vipTime = role.cardTime;
		        int time = vipTime - (int)(System.currentTimeMillis() / 1000l);

		        if (time > 0) {
					item.setDesc("月卡生效中，剩余 " + (int)Math.ceil(time / (86400d)) + " 天");
		        }
			} else {

				boolean find = false;
				for (Integer money : orders) {

					if (money == (int)item.getMoney()) {
						find = true;
						break;
					}
				}
				if (!find) {
					item.setDesc("另赠" + (int)item.getGold() + "元宝(限赠1次)");
				}
			}
			
			list2.add(item);
		}

		return this.success(list2);
	}

	public Object firstIcon() throws Exception {
	
	  	int roleId = this.roleId();

	  	int count = this.orderDao.after6(roleId);

	  	boolean bool = true;
	  	if (count > 0) {
	  		bool = false;
	  	}
	
	  	Result result = new Result();
	  	result.setValue("first", bool);
	  	return this.success(result.toMap());
	}

    public Object order(int id) throws Exception {

    	BasePayShop basePayShop = this.basePayShopDao.findOne(id);
    	if (basePayShop == null) {
    		return returnError(this.lineNum(), "参数出错");
    	}

    	Order order = new Order();
    	order.setUserId(this.userId());
    	order.setServerId(this.serverId());
    	order.setRoleId(this.roleId());
    	order.setType(basePayShop.getType());
    	order.setMoney(basePayShop.getMoney());
    	order.setGold(basePayShop.getGold());
    	order.setAddGold(basePayShop.getAddGold());

    	this.orderDao.create(order);

    	return this.success(order.getId());
    }
}
