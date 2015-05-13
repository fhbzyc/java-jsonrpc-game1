package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "role_activity" , indexes = {@Index(columnList="role_id" , unique = false)})
public class Activity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2903460560423052365L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

	@Column(name="act_id" , nullable = false , columnDefinition = "int default 0")
    private Integer actId;

	@Column(name = "act_key" , nullable = false , columnDefinition = "int default 0")
	private int key;

	@Column(name = "act_time" , nullable = false , columnDefinition = "timestamp")
	private Timestamp time;

	public Activity() {
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

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
