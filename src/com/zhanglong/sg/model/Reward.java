package com.zhanglong.sg.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

public class Reward implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7672931132030671625L;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Integer exp;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Integer coin;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Integer gold;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Integer money3; // 竞技场的币

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Integer money4; // 讨伐天下的币

	@JsonSerialize(include = Inclusion.NON_NULL)
	private int[] item_id;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private int[] item_num;

	@JsonSerialize(include = Inclusion.NON_NULL)
	private Boolean has;

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getMoney3() {
		return money3;
	}

	public void setMoney3(Integer money3) {
		this.money3 = money3;
	}

	public Integer getMoney4() {
		return money4;
	}

	public void setMoney4(Integer money4) {
		this.money4 = money4;
	}

	public int[] getItem_id() {
		return item_id;
	}

	public void setItem_id(int[] item_id) {
		this.item_id = item_id;
	}

	public int[] getItem_num() {
		return item_num;
	}

	public void setItem_num(int[] item_num) {
		this.item_num = item_num;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public Boolean getHas() {
		return has;
	}

	public void setHas(Boolean has) {
		this.has = has;
	}
}

