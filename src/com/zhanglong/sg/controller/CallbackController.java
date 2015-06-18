package com.zhanglong.sg.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import websocket.handler.EchoHandler;

import com.zhanglong.sg.dao.DailyTaskDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.dao.RoleDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Order;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;

@Controller
public class CallbackController {

	@Resource
	private OrderDao orderDao;

	@Resource
	private RoleDao roleDao;
	
	@Resource
	private DailyTaskDao dailyTaskDao;

	@RequestMapping(value="/callback", method = RequestMethod.GET)
	@ResponseBody
	public String callback(@RequestParam(value = "id" , required = false) String id, Model model) throws Exception {

		int orderId = Integer.valueOf(id);
		Order order = this.orderDao.findOne(orderId);
		if (order == null) {
			return "fail";
		} else if (order.getStatus() != Order.STATUS_PAY) {
			return "fail";
		}

		int roleId = order.getRoleId();
		
		List<Integer> orders = this.orderDao.group(roleId);

		Role role = this.roleDao.findOne(roleId);

        boolean find = false;
        for (int money : orders) {
			if (money == order.getMoney()) {
				find = true;
			}
		}

        if (!find) {
        	order.setAddGold(order.getGold());
        }

		order.setStatus(Order.STATUS_SUCCESS);

		role.countGold += order.getGold();

		if (role.countGold >= Role.VIP_GOLD[Role.VIP_GOLD.length - 1]) {
			role.vip = Role.VIP_GOLD.length;
		}

		for (int i = 0 ; i < Role.VIP_GOLD.length ; i++) {
			if (role.countGold < Role.VIP_GOLD[i]) {
				if (i > role.vip) {
					role.vip = i;
				}
				break;
			}
		}

		Result result = new Result();
		if (role.vip > 0) {
			this.dailyTaskDao.addVip(role, result);
		}

		result.setValue("vip", role.vip());

		this.roleDao.addGold(role, order.getGold() + order.getAddGold(), "订单<" + orderId + ">", FinanceLog.STATUS_PAY_GET_GOLD, result);
		if (order.getType() == Order.MOON_CARD) {
    		role.cardTime = (int)(System.currentTimeMillis() / 1000l) + 86400 * 30;
    		this.dailyTaskDao.addMoonCard(role, result);
		}

		this.roleDao.update(role, result);

    	String msg = Response.marshalSuccess(0, result.toMap());
    	EchoHandler.pri(roleId, msg);

		return "success";
	}
}
