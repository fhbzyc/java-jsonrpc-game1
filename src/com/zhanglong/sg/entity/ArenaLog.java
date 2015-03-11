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
@Table(name = "role_arena_log" , indexes = {@Index(columnList="role_id1" , unique = false) , @Index(columnList="role_id2" , unique = false)})
public class ArenaLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 690205652508187522L;

	public ArenaLog() {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //
	@Column(name = "arena_log_id")
	private Integer id;

	@Column(name = "arena_log_begin_time")
	private Long beginTime;

	@Column(name = "arena_log_end_time")
	private Long endTime;

	@Column(name = "role_id1")
	private Integer roleId1;

	@Column(name = "role_id2")
	private Integer roleId2;
	
	@Column(name = "arena_log_result")
	private Integer battleResult;

	@Column(name = "arena_log_rank1")
	private Integer rank1;

	@Column(name = "arena_log_rank2")
	private Integer rank2;

	@Column(name = "arena_log_data" , columnDefinition = "text")
	private String data;

	@Column(name = "arena_log_name1")
	private String name1;

	@Column(name = "arena_log_name2")
	private String name2;

	@Column(name = "arena_log_avatar1")
	private Integer avatar1;

	@Column(name = "arena_log_avatar2")
	private Integer avatar2;

	public void setId(Integer id) {
		this.id = id;
	}
	
	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}
	
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	public void setRoleId1(Integer roleId1) {
		this.roleId1 = roleId1;
	}
	
	public void setRoleId2(Integer roleId2) {
		this.roleId2 = roleId2;
	}
	
	public void setBattleResult(Integer battleResult) {
		this.battleResult = battleResult;
	}
	
	public void setRank1(Integer rank1) {
		this.rank1 = rank1;
	}

	public void setRank2(Integer rank2) {
		this.rank2 = rank2;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void setName1(String name) {
		this.name1 = name;
	}

	public void setName2(String name) {
		this.name2 = name;
	}

	public void setAvatar1(Integer avatar) {
		this.avatar1 = avatar;
	}

	public void setAvatar2(Integer avatar) {
		this.avatar2 = avatar;
	}


	public Integer getId() {
		return this.id;
	}
	
	public Long getBeginTime() {
		return this.beginTime;
	}
	
	public Long getEndTime() {
		return this.endTime;
	}
	
	public Integer getRoleId1() {
		return this.roleId1;
	}
	
	public Integer getRoleId2() {
		return this.roleId2;
	}
	
	public Integer getBattleResult() {
		return this.battleResult;
	}
	
	public Integer getRank1() {
		return this.rank1;
	}

	public Integer getRank2() {
		return this.rank2;
	}

	public String getData() {
		return this.data;
	}

	public String getName1() {
		return this.name1;
	}

	public String getName2() {
		return this.name2;
	}

	public Integer getAvatar1() {
		return this.avatar1;
	}

	public Integer getAvatar2() {
		return this.avatar2;
	}
}
