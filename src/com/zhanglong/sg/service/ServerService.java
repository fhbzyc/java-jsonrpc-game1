package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.controller.VersionController;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.model.Token;

@Service
public class ServerService extends BaseService {

	@Resource
	private ServerDao serverDao;

	public Object list(String imei) throws Throwable {

		boolean find = VersionController.inWriteList(imei);

		List<Server> list = this.serverDao.findAll();
		List<Server> servers = new ArrayList<Server>();
		for (Server server : list) {
			if (!find && !server.getEnable()) {
				continue;
			}
			servers.add(server);
		}

		return this.success(servers);
	}

	public Object conn(String tokenS) throws Throwable {

		Token token = this.tokenDao.findOne(tokenS);
		if (token == null) {
			this.returnError(-1, "TOKEN ERROE");
		}

		this.getHandler().userId = token.getUserId();
		this.getHandler().serverId = token.getServerId();
		this.getHandler().roleId = token.getRoleId();
		return this.success(true);
	}
}
