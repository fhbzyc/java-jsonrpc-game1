package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(AchievementPK.class)
@Table(name = "role_achievements")
public class Achievement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "role_id")
    public int aRoleId;

	@Id
	@Column(name = "achievement_id")
    public int achievementId;

	@Column(name = "achievement_type")
    public String type;

	@Column(name = "achievement_complete")
    public boolean complete;

	public int getARoleId() {
		return aRoleId;
	}

	public void setARoleId(int aRoleId) {
		this.aRoleId = aRoleId;
	}

	public int getAchievementId() {
		return achievementId;
	}

	public void setAchievementId(int achievementId) {
		this.achievementId = achievementId;
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
