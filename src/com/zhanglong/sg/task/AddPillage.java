package com.zhanglong.sg.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhanglong.sg.service.PillageService;
import com.zhanglong.sg.utils.SpringContextUtils;

public class AddPillage extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		PillageService pillageService = (PillageService) SpringContextUtils.getBean(PillageService.class);
		pillageService.addP();
	}
}
