package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
//import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "role_battle_logs")
public class BattleLog implements Serializable {

	public static int BATTLE_LOG_INIT = 0; // 新战斗
	public static int BATTLE_LOG_WIN = 1; // 胜利
	public static int BATTLE_LOG_LOST = 2; // 失败

	/**
	 * 
	 */
	private static final long serialVersionUID = 391390636547091423L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //
	@Column(name = "battle_id")
	private Integer id;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "story_id" , nullable = false , columnDefinition = "int default 0")
	private Integer storyId;

	@Column(name = "story_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer storyType;

	@Column(name = "battle_result" , nullable = false , columnDefinition = "smallint default 0")
	private Integer battleResult;  // 0 无发送结果  1胜利 2失败

	@Column(name = "battle_begin_time" , nullable = false , columnDefinition = "int default 0")
	private Integer beginTime;

	@Column(name = "battle_end_time" , nullable = false , columnDefinition = "int default 0")
	private Integer endTime;

	@Column(name = "battle_hero_id1" , nullable = false , columnDefinition = "int default 0")
	private Integer heroId1;

	@Column(name = "battle_hero_id2" , nullable = false , columnDefinition = "int default 0")
	private Integer heroId2;

	@Column(name = "battle_hero_id3" , nullable = false , columnDefinition = "int default 0")
	private Integer heroId3;

	@Column(name = "battle_hero_id4" , nullable = false , columnDefinition = "int default 0")
	private Integer heroId4;

	@Column(name = "battle_data" , nullable = false , columnDefinition = "text")
	private String data;

	@Column(name = "battle_role_id2" , nullable = false , columnDefinition = "int default 0")
	private int roleId2;

	public BattleLog() {
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public void setStoryId(Integer storyId) {
		this.storyId = storyId;
	}

	public void setStoryType(Integer storyType) {
		this.storyType = storyType;
	}

	public void setBattleResult(Integer battleResult) {
		this.battleResult = battleResult;
	}

	public void setBeginTime(Integer unixTime) {
		this.beginTime = unixTime;
	}

	public void setEndTime(Integer unixTime) {
		this.endTime = unixTime;
	}

	public void setHeroId1(Integer heroId) {
		this.heroId1 = heroId;
	}

	public void setHeroId2(Integer heroId) {
		this.heroId2 = heroId;
	}
	
	public void setHeroId3(Integer heroId) {
		this.heroId3 = heroId;
	}

	public void setHeroId4(Integer heroId) {
		this.heroId4 = heroId;
	}

	/**
	 * 
	 * @return
	 */
	public Integer getId() {
		return this.id;
	}

	public Integer getRoleId() {
		return this.roleId;
	}

	public Integer getStoryId() {
		return this.storyId;
	}
	
	public Integer getStoryType() {
		return this.storyType;
	}
	
	public Integer getBattleResult() {
		return this.battleResult;
	}
	
	public Integer getBeginTime() {
		return this.beginTime;
	}
	
	public Integer getEndTime() {
		return this.endTime;
	}

	public Integer getHeroId1() {
		return this.heroId1;
	}

	public Integer getHeroId2() {
		return this.heroId2;
	}
	
	public Integer getHeroId3() {
		return this.heroId3;
	}

	public Integer getHeroId4() {
		return this.heroId4;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getRoleId2() {
		return roleId2;
	}

	public void setRoleId2(int roleId2) {
		this.roleId2 = roleId2;
	}
}
