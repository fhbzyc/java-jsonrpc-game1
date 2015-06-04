package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "pay_shop")
public class BasePayShop implements Serializable , Cloneable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4206298211846580881L;

	public static int M_CARD = 1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pay_id")
    private Integer id;

	@Column(name = "pay_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer type;

	@Column(name = "pay_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

	@Column(name = "pay_add_gold" , nullable = false , columnDefinition = "int default 0")
    private Integer addGold;

	@Column(name = "pay_gold" , nullable = false , columnDefinition = "int default 0")
    private Integer gold;

	@Column(name = "pay_money" , nullable = false , columnDefinition = "int default 0")
    private Integer money;

	@JsonIgnore
	@Column(name = "pay_order" , nullable = false , columnDefinition = "int default 0")
    private Integer order;

	@Column(name = "pay_desc" , nullable = false , columnDefinition = "text")
    private String desc;

	@Column(name = "pay_recommend" , nullable = false , columnDefinition = "boolean default false")
    private Boolean recommend;

	public BasePayShop() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAddGold() {
		return addGold;
	}

	public void setAddGold(Integer teamExp) {
		this.addGold = teamExp;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getRecommend() {
		return recommend;
	}

	public void setRecommend(Boolean recommend) {
		this.recommend = recommend;
	}

	@Override
	public BasePayShop clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BasePayShop) super.clone();
	}
}
