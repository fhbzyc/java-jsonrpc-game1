package com.zhanglong.sg.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.GiftDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.GiftCode;
import com.zhanglong.sg.entity.GiftTemplate;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/exchangecode")
public class ExchangeCodeService extends BaseService {

	@Resource
	private GiftDao giftDao;

	/**
	 * 
	 * @param code
	 * @return
	 * @throws Throwable
	 */
    public Object exchange(String code) throws Throwable {

        int roleId = this.roleId();

        Result result = new Result();

        GiftCode gift = this.giftDao.findOne(code);

        if (gift == null) {
        	return this.returnError(this.lineNum(), "兑换码无效");
        }

        GiftTemplate giftTemplate = this.giftDao.findOne(gift.getGiftId());

        String giftName = giftTemplate.getName();

    	ObjectMapper mapper = new ObjectMapper();

    	Reward reward = mapper.readValue(giftTemplate.getReward(), Reward.class);

        if (reward != null) {

        	Role role = this.roleDao.findOne(roleId);
        	this.rewardDao.get(role, reward, "兑换码领取<" + giftName + ">", FinanceLog.STATUS_CODE_GET, result);
        	
        	this.giftDao.insertLog(role, giftTemplate, code);
        }

        this.giftDao.delete(gift);

        return this.success(result.toMap());
    }
}
