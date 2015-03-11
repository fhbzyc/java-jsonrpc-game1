package com.zhanglong.sg.model;

import java.io.Serializable;
import java.util.List;

import com.zhanglong.sg.entity.BaseItemShop;

public class ItemShopModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6877159718315552715L;

	private List<BaseItemShop> itemList;

	private int date;

	private int refreshNum;

	private long refreshTime;

	public List<BaseItemShop> getItemList() {
		return this.itemList;
	}

	public void setItemList(List<BaseItemShop> itemList) {
		this.itemList = itemList;
	}

	public int getDate() {
		return this.date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public long getRefreshTime() {
		return this.refreshTime;
	}

	public void setRefreshTime(long refreshTime) {
		this.refreshTime = refreshTime;
	}

	public int getRefreshNum() {
		return this.refreshNum;
	}

	public void setRefreshNum(int refreshNum) {
		this.refreshNum = refreshNum;
	}
}
