package com.zhanglong.sg.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.BaseCheckinDao;
import com.zhanglong.sg.dao.CheckinDao;
import com.zhanglong.sg.entity.BaseCheckin;
import com.zhanglong.sg.entity.Checkin;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.LjCheckin;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
@JsonRpcService("/checkin")
public class CheckinService extends BaseService {

	@Resource
	private BaseCheckinDao baseCheckinDao;

	@Resource
	private CheckinDao checkinDao;

	public Object list() throws Exception {

		int roleId = this.roleId();

		int month = this.checkinDao.month();
		BaseCheckin baseCheckin = this.baseCheckinDao.findOne(month);

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Integer>[] data = mapper.readValue(baseCheckin.getReward(), new TypeReference<HashMap<String, Integer>[]>(){});

        int num = 0;

        boolean find = false;

        List<Checkin> list = this.checkinDao.findAll(roleId);

        for (Checkin checkin : list) {
			if ((int)checkin.getDay() == Integer.valueOf(Utils.date())) {
				num = checkin.getNum();
				find = true;
			}
		}

        int days = list.size();
        if (find && days > 0) {
        	days--;
        }

		Result result = new Result();
		result.setValue("sign_times", days);
        result.setValue("reward", data);
        result.setValue("status", num);

		// 累计签到
		HashMap<Integer, Reward> obj = mapper.readValue(baseCheckin.getLj(), new TypeReference<Map<Integer, Reward>>(){});

		int needLj = this.ljNum(roleId);

        result.setValue("lj", obj.get(needLj));
        result.setValue("lj_num", new int[]{list.size() , needLj});

        return this.success(result.toMap());
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object checkin() throws Exception {

		int roleId = this.roleId();

		Checkin checkin = new Checkin();
		boolean find = false;
		int status = 0;
        List<Checkin> list = this.checkinDao.findAll(roleId);

        int ljNum = list.size();

        for (Checkin temp : list) {
			if ((int)temp.getDay() == Integer.valueOf(Utils.date())) {
				status = temp.getNum();
				if (status == 2) {
					return this.returnError(this.lineNum(), "不能再领");
				}
				checkin = temp;
				find = true;
			}
		}

        int month = this.checkinDao.month();
        if (!find) {

        	ljNum++;

        	checkin.setMonth(month);
        	checkin.setDay(Integer.valueOf(Utils.date()));
        	checkin.setNum(0);
        	checkin.setRoleId(roleId);
        }

	    int days = list.size();
        if (find && days > 0) {
        	days--;
        }

		String json = this.baseCheckinDao.findOne(month).getReward();

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Integer>[] data = mapper.readValue(json, new TypeReference<HashMap<String, Integer>[]>(){});

        HashMap<String, Integer> map = data[days];

        String desc = "签到" + (days + 1) + "天";

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);

        boolean isDouble = false;
        Integer vipLv = map.get("vip_lv");

        if (vipLv == null) {
        	status = 2;
        } else {

        	if (status == 1 && role.getVip() < vipLv) {
        		return this.returnError(this.lineNum(), "vip" + vipLv + "才能再领奖品");
        	}

        	if (status == 0 && vipLv > 0 && role.getVip() >= vipLv) {
        		isDouble = true;
        		status = 2;
        	} else {
        		status++;
        	}
        }

        checkin.setNum(status);

        if (find) {
        	this.checkinDao.update(roleId, checkin);
        } else {
        	this.checkinDao.create(roleId, checkin);
        }

        Integer coin = map.get("coin");
        if (coin != null) {
        	if (isDouble) {
        		coin *= 2;
        	}
        	this.roleDao.addCoin(role, coin, desc, FinanceLog.STATUS_SIGN_GET_COIN, result);
        }
        Integer gold = map.get("gold");
        if (gold != null) {
        	if (isDouble) {
        		gold *= 2;
        	}
        	this.roleDao.addGold(role, gold, desc, FinanceLog.STATUS_SIGN_GET_GOLD, result);
        }

        if (coin != null || gold != null) {
        	this.roleDao.update(role, result);
        }

        Integer itemId = map.get("item_id");
        Integer num = map.get("count");
        if (itemId != null && num != null) {
        	if (isDouble) {
        		num *= 2;
        	}
        	this.itemDao.addItem(roleId, itemId, num, result);
        }

        Integer heroId = map.get("hero_id");
        if (heroId != null) {

        	Hero hero = this.heroDao.findOne(roleId, heroId);
        	if (hero == null) {
        		this.heroDao.create(role, heroId, result);
        	} else {
        		int soulNum = this.heroDao.soulNumByStar(heroId);
        		this.itemDao.addItem(roleId, heroId - 6000, soulNum, result);
        	}
        }

    	result.setValue("sign_times", days);
    	result.setValue("status", status);
    	result.setValue("lj_num", new int[]{ljNum , this.ljNum(roleId)});
		return this.success(result.toMap());
	}

	public Object lj() throws Exception {

		int roleId = this.roleId();

		LjCheckin ljCheckin = this.checkinDao.findLj(roleId);
		int lj_num = ljCheckin.getNum();
		if (lj_num == 0) {
			lj_num = 3;
		}

		List<Checkin> list = this.checkinDao.findAll(roleId);
		int days = list.size();

		ObjectMapper mapper = new ObjectMapper();
		Result result = new Result();
		
		int month = this.checkinDao.month();
		BaseCheckin baseCheckin = this.baseCheckinDao.findOne(month);

		HashMap<Integer, Reward> obj = mapper.readValue(baseCheckin.getLj(), new TypeReference<Map<Integer, Reward>>(){});

		if (days >= lj_num) {

			Reward reward = obj.get(lj_num);
	        if (reward != null) {

	        	int oldNum = lj_num;
	        	if (lj_num % 7 == 0) {
	        		lj_num += 3;
	        	} else {
	        		lj_num += 2;
	        	}

	        	ljCheckin.setNum(lj_num);

	        	this.checkinDao.updateLj(ljCheckin);

	        	Role role = this.roleDao.findOne(roleId);
	        	this.rewardDao.get(role, reward, "累计签到<" + oldNum + ">", 0, result);
	        }

		} else {
			this.returnError(this.lineNum(), "天数不够不能领奖");
		}

        result.setValue("lj", obj.get(lj_num));
        result.setValue("lj_num", new int[]{days , lj_num});

		
		return this.success(result.toMap());
	}

	private int ljNum(int roleId) throws JsonParseException, JsonMappingException, IOException {

		int num = this.checkinDao.findLj(roleId).getNum();
		if (num == 0) {
			num = 3;
		}

		return num;
	}
}
