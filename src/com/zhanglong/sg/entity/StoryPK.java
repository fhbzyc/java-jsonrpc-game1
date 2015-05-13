package com.zhanglong.sg.entity;

import java.io.Serializable;

public class StoryPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4465461378234206155L;

	private Integer aRoleId;

	private Integer storyId;

	private Integer type;

	public void setStoryId(Integer storyId) {
		this.storyId = storyId;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public void setARoleId(Integer roleId) {
		this.aRoleId = roleId;
	}

	public Integer getStoryId() {
		return this.storyId;
	}

	public Integer getType() {
		return this.type;
	}

	public Integer getARoleId() {
		return this.aRoleId;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
}

