package com.zhanglong.sg.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.BasePayShopDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.entity.BasePayShop;
import com.zhanglong.sg.entity.Order;
import com.zhanglong.sg.result.Result;

@Service
public class PayService extends BaseService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private BasePayShopDao basePayShopDao;
    
	public Object list() throws Exception {

		List<BasePayShop> list = this.basePayShopDao.findAll();
		return this.success(list);
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
    	order.setAddGold(basePayShop.getOrder());

    	this.orderDao.create(order);

    	return this.success(order.getId());
    }
}
