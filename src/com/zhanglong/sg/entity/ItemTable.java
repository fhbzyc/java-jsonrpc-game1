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
@Table(name = "role_items" , indexes = {@Index(columnList="role_id" , unique = false)})
public class ItemTable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7195054150792375221L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //
	private Integer id;

	@Column(name="item_id" , nullable = false , columnDefinition = "int default 0")
    private Integer itemId;

	@Column(name = "item_level" , nullable = false , columnDefinition = "int default 0")
	private Integer level;

	@Column(name = "item_num" , nullable = false , columnDefinition = "int default 0")
	private Integer num;

	@Column(name="role_id")
    private Integer roleId;

	public ItemTable() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	
}
