package com.zhanglong.sg.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class DateNumDao {

	private String RedisKey = "DATENUM_";
	
	@Resource
	private RedisTemplate<String, DateNumModel> redisTemplate;
	
	public DateNumModel findOne(int roleId) {

		DateNumModel dateNumModel = (DateNumModel) this.redisTemplate.opsForHash().get(RedisKey, roleId);

		int date = Integer.valueOf(Utils.date());

        if (dateNumModel == null || dateNumModel.getDate() != date) {

        	dateNumModel = new DateNumModel();
        	dateNumModel.setDate(date);
        }

		return dateNumModel;
	}

    public void save(int roleId, DateNumModel dateNumModel) {
    	this.redisTemplate.opsForHash().put(RedisKey, roleId, dateNumModel);
    }
}
