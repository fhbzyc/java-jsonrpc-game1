package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_item")
public class BaseItem implements Serializable , Cloneable {

	private static final long serialVersionUID = -2039953064548304625L;

	@Id
	@Column(name = "item_id" , nullable = false , columnDefinition = "int default 0")
	private Integer baseId;

	@Column(name = "item_name" , nullable = false)
	private String name;

	@Column(name = "item_type" , nullable = false)
	private Integer type;

	@Column(name = "item_sell_coin" , nullable = false , columnDefinition = "int default 0")
	private Integer sellCoin;

	@Column(name = "item_make_coin" , nullable = false , columnDefinition = "int default 0")
	private Integer makeCoin;

	@Column(name = "item_color" , nullable = false , columnDefinition = "int default 0")
	private Integer color;

	@Column(name = "item_exp" , nullable = false , columnDefinition = "int default 0")
	private Integer exp;

	public BaseItem() {
	}
	
	public void setBaseId(Integer baseId) {
		this.baseId = baseId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setSellCoin(Integer sellCoin) {
		this.sellCoin = sellCoin;
	}

	public void setMakeCoin(Integer makeCoin) {
		this.makeCoin = makeCoin;
	}

	public Integer getBaseId() {
		return this.baseId;
	}

	public String getName() {
		return this.name;
	}

	public int getType() {
		return this.type;
	}

	public int getSellCoin() {
		return this.sellCoin;
	}

	public int getMakeCoin() {
		return this.makeCoin;
	}

	public Integer getColor() {
		return color;
	}

	public void setColor(Integer color) {
		this.color = color;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public int maxExp() {
		if (this.type == 5) {
			if (this.color == 1) {
				return 20;
			} else if (this.color == 2) {
				return 270;
			} else if (this.color == 3) {
				return 1620;
			}
		}

		return 0;
	}

	public BaseItem clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseItem) super.clone();
	}
}
