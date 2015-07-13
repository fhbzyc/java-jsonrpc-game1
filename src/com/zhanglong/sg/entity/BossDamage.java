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
@Table(name = "role_boss_damages" , indexes = {@Index(columnList="boss_date" , unique = false)})
public class BossDamage implements Serializable {

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

    @Column(name = "role_avatar" , nullable = false , columnDefinition = "int default 0")
    private int avatar;

    @Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
    private int serverId;

    @Column(name = "boss_damage" , nullable = false , columnDefinition = "int default 0")
	private int damage;

    @Column(name = "boss_date" , nullable = false , columnDefinition = "int default 0")
    private int date;

    @Column(name = "boss_heros" , nullable = false , columnDefinition = "varchar(1000) default ''")
    private String heros;

    public BossDamage() {
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

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(int avatar) {
		this.avatar = avatar;
	}

	public String getHeros() {
		return heros;
	}

	public void setHeros(String heros) {
		this.heros = heros;
	}
}
