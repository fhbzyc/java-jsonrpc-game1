package com.zhanglong.sg.dao;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.model.Token;

@Repository
public class TokenDao {

	private static String TokenKey = "TOKEN_";
	private static String UidKey = "TOKEN_UID_";

	@Autowired
    private RedisTemplate<String, Token> redisTemplate;

	public RedisTemplate<String, Token> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Token> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

    public Token findOne(String tokenS) {
    	return (Token) this.getRedisTemplate().opsForHash().get(TokenKey, tokenS);
    }

    public Token create(int userId) {
    	
    	Token token = (Token) this.getRedisTemplate().opsForHash().get(UidKey, userId);
    	if (token != null) {
    		this.getRedisTemplate().opsForHash().delete(TokenKey, token.getTokenS());
    	}

    	String tokenS = UUID.randomUUID().toString().replace("-", "");
  
    	token = new Token();
    	token.setUserId(userId);
    	token.setTokenS(tokenS);

    	this.getRedisTemplate().opsForHash().put(TokenKey, tokenS, token);

    	this.getRedisTemplate().opsForHash().put(UidKey, userId, token);
    	
    	return token;
    }
}
