package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class BaseMakeItemPK implements Serializable {

	private static final long serialVersionUID = -1634128164784605927L;

	public BaseMakeItemPK() {
		
	}

    @ManyToOne
    @JoinColumn(name="item_id",referencedColumnName="item_id")
    private BaseItem baseItem;

    @ManyToOne
    @JoinColumn(name="material_id",referencedColumnName="item_id")
    private BaseItem material;

    public void setBaseItem(BaseItem baseItem) {
    	this.baseItem = baseItem;
    }

    public void setMaterial(BaseItem material) {
    	this.material = material;
    }

    public BaseItem getBaseItem() {
    	return this.baseItem;
    }

    public BaseItem getMaterial() {
    	return this.material;
    }

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		return super.equals(arg0);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}
