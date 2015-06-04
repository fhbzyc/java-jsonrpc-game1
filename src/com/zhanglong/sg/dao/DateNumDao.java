package com.zhanglong.sg.dao;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class DateNumDao {

	private String RedisKey = "DATENUM_";
	
//	@Resource
//	private RedisTemplate<String, DateNumModel> redisTemplate;
	
	@Resource
    private JedisConnectionFactory jedisConnectionFactory;

	public DateNumModel findOne(int roleId) throws JsonParseException, JsonMappingException, IOException {

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String json = jedisConnection.getNativeConnection().get(RedisKey + roleId);
    	jedisConnection.close();

		DateNumModel dateNumModel = null;
		
    	if (json != null) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		dateNumModel = objectMapper.readValue(json, DateNumModel.class);
    	}

		int date = Integer.valueOf(Utils.date());

        if (dateNumModel == null || dateNumModel.getDate() != date) {

        	int dropItemNum = 0;
        	long dropItemTime = 0l;
        	
        	if (dateNumModel != null) {
            	dropItemNum = dateNumModel.dropItemNum;
            	dropItemTime = dateNumModel.dropItemTime;
        	}

        	dateNumModel = new DateNumModel();

        	dateNumModel.dropItemNum = dropItemNum;
        	dateNumModel.dropItemTime = dropItemTime;
        	dateNumModel.setDate(date);
        }

		return dateNumModel;
	}

    public void save(int roleId, DateNumModel dateNumModel) throws JsonProcessingException {

    	ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(dateNumModel);

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	jedisConnection.getNativeConnection().set(RedisKey + roleId, json);
    	jedisConnection.close();
    }
}
