package com.zhanglong.sg.model;

import java.util.ArrayList;

public class ArenaPlayerModel implements Cloneable {

	public int rank;

	public int roleId;

	public String name;

	public Integer avatar;

	public int level;

	public int wins = 0;

	public ArrayList<Object> generalList = new ArrayList<Object>();

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
