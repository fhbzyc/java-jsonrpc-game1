package com.zhanglong.sg.entity;

import java.io.Serializable;

//@Embeddable
public class HeroPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3134564034793560557L;

	private Integer aRoleId;

	private Integer heroId;

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getHeroId() {
	   return this.heroId;
	}

	public void setARoleId(Integer roleId) {
		this.aRoleId = roleId;
	}

	public Integer getARoleId() {
		return this.aRoleId;
	}
}

