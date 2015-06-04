package com.zhanglong.sg.service;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.result.Result;

@Service
public class MessageService extends BaseService {

	public Object msg() throws Throwable {

	  	Result result = new Result();
	  	result.setValue("msgs", new int[]{});
	  	return this.success(result.toMap());
	}
}
