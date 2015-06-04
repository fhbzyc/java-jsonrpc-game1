package com.zhanglong.sg.dao;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity2.BaseDailyTask;
import com.zhanglong.sg.model.DailyTaskModel;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Repository
public class DailyTaskDao extends BaseDao {

	private static String RedisKey = "DAILY_TASK_";

	@Resource
	private BaseDailyTaskDao baseDailyTaskDao;
	
//	@Resource
//	private RedisTemplate<String, DailyTaskModel> redisTemplate;
	
	@Resource
    private JedisConnectionFactory jedisConnectionFactory;

	public DailyTaskModel getDailyTaskModel(Role role) throws JsonParseException, JsonMappingException, IOException {

    	int todayDate = Integer.valueOf(Utils.date());

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String json = jedisConnection.getNativeConnection().get(RedisKey + role.getRoleId());
    	jedisConnection.close();

    	DailyTaskModel dailyTaskModel = null;

    	if (json != null) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		dailyTaskModel = objectMapper.readValue(json, DailyTaskModel.class);
    	}
    	
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

	public List<BaseDailyTask> taskList(Role role) throws JsonParseException, JsonMappingException, IOException {

    	DailyTaskModel dailyTaskModel = this.getDailyTaskModel(role);

        return dailyTaskModel.taskList(role.level());
    }

	/**
	 * 副本
	 * @param num
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addCopy(Role role, int num, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "copy", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 精英副本
	 * @param num
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addHeroCopy(Role role, int num, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = addNum(role, "hero_copy", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 升级技能
	 * @param num
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addSkill(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "skill", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 抽奖次数
	 * @param num
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addBar(Role role, int num, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "bar", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 点金手
	 * @param num
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addCoin(Role role, int num, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "coin", num);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	public void pk(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "pk", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * VIP月卡
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addMoonCard(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "gold", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 剿匪令
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addVip(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "card", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 三国学院
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addSpecialCopy(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "special_copy", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 讨伐天下
	 * @param role
	 * @param result
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addCrusade(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "fight_copy", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 乱斗堂
	 * @param role
	 * @param result
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void addLdt(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "fight", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 锻造
	 * @param role
	 * @param result
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void addEquipExp(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "equip_up", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 掠夺
	 * @param role
	 * @param result
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void addPillage(Role role, Result result) throws JsonParseException, JsonMappingException, IOException {
		BaseDailyTask dailyTask = this.addNum(role, "pillage", 1);
		if (dailyTask != null) {
			result.addDailyTask(dailyTask);
		}
	}

	/**
	 * 完成任务
	 * @param taskId
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public void complete(Role role, int taskId) throws JsonParseException, JsonMappingException, IOException {

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
	private BaseDailyTask addNum(Role role, String type, int num) throws JsonParseException, JsonMappingException, IOException {

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



	private void save(int roleId, DailyTaskModel dailyTaskModel) throws JsonProcessingException {
		
    	ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(dailyTaskModel);

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	jedisConnection.getNativeConnection().set(RedisKey + roleId, json);
    	jedisConnection.close();
	}
}
