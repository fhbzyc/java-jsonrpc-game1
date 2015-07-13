package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role_boss_killer")
public class BossKiller implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "role_id" , nullable = false , columnDefinition = "int default 0")
    private int roleId;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
    private int serverId;

    @Column(name = "boss_date" , nullable = false , columnDefinition = "int default 0")
    private int date;

    public BossKiller() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}
}
