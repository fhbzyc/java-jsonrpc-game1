package com.zhanglong.sg.task;

import java.sql.Timestamp;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhanglong.sg.entity.OnlineLog;
import com.zhanglong.sg.utils.SpringContextUtils;

import websocket.handler.EchoHandler;

public class Online extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {

		SessionFactory sessionFactory = (SessionFactory) SpringContextUtils.getBean("sessionFactory");
		Session session = sessionFactory.openSession();
		
		OnlineLog online = new OnlineLog();
		online.setNum(EchoHandler.size());
		online.setTime(new Timestamp(System.currentTimeMillis()));

		session.save(online);
		session.close();
	}
}
