package com.zhanglong.sg.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhanglong.sg.service.BossService;
import com.zhanglong.sg.utils.SpringContextUtils;

public class Boss extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub

		BossService bossService = (BossService) SpringContextUtils.getBean(BossService.class);
		try {
			bossService.updateRank();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
