package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "role_checkin" , indexes = {@Index(columnList="role_id" , unique = false)})
public class Checkin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2927931565448110656L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="checkin_id")
	private Integer id;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

	@Column(name="checkin_month" , nullable = false , columnDefinition = "int default 0")
	private Integer month;

	@Column(name="checkin_day" , nullable = false , columnDefinition = "int default 0")
	private Integer day;

	@Column(name="checkin_num" , nullable = false , columnDefinition = "int default 0")
	private Integer num;

	public Checkin() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
