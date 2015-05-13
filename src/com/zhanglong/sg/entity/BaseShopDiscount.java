package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_shop_discount")
public class BaseShopDiscount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5642102298623669913L;

	@Id
	@Column(name = "shop_type")
    private Integer type;

    @Column(name = "shop_discount" , nullable = false , columnDefinition = "smallint default 0")
    private Integer discount;

    @Column(name = "begin_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp beginTime;

    @Column(name = "end_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp endTime;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
}
