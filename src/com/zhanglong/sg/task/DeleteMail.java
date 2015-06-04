package com.zhanglong.sg.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zhanglong.sg.service.MailService;
import com.zhanglong.sg.utils.SpringContextUtils;

public class DeleteMail extends QuartzJobBean {

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		((MailService)SpringContextUtils.getBean(MailService.class)).del();
	}
}
