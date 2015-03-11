package com.zhanglong.sg.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.entity.BaseDailyTask;
import com.zhanglong.sg.entity.BaseMission;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
@JsonRpcService("/mission")
public class MissionService extends BaseClass {

    /**
     * 任务列表
     * @param tokenS
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> taskList() throws Throwable {

    	int roleId = this.roleId();

        Role role = roleDao.findOne(roleId);

        List<BaseMission> list = missionDao.findAll(role);

        Result result = new Result();
        for (BaseMission mission : list) {
			if (!mission.getComplete()) {
				result.addMission(mission);
			}
		}

        HashMap<String, Object> r = result.toMap();
        if (r.get("task") == null) {
        	r.put("task", new int[]{});
        }
        return r;
    }

    /**
     * 领取奖励
     * @param taskId
     * @return
     * @throws Throwable
     */
    public Object completeTask(int missionId) throws Throwable {

    	int roleId = this.roleId();

        Role role = roleDao.findOne(roleId);

        List<BaseMission> list = missionDao.findAll(role);

        boolean find = false;
        BaseMission task = new BaseMission();
        for (BaseMission mission : list) {
            if (mission.getId() == missionId) {

                if (mission.getNum() < mission.getGoal()) {
                    throw new Throwable("目标数量还未达成");
                }

                if (mission.getComplete()) {
                    throw new Throwable("已领奖");
                }
                
                task = mission;
                find = true;
            }
        }

        if (!find) {
            throw new Throwable("参数出错,没有领这个任务");
        }

        Result result = new Result();

        missionDao.complete(role, missionId, result);
        missionDao.newMission(role, result);

        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> map = mapper.readValue(task.getReward(), new TypeReference<Map<String, Object>>(){});
            Reward reward = (Reward) map.get("reward");
            rewardDao.get(role, reward, "任务领取<" + task.getName() + ">", FinanceLog.STATUS_TASK_GET, result);
        } catch (Exception e) {
            throw new Throwable("JSON解析出错");
        }

        return result.toMap();
    }

    /**
     * 
     * @return
     * @throws Throwable
     */
    public Object dailyTaskList() throws Throwable {

    	int roleId = this.roleId();

        Role role = roleDao.findOne(roleId);

        Result result = new Result();

        List<BaseDailyTask> list = dailyTaskDao.taskList(role);
        for (BaseDailyTask dailyTask : list) {

        	if (!dailyTask.getComplete()) {
        		result.addDailyTask(dailyTask);
        	}
		}

        HashMap<String, Object> r = result.toMap();
        if (r.get("daily_task") == null) {
        	r.put("daily_task", new int[]{});
        }

        return r;
    }

    /**
     * 领取日常任务
     * @param tokenS
     * @param taskId
     * @return
     * @throws Throwable
     */
	public HashMap<String, Object> completeDailyTask(int taskId) throws Throwable {

    	int roleId = this.roleId();

        Role role = roleDao.findOne(roleId);

        List<BaseDailyTask> taskList = dailyTaskDao.taskList(role);

        boolean find = false;
        BaseDailyTask task = null;
        for (BaseDailyTask dailyTask : taskList) {
            if (dailyTask.getId() == taskId) {

            	if (dailyTask.getComplete()) {
            		throw new Throwable("此任务已领奖");
            	}

                String type = dailyTask.getType();

                if (dailyTask.getNum() < dailyTask.getGoal() && !type.equals("daily_noon") && !type.equals("daily_night") && !type.equals("daily_midnight")) {
                    throw new Throwable("目标数量还未达成");
                }

                task = dailyTask;
                find = true;
            }
        }

        if (!find) {
            throw new Throwable("参数出错,没有领这个任务");
        }

        Result result = new Result();

        if (task.getType().equals("daily_noon") || task.getType().equals("daily_night") || task.getType().equals("daily_midnight")) {

            Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());

            long begintime = 0;
            long endtime = 0;
            if (task.getType().equals("daily_noon")) {
                begintime = date.getTime() + 12 * 3600 * 1000;
                endtime = date.getTime() + 14 * 3600 * 1000;
            } else if (task.getType().equals("daily_night")) {
                begintime = date.getTime() + 18 * 3600 * 1000;
                endtime = date.getTime() + 20 * 3600 * 1000;
            } else if (task.getType().equals("daily_midnight")) {
                begintime = date.getTime() + 21 * 3600 * 1000;
                endtime = date.getTime() + 24 * 3600 * 1000;
            }

            if (System.currentTimeMillis() < begintime) {
                throw new Throwable("时间未到, 不能领取体力");
            } else if (System.currentTimeMillis() > endtime) {
                throw new Throwable("时间已过, 不能领取体力");
            }

            dailyTaskDao.complete(role, taskId);

            role.setPhysicalStrength(role.getPhysicalStrength() + 60);
            roleDao.update(role, result);

            return result.toMap();
        }

        dailyTaskDao.complete(role, taskId);

        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> map = mapper.readValue(task.getReward(), new TypeReference<Map<String, Object>>(){});
        Reward reward = (Reward) map.get("reward");
        rewardDao.get(role, reward, "任务领取<" + task.getName() + ">", FinanceLog.STATUS_TASK_GET, result);

        return result.toMap();
    }

    /**
     * 点金手
     * @param times
     * @return
     * @throws Throwable
     */
    public Object getCoin(int times) throws Throwable {

    	int roleId = this.roleId();

        if (times != 1 && times != 3) {
            throw new Throwable("Illegal operation !");
        }

        Result result = new Result();

        Role role = roleDao.findOne(roleId);

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
        
        if (dateNumModel.getBuyCoinNum() + times > role.maxGetCoinTimes()) {
            com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
            error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
            error.setMessage("提升VIP等级，可增加每日点金次数，前去充值？");
            return new ErrorResult(error);
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
        	return ErrorResult.NotEnoughGold;
        } else {

        	roleDao.addCoin(role, coin, "点金手", FinanceLog.STATUS_GOLD_BUY_COIN, result);
        	roleDao.subGold(role, gold, "使用点金手花费<" + gold + ">元宝", FinanceLog.STATUS_GOLD_BUY_COIN);
        	roleDao.update(role, result);
        	
        	dateNumModel.setBuyCoinNum(dateNumModel.getBuyCoinNum() + times);
        	this.dateNumDao.save(roleId, dateNumModel);
        	
        }

        dailyTaskDao.addCoin(role, times, result);

        HashMap<String, Object> map = result.toMap();
        result.setValue("get_coin", new int[]{dateNumModel.getBuyCoinNum(), role.maxGetCoinTimes(), getCoinNeedGold(dateNumModel.getBuyCoinNum()), getMinCoin(role.level(), dateNumModel.getBuyCoinNum())});
        result.setValue("random_coin", random_coin);
        return result.toMap();
    }

//    private int vipWipeOutItemNum(int vip) {
//
//    	if (vip < 1) {
//    		vip = 1;
//    	}
//    	return 20 + (vip - 1) * 10;
//    }
    
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
