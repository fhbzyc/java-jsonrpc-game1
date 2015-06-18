package com.zhanglong.sg.service;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.NoticeDao;
import com.zhanglong.sg.entity.Notice;

@Service
public class NoticeService extends BaseService {

    @Resource
    private NoticeDao noticeDao;

    public Object notice() throws IOException {
    	Notice notice = this.noticeDao.findOne();
    	if (notice != null) {
    		return this.success(this.noticeDao.findOne());
    	} else {
    		return this.success(false);
    	}
    }
}
