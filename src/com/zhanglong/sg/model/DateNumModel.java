package com.zhanglong.sg.model;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

public class DateNumModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8022847390225737484L;

	public int date;

	public int buyApNum;

	public int buyCoinNum;

	public int barCoinNum;

	public int specialCopy1;

	public int specialCopy2;

	public int specialCopy3;

	public int specialCopy4;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int specialCopy5;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int specialCopy6;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int specialCopy7;

	public long specialCopyCoolTime1;

	public long specialCopyCoolTime2;

	public long specialCopyCoolTime3;

	public long specialCopyCoolTime4;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long specialCopyCoolTime5;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long specialCopyCoolTime6;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long specialCopyCoolTime7;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int pillageBuyNum = 0;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int online = 0; // 在线奖励

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int buyArenaNum;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int arenaBattleNum;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long arenaTime;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long barTime; // 铜币抽的时间

	private long arenaCd;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public long dropItemTime = 0l;

	@JsonSerialize(include = Inclusion.NON_NULL)
	public int dropItemNum = 0;

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

	public int getSpecialCopy5() {
		return specialCopy5;
	}

	public void setSpecialCopy5(int specialCopy5) {
		this.specialCopy5 = specialCopy5;
	}

	public int getSpecialCopy6() {
		return specialCopy6;
	}

	public void setSpecialCopy6(int specialCopy6) {
		this.specialCopy6 = specialCopy6;
	}

	public int getSpecialCopy7() {
		return specialCopy7;
	}

	public void setSpecialCopy7(int specialCopy7) {
		this.specialCopy7 = specialCopy7;
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

	public long getSpecialCopyCoolTime5() {
		return specialCopyCoolTime5;
	}

	public void setSpecialCopyCoolTime5(long specialCopyCoolTime5) {
		this.specialCopyCoolTime5 = specialCopyCoolTime5;
	}

	public long getSpecialCopyCoolTime6() {
		return specialCopyCoolTime6;
	}

	public void setSpecialCopyCoolTime6(long specialCopyCoolTime6) {
		this.specialCopyCoolTime6 = specialCopyCoolTime6;
	}

	public long getSpecialCopyCoolTime7() {
		return specialCopyCoolTime7;
	}

	public void setSpecialCopyCoolTime7(long specialCopyCoolTime7) {
		this.specialCopyCoolTime7 = specialCopyCoolTime7;
	}

	public int getPillageBuyNum() {
		return pillageBuyNum;
	}

	public void setPillageBuyNum(int pillageBuyNum) {
		this.pillageBuyNum = pillageBuyNum;
	}

	public int getOnline() {
		return online;
	}

	public void setOnline(int online) {
		this.online = online;
	}

	public int getBuyArenaNum() {
		return buyArenaNum;
	}

	public void setBuyArenaNum(int buyArenaNum) {
		this.buyArenaNum = buyArenaNum;
	}

	public int getArenaBattleNum() {
		return arenaBattleNum;
	}

	public void setArenaBattleNum(int arenaBattleNum) {
		this.arenaBattleNum = arenaBattleNum;
	}

	public long getArenaTime() {
		return arenaTime;
	}

	public void setArenaTime(long arenaTime) {
		this.arenaTime = arenaTime;
	}

	public long getArenaCd() {
		return arenaCd;
	}

	public void setArenaCd(long arenaCd) {
		this.arenaCd = arenaCd;
	}

	public long getBarTime() {
		return barTime;
	}

	public void setBarTime(long barTime) {
		this.barTime = barTime;
	}

	public long getDropItemTime() {
		return dropItemTime;
	}

	public void setDropItemTime(long dropItemTime) {
		this.dropItemTime = dropItemTime;
	}

	public int getDropItemNum() {
		return dropItemNum;
	}

	public void setDropItemNum(int dropItemNum) {
		this.dropItemNum = dropItemNum;
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
