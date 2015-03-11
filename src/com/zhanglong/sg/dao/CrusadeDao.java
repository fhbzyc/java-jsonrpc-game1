package com.zhanglong.sg.dao;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.model.CrusadeModel;
import com.zhanglong.sg.utils.Utils;

@Repository
public class CrusadeDao {

	@Resource
	private RedisTemplate<String, CrusadeModel> redisTemplate;

	public CrusadeModel findOne(int roleId, int level, int power) throws JsonParseException, JsonMappingException, IOException  {

		CrusadeModel crusadeModel = (CrusadeModel) this.redisTemplate.opsForHash().get("CRUSADE_", roleId);

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

	public void save(int roleId, CrusadeModel crusadeModel) {
		this.redisTemplate.opsForHash().put("CRUSADE_", roleId, crusadeModel);
	}
}

