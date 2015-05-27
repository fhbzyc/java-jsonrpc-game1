package com.zhanglong.sg.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;

import com.zhanglong.sg.service.PillageService;

public class AddPillage extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		PillageService pillageService = ContextLoader.getCurrentWebApplicationContext().getBean(PillageService.class);
		pillageService.addP();
	}
}
