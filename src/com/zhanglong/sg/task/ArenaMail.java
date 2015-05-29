package com.zhanglong.sg.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;

import com.zhanglong.sg.service.ActivityService;
import com.zhanglong.sg.service.ArenaService;

public class ArenaMail extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		ArenaService arenaService = ContextLoader.getCurrentWebApplicationContext().getBean(ArenaService.class);
		arenaService.sendMail();
		

		ActivityService activityService = ContextLoader.getCurrentWebApplicationContext().getBean(ActivityService.class);
		try {
			activityService.task();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
