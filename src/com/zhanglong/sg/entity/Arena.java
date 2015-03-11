package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role_arena")
public class Arena implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7881103756844527934L;

    @Id
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "arena_rank" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer rank;

    @Column(name = "arena_battle_num" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer battleNum;

    @Column(name = "arena_date" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer date;
    
    @Column(name = "arena_battle_time" , nullable = false , columnDefinition = "bigint(20) default 0")
    private Long battleTime;

    @Column(name = "arena_win_num" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer winNum;

    @Column(name = "arena_general1" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer generalId1;
    
    @Column(name = "arena_general2" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer generalId2;
    
    @Column(name = "arena_general3" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer generalId3;
    
    @Column(name = "arena_general4" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer generalId4;

    // 购买挑战次数
    @Column(name = "arena_buy_num" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer buyNum;

    public void setRoleId(Integer roleId) {
    	this.roleId = roleId;
    }

    public Integer getRoleId() {
    	return this.roleId;
    }

    public void setRank(Integer rank) {
    	this.rank = rank;
    }

    public Integer getRank() {
    	return this.rank;
    }
    
    public void setDate(Integer date) {
    	this.date = date;
    }

    public Integer getDate() {
    	return this.date;
    }
    
    public void setBattleNum(Integer battleNum) {
    	this.battleNum = battleNum;
    }

    public Integer getBattleNum() {
    	return this.battleNum;
    }

    public void setBattleTime(Long battleTime) {
    	this.battleTime = battleTime;
    }

    public Long getBattleTime() {
    	return this.battleTime;
    }

    public void setWinNum(Integer winNum) {
    	this.winNum = winNum;
    }

    public Integer getWinNum() {
    	return this.winNum;
    }

    public void setGeneralId1(Integer generalId1) {
    	this.generalId1 = generalId1;
    }

    public Integer getGeneralId1() {
    	return this.generalId1;
    }

    public void setGeneralId2(Integer generalId2) {
    	this.generalId2 = generalId2;
    }

    public Integer getGeneralId2() {
    	return this.generalId2;
    }

    public void setGeneralId3(Integer generalId3) {
    	this.generalId3 = generalId3;
    }

    public Integer getGeneralId3() {
    	return this.generalId3;
    }

    public void setGeneralId4(Integer generalId4) {
    	this.generalId4 = generalId4;
    }

    public Integer getGeneralId4() {
    	return this.generalId4;
    }

    public void setBuyNum(Integer num) {
    	this.buyNum = num;
    }

    public Integer getBuyNum() {
    	return this.buyNum;
    }
}
