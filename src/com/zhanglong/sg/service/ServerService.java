package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.entity.Server;

@JsonRpcService("/server")
public class ServerService extends BaseClass {

	@Resource
	private ServerDao serverDao;
	
	public Object list(String imei) throws Throwable {

		boolean find = VersionService.inWriteList(imei);

		int param = 0;
		if (find) {
			param = -1;
		}

		ServerDao serverDao = new ServerDao();
		List<Server> list = serverDao.findAll();
		List<Server> servers = new ArrayList<Server>();
		for (Server server : list) {
			if (server.getState() > param) {
				servers.add(server);
			}
		}

		return servers;
	}
}
