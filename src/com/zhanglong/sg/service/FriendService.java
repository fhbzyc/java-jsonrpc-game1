package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import websocket.handler.EchoHandler;

import com.zhanglong.sg.dao.FriendDao;
import com.zhanglong.sg.entity.Friend;
import com.zhanglong.sg.entity.FriendAp;
import com.zhanglong.sg.entity.FriendMsg;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.PlayerModel2;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;

@Service
public class FriendService extends BaseService {

    @Resource
    private FriendDao friendDao;

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object list() throws Exception {

    	int roleId = this.roleId();

        List<Friend> friends = this.friendDao.allFriends(roleId);

        List<Object> list = new ArrayList<Object>();

        List<FriendAp> friendAps = this.friendDao.mySendAp(roleId);

        for (Friend friend : friends) {
        	
        	int friendId = 0;

			if (friend.getRoleId() != roleId) {
				friendId = friend.getRoleId();
			} else if (friend.getRoleId2() != roleId) {
				friendId = friend.getRoleId2();
			}

			if (friendId != 0) {

	    		boolean send = false;
	    		for (FriendAp friendAp : friendAps) {
					if ((int)friendAp.getRoleId() == friendId) {
						send = true;
						break;
					}
				}

				Role role = this.roleDao.findOne(friendId);

				list.add(new Object[]{role.getRoleId() , role.name , role.avatar , role.level , send});
			}
		}

        return this.success(list);
    }

    /**
     * 推荐好友
     * @return
     * @throws Exception
     */
     public Object players() throws Exception {

    	int roleId = this.roleId();

    	Role role = this.roleDao.findOne(roleId);

    	List<Friend> friends = this.friendDao.allFriends(roleId);

    	List<Role> roles = this.roleDao.get100ByLevel(role.serverId, role.level);

    	List<Role> list = new ArrayList<Role>();
    	
    	for (Role role2 : roles) {
    		boolean find = false;
    		for (Friend friend : friends) {
    			if ((int)role2.getRoleId() == (int)friend.getRoleId() || (int)role2.getRoleId() == (int)friend.getRoleId2()) {
    				find = true;
    			}
			}
    		if (!find) {
    			list.add(role2);
    		}
		}

    	Collections.shuffle(list);
    	
    	if (list.size() > 10) {
    		list = list.subList(0, 10);
    	}

    	List<Object> list2 = new ArrayList<Object>();
    	
    	for (Role role2 : list) {
			PlayerModel2 player = new PlayerModel2();

			player.avatar = role2.avatar;
			player.level = role2.level;
			player.name = role2.name;
			player.roleId = role2.getRoleId();
			list2.add(player.toArray());
		}
    	return this.success(list2);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object msgList() throws Exception {

    	int roleId = this.roleId();

    	List<FriendMsg> friendMsgs = this.friendDao.allFriendMsg(roleId);

    	return this.success(friendMsgs);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object sendMsg(int playerId) throws Exception {

    	int roleId = this.roleId();

    	if (playerId == roleId) {
    		return this.success(true);
    	}

    	FriendMsg friendMsg = this.friendDao.findOneMsg(playerId, roleId);
    	if (friendMsg != null) {
    		return this.success(true);
    	}

    	Role role = this.roleDao.findOne(roleId);
    	friendMsg = this.friendDao.createFriendMsg(role, playerId);

    	Result result = new Result();
    	result.setValue("friend", friendMsg);

    	String msg = Response.marshalSuccess(0, result.toMap());
    	EchoHandler.pri(playerId, msg);

    	return this.success(true);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object addFriend(int playerId) throws Exception {

    	int roleId = this.roleId();

    	this.friendDao.deleteFriendMsg(playerId);

    	this.friendDao.createFriend(roleId, playerId);

		Role p = this.roleDao.findOne(playerId);

		PlayerModel2 player = new PlayerModel2();
		player.avatar = p.avatar;
		player.level = p.level;
		player.name = p.name;
		player.roleId = playerId;
    	
    	return this.success(new Object[]{player.toArray()});
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object delMsg(int friendId) throws Exception {

    	this.friendDao.deleteFriendMsg(friendId);
    	return this.success(true);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object delFriend(int friendId) throws Exception {

    	int roleId = this.roleId();

    	this.friendDao.deleteFriendMsg(friendId);

    	this.friendDao.deleteFriend(roleId, friendId);

    	return this.success(true);
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object sendAp(int friendId) throws Exception {

    	int roleId = this.roleId();
    	List<FriendAp> friendAps = this.friendDao.mySendAp(roleId);

    	for (FriendAp friendAp : friendAps) {
			if (friendAp.getRoleId() == friendId) {
				return this.success(true);
			}
		}

    	Role role = this.roleDao.findOne(roleId);
    	FriendAp friendAp = this.friendDao.createFriendAp(role, friendId);

    	Result result = new Result();
    	result.setValue("friendap", friendAp);

    	String msg = Response.marshalSuccess(0, result.toMap());
    	EchoHandler.pri(friendId, msg);
    	
    	return this.success(true);
    }

    public Object apList() throws Exception {

    	int roleId = this.roleId();
    	List<FriendAp> myAps = this.friendDao.allFriendAp(roleId);

    	return this.success(myAps);
    }

    public Object getAp(int id) throws Exception {

    	int roleId = this.roleId();
    	List<FriendAp> myAps = this.friendDao.allFriendAp(roleId);

    	for (FriendAp friendAp : myAps) {
			if (friendAp.getId() == id) {
				this.friendDao.deleteFriendAp(friendAp);

				Role role =this.roleDao.findOne(roleId);

				Result result = new Result();
				this.roleDao.addAp(role, 1, result);
				return this.success(result);
			}
		}

    	return this.success(new Result());
    }
}
