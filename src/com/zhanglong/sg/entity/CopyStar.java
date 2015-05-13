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
@Table(name = "role_copy_star" , indexes = {@Index(columnList="role_id" , unique = false)})
public class CopyStar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5989824340338101987L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name="story_type" , nullable = false , columnDefinition = "smallint default 0")
    private int type;

	@Column(name = "story_chapter" , nullable = false , columnDefinition = "int default 0")
	private int chapter;

	@Column(name = "story_star" , nullable = false , columnDefinition = "int default 0")
	private int star;

	@Column(name="role_id" , nullable = false , columnDefinition = "int default 0")
    private int roleId;

	public CopyStar() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
}
