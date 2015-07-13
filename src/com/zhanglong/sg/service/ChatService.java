package com.zhanglong.sg.service;

import org.springframework.stereotype.Service;

import websocket.handler.Broadcast;
import websocket.handler.EchoHandler;

import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.Chat;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;

@Service
public class ChatService extends BaseService {

    public Object pub(String msg) throws Exception {

        int roleId = roleId();

        Role role = this.roleDao.findOne(roleId);
        if (role.disableMsg) {
            return returnError(this.lineNum(), "你已被禁言，请与客服联系");
        }

        Result result = new Result();

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
        if (dateNumModel.chatNum() <= 0) {
            int gold = 5;
            if (role.gold < gold) {
                return this.returnError(2, ErrorResult.NotEnoughGold);
            } else {
                this.roleDao.subGold(role, gold, "公共聊天", FinanceLog.STATUS_CHAT, result);
            }
        }

        dateNumModel.chatNum += 1;
        this.dateNumDao.save(roleId, dateNumModel);

        result.setValue("chat_num", dateNumModel.chatNum());

        for (String string : RoleService.sensitives) {
            msg = msg.replace(string, "**");
        }

        Result message = new Result();

        Chat chat = new Chat();
        chat.avatar = role.avatar;
        chat.level = role.level();
        chat.roleId = roleId;
        chat.name = role.name;
        chat.msg = msg;
        chat.time = System.currentTimeMillis() / 1000l;

        result.setValue("chat", chat);

        message.setValue("chat", chat);
        msg = Response.marshalSuccess(0, message.toMap());

        Broadcast broadcast = new Broadcast();
        broadcast.send(roleId, this.serverId(), msg);

        return this.success(result.toMap());
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

        chat.name = this.roleDao.findOne(playerId).getName();
        result.setValue("chat", chat);
        
        return this.success(result);
    }
}
