package com.zhanglong.sg.service;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/online")
public class OnlineService extends BaseService {

	/**
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Object num() throws Throwable {

		int roleId = this.roleId();

		Result result = new Result();

		Role role = this.roleDao.findOne(roleId);
		
		int num = getNum(role);
		result.setValue("num", num);

		result.setValue("coin", coin(num));
		result.setValue("gold", gold(num));

		return this.success(result.toMap());
	}
	
	/**
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Object reward() throws Throwable {

		int roleId = this.roleId();

		Role role = this.roleDao.findOne(roleId);
		int num = getNum(role);

		if (num >= 3) {
			return this.returnError(this.lineNum(), "最多了领三次");
		}

		this.setNum(roleId, num + 1);

		Result result = new Result();

		result.setValue("num", num + 1);

		this.roleDao.addCoin(role, coin(num), "在线奖励第<" + num +">天", 0, result);
		this.roleDao.addGold(role, gold(num), "在线奖励第<" + num +">天", 0, result);
		this.roleDao.update(role, result);

		result.setValue("coin", coin(num + 1));
		result.setValue("gold", gold(num + 1));
		
		return this.success(result.toMap());
	}

	public static int coin(int num) {
		int coin = 0;
		switch (num) {
		case 0 :
			coin = 20000;
			break;
		case 1 :
			coin = 30000;
			break;
		case 2 :
			coin = 40000;
			break;
		}
		
		return coin;
	}

	public static int gold(int num) {
		int gold = 0;
		switch (num) {
		case 0 :
			gold = 10;
			break;
		case 1 :
			gold = 20;
			break;
		case 2 :
			gold = 40;
			break;
		}

		return gold;
	}
	
	public int getNum(Role role) {

		long createTime = role.getCreateTime().getTime();

		int num = 0;

		if ((System.currentTimeMillis() - createTime) / (3600 * 24 * 1000) >= 7) {
			num = 4;
		} else {
			DateNumModel dateNumModel = this.dateNumDao.findOne(role.getRoleId());
			num = dateNumModel.getOnline();
		}

		return num;
	}

	private void setNum(int roleId, int num) {
		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		dateNumModel.setOnline(num);
		this.dateNumDao.save(roleId, dateNumModel);
	}
}
