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
@Table(name = "role_lj_checkin" , indexes = {@Index(columnList="role_id" , unique = false)})
public class LjCheckin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2815335154798377083L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Integer id;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private int roleId;

	@Column(name="month" , nullable = false , columnDefinition = "int default 0")
	private int month;

	@Column(name="num" , nullable = false , columnDefinition = "int default 0")
	private int num;

	public LjCheckin() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
