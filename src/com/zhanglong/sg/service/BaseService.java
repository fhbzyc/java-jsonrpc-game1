package com.zhanglong.sg.service;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Service;

import websocket.handler.EchoHandler;
import websocket.handler.Handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.zhanglong.sg.dao.BaseHeroDao;
import com.zhanglong.sg.dao.BaseItemDao;
import com.zhanglong.sg.dao.DailyTaskDao;
import com.zhanglong.sg.dao.DateNumDao;
import com.zhanglong.sg.dao.HeroDao;
import com.zhanglong.sg.dao.ItemDao;
import com.zhanglong.sg.dao.MissionDao;
import com.zhanglong.sg.dao.RewardDao;
import com.zhanglong.sg.dao.RoleDao;
import com.zhanglong.sg.dao.TokenDao;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;

@Service
public class BaseService {

	@Resource
	protected RoleDao roleDao;

	@Resource
	protected ItemDao itemDao;

	@Resource
	protected HeroDao heroDao;

    @Resource
    protected BaseItemDao baseItemDao;

    @Resource
    protected BaseHeroDao baseHeroDao;

    @Resource
    protected RewardDao rewardDao;

    @Resource
    protected DailyTaskDao dailyTaskDao;

    @Resource
    protected MissionDao missionDao;

    @Resource
    protected TokenDao tokenDao;

    @Resource
    protected DateNumDao dateNumDao;

    public Handler getHandler() {
		return EchoHandler.connections.get();
	}

	protected int userId() {

    	return this.getHandler().userId;
    }

	protected int serverId() {

    	return this.getHandler().serverId;
    }

    protected int roleId() {
    	return this.getHandler().roleId;
    }

    protected int lineNum() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

    protected Object returnError(int lineNum, String msg) throws JsonParseException, JsonMappingException, IOException  {

    	String str = Response.marshalError(this.getHandler().requestId, lineNum, msg);
    	try {
			this.getHandler().session.sendMessage(new TextMessage(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
	}

    protected Object success(Object result) throws JsonParseException, JsonMappingException, IOException  {

    	if (result.getClass() == Result.class) {
    		Method mh = ReflectionUtils.findMethod(result.getClass(), "toMap", new Class[]{});
    		result = ReflectionUtils.invokeMethod(mh, result, new Object[]{});
    	}

    	String str = Response.marshalSuccess(this.getHandler().requestId, result);
    	try {
			this.getHandler().session.sendMessage(new TextMessage(str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	return null;
	}
}
