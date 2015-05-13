package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_hero_shop")
public class BaseHeroShop implements Serializable {

	private static final long serialVersionUID = -3808640349786901401L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "goods_type" , nullable = false)
	private String type;

	@Column(name = "goods_quality" , nullable = false , columnDefinition = "int default 0")
	private Integer quality;

	@Column(name = "item_id" , nullable = false , columnDefinition = "int default 0")
	private Integer itemId;

	@Column(name = "goods_min_num" , nullable = false , columnDefinition = "int default 0")
	private Integer minNum;

	@Column(name = "goods_max_num" , nullable = false , columnDefinition = "int default 0")
	private Integer maxNum;

	@Column(name = "goods_weight" , nullable = false , columnDefinition = "int default 0")
	private Integer weight;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuality() {
		return quality;
	}

	public void setQuality(Integer quality) {
		this.quality = quality;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getMinNum() {
		return minNum;
	}

	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}

	public Integer getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(Integer maxNum) {
		this.maxNum = maxNum;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
