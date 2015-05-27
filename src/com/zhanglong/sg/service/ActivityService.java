package com.zhanglong.sg.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.ActivityDao;
import com.zhanglong.sg.dao.BaseActivityDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.entity.Activity;
import com.zhanglong.sg.entity.BaseActivity;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;

@Service
public class ActivityService extends BaseService {

	@Resource
	private BaseActivityDao baseActivityDao;

	@Resource
	private ActivityDao activityDao;

	@Resource
	private OrderDao orderDao;

	public Object list() throws Exception {

		int roleId = this.roleId();
		int serverId = this.serverId();
		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

		List<Activity> myActs = this.activityDao.findAll(roleId);

        ObjectMapper mapper = new ObjectMapper();

        Role role = this.roleDao.findOne(roleId);

        List<BaseActivity> rep = new ArrayList<BaseActivity>();

		for (BaseActivity baseAct : list) {

			if (baseAct.getType().equals("first")) {
				// 首冲
	            for (Activity activity : myActs) {
					if (activity.getActId() == (int)baseAct.getId()) {
						continue;
					}
				}
			}

			if (baseAct.getType().equals("7days")) {
				// 新服七天
				long createTime = role.createTime.getTime();

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = simpleDateFormat.format(new Date(createTime));
				Date date = simpleDateFormat.parse(dateString);  
				createTime = date.getTime();

				int days = (int)((System.currentTimeMillis() - createTime) / (86400l * 1000l));
		        if (days >= 7) {
		        	continue;
		        }
			}

			HashMap<Integer, Reward> rewards = mapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});
	        for (Iterator<Map.Entry<Integer, Reward>> iter = rewards.entrySet().iterator(); iter.hasNext();) {
	            Map.Entry<Integer, Reward> entry = iter.next();
	            Reward reward = entry.getValue();
	            reward.setHas(false);

	            if (baseAct.getType().equals("7days")) {
					long createTime = role.createTime.getTime();
					int days = (int)((System.currentTimeMillis() - createTime) / (86400l * 1000l));
			        if (days >= entry.getKey() - 1) {
			        	reward.setHas(true);
			        }
	            } else if (baseAct.getType().equals("levelup")) {
	            	if (role.level() >= entry.getKey()) {
	            		reward.setHas(true);
	            	}
	            } else if (baseAct.getType().equals("first")) {
	            	int c = this.orderDao.after6(roleId);
	            	if (c > 0) {
	            		reward.setHas(true);
	            	}
	            }

	            for (Activity activity : myActs) {
					if (activity.getActId() == (int)baseAct.getId() && activity.getKey() == (int)entry.getKey()) {
						iter.remove();
						break;
					}
				}
	        }
	        baseAct.setReward(mapper.writeValueAsString(rewards));
	        rep.add(baseAct);
		}

	  	Result result = new Result();
	  	result.setValue("activity", rep);
	  	return this.success(result.toMap());
	}

	public Object getReward(int actId, int key) throws Throwable {

		int roleId = this.roleId();
		int serverId = this.serverId();

		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

		BaseActivity baseAct = null;

		for (BaseActivity activity : list) {
			if (activity.getId() == actId) {
				baseAct = activity;
				if (activity.getType().equals("first")) {
	            	int c = this.orderDao.after6(roleId);
	            	if (c == 0) {
	            		return this.returnError(this.lineNum(), "不可领取");
	            	}
				}
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
