package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gift_template")
public class GiftTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2268441240840905002L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_id" , nullable = false , columnDefinition = "int default 0")
    private Integer id;

    @Column(name = "gift_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(name = "gift_reward" , nullable = false , columnDefinition = "text")
    private String reward;

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

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}
}
