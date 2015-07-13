package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "base_achievement")
public class BaseAchievement implements Serializable , Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String TYPE_CRUSADE    = "crusade";
	public static String TYPE_HERO_CLASS = "hero_class";
	public static String TYPE_HERO_Id    = "hero_num";
	public static String TYPE_KILL_NUM   = "kill_num";
	public static String TYPE_PK         = "pk";
	public static String TYPE_VIP        = "vip";
	public static String TYPE_STAR       = "star";

	@Id
    @Column(name = "achievement_id")
    private Integer id;

    @JsonIgnore
    @Column(name = "achievement_parent_id" , columnDefinition = "int default 0")
    private int parentId;

    @Column(name = "achievement_goal" , columnDefinition = "int default 0")
    private int goal;

    @Column(name = "achievement_money" , columnDefinition = "int default 0")
    private int money;

    @JsonIgnore
    @Column(name = "achievement_target" , columnDefinition = "int default 0")
    private int target;

    @Column(name = "achievement_name")
    private String name;

    @Column(name = "achievement_type")
    private String type;

    @Column(name = "achievement_reward" , nullable = false , columnDefinition = "text")
    private String reward;

    @Column(name = "achievement_desc" , nullable = false , columnDefinition = "text")
    private String desc;

    @Column(name = "achievement_other" , columnDefinition = "text")
    private String other;

    @Transient
    private int num;

    @Transient
    private int moneyType;

    @Transient
    private boolean complete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getGoal() {
		return goal;
	}

	public void setGoal(int goal) {
		this.goal = goal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public boolean getComplete() {
		return complete;
	}

	public void setComplete(boolean isComplete) {
		this.complete = isComplete;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(int moneyType) {
		this.moneyType = moneyType;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public BaseAchievement clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseAchievement) super.clone();
	}
}
