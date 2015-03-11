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

	public BaseItem() {
//		this.id = id;
//		this.name = name;
//		this.type = type;
//		this.sell_coin = sell_coin;
//		this.make_coin = make_coin;
//		this.make_need_id = new int[make_need.length];
////		this.make_need_num = new int[make_need.length];
//		for (int i = 0 ; i < make_need.length ; i++) {
//			this.make_need_id[i] = make_need[i][0];
//			this.make_need_num[i] = make_need[i][1];
//		}
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

	public BaseItem clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseItem) super.clone();
	}
}
