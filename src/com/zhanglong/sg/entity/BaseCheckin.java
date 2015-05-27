package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_checkin")
public class BaseCheckin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4373091087428980868L;

	@Id
    private Integer month;

	@Column(name = "reward" , nullable = false , columnDefinition = "text")
	private String reward;

	// 累计签到奖励
	@Column(name = "lj" , nullable = false , columnDefinition = "text")
	private String lj;

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public String getLj() {
		return lj;
	}

	public void setLj(String lj) {
		this.lj = lj;
	}
}
