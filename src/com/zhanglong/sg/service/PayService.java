package com.zhanglong.sg.service;

import java.util.List;

import javax.annotation.Resource;

import com.googlecode.jsonrpc4j.JsonRpcService;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.BasePayShopDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.entity.BasePayShop;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/pay")
public class PayService extends BaseService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private BasePayShopDao basePayShopDao;
    
	public Object list() throws Throwable {

		List<BasePayShop> list = this.basePayShopDao.findAll();
		return this.success(list);
	}

	public Object firstIcon() throws Throwable {
	
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
}
