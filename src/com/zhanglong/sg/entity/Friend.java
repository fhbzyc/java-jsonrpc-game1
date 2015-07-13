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
@Table(name = "role_friends" , indexes = {@Index(columnList="role_id" , unique = false) , @Index(columnList="role_id2" , unique = false)})
public class Friend implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

	@Column(name="role_id2" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId2;

	public Friend() {
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

	public Integer getRoleId2() {
		return roleId2;
	}

	public void setRoleId2(Integer roleId2) {
		this.roleId2 = roleId2;
	}
}
