package com.zhanglong.sg.entity;

import java.io.Serializable;

public class MissionPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer aRoleId;

	private Integer missionId;

	public Integer getMissionId() {
		return missionId;
	}

	public void setMissionId(Integer missionId) {
		this.missionId = missionId;
	}

	public void setARoleId(Integer roleId) {
		this.aRoleId = roleId;
	}

	public Integer getARoleId() {
		return this.aRoleId;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}

