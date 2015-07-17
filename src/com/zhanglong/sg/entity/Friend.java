package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(FriendPK.class)
@Table(name = "role_friends")
public class Friend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="role_id")
    private Integer roleId;

	@Id
	@Column(name="role_id2")
    private Integer roleId2;

	public Friend() {
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleId2() {
		return roleId2;
	}

	public void setRoleId2(Integer roleId2) {
		this.roleId2 = roleId2;
	}
}
