package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;

import com.zhanglong.sg.dao.ActivityDao;
import com.zhanglong.sg.dao.BaseActivityDao;
import com.zhanglong.sg.entity.Activity;
import com.zhanglong.sg.entity.BaseActivity;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/activity")
public class ActivityService extends BaseService {

	@Resource
	private BaseActivityDao baseActivityDao;

	@Resource
	private ActivityDao activityDao;

	public Object list() throws Throwable {

		int roleId = this.roleId();
		int serverId = this.serverId();
		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

		List<Activity> myActs = this.activityDao.findAll(roleId);
		
        ObjectMapper mapper = new ObjectMapper();

		for (BaseActivity baseAct : list) {
			HashMap<Integer, Reward> rewards = mapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});
	        for (Iterator<Map.Entry<Integer, Reward>> iter = rewards.entrySet().iterator(); iter.hasNext();) {
	            Map.Entry<Integer, Reward> entry = iter.next();
	            Reward reward = entry.getValue();
	            if (reward.getHas() == null) {
	            	reward.setHas(false);
	            }

	            for (Activity activity : myActs) {
					if (activity.getActId() == (int)baseAct.getId() && activity.getKey() == (int)entry.getKey()) {
						iter.remove();
						break;
					}
				}
	        }
	        baseAct.setReward(mapper.writeValueAsString(rewards));
		}

	  	Result result = new Result();
	  	result.setValue("activity", list);
	  	return this.success(result.toMap());
	}

	public Object getReward(int actId, int key) throws Throwable {

		int roleId = this.roleId();
		int serverId = this.serverId();

		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

		BaseActivity baseAct = null;

		for (BaseActivity baseActivity2 : list) {
			if (baseActivity2.getId() == actId) {
				baseAct = baseActivity2;
			}
		}

		if (baseAct == null) {
			return this.returnError(this.lineNum(), "参数出错");
		}

        ObjectMapper mapper = new ObjectMapper();
		HashMap<Integer, Reward> rewards = mapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});
		Reward reward = rewards.get(key);
		if (reward == null) {
			return this.returnError(this.lineNum(), "参数出错");
		}

		Result result = new Result();

		Activity act = this.activityDao.findOne(roleId, actId, key);
		if (act == null) {
			act = new Activity();
			act.setActId(actId);
			act.setKey(key);
			act.setRoleId(roleId);
			this.activityDao.create(act);

			Role role = this.roleDao.findOne(roleId);

			this.rewardDao.get(role, reward, "活动领奖<" + baseAct.getName() + ">", FinanceLog.STATUS_ACTIVITY_PAY, result);
		}

		return this.success(result.toMap());
	}
}
