package com.zhanglong.sg.service;

import com.googlecode.jsonrpc4j.JsonRpcService;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/message")
public class MessageService extends BaseService {

	public Object msg() throws Throwable {

	  	Result result = new Result();
	  	result.setValue("msgs", new int[]{});
	  	return this.success(result.toMap());
	}
}
