package com.zhanglong.sg.model;

import java.io.Serializable;
import java.util.HashMap;

public class MissionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8266645699969141748L;

	private HashMap<Integer, Integer> missionMap = new HashMap<Integer, Integer>();

	public HashMap<Integer, Integer> getMissionMap() {
		return missionMap;
	}

	public void setMissionMap(HashMap<Integer, Integer> missionMap) {
		this.missionMap = missionMap;
	}
}
