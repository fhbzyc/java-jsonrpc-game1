package com.zhanglong.sg.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.NoticeDao;

@Service
@JsonRpcService("/notice")
public class NoticeService extends BaseService {

    @Resource
    private NoticeDao noticeDao;

    public Object notice() throws IOException {
    	return this.success(this.noticeDao.findOne());
    }
}
