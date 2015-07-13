package com.zhanglong.sg.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.dao.RoleDao;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Power;
import com.zhanglong.sg.service.RoleService;
import com.zhanglong.sg.utils.SpringContextUtils;

public class CrusadeModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6795317386085461737L;

	private int roleId;

	private int date = 0;

	private List<BattlePlayerModel> list = new ArrayList<BattlePlayerModel>();

	private int num = 0;

	private HashMap<Integer, Integer> hpMap = new HashMap<Integer, Integer>();

	private HashMap<Integer, Float> cdMap = new HashMap<Integer, Float>();

	public CrusadeModel() {
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public List<BattlePlayerModel> getList() {
		return list;
	}

	public void setList(List<BattlePlayerModel> list) {
		this.list = list;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getNum() {
		return num;
	}

	public HashMap<Integer, Integer> getHpMap() {
		return hpMap;
	}

	public void setHpMap(HashMap<Integer, Integer> hpMap) {
		this.hpMap = hpMap;
	}

	public HashMap<Integer, Float> getCdMap() {
		return cdMap;
	}

	public void setCdMap(HashMap<Integer, Float> cdMap) {
		this.cdMap = cdMap;
	}

	public void newPlayers(int level, int power) throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();

		int serverId = 0;

		RoleDao roleDao = (RoleDao) SpringContextUtils.getBean(RoleDao.class);
		serverId = roleDao.findOne(this.roleId).getServerId();

		this.list = new ArrayList<BattlePlayerModel>();
		for (int i = 0; i < 15; i++) {

			BattlePlayerModel player = new BattlePlayerModel();

			Power p = this.getRolePower(this.roleId, serverId, power, i + 1);
			if (p != null) {

				player.setRoleId(p.getRoleId());

				player.setAvatar(p.getAvatar());
				player.setLevel(p.getLevel());
				player.setName(p.getName());

				Hero[] heros = objectMapper.readValue(p.getData(), Hero[].class);
				
		    	for (int j = 0 ; j < heros.length ; j++) {
		    		if (j >= 4) {
		    			break;
		    		}
		    		player.addAHero(heros[j]);
				}
			} else {
				player.setRoleId(i + 1);
				player.setAvatar(i % 9);

		    	String[] firstName = RoleService.FirstName;
		    	String[] lastName = RoleService.LastName;

		    	Random random = new Random();
		    	player.setName(firstName[random.nextInt(firstName.length)] + lastName[random.nextInt(lastName.length)]);

		    	int power2 = power * numByLevel(level, i) / 100;

		    	int maxLevel = 1;
		    	
		    	List<Hero> hero = this.hero(power2);
		    	for (Hero hero2 : hero) {
		    		player.addAHero(hero2);
		    		if (hero2.level() > maxLevel) {
		    			maxLevel = hero2.level();
		    		}
				}

		    	player.setLevel(maxLevel);
			}

			this.list.add(player);
		}
	}

	private int numByLevel(int level, int index) {

		if (level < 40) {
			switch (index) {
			case 1: return 64;
			case 2: return 66;
			case 3: return 68;
			case 4: return 70;
			case 5: return 72;
			case 6: return 74;
			case 7: return 78;
			case 8: return 80; 
			case 9: return 86;
			case 10: return 90;
			case 11: return 100;
			case 12: return 104;
			case 13: return 108;
			case 14: return 112;
			case 15: return 120;
			default:
				return 64;
			}

		} else {

			switch (index) {
			case 1: return 88;
			case 2: return 90;
			case 3: return 92;
			case 4: return 94;
			case 5: return 95;
			case 6: return 96;
			case 7: return 97;
			case 8: return 98; 
			case 9: return 100;
			case 10: return 106;
			case 11: return 108;
			case 12: return 110;
			case 13: return 112;
			case 14: return 114;
			case 15: return 120;

			default:
				return 64;
			}
		}
	}

	private List<Hero> hero(int power) {

		List<Object[]> configs = new ArrayList<Object[]>();
		configs.add(new Object[]{1,4279, new int[]{10002,20,1,1,20,10,0,0} ,new int[]{10009,20,1,1,20,10,0,0} ,new int[]{10010,20,1,1,20,10,0,0} ,new int[]{10012,20,1,1,20,10,0,0}});
		configs.add(new Object[]{4280,4559, new int[]{10011,21,1,2,21,11,0,0} ,new int[]{10009,21,1,2,21,11,0,0} ,new int[]{10002,21,1,1,21,11,0,0} ,new int[]{10012,21,1,1,21,11,0,0}});
		configs.add(new Object[]{4560,4839, new int[]{10012,22,2,2,22,12,0,0} ,new int[]{10014,22,3,2,22,12,0,0} ,new int[]{10010,22,1,2,22,12,0,0} ,new int[]{10000,22,1,2,22,12,0,0}});
		configs.add(new Object[]{4840,5119, new int[]{10000,23,2,2,23,13,0,0} ,new int[]{10002,23,1,2,23,13,0,0} ,new int[]{10014,23,2,2,23,13,0,0} ,new int[]{10011,23,1,2,23,13,0,0}});
		configs.add(new Object[]{5120,5399, new int[]{10011,24,1,2,24,14,0,0} ,new int[]{10017,24,2,3,24,14,0,0} ,new int[]{10014,24,2,2,24,14,0,0} ,new int[]{10000,24,2,2,24,14,0,0}});
		configs.add(new Object[]{5400,5679, new int[]{10002,25,1,1,25,15,0,0} ,new int[]{10009,25,2,1,25,15,0,0} ,new int[]{10011,25,1,2,25,15,0,0} ,new int[]{10012,25,1,2,25,15,0,0}});
		configs.add(new Object[]{5680,5999, new int[]{10010,25,2,2,25,15,0,0} ,new int[]{10014,26,3,2,26,16,0,0} ,new int[]{10012,25,1,2,25,15,0,0} ,new int[]{10016,26,2,2,26,16,0,0}});
		configs.add(new Object[]{6000,6399, new int[]{10000,26,2,2,26,16,6,0} ,new int[]{10014,27,3,2,27,17,7,0} ,new int[]{10002,26,1,2,26,16,6,0} ,new int[]{10016,27,1,2,27,17,7,0}});
		configs.add(new Object[]{6400,6799, new int[]{10000,26,2,2,26,16,6,0} ,new int[]{10002,28,1,2,28,18,8,0} ,new int[]{10014,27,3,2,27,17,7,0} ,new int[]{10001,28,3,2,28,18,8,0}});
		configs.add(new Object[]{6800,7199, new int[]{10001,27,3,2,27,17,7,0} ,new int[]{10004,29,2,3,29,19,9,0} ,new int[]{10014,28,3,2,28,18,8,0} ,new int[]{10000,29,2,2,29,19,9,0}});
		configs.add(new Object[]{7200,7599, new int[]{10016,27,2,3,27,17,7,0} ,new int[]{10017,29,2,2,29,19,9,0} ,new int[]{10014,28,3,3,28,18,8,0} ,new int[]{10012,29,2,3,29,19,9,0}});
		configs.add(new Object[]{7600,7999, new int[]{10011,28,1,3,28,18,8,0} ,new int[]{10002,30,2,3,30,20,10,0} ,new int[]{10009,29,2,3,29,19,9,0} ,new int[]{10003,30,2,2,30,20,10,0}});
		configs.add(new Object[]{8000,8399, new int[]{10011,28,1,3,28,18,8,0} ,new int[]{10004,30,2,3,30,20,10,0} ,new int[]{10009,29,2,3,29,19,9,0} ,new int[]{10012,30,2,3,30,20,10,0}});
		configs.add(new Object[]{8400,8799, new int[]{10005,29,3,3,29,19,9,0} ,new int[]{10017,31,2,3,31,21,11,0} ,new int[]{10014,30,3,3,30,20,10,0} ,new int[]{10003,31,2,3,31,21,11,0}});
		configs.add(new Object[]{8800,9199, new int[]{10011,30,2,3,30,20,10,0} ,new int[]{10004,32,2,3,32,22,12,0} ,new int[]{10014,31,3,3,31,21,11,0} ,new int[]{10012,32,2,3,32,22,12,0}});
		configs.add(new Object[]{9200,9599, new int[]{10012,30,2,3,30,20,10,0} ,new int[]{10004,32,2,3,32,22,12,0} ,new int[]{10014,31,3,3,31,21,11,0} ,new int[]{10013,32,2,3,32,22,12,0}});
		configs.add(new Object[]{9600,9999, new int[]{10005,31,3,3,31,21,11,0} ,new int[]{10010,33,2,3,33,23,13,0} ,new int[]{10009,32,2,3,32,22,12,0} ,new int[]{10001,33,3,3,33,23,13,0}});
		configs.add(new Object[]{10000,10399, new int[]{10005,31,3,3,31,21,11,0} ,new int[]{10010,33,2,3,33,23,13,0} ,new int[]{10014,32,3,3,32,22,12,0} ,new int[]{10016,33,2,3,33,23,13,0}});
		configs.add(new Object[]{10400,10799, new int[]{10016,32,2,3,32,22,12,0} ,new int[]{10004,34,2,3,34,24,14,0} ,new int[]{10009,33,2,3,33,23,13,0} ,new int[]{10001,34,3,3,34,24,14,0}});
		configs.add(new Object[]{10800,11199, new int[]{10005,32,3,3,32,22,12,0} ,new int[]{10010,34,2,3,34,24,14,0} ,new int[]{10009,33,2,3,33,23,13,0} ,new int[]{10015,34,2,3,34,24,14,0}});
		configs.add(new Object[]{11200,11599, new int[]{10001,33,3,3,33,23,13,0} ,new int[]{10014,35,3,3,35,25,15,0} ,new int[]{10002,34,2,3,34,24,14,0} ,new int[]{10005,35,3,3,35,25,15,0}});
		configs.add(new Object[]{11600,11999, new int[]{10015,33,2,3,33,23,13,0} ,new int[]{10014,35,3,3,35,25,15,0} ,new int[]{10017,34,2,3,34,24,14,0} ,new int[]{10013,35,2,3,35,25,15,0}});
		configs.add(new Object[]{12000,12399, new int[]{10001,34,3,3,34,24,14,0} ,new int[]{10010,36,2,3,36,26,16,0} ,new int[]{10004,35,2,4,35,25,15,0} ,new int[]{10005,36,3,3,36,26,16,0}});
		configs.add(new Object[]{12400,12879, new int[]{10016,34,2,3,34,19,9,0} ,new int[]{10004,36,2,4,36,21,11,0} ,new int[]{10002,35,2,3,35,20,10,0} ,new int[]{10011,36,2,3,36,21,11,0}});
		configs.add(new Object[]{12880,13359, new int[]{10005,35,3,4,35,20,10,0} ,new int[]{10010,37,2,3,37,22,12,0} ,new int[]{10002,36,2,3,36,21,11,0} ,new int[]{10016,37,2,4,37,22,12,0}});
		configs.add(new Object[]{13360,13839, new int[]{10001,35,3,4,35,20,10,0} ,new int[]{10002,37,2,4,37,22,12,0} ,new int[]{10017,36,2,4,36,21,11,0} ,new int[]{10005,37,3,3,37,22,12,0}});
		configs.add(new Object[]{13840,14319, new int[]{10013,36,2,4,36,21,11,0} ,new int[]{10009,38,2,4,38,23,13,0} ,new int[]{10004,37,2,4,37,22,12,0} ,new int[]{10016,38,2,4,38,23,13,0}});
		configs.add(new Object[]{14320,14799, new int[]{10013,36,2,4,36,21,11,0} ,new int[]{10014,38,2,4,38,23,13,0} ,new int[]{10002,37,2,4,37,22,12,0} ,new int[]{10001,38,3,4,38,23,13,0}});
		configs.add(new Object[]{14800,15279, new int[]{10001,37,3,4,37,22,12,0} ,new int[]{10009,39,2,4,39,24,14,0} ,new int[]{10004,38,2,4,38,23,13,0} ,new int[]{10016,39,2,4,39,24,14,0}});
		configs.add(new Object[]{15280,15759, new int[]{10012,38,3,4,38,23,13,0} ,new int[]{10002,40,3,4,40,25,15,0} ,new int[]{10009,39,3,4,39,24,14,0} ,new int[]{10001,40,3,4,40,25,15,0}});
		configs.add(new Object[]{15760,16239, new int[]{10000,38,3,4,38,23,13,0} ,new int[]{10010,40,3,4,40,25,15,0} ,new int[]{10009,39,3,4,39,24,14,0} ,new int[]{10015,40,3,4,40,25,15,0}});
		configs.add(new Object[]{16240,16719, new int[]{10011,39,3,4,39,24,14,0} ,new int[]{10014,41,3,4,41,26,16,0} ,new int[]{10010,40,3,4,40,25,15,0} ,new int[]{10003,41,3,4,41,26,16,0}});
		configs.add(new Object[]{16720,17199, new int[]{10012,39,3,4,39,24,14,0} ,new int[]{10014,41,3,4,41,26,16,0} ,new int[]{10010,40,3,4,40,25,15,0} ,new int[]{10011,41,3,4,41,26,16,0}});
		configs.add(new Object[]{17200,17679, new int[]{10011,40,3,4,40,25,15,0} ,new int[]{10014,41,3,4,41,26,16,0} ,new int[]{10002,41,3,4,41,26,16,0} ,new int[]{10005,42,3,4,42,27,17,0}});
		configs.add(new Object[]{17680,18159, new int[]{10015,40,3,4,40,25,15,0} ,new int[]{10014,41,3,4,41,26,16,0} ,new int[]{10009,41,3,4,41,26,16,0} ,new int[]{10001,42,3,4,42,27,17,0}});
		configs.add(new Object[]{18160,18639, new int[]{10001,41,3,4,41,26,16,0} ,new int[]{10004,42,3,4,42,27,17,0} ,new int[]{10014,42,3,4,42,27,17,0} ,new int[]{10003,43,3,4,43,28,18,0}});
		configs.add(new Object[]{18640,19119, new int[]{10005,41,3,4,41,26,16,0} ,new int[]{10017,42,3,4,42,27,17,0} ,new int[]{10014,42,3,4,42,27,17,0} ,new int[]{10011,43,3,4,43,28,18,0}});
		configs.add(new Object[]{19120,19599, new int[]{10015,42,3,5,42,27,17,0} ,new int[]{10004,43,3,5,43,28,18,0} ,new int[]{10011,43,3,5,43,28,18,0} ,new int[]{10000,44,3,5,44,29,19,0}});
		configs.add(new Object[]{19600,20119, new int[]{10001,42,3,5,42,27,17,0} ,new int[]{10004,43,3,5,43,28,18,0} ,new int[]{10014,43,3,5,43,28,18,0} ,new int[]{10003,44,3,5,44,29,19,0}});
		configs.add(new Object[]{20120,20639, new int[]{10003,43,3,5,43,28,18,0} ,new int[]{10014,44,3,5,44,29,19,0} ,new int[]{10009,44,3,5,44,29,19,0} ,new int[]{10013,45,3,5,45,30,20,0}});
		configs.add(new Object[]{20640,21159, new int[]{10003,43,3,5,43,28,18,0} ,new int[]{10014,44,3,5,44,29,19,0} ,new int[]{10002,44,3,5,44,29,19,0} ,new int[]{10011,45,3,5,45,30,20,0}});
		configs.add(new Object[]{21160,21679, new int[]{10015,44,3,5,44,29,19,0} ,new int[]{10004,45,3,5,45,30,20,0} ,new int[]{10014,45,3,5,45,30,20,0} ,new int[]{10013,46,3,5,46,31,21,0}});
		configs.add(new Object[]{21680,22199, new int[]{10011,44,3,5,44,29,19,0} ,new int[]{10004,45,3,5,45,30,20,0} ,new int[]{10014,45,3,6,45,30,20,0} ,new int[]{10013,46,3,5,46,31,21,0}});
		configs.add(new Object[]{22200,22719, new int[]{10005,45,3,5,45,25,15,0} ,new int[]{10010,46,3,5,46,26,16,0} ,new int[]{10009,46,3,5,46,26,16,0} ,new int[]{10001,47,3,5,47,27,17,0}});
		configs.add(new Object[]{22720,23239, new int[]{10016,45,3,5,45,25,15,0} ,new int[]{10010,46,3,5,46,26,16,0} ,new int[]{10014,46,3,5,46,26,16,0} ,new int[]{10011,47,3,5,47,27,17,0}});
		configs.add(new Object[]{23240,23759, new int[]{10005,46,3,5,46,26,16,0} ,new int[]{10002,47,3,5,47,27,17,0} ,new int[]{10009,47,3,5,47,27,17,0} ,new int[]{10001,48,3,5,48,28,18,0}});
		configs.add(new Object[]{23760,24279, new int[]{10011,47,3,5,47,27,17,0} ,new int[]{10014,48,3,5,48,28,18,0} ,new int[]{10002,48,3,5,48,28,18,0} ,new int[]{10005,49,3,5,49,29,19,0}});
		configs.add(new Object[]{24280,24799, new int[]{10001,47,3,5,47,27,17,0} ,new int[]{10009,48,3,5,48,28,18,0} ,new int[]{10002,48,3,5,48,28,18,0} ,new int[]{10016,49,3,5,49,29,19,0}});
		configs.add(new Object[]{24800,25319, new int[]{10013,48,3,5,48,28,18,0} ,new int[]{10010,49,3,5,49,29,19,0} ,new int[]{10004,49,3,5,49,29,19,0} ,new int[]{10005,50,3,5,50,30,20,0}});
		configs.add(new Object[]{25320,25839, new int[]{10016,48,3,5,48,28,18,0} ,new int[]{10002,49,3,5,49,29,19,0} ,new int[]{10004,49,3,5,49,29,19,0} ,new int[]{10005,50,3,5,50,30,20,0}});
		configs.add(new Object[]{25840,26359, new int[]{10001,49,3,5,49,29,19,0} ,new int[]{10010,50,3,5,50,30,20,0} ,new int[]{10017,50,3,5,50,30,20,0} ,new int[]{10016,51,3,5,51,31,21,0}});
		configs.add(new Object[]{26360,26879, new int[]{10013,50,3,6,50,30,10,5} ,new int[]{10009,50,3,6,50,30,10,5} ,new int[]{10004,51,3,6,51,31,11,6} ,new int[]{10005,52,3,5,52,32,12,7}});
		configs.add(new Object[]{26880,27399, new int[]{10015,51,3,6,51,31,11,6} ,new int[]{10009,51,3,6,51,31,11,6} ,new int[]{10017,52,3,6,52,32,12,7} ,new int[]{10016,53,3,5,53,33,13,8}});
		configs.add(new Object[]{27400,27919, new int[]{10001,51,3,6,51,31,11,6} ,new int[]{10014,52,3,6,52,32,12,7} ,new int[]{10002,52,3,6,52,32,12,7} ,new int[]{10016,53,3,6,53,33,13,8}});
		configs.add(new Object[]{27920,28439, new int[]{10000,52,4,6,52,32,12,7} ,new int[]{10002,53,4,6,53,33,13,8} ,new int[]{10009,53,4,6,53,33,13,8} ,new int[]{10001,54,4,6,54,34,14,9}});
		configs.add(new Object[]{28440,28959, new int[]{10011,53,4,6,53,33,13,8} ,new int[]{10014,54,4,6,54,34,14,9} ,new int[]{10017,54,4,6,54,34,14,9} ,new int[]{10003,55,4,6,55,35,15,10}});
		configs.add(new Object[]{28960,29479, new int[]{10016,53,4,6,53,33,13,8} ,new int[]{10014,54,4,6,54,34,14,9} ,new int[]{10010,54,4,6,54,34,14,9} ,new int[]{10003,55,4,6,55,35,15,10}});
		configs.add(new Object[]{29480,29999, new int[]{10011,54,4,6,54,34,14,9} ,new int[]{10014,55,4,6,55,35,15,10} ,new int[]{10002,55,4,6,55,35,15,10} ,new int[]{10001,56,4,6,56,36,16,11}});
		configs.add(new Object[]{30000,30559, new int[]{10001,55,4,6,55,35,15,10} ,new int[]{10017,56,4,6,56,36,16,11} ,new int[]{10014,56,4,6,56,36,16,11} ,new int[]{10003,57,4,6,57,37,17,12}});
		configs.add(new Object[]{30560,31119, new int[]{10016,55,4,6,55,35,15,10} ,new int[]{10004,56,4,6,56,36,16,11} ,new int[]{10014,56,4,6,56,36,16,11} ,new int[]{10015,57,4,6,57,37,17,12}});
		configs.add(new Object[]{31120,31679, new int[]{10001,56,4,6,56,36,16,11} ,new int[]{10002,57,4,6,57,37,17,12} ,new int[]{10011,57,4,6,57,37,17,12} ,new int[]{10003,58,4,6,58,38,18,13}});
		configs.add(new Object[]{31680,32239, new int[]{10003,57,4,6,57,37,17,12} ,new int[]{10014,58,4,6,58,38,18,13} ,new int[]{10009,58,4,6,58,38,18,13} ,new int[]{10011,59,4,6,59,39,19,14}});
		configs.add(new Object[]{32240,32799, new int[]{10003,58,4,6,58,38,18,13} ,new int[]{10014,59,4,6,59,39,19,14} ,new int[]{10017,59,4,6,59,39,19,14} ,new int[]{10011,60,4,6,60,40,20,15}});
		configs.add(new Object[]{32800,33359, new int[]{10001,59,4,7,59,39,19,14} ,new int[]{10004,60,4,6,60,40,20,15} ,new int[]{10014,60,4,6,60,40,20,15} ,new int[]{10003,61,4,6,61,41,21,16}});
		configs.add(new Object[]{33360,33919, new int[]{10015,60,4,6,60,40,20,15} ,new int[]{10017,61,4,7,61,41,21,16} ,new int[]{10014,61,4,7,61,41,21,16} ,new int[]{10000,62,4,6,62,42,22,17}});
		configs.add(new Object[]{33920,34479, new int[]{10011,61,4,6,61,41,21,11} ,new int[]{10004,62,4,7,62,42,22,12} ,new int[]{10009,62,4,6,62,42,22,12} ,new int[]{10003,63,4,6,63,43,23,13}});
		configs.add(new Object[]{34480,35039, new int[]{10012,61,4,6,61,41,21,11} ,new int[]{10002,62,4,6,62,42,22,12} ,new int[]{10014,62,4,7,62,42,22,12} ,new int[]{10011,63,4,7,63,43,23,13}});
		configs.add(new Object[]{35040,35599, new int[]{10005,62,4,7,62,42,22,12} ,new int[]{10017,63,4,7,63,43,23,13} ,new int[]{10014,63,4,7,63,43,23,13} ,new int[]{10003,64,4,7,64,44,24,14}});
		configs.add(new Object[]{35600,36159, new int[]{10011,63,4,7,63,43,23,13} ,new int[]{10004,64,4,7,64,44,24,14} ,new int[]{10014,64,4,7,64,44,24,14} ,new int[]{10001,65,4,7,65,45,25,15}});
		configs.add(new Object[]{36160,36719, new int[]{10012,63,4,7,63,43,23,13} ,new int[]{10004,64,4,7,64,44,24,14} ,new int[]{10014,64,4,7,64,44,24,14} ,new int[]{10013,65,4,7,65,45,25,15}});
		configs.add(new Object[]{36720,37279, new int[]{10013,64,4,7,64,44,24,14} ,new int[]{10010,65,4,7,65,45,25,15} ,new int[]{10009,65,4,7,65,45,25,15} ,new int[]{10001,66,4,7,66,46,26,16}});
		configs.add(new Object[]{37280,37839, new int[]{10012,65,4,7,65,45,25,15} ,new int[]{10014,65,4,7,65,45,25,15} ,new int[]{10002,66,4,7,66,46,26,16} ,new int[]{10015,67,4,7,67,47,27,17}});
		configs.add(new Object[]{37840,38399, new int[]{10011,65,4,7,65,45,25,10} ,new int[]{10004,66,4,7,66,46,26,11} ,new int[]{10009,66,4,7,66,46,26,11} ,new int[]{10001,67,4,7,67,47,27,12}});
		configs.add(new Object[]{38400,38959, new int[]{10000,66,5,7,66,46,26,11} ,new int[]{10014,67,5,7,67,47,27,12} ,new int[]{10002,67,5,7,67,47,27,12} ,new int[]{10003,68,5,7,68,48,28,13}});
		configs.add(new Object[]{38960,39519, new int[]{10001,67,5,7,67,47,27,12} ,new int[]{10010,68,5,7,68,48,28,13} ,new int[]{10004,68,5,7,68,48,28,13} ,new int[]{10005,69,5,7,69,49,29,14}});
		configs.add(new Object[]{39520,40079, new int[]{10005,68,5,7,68,48,28,13} ,new int[]{10002,69,5,7,69,49,29,14} ,new int[]{10014,69,5,7,69,49,29,14} ,new int[]{10016,70,5,7,70,50,30,15}});
		configs.add(new Object[]{40080,40679, new int[]{10001,68,5,7,68,48,28,13} ,new int[]{10017,69,5,7,69,49,29,14} ,new int[]{10004,69,5,7,69,49,29,14} ,new int[]{10003,70,5,7,70,50,30,15}});
		configs.add(new Object[]{40680,41279, new int[]{10013,69,5,7,69,49,29,14} ,new int[]{10009,70,5,7,70,50,30,15} ,new int[]{10017,70,5,7,70,50,30,15} ,new int[]{10005,71,5,7,71,51,31,16}});
		configs.add(new Object[]{41280,41879, new int[]{10012,70,5,7,70,50,30,10} ,new int[]{10014,70,5,7,70,50,30,10} ,new int[]{10002,71,5,7,71,51,31,11} ,new int[]{10016,71,5,7,71,51,31,11}});
		configs.add(new Object[]{41880,42479, new int[]{10001,70,5,7,70,50,30,10} ,new int[]{10009,71,5,7,71,51,31,11} ,new int[]{10004,71,5,7,71,51,31,11} ,new int[]{10013,72,5,7,72,52,32,12}});
		configs.add(new Object[]{42480,43079, new int[]{10000,71,5,7,71,51,31,11} ,new int[]{10002,72,5,7,72,52,32,12} ,new int[]{10017,72,5,7,72,52,32,12} ,new int[]{10001,73,5,8,73,53,33,13}});
		configs.add(new Object[]{43080,43679, new int[]{10012,72,5,7,72,52,32,12} ,new int[]{10014,73,5,7,73,53,33,13} ,new int[]{10010,73,5,8,73,53,33,13} ,new int[]{10003,74,5,7,74,54,34,14}});
		configs.add(new Object[]{43680,44279, new int[]{10011,72,5,7,72,52,32,12} ,new int[]{10009,73,5,8,73,53,33,13} ,new int[]{10004,73,5,7,73,53,33,13} ,new int[]{10013,74,5,7,74,54,34,14}});
		configs.add(new Object[]{44280,44879, new int[]{10013,73,5,8,73,53,33,13} ,new int[]{10014,74,5,7,74,54,34,14} ,new int[]{10002,74,5,7,74,54,34,14} ,new int[]{10001,75,5,7,75,55,35,15}});
		configs.add(new Object[]{44880,45479, new int[]{10001,74,5,8,74,54,34,14} ,new int[]{10002,75,5,8,75,55,35,15} ,new int[]{10014,75,5,8,75,55,35,15} ,new int[]{10012,76,5,8,76,56,36,16}});
		configs.add(new Object[]{45480,46079, new int[]{10001,75,5,8,75,55,35,15} ,new int[]{10004,76,5,8,76,56,36,16} ,new int[]{10011,76,5,8,76,56,36,16} ,new int[]{10003,77,5,8,77,57,37,17}});
		configs.add(new Object[]{46080,46879, new int[]{10003,76,5,8,76,56,36,16} ,new int[]{10014,77,5,8,77,57,37,17} ,new int[]{10004,77,5,8,77,57,37,17} ,new int[]{10016,78,5,8,78,58,38,18}});
		configs.add(new Object[]{46880,47679, new int[]{10012,77,5,8,77,57,37,17} ,new int[]{10009,78,5,8,78,58,38,18} ,new int[]{10017,78,5,8,78,58,38,18} ,new int[]{10011,79,5,8,79,59,39,19}});
		configs.add(new Object[]{47680,48479, new int[]{10001,78,5,8,78,58,38,18} ,new int[]{10004,79,5,8,79,59,39,19} ,new int[]{10014,79,5,8,79,59,39,19} ,new int[]{10003,80,5,8,80,60,40,20}});
		configs.add(new Object[]{48480,49279, new int[]{10011,79,5,8,79,59,39,19} ,new int[]{10014,79,5,8,79,59,39,19} ,new int[]{10002,80,5,8,80,60,40,20} ,new int[]{10001,80,5,8,80,60,40,20}});
		configs.add(new Object[]{49280,50079, new int[]{10005,80,5,8,80,60,40,20} ,new int[]{10002,80,5,8,80,60,40,20} ,new int[]{10014,80,5,8,80,60,40,20} ,new int[]{10003,80,5,8,80,60,40,20}});
		configs.add(new Object[]{50080,50879, new int[]{10014,80,5,8,80,60,40,20} ,new int[]{10004,80,5,8,80,60,40,20} ,new int[]{10011,80,5,8,80,60,40,20} ,new int[]{10005,80,5,8,80,60,40,20}});
		configs.add(new Object[]{50880,75000, new int[]{10001,80,5,8,80,60,40,20} ,new int[]{10009,80,5,8,80,60,40,20} ,new int[]{10002,80,5,8,80,60,40,20} ,new int[]{10003,80,5,8,80,60,40,20}});

		List<Hero> heros = new ArrayList<Hero>();
		for (Object[] object : configs) {
			int min = (int)object[0];
			int max = (int)object[1];

			if (power >= min && power <= max) {
				heros.add(this.toHero((int[])object[2]));
				heros.add(this.toHero((int[])object[3]));
				heros.add(this.toHero((int[])object[4]));
				heros.add(this.toHero((int[])object[5]));

				return heros;
			}
		}

		if (heros.size() == 0) {

			Object[] config = configs.get(configs.size() - 1);

			heros.add(this.toHero((int[])config[2]));
			heros.add(this.toHero((int[])config[3]));
			heros.add(this.toHero((int[])config[4]));
			heros.add(this.toHero((int[])config[5]));
		}
        return heros;
	}

	private Hero toHero(int[] arr) {
		
		int level = arr[1];

		Hero hero = new Hero();
		hero.setHeroId(arr[0]);
		hero.setExp(Hero.EXP[level - 1] - 1);
		hero.setLevel(level);
		hero.setStar(arr[2]);
		hero.setCLASS(arr[3]);
		hero.setStr(arr[1]);
		hero.setDex(arr[1]);
		hero.setINT(arr[1]);
		hero.setSkill1Level(arr[4]);
		hero.setSkill2Level(arr[5]);
		hero.setSkill3Level(arr[6]);
		hero.setSkill4Level(arr[7]);
		hero.setEquip1(true);
		hero.setEquip2(true);
		hero.setEquip3(true);
		hero.setEquip4(true);
		hero.setEquip5(true);
		hero.setEquip6(true);
		return hero;
	}

	private Power getRolePower(int roleId, int serverId, int myPower, int index) {

		PowerDao powerDao = (PowerDao) SpringContextUtils.getBean(PowerDao.class);

		int min = 0;
		int max = 0;

		if (myPower <= 14000) {
			switch (index) {
			case 1:
				min = myPower * 64; max = myPower * 67; break;
			case 2:
				min = myPower * 68; max = myPower * 71; break;
			case 3:
				min = myPower * 72; max = myPower * 75; break;
			case 4:
				min = myPower * 76; max = myPower * 79; break;
			case 5:
				min = myPower * 80; max = myPower * 83; break;
			case 6:
				min = myPower * 84; max = myPower * 87; break;
			case 7:
				min = myPower * 88; max = myPower * 91; break;
			case 8:
				min = myPower * 92; max = myPower * 95; break;
			case 9:
				min = myPower * 96; max = myPower * 99; break;
			case 10:
				min = myPower * 100; max = myPower * 103; break;
			case 11:
				min = myPower * 104; max = myPower * 107; break;
			case 12:
				min = myPower * 108; max = myPower * 111; break;
			case 13:
				min = myPower * 112; max = myPower * 115; break;
			case 14:
				min = myPower * 116; max = myPower * 117; break;
			default:
				min = myPower * 118; max = myPower * 122; break;
			}
		} else if (myPower <= 22000) {
			switch (index) {
			case 1:
				min = myPower * 76; max = myPower * 78; break;
			case 2:
				min = myPower * 79; max = myPower * 81; break;
			case 3:
				min = myPower * 82; max = myPower * 84; break;
			case 4:
				min = myPower * 85; max = myPower * 87; break;
			case 5:
				min = myPower * 88; max = myPower * 90; break;
			case 6:
				min = myPower * 91; max = myPower * 93; break;
			case 7:
				min = myPower * 94; max = myPower * 96; break;
			case 8:
				min = myPower * 97; max = myPower * 99; break;
			case 9:
				min = myPower * 100; max = myPower * 102; break;
			case 10:
				min = myPower * 103; max = myPower * 105; break;
			case 11:
				min = myPower * 106; max = myPower * 108; break;
			case 12:
				min = myPower * 109; max = myPower * 111; break;
			case 13:
				min = myPower * 112; max = myPower * 115; break;
			case 14:
				min = myPower * 116; max = myPower * 119; break;
			default:
				min = myPower * 120; max = myPower * 126; break;
			}
		} else {
			switch (index) {
			case 1:
				min = myPower * 82; max = myPower * 84; break;
			case 2:
				min = myPower * 85; max = myPower * 87; break;
			case 3:
				min = myPower * 88; max = myPower * 90; break;
			case 4:
				min = myPower * 91; max = myPower * 93; break;
			case 5:
				min = myPower * 94; max = myPower * 96; break;
			case 6:
				min = myPower * 97; max = myPower * 99; break;
			case 7:
				min = myPower * 100; max = myPower * 102; break;
			case 8:
				min = myPower * 103; max = myPower * 105; break;
			case 9:
				min = myPower * 106; max = myPower * 108; break;
			case 10:
				min = myPower * 109; max = myPower * 111; break;
			case 11:
				min = myPower * 112; max = myPower * 114; break;
			case 12:
				min = myPower * 115; max = myPower * 117; break;
			case 13:
				min = myPower * 118; max = myPower * 120; break;
			case 14:
				min = myPower * 121; max = myPower * 123; break;
			default:
				min = myPower * 124; max = myPower * 134; break;
			}
		}

		return powerDao.getOneByPower(roleId, serverId, min / 100, max / 100);
	}
}
