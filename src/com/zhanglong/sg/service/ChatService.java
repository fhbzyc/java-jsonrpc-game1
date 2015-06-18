package com.zhanglong.sg.service;

import org.springframework.stereotype.Service;

import websocket.handler.Broadcast;
import websocket.handler.EchoHandler;

import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Chat;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;

@Service
public class ChatService extends BaseService {

	public void pub(String msg) throws Exception {

        for (String string : RoleService.sensitives) {
        	msg = msg.replace(string, "**");
        }

		int roleId = roleId();

		Role role = this.roleDao.findOne(roleId);

		Chat chat = new Chat();
		chat.avatar = role.avatar;
		chat.level = role.level();
		chat.roleId = roleId;
		chat.name = role.name;
		chat.msg = msg;
		chat.time = System.currentTimeMillis() / 1000l;

		Result result = new Result();
		result.setValue("chat", chat);
		msg = Response.marshalSuccess(0, result.toMap());

		Broadcast broadcast = new Broadcast();
		broadcast.send(this.serverId(), msg);
	}

	public Object pri(int playerId, String msg) throws Exception {

		int roleId = roleId();

		Role role = this.roleDao.findOne(roleId);

		Chat chat = new Chat();
		chat.avatar = role.avatar;
		chat.level = role.level();
		chat.roleId = roleId;
		chat.name = role.name;
		chat.msg = msg;
		chat.type = Chat.PRIVATECHAT;
		chat.time = System.currentTimeMillis() / 1000l;

		Result result = new Result();
		result.setValue("chat", chat);
		msg = Response.marshalSuccess(0, result.toMap());

		EchoHandler.pri(playerId, msg);

		return this.success(result);
	}
}
