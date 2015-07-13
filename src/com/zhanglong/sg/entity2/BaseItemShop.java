package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "base_item_shop")
public class BaseItemShop implements Serializable , Cloneable {

	public static int MONEY_TYPE_COIN = 1;
	public static int MONEY_TYPE_GOLD = 2;
	public static int MONEY_TYPE_3 = 3; // 竞技场币
	public static int MONEY_TYPE_4 = 4; // 远征币
	public static int MONEY_TYPE_5 = 5; // 远征币

	public static int SHOP_TYPE_1 = 1; // 普通商店
	public static int SHOP_TYPE_3 = 3; // 竞技场商店
	public static int SHOP_TYPE_4 = 4; // 远征商店
	public static int SHOP_TYPE_5 = 5; // BOSS商店

	/**
	 * 
	 */
	private static final long serialVersionUID = 8494356799083057614L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="item_id" , nullable = false , columnDefinition = "int default 0")
    private Integer itemId;

    @JsonIgnore
	@Column(name = "goods_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer type;

	@Column(name = "goods_money_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer moneyType;

	@Column(name = "goods_price" , nullable = false , columnDefinition = "int default 0")
	private Integer price;

	@Column(name = "goods_num" , nullable = false , columnDefinition = "int default 0")
	private Integer num;

	@Transient
	private boolean sold;
	
	public BaseItemShop() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getMoneyType() {
		return moneyType;
	}

	public void setMoneyType(Integer moneyType) {
		this.moneyType = moneyType;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public boolean getSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public BaseItemShop clone() throws CloneNotSupportedException {

        return (BaseItemShop) super.clone();
    }
}
