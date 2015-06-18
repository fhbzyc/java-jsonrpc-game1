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

	public static int TYPE_0 = 0; // 可重复
	public static int TYPE_1 = 1;

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

    @Column(name = "gift_type" , nullable = false , columnDefinition = "smallint default 0")
    private int type;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
