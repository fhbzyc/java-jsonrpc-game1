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

	@Column(name = "story_type" , nullable = false , columnDefinition = "tinyint(1) default 0")
	private Integer storyType;

	@Column(name = "battle_result" , nullable = false , columnDefinition = "tinyint(1) default 0")
	private Integer battleResult;  // 0 无发送结果  1胜利 2失败

	@Column(name = "battle_begin_time" , nullable = false , columnDefinition = "int default 0")
	private Integer beginTime;

	@Column(name = "battle_end_time" , nullable = false , columnDefinition = "int default 0")
	private Integer endTime;

	@Column(name = "battle_general_base_id_1" , nullable = false , columnDefinition = "int default 0")
	private Integer generalBaseId1;

	@Column(name = "battle_general_base_id_2" , nullable = false , columnDefinition = "int default 0")
	private Integer generalBaseId2;

	@Column(name = "battle_general_base_id_3" , nullable = false , columnDefinition = "int default 0")
	private Integer generalBaseId3;

	@Column(name = "battle_general_base_id_4" , nullable = false , columnDefinition = "int default 0")
	private Integer generalBaseId4;

	@Column(name = "battle_data" , nullable = false , columnDefinition = "text")
	private String data;

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

	public void setGeneralBaseId1(Integer generalBaseId) {
		this.generalBaseId1 = generalBaseId;
	}

	public void setGeneralBaseId2(Integer generalBaseId) {
		this.generalBaseId2 = generalBaseId;
	}
	
	public void setGeneralBaseId3(Integer generalBaseId) {
		this.generalBaseId3 = generalBaseId;
	}

	public void setGeneralBaseId4(Integer generalBaseId) {
		this.generalBaseId4 = generalBaseId;
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

	public Integer getGeneralBaseId1() {
		return this.generalBaseId1;
	}

	public Integer getGeneralBaseId2() {
		return this.generalBaseId2;
	}
	
	public Integer getGeneralBaseId3() {
		return this.generalBaseId3;
	}

	public Integer getGeneralBaseId4() {
		return this.generalBaseId4;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
