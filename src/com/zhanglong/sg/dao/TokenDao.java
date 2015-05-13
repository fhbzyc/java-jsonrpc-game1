package com.zhanglong.sg.dao;

import java.io.IOException;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.model.Token;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class TokenDao {

	private static String TOKEN_KEY = "TOKEN_";
	private static String USER_ID_KEY = "TOKEN_UID_";

	@Resource
    private JedisConnectionFactory jedisConnectionFactory;

    public Token findOne(String tokenS) throws JsonParseException, JsonMappingException, IOException {

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String json = jedisConnection.getNativeConnection().get(TOKEN_KEY + tokenS);
    	jedisConnection.close();
    	if (json == null) {
    		return null;
    	}

    	ObjectMapper objectMapper = new ObjectMapper();
    	Token token = objectMapper.readValue(json, Token.class);
    	return token;
    }

    public String getTokenByUserId(int userId) {
    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String tokenS = jedisConnection.getNativeConnection().get(USER_ID_KEY + userId);
    	jedisConnection.close();
    	return tokenS;
    }

    public Token create(int userId) throws JsonProcessingException {

    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	String tokenS = jedisConnection.getNativeConnection().get(USER_ID_KEY + userId);

    	if (tokenS != null) {
    		jedisConnection.getNativeConnection().del(TOKEN_KEY + tokenS);
    	}

    	tokenS = UUID.randomUUID().toString().replace("-", "");

    	Token token = new Token();
    	token.setUserId(userId);
    	token.setTokenS(tokenS);

    	ObjectMapper objectMapper = new ObjectMapper();
    	jedisConnection.getNativeConnection().set(TOKEN_KEY + token.getTokenS(), objectMapper.writeValueAsString(token));

    	jedisConnection.getNativeConnection().set(USER_ID_KEY + userId, tokenS);
    	jedisConnection.close();
    	
    	return token;
    }

    public void setToken(Token token) throws JsonProcessingException {
    	ObjectMapper objectMapper = new ObjectMapper();
    	JedisConnection jedisConnection = this.jedisConnectionFactory.getConnection();
    	jedisConnection.getNativeConnection().set(TOKEN_KEY + token.getTokenS(), objectMapper.writeValueAsString(token));
    	jedisConnection.close();
    }
}
