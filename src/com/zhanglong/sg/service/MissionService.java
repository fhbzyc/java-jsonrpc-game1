package com.zhanglong.sg.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.BaseMissionDao;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Mission;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseDailyTask;
import com.zhanglong.sg.entity2.BaseMission;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
public class MissionService extends BaseService {

	@Resource
	private BaseMissionDao baseMissionDao;

    /**
     * 任务列表
     * @return
     * @throws Exception
     */
    public Object taskList() throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        List<Mission> list = this.missionDao.findAll(role);

        Result result = new Result();
        for (Mission mission : list) {

			if (!mission.getComplete()) {

				BaseMission baseMission = this.baseMissionDao.findOne(mission.getMissionId());
				baseMission.setNum(mission.getNum());

				result.addMission(baseMission);
			}
		}

        this.missionDao.newMission(role, result);

        HashMap<String, Object> r = result.toMap();
        if (r.get("task") == null) {
        	r.put("task", new Object[]{});
        }

        return this.success(r);
    }

    /**
     * 领取奖励
     * @param taskId
     * @return
     * @throws Exception
     */
    public Object completeTask(int missionId) throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        List<Mission> list = this.missionDao.findAll(role);

        Result result = new Result();

        boolean find = false;
        BaseMission task = new BaseMission();
        for (Mission mission : list) {
            if (mission.getMissionId() == missionId) {

            	BaseMission baseMission = this.baseMissionDao.findOne(mission.getMissionId());

                if (mission.getNum() < baseMission.getGoal()) {
                	return this.returnError(this.lineNum(), "数量不足");
                	//return this.success(result.toMap());
                }

                if (mission.getComplete()) {
                	return this.success(new Result().toMap());
                }

                task = baseMission;
                find = true;
            }
        }

        if (!find) {
        	return this.returnError(this.lineNum(), "不存在");
        	//return this.success(result.toMap());
        }

        this.missionDao.complete(role, missionId, result);
        this.missionDao.newMission(role, result);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Reward reward = mapper.readValue(task.getReward(), Reward.class);
            this.rewardDao.get(role, reward, "任务领取<" + task.getName() + ">", FinanceLog.STATUS_TASK_GET, result);
        } catch (Exception e) {
            return this.returnError(this.lineNum(), "JSON解析出错");
        }

        return this.success(result.toMap());
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object dailyTaskList() throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        Result result = new Result();

        List<BaseDailyTask> list = this.dailyTaskDao.taskList(role);
        for (BaseDailyTask dailyTask : list) {

        	if (!dailyTask.getComplete()) {
        		result.addDailyTask(dailyTask);
        	}
		}

        HashMap<String, Object> r = result.toMap();
        if (r.get("daily_task") == null) {
        	r.put("daily_task", new int[]{});
        }

        return this.success(r);
    }

    /**
     * 领取日常任务
     * @param taskId
     * @return
     * @throws Exception
     */
	public Object completeDailyTask(int taskId) throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        List<BaseDailyTask> taskList = dailyTaskDao.taskList(role);

        boolean find = false;
        BaseDailyTask task = null;
        for (BaseDailyTask dailyTask : taskList) {
            if (dailyTask.getId() == taskId) {

            	if (dailyTask.getComplete()) {
            		return this.success(new Result().toMap());
            	}

                String type = dailyTask.getType();

                if (dailyTask.getNum() < dailyTask.getGoal() && !type.equals("noon") && !type.equals("night") && !type.equals("midnight")) {
                    return this.returnError(this.lineNum(), "目标数量还未达成");
                }

                task = dailyTask;
                find = true;
            }
        }

        if (!find) {
            return this.returnError(this.lineNum(), "参数出错,没有领这个任务");
        }

        Result result = new Result();

        if (task.getType().equals("noon") || task.getType().equals("night") || task.getType().equals("midnight")) {

            Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());

            long begintime = 0;
            long endtime = 0;
            if (task.getType().equals("noon")) {
                begintime = date.getTime() + 12 * 3600 * 1000;
                endtime = date.getTime() + 14 * 3600 * 1000;
            } else if (task.getType().equals("night")) {
                begintime = date.getTime() + 18 * 3600 * 1000;
                endtime = date.getTime() + 20 * 3600 * 1000;
            } else if (task.getType().equals("midnight")) {
                begintime = date.getTime() + 21 * 3600 * 1000;
                endtime = date.getTime() + 24 * 3600 * 1000;
            }

            if (System.currentTimeMillis() < begintime) {
                return this.returnError(this.lineNum(), "时间未到, 不能领取体力");
            } else if (System.currentTimeMillis() > endtime) {
                return this.returnError(this.lineNum(), "时间已过, 不能领取体力");
            }

        }

        this.dailyTaskDao.complete(role, taskId);

        ObjectMapper mapper = new ObjectMapper();
        Reward reward = mapper.readValue(task.getReward(), Reward.class);
        this.rewardDao.get(role, reward, "任务领取<" + task.getName() + ">", FinanceLog.STATUS_TASK_GET, result);

        return this.success(result.toMap());
    }

    /**
     * 点金手
     * @param times
     * @return
     * @throws Exception
     */
    public Object getCoin(int times) throws Exception {

    	int roleId = this.roleId();

        if (times != 1 && times != 3) {
            return this.returnError(this.lineNum(), "Illegal operation !");
        }

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
        
        if (dateNumModel.getBuyCoinNum() + times > role.maxGetCoinTimes()) {

            return this.returnError(2, "提升VIP等级，可增加每日点金次数，前去充值？");
        }

        int gold = 0;
        int coin = 0;

        int[][] random_coin = new int[times][];
 
        for(int i = 0 ; i < times ; i++) {

            int g = getCoinNeedGold(dateNumModel.getBuyCoinNum() + i);
            gold += g;

            int c = getMinCoin(role.level(), dateNumModel.getBuyCoinNum());

            int coinTimes = 1;
            Random random = new Random();
            int r = random.nextInt(100);
            if (r < 30) {
                r = random.nextInt(100);
                if (r < 35) {
                    coinTimes = 2;    
                } else if (r < 55) {
                    coinTimes = 3;
                } else if (r < 75) {
                    coinTimes = 4;
                } else if (r < 85) {
                    coinTimes = 6;
                } else if (r < 95) {
                    coinTimes = 8;
                } else {
                    coinTimes = 10;
                }
            }

            c *= coinTimes;
            coin += c;
            random_coin[i] = new int[]{c, coinTimes, g};
        }

        if (role.getGold() < gold) {
        	return this.returnError(2, "元宝不足");
        } else {

        	this.roleDao.addCoin(role, coin, "点金手", FinanceLog.STATUS_GOLD_BUY_COIN, result);
        	this.roleDao.subGold(role, gold, "使用点金手花费<" + gold + ">元宝", FinanceLog.STATUS_GOLD_BUY_COIN, result);
        	// this.roleDao.update(role, result);
        	
        	dateNumModel.setBuyCoinNum(dateNumModel.getBuyCoinNum() + times);
        	this.dateNumDao.save(roleId, dateNumModel);
        	
        }

        this.dailyTaskDao.addCoin(role, times, result);

        result.setValue("get_coin", new int[]{dateNumModel.getBuyCoinNum(), role.maxGetCoinTimes(), getCoinNeedGold(dateNumModel.getBuyCoinNum()), getMinCoin(role.level(), dateNumModel.getBuyCoinNum())});
        result.setValue("random_coin", random_coin);
        return this.success(result.toMap());
    }

    public static int getCoinNeedGold(int times) {
	  	int n = times / 2;
	  	int gold = (int)(10 * Math.pow(2, n));
	  	if (gold > 640) {
	  		gold = 640;
	  	}
  		return gold;
	}
    
    public static int getMinCoin(int level, int times) {
    	return 20000 + level * 100 + times * 1500;
	}
}
