package com.zhanglong.sg.model;

public class PlayerModel2 implements Cloneable {

	public int roleId;

	public String name;

	public int avatar;
	
	public int level;

	@Override
	public PlayerModel2 clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (PlayerModel2) super.clone();
	}
}
