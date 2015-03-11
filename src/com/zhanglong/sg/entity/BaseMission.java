package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "base_mission")
public class BaseMission implements Serializable , Cloneable {

	private static final long serialVersionUID = -5902095156221396809L;

//    private static final long serialVersionUID = 1L;
//    /** @deprecated */
//    @Deprecated
//    public static final int STATUS_COMPLETED = 1;
//    public static final int STATUS_PROGRESS_COMPLETED = 1;
//    /** @deprecated */
//    @Deprecated
//    public static final int STATUS_UNFINISHED = 0;
//    public static final int STATUS_PROGRESS_UNFINISHED = 0;
//    /** @deprecated */
//    @Deprecated
//    public static final int STATUS_GOT_REWARD = 2;
//    public static final int STATUS_PROGRESS_GOT_REWARD = 2;
//    public static final int STATUS_TASK_UNAVAILABLE = 0;
//    public static final int STATUS_TASK_AVAILABLE = 1;

    @Id
    @Column(name = "mission_id")
    private Integer id;

    @JsonIgnore
    @Column(name = "mission_parent_id" , columnDefinition = "int default 0")
    private Integer parentId;

    @Column(name = "mission_goal" , columnDefinition = "int default 0")
    private Integer goal;

    @JsonIgnore
    @Column(name = "mission_level" , columnDefinition = "int default 0")
    private Integer level;

    @Column(name = "mission_target" , columnDefinition = "int default 0")
    private Integer target;

    @Column(name = "mission_name")
    private String name;

    @Column(name = "mission_type")
    private String type;

    @Column(name = "mission_reward" , nullable = false , columnDefinition = "text")
    private String reward;

    @Column(name = "mission_desc" , nullable = false , columnDefinition = "text")
    private String desc;
    
    @Transient
    private int num;

    @Transient
    @JsonIgnore
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public Integer getGoal() {
		return goal;
	}

	public void setGoal(Integer goal) {
		this.goal = goal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getTarget() {
		return target;
	}

	public void setTarget(Integer target) {
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

	@Override
	public BaseMission clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseMission) super.clone();
	}
}
