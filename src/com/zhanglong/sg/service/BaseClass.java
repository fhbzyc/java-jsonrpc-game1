package com.zhanglong.sg.service;

import javax.annotation.Resource;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;

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
import com.zhanglong.sg.result.ErrorResult;

@Service
public class BaseClass {

//	@Resource
	protected HttpRequest httpRequest;

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

    protected int serverId() throws Throwable {
    	return 1;
    }

    protected int roleId() throws Throwable {

    	return 1;
    	
//    	String tokenS = this.httpRequest.getHeaders().getFirst("Token");
//    	if (tokenS == null) {
//    		throw new Throwable("token验证失败");
//    	}
//
//    	Token token = this.tokenDao.findOne(tokenS);
//    	if (token == null) {
//    		throw new Throwable("token验证失败");
//    	}
//
//    	return token.getRoleId();
    }

    protected int lineNum() {
		return Thread.currentThread().getStackTrace()[2].getLineNumber();
	}

    protected Object returnError(int lineNum, String msg) {
    	
    	com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
    	error.setCode(lineNum);
    	error.setMessage(msg);
    	return new ErrorResult(error);
	}

//    protected int roleId = 1;

}
