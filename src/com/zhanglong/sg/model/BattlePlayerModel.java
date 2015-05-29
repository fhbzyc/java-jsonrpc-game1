package com.zhanglong.sg.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zhanglong.sg.entity.Hero;

/**
 * 对战对手数据
 * @author Speed
 *
 */
public class BattlePlayerModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6939211554025468041L;

	private int roleId;

	private String name;

	private Integer avatar;

	private int level;

	private int rewardNum = 0;

	private List<Reward> rewards = new ArrayList<Reward>();

	private ArrayList<Hero> heros = new ArrayList<Hero>();

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAvatar() {
		return avatar;
	}

	public void setAvatar(Integer avatar) {
		this.avatar = avatar;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public ArrayList<Hero> getHeros() {
		return heros;
	}

	public void setHeros(ArrayList<Hero> heros) {
		this.heros = heros;
	}

	public int getRewardNum() {
		return rewardNum;
	}

	public void setRewardNum(int rewardNum) {
		this.rewardNum = rewardNum;
	}

	public List<Reward> getRewards() {
		return rewards;
	}

	public void setRewards(List<Reward> rewards) {
		this.rewards = rewards;
	}

	public void addAHero(Hero hero) {
		hero.setLevel(hero.level());
		this.heros.add(hero);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
