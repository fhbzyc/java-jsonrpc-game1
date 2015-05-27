package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseDailyTask;
import com.zhanglong.sg.model.DailyTaskModel;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Repository
public class DailyTaskDao extends BaseDao {

	private static String RedisKey = "DAILY_TASK2_";

	@Resource
	private BaseDailyTaskDao baseDailyTaskDao;
	
	@Resource
	private RedisTemplate<String, DailyTaskModel> redisTemplate;

	public DailyTaskModel getDailyTaskModel(Role role) {

    	int todayDate = Integer.valueOf(Utils.date());

    	DailyTaskModel dailyTaskModel = (DailyTaskModel) this.redisTemplate.opsForHash().get(RedisKey, role.getRoleId());
    	if (dailyTaskModel == null || todayDate != dailyTaskModel.getDate()) {

    		List<BaseDailyTask> dailyTasks = this.baseDailyTaskDao.findAll();
    		for (BaseDailyTask baseDailyTask : dailyTasks) {
				if (baseDailyTask.getType().equals("card") && role.vip > 0) {
					baseDailyTask.setNum(1);
				}
				if (baseDailyTask.getType().equals("gold") && role.cardTime > (int)(System.currentTimeMillis() / 1000l)) {
					baseDailyTask.setNum(1);
				}
			}

    		dailyTaskModel = new DailyTaskModel();
    		dailyTaskModel.setTaskList(dailyTasks);
    		dailyTaskModel.setDate(Integer.valueOf(Utils.date()));
    		this.save(role.getRoleId(), dailyTaskModel);
    	}

    	dailyTaskModel.taskList(role.level());

    	return dailyTaskModel;
	}

	public List<BaseDailyTask> taskList(Role role) {

    	DailyTaskModel dailyTaskModel = this.getDailyTaskModel(role);

        return dailyTaskModel.taskList(role.level());
    }

	/**
	 * 副本
	 * @param num
	 * @return
	 */
	public void addCopy(Role role, int num, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "copy", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 精英副本
	 * @param num
	 * @return
	 */
	public void addHeroCopy(Role role, int num, Result result) {
		BaseDailyTask dailyTask = addNum(role, "hero_copy", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 升级技能
	 * @param num
	 * @return
	 */
	public void addSkill(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "skill", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 抽奖次数
	 * @param num
	 * @return
	 */
	public void addBar(Role role, int num, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "bar", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 点金手
	 * @param num
	 * @return
	 */
	public void addCoin(Role role, int num, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "coin", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	public void pk(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "pk", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * VIP月卡
	 * @return
	 */
	public void addMoonCard(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "gold", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 剿匪令
	 * @return
	 */
	public void addVip(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "card", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 三国学院
	 * @return
	 */
	public void addSpecialCopy(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "special_copy", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 讨伐天下
	 * @param role
	 * @param result
	 */
	public void addCrusade(Role role, Result result) {
		BaseDailyTask dailyTask = this.addNum(role, "crusade", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 完成任务
	 * @param taskId
	 * @return
	 */
	public void complete(Role role, int taskId) {

		DailyTaskModel dailyTaskModel = this.getDailyTaskModel(role);
		
		List<BaseDailyTask> list = dailyTaskModel.taskList(role.level());

		for (BaseDailyTask dailyTask : list) {
			if (dailyTask.getId() == taskId) {
				dailyTask.setComplete(true);
			}
		}

    	this.save(role.getRoleId(), dailyTaskModel);
	}

//	/**
//	 * 月卡到期时间
//	 * @return
//	 */
//	public long getVipTime() {
//		this.taskList();
//		return this.vipTime;
//	}
//
//	private void setTask(List<DailyTask> list) {
//
//		this.tList = list;
//		this.save();
//	}
//
	private BaseDailyTask addNum(Role role, String type, int num) {

		DailyTaskModel dailyTaskModel = this.getDailyTaskModel(role);
		List<BaseDailyTask> list = dailyTaskModel.taskList(role.level());

		for (BaseDailyTask dailyTask : list) {
			if (!dailyTask.getComplete() && dailyTask.getType().equals(type)) {

	        	dailyTask.setNum(dailyTask.getNum() + num);
	        	
	        	this.save(role.getRoleId(), dailyTaskModel);
	        	
	        	return dailyTask;
			}
		}

		return null;
	}

//	public List<DailyTask> findAll() {
//    	Session session = this.getSessionFactory().getCurrentSession();
//    	Criteria criteria  = session.createCriteria(DailyTask.class);
//    	return criteria.add(Restrictions.eq("available", DailyTask.STATUS_TASK_AVAILABLE)).list();
//	}



	private void save(int roleId, DailyTaskModel dailyTaskModel) {
		this.redisTemplate.opsForHash().put(RedisKey, roleId, dailyTaskModel);
	}
}
