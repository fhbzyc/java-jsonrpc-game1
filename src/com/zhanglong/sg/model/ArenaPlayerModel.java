package com.zhanglong.sg.model;

import java.util.ArrayList;

import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.BaseHeroEquipDao;
import com.zhanglong.sg.dao.HeroDao;
import com.zhanglong.sg.dao.RoleDao;
import com.zhanglong.sg.entity.Arena;
import com.zhanglong.sg.entity.BaseHeroEquip;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.service.RoleService;
import com.zhanglong.sg.utils.Utils;

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
