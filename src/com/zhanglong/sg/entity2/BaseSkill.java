package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_skill")
public class BaseSkill implements Serializable {

	private static final long serialVersionUID = -3992205919499178901L;

	@Id
	@Column(name = "skill_id" , nullable = false , columnDefinition = "int default 0")
	private Integer id;

	@Column(name = "skill_name" , nullable = false , columnDefinition = "varchar(255) default ''" , length = 255)
	private String name;

	@Column(name = "skill_max_level" , nullable = false , columnDefinition = "int default 0")
	private int maxLevel;

	@Column(name = "skill_base_coin" , nullable = false , columnDefinition = "int default 0")
	private int baseCoin;

	@Column(name = "skill_levelup_coin" , nullable = false , columnDefinition = "int default 0")
	private int levelupCoin;

	@Column(name = "skill_is_combo" , nullable = false , columnDefinition = "boolean default FALSE")
	private boolean combo;

	public BaseSkill() {
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public void setBaseCoin(int baseCoin) {
		this.baseCoin = baseCoin;
	}

	public void setLevelupCoin(int levelupCoin) {
		this.levelupCoin = levelupCoin;
	}

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public int getMaxLevel() {
		return this.maxLevel;
	}

	public int getBaseCoin() {
		return this.baseCoin;
	}

	public int getLevelupCoin() {
		return this.levelupCoin;
	}

	public boolean getCombo() {
		return combo;
	}

	public void setCombo(boolean combo) {
		this.combo = combo;
	}
}

