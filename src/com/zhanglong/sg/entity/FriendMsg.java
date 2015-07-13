package com.zhanglong.sg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "role_friend_msg" , indexes = {@Index(columnList="role_id" , unique = false)})
public class FriendMsg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

	@Column(name="role_id2" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId2;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "role_avatar" , nullable = false , columnDefinition = "int default 0")
    private int avatar;

    @Column(name = "role_level" , nullable = false , columnDefinition = "int default 0")
    private int level;

    public FriendMsg() {
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
}
