package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "base_hero_equip")
public class BaseHeroEquip implements Serializable {

	private static final long serialVersionUID = -910019074185743987L;

	public BaseHeroEquip() {
		
	}

	public BaseHeroEquip(int generalId, int CLASS, int equip1, int equip2, int equip3, int equip4, int equip5, int equip6) {

		BaseHeroEquipPK pk = new BaseHeroEquipPK();

		BaseHero baseGeneral = new BaseHero();
		baseGeneral.setId(generalId);

		pk.setBaseHero(baseGeneral);
		pk.setCLASS(CLASS);

		BaseItem e1 = new BaseItem();
		e1.setBaseId(equip1);
	
		BaseItem e2 = new BaseItem();
		e2.setBaseId(equip2);
		
		BaseItem e3 = new BaseItem();
		e3.setBaseId(equip3);
		
		BaseItem e4 = new BaseItem();
		e4.setBaseId(equip4);
		
		BaseItem e5 = new BaseItem();
		e5.setBaseId(equip5);
		
		BaseItem e6 = new BaseItem();
		e6.setBaseId(equip6);
		
		this.setPk(pk);
		this.setEquip1(e1);
		this.setEquip2(e2);
		this.setEquip3(e3);
		this.setEquip4(e4);
		this.setEquip5(e5);
		this.setEquip6(e6);
	}

	@Id
	@EmbeddedId
    private BaseHeroEquipPK pk;

    @ManyToOne
    @JoinColumn(name="equip1_id",referencedColumnName="item_id")
	private BaseItem equip1;

    @ManyToOne
    @JoinColumn(name="equip2_id",referencedColumnName="item_id")
	private BaseItem equip2;

    @ManyToOne
    @JoinColumn(name="equip3_id",referencedColumnName="item_id")
	private BaseItem equip3;

    @ManyToOne
    @JoinColumn(name="equip4_id",referencedColumnName="item_id")
	private BaseItem equip4;

    @ManyToOne
    @JoinColumn(name="equip5_id",referencedColumnName="item_id")
	private BaseItem equip5;

    @ManyToOne
    @JoinColumn(name="equip6_id",referencedColumnName="item_id")
	private BaseItem equip6;

	public void setPk(BaseHeroEquipPK pk) {
		this.pk = pk;
	}

	public void setEquip1(BaseItem equip) {
		this.equip1 = equip;
	}

	public void setEquip2(BaseItem equip) {
		this.equip2 = equip;
	}

	public void setEquip3(BaseItem equip) {
		this.equip3 = equip;
	}

	public void setEquip4(BaseItem equip) {
		this.equip4 = equip;
	}

	public void setEquip5(BaseItem equip) {
		this.equip5 = equip;
	}

	public void setEquip6(BaseItem equip) {
		this.equip6 = equip;
	}

	public BaseHeroEquipPK getPk() {
		return this.pk;
	}

	public BaseItem getEquip1() {
		return this.equip1;
	}

	public BaseItem getEquip2() {
		return this.equip2;
	}

	public BaseItem getEquip3() {
		return this.equip3;
	}

	public BaseItem getEquip4() {
		return this.equip4;
	}

	public BaseItem getEquip5() {
		return this.equip5;
	}

	public BaseItem getEquip6() {
		return this.equip6;
	}
}
