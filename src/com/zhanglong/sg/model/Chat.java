package com.zhanglong.sg.model;

import java.io.Serializable;

public class Chat implements Serializable {

	public static int BROADCAST = 0; // 广播
	public static int PRIVATECHAT = 1; // 私聊

	/**
	 * 
	 */
	private static final long serialVersionUID = -8499843708879909081L;

	public int type; // 0 广播  1私聊

	public int roleId;

	public int avatar;

	public int level;

	public String name = "";

	public String msg = "";

	public long time;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
