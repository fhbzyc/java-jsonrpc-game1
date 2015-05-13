package com.zhanglong.sg.model;

import java.util.ArrayList;

public class PlayerModel implements Cloneable {

	public int roleId;

	public String name;

	public int avatar;
	
	public int level;

	public ArrayList<Object> generalList = new ArrayList<Object>();

//	public void toPlayer() throws Throwable {
//
//		RoleModel Role = new RoleModel(this.roleId);
//		this.name = Role.name;
//		this.avatar = Role.avatar;
//		this.level = Role.getLevel();
//
//		ArenaModel arenaModel = new ArenaModel(this.roleId);
//
//		Arena arenaTable = arenaModel.getArenaTable();
//
//		int general1 = arenaTable.getGeneralId1();
//		int general2 = arenaTable.getGeneralId2();
//		int general3 = arenaTable.getGeneralId3();
//		int general4 = arenaTable.getGeneralId4();
//
//		GeneralModel General = new GeneralModel(this.roleId);
//		if (general1 != 0) {
//			Hero general = General.findOne(general1);
//			this.generalList.add(general.toArray());
//		}
//		 
//		if (general2 != 0) {
//			Hero general = General.findOne(general2);
//			this.generalList.add(general.toArray());
//		}
//		 
//		if (general3 != 0) {
//			Hero general = General.findOne(general3);
//			this.generalList.add(general.toArray());
//		}
//		 
//		if (general4 != 0) {
//			Hero general = General.findOne(general4);
//			this.generalList.add(general.toArray());
//		}
//		 
//		if (this.generalList.size() == 0) {
//			Hero general = General.findOne(10002);
//			this.generalList.add(general.toArray());
//		}
//	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
}
