package com.zhanglong.sg.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity.BaseDailyTask;

public class DailyTaskModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 940013853354145995L;

	private List<BaseDailyTask> taskList;

	private int date;

	public List<BaseDailyTask> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<BaseDailyTask> taskList) {
		this.taskList = taskList;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public List<BaseDailyTask> taskList(int level) {

        for (BaseDailyTask dailyTask : this.taskList) {
        	if (dailyTask.getType().equals("daily_card")) {

        		try {
        			
        			Reward reward = new Reward();
        			reward.setItem_id(new int[]{4208});
        			reward.setItem_num(new int[]{this.vipWipeOutItemNum(level)});
        	        HashMap<String, Object> map = new HashMap<String, Object>();
        	        map.put("reward", reward);

        	        ObjectMapper mapper = new ObjectMapper();
        	        dailyTask.setReward(mapper.writeValueAsString(reward));

				} catch (Exception e) {

				}
        	}
		}

        return this.taskList;
	}

    private int vipWipeOutItemNum(int vip) {

    	if (vip < 1) {
    		vip = 1;
    	}
    	return 20 + (vip - 1) * 10;
    }
}
