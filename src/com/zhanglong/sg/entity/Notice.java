package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_notice")
public class Notice implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3236617707876999508L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notice_id")
	private Integer id;

	@Column(name = "notice_name" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String name;

	@Column(name = "notice_begin_time" , nullable = false , columnDefinition = "timestamp")
	private String beginTime;

	@Column(name = "notice_end_time" , nullable = false , columnDefinition = "timestamp")
	private String endTime;

	@Column(name = "notice_title" , nullable = false , columnDefinition = "varchar(255) default ''")
	private String title;

	@Column(name = "notice_content" , nullable = false , columnDefinition = "text")
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

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
}
