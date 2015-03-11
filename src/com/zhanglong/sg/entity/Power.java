package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role_powers")
public class Power implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4223863758778586806L;

	@Id
	@Column(name = "role_id")
	private Integer roleId;

	@Column(name="role_power" , nullable = false , columnDefinition = "int default 0")
	private Integer power;

	@Column(name="role_level" , nullable = false , columnDefinition = "int default 0")
	private Integer level;

	@Column(name="role_avatar" , nullable = false , columnDefinition = "int default 0")
	private Integer avatar;

    @Column(name = "role_name" , nullable = false , length = 20)
    private String name;

	@Column(name = "power_data" , nullable = false , columnDefinition = "text")
	private String data;

	@Column(name="server_id" , nullable = false , columnDefinition = "int default 0")
	private Integer serverId;

	public Power() {
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPower() {
		return power;
	}

	public void setPower(Integer power) {
		this.power = power;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getAvatar() {
		return avatar;
	}

	public void setAvatar(Integer avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
}
