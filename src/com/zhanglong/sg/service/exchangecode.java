package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.Map;

import me.gall.sgp.node.pojo.app.Gift;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.RewardDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

public class exchangecode extends BaseClass {

	/**
	 * 兑换
	 * @param tokenS
	 * @param code
	 * @return
	 * @throws Throwable
	 */
    public HashMap<String, Object> exchange(String code) throws Throwable {

        int roleId = this.roleId();

        Result result = new Result();

        String rewardString = "";
        String giftName = "";
        String giftNameZh = "";
        try {

           // GiftCodeServiceImpl giftCodeServiceImpl = this.context.getSgpService(GiftCodeServiceImpl.class);
            Gift gift = new Gift();

            giftName = gift.getCodeId();
            giftNameZh = gift.getName();

         //   rewardString = giftCodeServiceImpl.redeem(roleId, gift.getUuid(), code);
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception("兑换码无效");
		}

    	ObjectMapper mapper = new ObjectMapper();
    	HashMap<String, Reward> map = mapper.readValue(rewardString, new TypeReference<Map<String, Reward>>(){});

    	Reward reward = map.get("reward");

        if (reward != null) {

        	Role role = this.roleDao.findOne(roleId);
        	this.rewardDao.get(role, reward, "兑换码领取<" + giftNameZh + ">", FinanceLog.STATUS_CODE_GET, result);
        }

        return result.toMap();
    }
}
