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
import com.zhanglong.sg.model.CrusadeModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class CrusadeDao {

	@Resource
    private JedisConnectionFactory jedisConnectionFactory;

	public CrusadeModel findOne(int roleId, int level, int power) throws JsonParseException, JsonMappingException, IOException {

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String json = jedisConnection.getNativeConnection().get("CRUSADE_" + roleId);
    	jedisConnection.close();

		CrusadeModel crusadeModel = null;

    	if (json != null) {
    		ObjectMapper objectMapper = new ObjectMapper();
    		crusadeModel = objectMapper.readValue(json, CrusadeModel.class);
    	}

		int date = Integer.valueOf(Utils.date());

		if (crusadeModel != null) {


			if (date != crusadeModel.getDate()) {
				crusadeModel.setNum(0);
				crusadeModel.setDate(date);
			}

			return crusadeModel;
		}

		crusadeModel = new CrusadeModel();
		crusadeModel.setRoleId(roleId);
		crusadeModel.setDate(date);
		crusadeModel.setNum(0);
		crusadeModel.newPlayers(level, power);
		this.save(roleId, crusadeModel);
		return crusadeModel;
	}

	public void save(int roleId, CrusadeModel crusadeModel) throws JsonProcessingException {

    	ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(crusadeModel);

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	jedisConnection.getNativeConnection().set("CRUSADE_" + roleId, json);
    	jedisConnection.close();
	}
}

