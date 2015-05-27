package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_activity")
public class BaseActivity implements Serializable , Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1246178396676946771L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "activity_id")
	private Integer id;

	@Column(name = "activity_name" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String name;

	@Column(name = "activity_type" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String type;

	@Column(name = "activity_icon" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String icon;

	@Column(name = "activity_begin_time" , nullable = false , columnDefinition = "timestamp")
	private Timestamp beginTime;

	@Column(name = "activity_end_time" , nullable = false , columnDefinition = "timestamp")
	private Timestamp endTime;

	@Column(name = "activity_reward" , nullable = false , columnDefinition = "text")
	private String reward;

	@Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
	private int serverId;

	@Column(name = "activity_enable" , nullable = false , columnDefinition = "boolean")
	private boolean enable;

	@Column(name = "activity_order" , nullable = false , columnDefinition = "int default 0")
	private int order;

	@Column(name = "activity_title" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String title;

	@Column(name = "activity_content" , nullable = false , columnDefinition = "text")
	private String content;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public BaseActivity clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseActivity) super.clone();
	}
}
