package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class BaseHeroEquipPK implements Serializable {

	private static final long serialVersionUID = -2477755840270868982L;

	public BaseHeroEquipPK() {
		
	}

    @ManyToOne
    @JoinColumn(name="hero_id" , referencedColumnName="hero_id")
    private BaseHero basehero;

    @Column(name = "hero_class" , nullable = false)
    private Integer CLASS;

    public void setBaseHero(BaseHero basehero) {
    	this.basehero = basehero;
    }

    public BaseHero getBaseHero() {
    	return this.basehero;
    }

    public void setCLASS(Integer CLASS) {
    	this.CLASS = CLASS;
    }

    public Integer getCLASS() {
    	return this.CLASS;
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
