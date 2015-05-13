package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(BaseSpecialCopyPK.class)
@Table(name = "base_special_copy")
public class BaseSpecialCopy implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2996442305001891837L;

	@Id
    @Column(name = "week" , nullable = false , columnDefinition = "smallint default 0")
    private Integer week;

	@Id
    @Column(name = "type" , nullable = false , columnDefinition = "smallint default 0")
    private Integer type;

    @Column(name = "begin_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp beginTime;

    @Column(name = "end_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp endTime;

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

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
