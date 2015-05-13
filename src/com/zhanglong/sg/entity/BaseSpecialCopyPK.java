package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class BaseSpecialCopyPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3606952161456314846L;

    private Integer week;

    private Integer type;

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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
