package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role_kill_rank")
public class KillRank implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

	@Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
	private int serverId;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "role_avatar" , nullable = false , columnDefinition = "int default 0")
    private int avatar;

    @Column(name = "role_level" , nullable = false , columnDefinition = "int default 0")
    private int level;

	@Column(name = "kill_num" , nullable = false , columnDefinition = "int default 0")
	private int num;

	@Column(name = "kill_time" , nullable = false , columnDefinition = "timestamp")
	private Timestamp time;

	public KillRank() {
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
