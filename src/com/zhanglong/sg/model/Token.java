package com.zhanglong.sg.model;

import java.io.Serializable;

public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8235455759518795L;

	private int userId;

	private int roleId;

	private int serverId;

	private String tokenS;

	public Token() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getTokenS() {
		return tokenS;
	}

	public void setTokenS(String tokenS) {
		this.tokenS = tokenS;
	}
}
