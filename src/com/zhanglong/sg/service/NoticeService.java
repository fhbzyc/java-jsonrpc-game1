package com.zhanglong.sg.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.NoticeDao;

@Service
public class NoticeService extends BaseService {

    @Resource
    private NoticeDao noticeDao;

    public Object notice() throws IOException {
    	return this.success(this.noticeDao.findOne());
    }
}
