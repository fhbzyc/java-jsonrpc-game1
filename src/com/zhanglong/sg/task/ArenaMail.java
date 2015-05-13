package com.zhanglong.sg.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.entity.RankLog;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.model.ArenaPlayerModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.service.ArenaService;
import com.zhanglong.sg.utils.Utils;

public class ArenaMail extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		ArenaService arenaService = ContextLoader.getCurrentWebApplicationContext().getBean(ArenaService.class);
		arenaService.sendMail();
	}
}

