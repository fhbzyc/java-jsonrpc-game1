package com.zhanglong.sg.model;

import java.io.Serializable;

public class CopyStarModel implements Serializable , Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 906486677715091122L;

	private int type;

	private int chapter;

	private int star;

	private int[][] items;

	private boolean have;

	public CopyStarModel() {
	}

	public CopyStarModel(int type, int chapter, int star, int[][] items) {
		this.setType(type);
		this.setChapter(chapter);
		this.setStar(star);
		this.setItems(items);
	}

	public int getChapter() {
		return chapter;
	}

	public void setChapter(int chapter) {
		this.chapter = chapter;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public int[][] getItems() {
		return items;
	}

	public void setItems(int[][] items) {
		this.items = items;
	}

	public boolean isHave() {
		return have;
	}

	public void setHave(boolean have) {
		this.have = have;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	protected CopyStarModel clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (CopyStarModel) super.clone();
	}
}
