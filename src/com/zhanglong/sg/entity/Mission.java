package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(MissionPK.class)
@Table(name = "role_missions")
public class Mission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "role_id")
    public int aRoleId;

	@Id
	@Column(name = "mission_id")
    public int missionId;

	@Column(name = "mission_type")
    public String type;

	@Column(name = "mission_num")
    public int num;

	@Column(name = "mission_complete")
    public boolean complete;

	public int getARoleId() {
		return aRoleId;
	}

	public void setARoleId(int aRoleId) {
		this.aRoleId = aRoleId;
	}

	public int getMissionId() {
		return missionId;
	}

	public void setMissionId(int missionId) {
		this.missionId = missionId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
}
