package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@Entity
@Table(name = "base_daily_task")
public class BaseDailyTask implements Serializable , Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5360032278208252002L;

	@Id
    @Column(name = "task_id")
    private Integer id;

    @Column(name = "task_goal" , columnDefinition = "int default 0")
    private Integer goal;

    @Column(name = "task_name")
    private String name;

    @Column(name = "task_type")
    private String type;

    @Column(name = "task_reward" , nullable = false , columnDefinition = "text")
    private String reward;

    @JsonSerialize(include = Inclusion.NON_NULL)
    @Column(name = "task_desc" , nullable = false , columnDefinition = "text")
    private String desc;

    @Transient
    private int num;

    @Transient
    private boolean complete;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReward() {
		return this.reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public Integer getGoal() {
		return this.goal;
	}

	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean getComplete() {
		return this.complete;
	}

	public void setComplete(boolean isComplete) {
		this.complete = isComplete;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getDesc() {
		return this.desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public BaseDailyTask clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseDailyTask) super.clone();
	}
}
