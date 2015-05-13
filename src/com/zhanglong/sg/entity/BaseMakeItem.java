package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_make_item")
public class BaseMakeItem implements Serializable {

	private static final long serialVersionUID = -5272760035760105769L;

	@Id
	@EmbeddedId
    private BaseMakeItemPK pk;

	@Column(name = "item_num" , nullable = false , columnDefinition = "int default 0")
	private Integer num;

    public void setPk(BaseMakeItemPK pk) {
    	this.pk = pk;
    }

    public void setNum(Integer num) {
    	this.num = num;
    }

    public BaseMakeItemPK getPk() {
    	return this.pk;
    }

    public Integer getNum() {
    	return this.num ;
    }

	public BaseMakeItem() {
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

	public BaseMakeItem(int itemId, int materialId, int num) {

		BaseItem baseItem = new BaseItem();
		baseItem.setBaseId(itemId);

		BaseItem material = new BaseItem();
		material.setBaseId(materialId);

		BaseMakeItemPK pk = new BaseMakeItemPK();
		pk.setBaseItem(baseItem);
		pk.setMaterial(material);

		this.setPk(pk);
		this.setNum(num);
	}
}
