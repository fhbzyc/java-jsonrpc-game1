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
import com.zhanglong.sg.dao.FinanceLogDao;
import com.zhanglong.sg.dao.MailDao;
import com.zhanglong.sg.dao.OrderDao;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.entity.Activity;
import com.zhanglong.sg.entity.BaseActivity;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Mail;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
public class ActivityService extends BaseService {

	@Resource
	private BaseActivityDao baseActivityDao;

	@Resource
	private ActivityDao activityDao;

	@Resource
	private OrderDao orderDao;

	@Resource
	private ServerDao serverDao;

    @Resource
    private MailDao mailDao;

    @Resource
    private FinanceLogDao financeLogDao;

	public Object list() throws Exception {

		int roleId = this.roleId();
		int serverId = this.serverId();
		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

		List<Activity> myActs = this.activityDao.findAll(roleId);

        ObjectMapper mapper = new ObjectMapper();

        Role role = this.roleDao.findOne(roleId);

        List<BaseActivity> rep = new ArrayList<BaseActivity>();

        int touzi_num = this.financeLogDao.count(roleId, FinanceLog.STATUS_TOUZIJIHUA);
        boolean touzi = touzi_num > 0 ? true : false;

		for (BaseActivity baseAct : list) {

//			if (baseAct.getType().equals("first")) {
//				// 首冲
//	            for (Activity activity : myActs) {
//					if (activity.getActId() == (int)baseAct.getId()) {
//						continue;
//					}
//				}
//			}

			if (baseAct.getType().equals("7days")) {
				// 新服七天
				long createTime = role.createTime.getTime();

				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String dateString = simpleDateFormat.format(new Date(createTime));
				Date date = simpleDateFormat.parse(dateString);  
				createTime = date.getTime();

				int days = (int)(Math.ceil((double)(System.currentTimeMillis() - createTime) / (double)(86400l * 1000l)));
		        if (days > 7) {
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
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					String dateString = simpleDateFormat.format(new Date(createTime));
					Date date = simpleDateFormat.parse(dateString);  
					createTime = date.getTime();

					int days = (int)(Math.ceil((double)(System.currentTimeMillis() - createTime) / (double)(86400l * 1000l)));
			        if (days >= entry.getKey()) {
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
	            } else if (baseAct.getType().equals("money")) {
	            	int s = this.orderDao.sum(roleId, baseAct.getBeginTime().getTime());
	            	if (s >= entry.getKey()) {
	            		reward.setHas(true);
	            	}
	            } else if (baseAct.getType().equals("holiday")) {
	            	int days = Integer.valueOf(Utils.date());
	            	if (days == (int)entry.getKey()) {
	            		reward.setHas(true);
	            	} else if (days > (int)entry.getKey()) {
	            		iter.remove();
	            		continue;
	            	}
	            } else if (baseAct.getType().equals("vip")) {
	            	if (role.vip >= entry.getKey()) {
	            		reward.setHas(true);
	            	}
	            } else if (baseAct.getType().equals("gold")) {
	            	int sum = this.financeLogDao.sum(roleId, 2, baseAct.getBeginTime());
	            	if (sum >= entry.getKey()) {
	            		reward.setHas(true);
	            	}
	            } else if (baseAct.getType().equals("touzi")) {
	            	// 投资计划
	            	if (touzi && role.level() >= entry.getKey()) {
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

//	        if (rewards.size() == 0 && !baseAct.getType().equals("notice") && !baseAct.getType().equals("pk_rank") && !baseAct.getType().equals("lv_rank")) {
//	        	continue;
//	        }

	        String type = baseAct.getType();
	        if (type.equals("pk_rank") || type.equals("lv_rank") || type.equals("kill_rank") || type.equals("soul_double")) {
	        	baseAct.setType("notice");
	        	rewards.clear();
	        } else if (rewards.size() == 0 && !baseAct.getType().equals("notice")) {
	        	continue;
	        }

	        baseAct.setReward(mapper.writeValueAsString(rewards));
	        rep.add(baseAct);
		}

	  	Result result = new Result();
	  	result.setValue("activity", rep);
	  	result.setValue("touzi", touzi);
	  	return this.success(result.toMap());
	}

	public Object getReward(int actId, int key) throws Exception {

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

	public void task() throws Exception {

		List<Server> list = this.serverDao.findAll();
		for (Server server : list) {
			int serverId = server.getId();

			List<BaseActivity> activities = this.baseActivityDao.findAll(serverId);
			for (BaseActivity baseAct : activities) {
				if (baseAct.getType().equals("lv_rank")) {
					String endTime = new SimpleDateFormat("yyyyMMdd").format(new Date(baseAct.getEndTime().getTime()));
					if (Utils.date().equals(endTime)) {
						ObjectMapper objectMapper = new ObjectMapper();
						HashMap<Integer, Reward> rewards = objectMapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});

						List<Role> levelList = this.roleDao.expTop20(serverId);
						for (int i = 0 ; i < levelList.size() ; i++) {
							int r = i + 1;
							Reward reward = rewards.get(r);
							if (reward != null) {

								Mail mail = new Mail();
								mail.setAttachment(objectMapper.writeValueAsString(reward));
								mail.setFromName("GM");
								mail.setRoleId(levelList.get(i).getRoleId());
								mail.setTitle("【冲级活动】奖励!");
								mail.setContent("恭喜主公战队等级急速前进，并且在【冲级活动】中获得好的名次：" + r + "名：\n"+
"特地授予你以下奖励。\n\n"+
"客服：萌小乔");
					            this.mailDao.create(mail);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 投资计划
	 * @return
	 * @throws Exception
	 */
	public Object touzi() throws Exception {

		int roleId = this.roleId();
		Role role = this.roleDao.findOne(roleId);

		int gold = 1000;

		if (role.vip < 2) {
			return this.returnError(this.lineNum(), "VIP2以上才能购买投资计划");
		}
		if (role.gold < gold) {
			return this.returnError(2, ErrorResult.NotEnoughGold);
		}

		Result result = new Result();
		this.roleDao.subGold(role, 1000, "", FinanceLog.STATUS_TOUZIJIHUA, result);

		int serverId = this.serverId();
		List<BaseActivity> list = this.baseActivityDao.findAll(serverId);

        ObjectMapper mapper = new ObjectMapper();

        List<BaseActivity> rep = new ArrayList<BaseActivity>();

		for (BaseActivity baseAct : list) {

			if (!baseAct.getType().equals("touzi")) {
				continue;
			}

			HashMap<Integer, Reward> rewards = mapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});
	        for (Iterator<Map.Entry<Integer, Reward>> iter = rewards.entrySet().iterator(); iter.hasNext();) {
	            Map.Entry<Integer, Reward> entry = iter.next();
	            Reward reward = entry.getValue();
	            reward.setHas(false);

            	if (role.level() >= entry.getKey()) {
            		reward.setHas(true);
            	}
	        }

	        baseAct.setReward(mapper.writeValueAsString(rewards));
	        rep.add(baseAct);
		}

	  	result.setValue("activity", rep);
		result.setValue("touzi", true);
	
		return this.success(result);
	}
}
