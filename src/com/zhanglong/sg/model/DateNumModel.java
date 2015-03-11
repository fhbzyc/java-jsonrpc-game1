package com.zhanglong.sg.model;

import java.io.Serializable;

public class DateNumModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8022847390225737484L;

	private int date;

	private int buyApNum;

	private int buyCoinNum;

	private int barCoinNum;

	private int specialCopy1;

	private int specialCopy2;

	private int specialCopy3;

	private int specialCopy4;

	private long specialCopyCoolTime1;

	private long specialCopyCoolTime2;

	private long specialCopyCoolTime3;

	private long specialCopyCoolTime4;

	private long arenaNum;

	private long arenaCd;

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getBuyApNum() {
		return buyApNum;
	}

	public void setBuyApNum(int buyApNum) {
		this.buyApNum = buyApNum;
	}

	public int getSpecialCopy1() {
		return specialCopy1;
	}

	public void setSpecialCopy1(int specialCopy1) {
		this.specialCopy1 = specialCopy1;
	}

	public int getSpecialCopy2() {
		return specialCopy2;
	}

	public void setSpecialCopy2(int specialCopy2) {
		this.specialCopy2 = specialCopy2;
	}

	public int getSpecialCopy3() {
		return specialCopy3;
	}

	public void setSpecialCopy3(int specialCopy3) {
		this.specialCopy3 = specialCopy3;
	}

	public int getSpecialCopy4() {
		return specialCopy4;
	}

	public void setSpecialCopy4(int specialCopy4) {
		this.specialCopy4 = specialCopy4;
	}

	public long getSpecialCopyCoolTime1() {
		return specialCopyCoolTime1;
	}

	public void setSpecialCopyCoolTime1(long specialCopyCoolTime1) {
		this.specialCopyCoolTime1 = specialCopyCoolTime1;
	}

	public long getSpecialCopyCoolTime2() {
		return specialCopyCoolTime2;
	}

	public void setSpecialCopyCoolTime2(long specialCopyCoolTime2) {
		this.specialCopyCoolTime2 = specialCopyCoolTime2;
	}

	public long getSpecialCopyCoolTime3() {
		return specialCopyCoolTime3;
	}

	public void setSpecialCopyCoolTime3(long specialCopyCoolTime3) {
		this.specialCopyCoolTime3 = specialCopyCoolTime3;
	}

	public long getSpecialCopyCoolTime4() {
		return specialCopyCoolTime4;
	}

	public void setSpecialCopyCoolTime4(long specialCopyCoolTime4) {
		this.specialCopyCoolTime4 = specialCopyCoolTime4;
	}

    public int getBuyCoinNum() {
		return buyCoinNum;
	}

	public void setBuyCoinNum(int buyCoinNum) {
		this.buyCoinNum = buyCoinNum;
	}

	public int getBarCoinNum() {
		return barCoinNum;
	}

	public void setBarCoinNum(int barCoinNum) {
		this.barCoinNum = barCoinNum;
	}

	public long getArenaNum() {
		return arenaNum;
	}

	public void setArenaNum(long arenaNum) {
		this.arenaNum = arenaNum;
	}

	public long getArenaCd() {
		return arenaCd;
	}

	public void setArenaCd(long arenaCd) {
		this.arenaCd = arenaCd;
	}

	public int buyApNeedGold() {
    	int n = this.buyApNum / 2;
    	int gold = (int)(50 * Math.pow(2, n));
		if (gold > 800) {
			gold = 800;
		}
		return gold;
    }
}
