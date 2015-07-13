package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import websocket.handler.EchoHandler;

import com.zhanglong.sg.dao.WhiteListDao;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.model.Token;

@Service
public class ServerService extends BaseService {

    @Resource
    private WhiteListDao imeiDao;

    @Resource
    private ServerDao serverDao;

    public Object list(String imei) throws Exception {

        boolean find = this.imeiDao.find(imei);

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

    public Object conn(String tokenS) throws Exception {

        Token token = this.tokenDao.findOne(tokenS);
        if (token == null) {
        	return this.returnError(-1, "Session disconnect");
        }

        this.getHandler().userId = token.getUserId();
        this.getHandler().serverId = token.getServerId();
        this.getHandler().roleId = token.getRoleId();

        if (token.getRoleId() > 0) {
            EchoHandler.close(this.getHandler().getSession(), token.getRoleId(), false);
        }

        return this.success(true);
    }
}
