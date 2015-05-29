package com.zhanglong.sg.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.web.context.ContextLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.dao.RoleDao;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Power;
import com.zhanglong.sg.service.RoleService;

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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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

		RoleDao roleDao = ContextLoader.getCurrentWebApplicationContext().getBean(RoleDao.class);
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

		ArrayList<Object[]> configs = new ArrayList<Object[]>();
		configs.add(new Object[]{1,4279, new int[]{10002,20,1,1} ,new int[]{10009,20,1,1} ,new int[]{10010,20,1,1} ,new int[]{10012,20,1,1}});
		configs.add(new Object[]{4280,4559, new int[]{10011,21,1,2} ,new int[]{10009,21,1,2} ,new int[]{10002,21,1,1} ,new int[]{10012,21,1,1}});
		configs.add(new Object[]{4560,4839, new int[]{10012,22,2,2} ,new int[]{10014,22,3,2} ,new int[]{10010,22,1,2} ,new int[]{10000,22,1,2}});
		configs.add(new Object[]{4840,5119, new int[]{10000,23,2,2} ,new int[]{10002,23,1,2} ,new int[]{10014,23,2,2} ,new int[]{10011,23,1,2}});
		configs.add(new Object[]{5120,5399, new int[]{10011,24,1,2} ,new int[]{10017,24,2,3} ,new int[]{10014,24,2,2} ,new int[]{10000,24,2,2}});
		configs.add(new Object[]{5400,5679, new int[]{10002,25,1,1} ,new int[]{10009,25,2,1} ,new int[]{10011,25,1,2} ,new int[]{10012,25,1,2}});
		configs.add(new Object[]{5680,5999, new int[]{10010,25,2,2} ,new int[]{10014,26,3,2} ,new int[]{10012,25,1,2} ,new int[]{10016,26,2,2}});
		configs.add(new Object[]{6000,6399, new int[]{10000,26,2,2} ,new int[]{10014,27,3,2} ,new int[]{10002,26,1,2} ,new int[]{10016,27,1,2}});
		configs.add(new Object[]{6400,6799, new int[]{10000,26,2,2} ,new int[]{10002,28,1,2} ,new int[]{10014,27,3,2} ,new int[]{10001,28,3,2}});
		configs.add(new Object[]{6800,7199, new int[]{10001,27,3,2} ,new int[]{10017,29,2,3} ,new int[]{10014,28,3,2} ,new int[]{10000,29,2,2}});
		configs.add(new Object[]{7200,7599, new int[]{10016,27,2,3} ,new int[]{10017,29,2,2} ,new int[]{10014,28,3,3} ,new int[]{10012,29,2,3}});
		configs.add(new Object[]{7600,7999, new int[]{10011,28,1,3} ,new int[]{10002,30,2,3} ,new int[]{10009,29,2,3} ,new int[]{10003,30,2,2}});
		configs.add(new Object[]{8000,8399, new int[]{10011,28,1,3} ,new int[]{10004,30,2,3} ,new int[]{10009,29,2,3} ,new int[]{10003,30,2,3}});
		configs.add(new Object[]{8400,8799, new int[]{10005,29,3,3} ,new int[]{10017,31,2,3} ,new int[]{10014,30,3,3} ,new int[]{10003,31,2,3}});
		configs.add(new Object[]{8800,9199, new int[]{10011,30,2,3} ,new int[]{10004,32,2,3} ,new int[]{10014,31,3,3} ,new int[]{10013,32,2,3}});
		configs.add(new Object[]{9200,9599, new int[]{10011,30,2,3} ,new int[]{10004,32,2,3} ,new int[]{10014,31,3,3} ,new int[]{10013,32,2,3}});
		configs.add(new Object[]{9600,9999, new int[]{10005,31,3,3} ,new int[]{10010,33,2,3} ,new int[]{10009,32,2,3} ,new int[]{10001,33,3,3}});
		configs.add(new Object[]{10000,10399, new int[]{10005,31,3,3} ,new int[]{10010,33,2,3} ,new int[]{10014,32,3,3} ,new int[]{10016,33,2,3}});
		configs.add(new Object[]{10400,10799, new int[]{10016,32,2,3} ,new int[]{10004,34,2,3} ,new int[]{10009,33,2,3} ,new int[]{10001,34,3,3}});
		configs.add(new Object[]{10800,11199, new int[]{10005,32,3,3} ,new int[]{10004,34,2,3} ,new int[]{10009,33,2,3} ,new int[]{10015,34,2,3}});
		configs.add(new Object[]{11200,11599, new int[]{10001,33,3,3} ,new int[]{10014,35,3,3} ,new int[]{10002,34,2,3} ,new int[]{10005,35,3,3}});
		configs.add(new Object[]{11600,11999, new int[]{10015,33,2,3} ,new int[]{10014,35,3,3} ,new int[]{10002,34,2,3} ,new int[]{10013,35,2,3}});
		configs.add(new Object[]{12000,12399, new int[]{10001,34,3,3} ,new int[]{10010,36,2,3} ,new int[]{10004,35,2,4} ,new int[]{10005,36,3,3}});
		configs.add(new Object[]{12400,12639, new int[]{10016,34,2,3} ,new int[]{10010,36,2,4} ,new int[]{10002,35,2,3} ,new int[]{10011,36,2,3}});
		configs.add(new Object[]{12640,12879, new int[]{10005,35,3,4} ,new int[]{10010,37,2,3} ,new int[]{10004,36,2,3} ,new int[]{10016,37,2,4}});
		configs.add(new Object[]{12880,13119, new int[]{10001,35,3,4} ,new int[]{10002,37,2,4} ,new int[]{10004,36,2,4} ,new int[]{10005,37,3,3}});
		configs.add(new Object[]{13120,13359, new int[]{10013,36,2,4} ,new int[]{10009,38,2,4} ,new int[]{10004,37,2,4} ,new int[]{10016,38,2,4}});
		configs.add(new Object[]{13360,13599, new int[]{10013,36,2,4} ,new int[]{10009,38,2,4} ,new int[]{10002,37,2,4} ,new int[]{10001,38,3,4}});
		configs.add(new Object[]{13600,13839, new int[]{10001,37,3,4} ,new int[]{10009,39,2,4} ,new int[]{10002,38,2,4} ,new int[]{10016,39,2,4}});
		configs.add(new Object[]{13840,14079, new int[]{10000,38,3,4} ,new int[]{10002,40,3,4} ,new int[]{10009,39,3,4} ,new int[]{10001,40,3,4}});
		configs.add(new Object[]{14080,14319, new int[]{10000,38,3,4} ,new int[]{10010,40,3,4} ,new int[]{10009,39,3,4} ,new int[]{10015,40,3,4}});
		configs.add(new Object[]{14320,14559, new int[]{10011,39,3,4} ,new int[]{10014,41,3,4} ,new int[]{10010,40,3,4} ,new int[]{10003,41,3,4}});
		configs.add(new Object[]{14560,14799, new int[]{10012,39,3,4} ,new int[]{10014,41,3,4} ,new int[]{10010,40,3,4} ,new int[]{10003,41,3,4}});
		configs.add(new Object[]{14800,15039, new int[]{10011,40,3,4} ,new int[]{10014,42,3,4} ,new int[]{10002,41,3,4} ,new int[]{10001,42,3,4}});
		configs.add(new Object[]{15040,15279, new int[]{10011,40,3,4} ,new int[]{10014,42,3,4} ,new int[]{10009,41,3,4} ,new int[]{10001,42,3,4}});
		configs.add(new Object[]{15280,15519, new int[]{10001,41,3,4} ,new int[]{10004,43,3,4} ,new int[]{10014,42,3,4} ,new int[]{10003,43,3,4}});
		configs.add(new Object[]{15520,15759, new int[]{10005,41,3,4} ,new int[]{10017,43,3,4} ,new int[]{10014,42,3,4} ,new int[]{10011,43,3,4}});
		configs.add(new Object[]{15760,15999, new int[]{10015,42,3,5} ,new int[]{10004,44,3,5} ,new int[]{10011,43,3,5} ,new int[]{10000,44,3,5}});
		configs.add(new Object[]{16000,16239, new int[]{10001,42,3,5} ,new int[]{10004,44,3,5} ,new int[]{10014,43,3,5} ,new int[]{10003,44,3,5}});
		configs.add(new Object[]{16240,16479, new int[]{10003,43,3,5} ,new int[]{10014,45,3,5} ,new int[]{10009,44,3,5} ,new int[]{10011,45,3,5}});
		configs.add(new Object[]{16480,16719, new int[]{10003,43,3,5} ,new int[]{10014,45,3,5} ,new int[]{10002,44,3,5} ,new int[]{10011,45,3,5}});
		configs.add(new Object[]{16720,16959, new int[]{10011,44,3,5} ,new int[]{10004,46,3,5} ,new int[]{10014,45,3,5} ,new int[]{10013,46,3,5}});
		configs.add(new Object[]{16960,17199, new int[]{10011,44,3,5} ,new int[]{10004,46,3,5} ,new int[]{10009,45,3,6} ,new int[]{10013,46,3,5}});
		configs.add(new Object[]{17200,17439, new int[]{10005,45,3,6} ,new int[]{10010,47,3,5} ,new int[]{10009,46,3,5} ,new int[]{10001,47,3,5}});
		configs.add(new Object[]{17440,17679, new int[]{10016,45,3,5} ,new int[]{10010,47,3,6} ,new int[]{10009,46,3,5} ,new int[]{10011,47,3,5}});
		configs.add(new Object[]{17680,17919, new int[]{10005,46,3,5} ,new int[]{10002,48,3,5} ,new int[]{10009,47,3,5} ,new int[]{10001,48,3,6}});
		configs.add(new Object[]{17920,18159, new int[]{10011,47,3,6} ,new int[]{10014,49,3,6} ,new int[]{10002,48,3,6} ,new int[]{10005,49,3,6}});
		configs.add(new Object[]{18160,18399, new int[]{10001,47,3,6} ,new int[]{10014,49,3,6} ,new int[]{10002,48,3,6} ,new int[]{10016,49,3,6}});
		configs.add(new Object[]{18400,18639, new int[]{10016,48,3,6} ,new int[]{10010,50,3,6} ,new int[]{10004,49,3,6} ,new int[]{10005,50,3,6}});
		configs.add(new Object[]{18640,18879, new int[]{10016,48,3,6} ,new int[]{10002,50,3,6} ,new int[]{10004,49,3,6} ,new int[]{10005,50,3,6}});
		configs.add(new Object[]{18880,19119, new int[]{10001,49,3,6} ,new int[]{10010,51,3,6} ,new int[]{10017,50,3,6} ,new int[]{10016,51,3,6}});
		configs.add(new Object[]{19120,19359, new int[]{10013,50,3,6} ,new int[]{10009,52,3,6} ,new int[]{10004,51,3,6} ,new int[]{10005,52,3,6}});
		configs.add(new Object[]{19360,19599, new int[]{10001,51,3,6} ,new int[]{10009,53,3,6} ,new int[]{10002,52,3,6} ,new int[]{10016,53,3,6}});
		configs.add(new Object[]{19600,19839, new int[]{10001,51,3,6} ,new int[]{10014,53,3,6} ,new int[]{10002,52,3,6} ,new int[]{10016,53,3,6}});
		configs.add(new Object[]{19840,20079, new int[]{10000,52,4,6} ,new int[]{10002,54,4,6} ,new int[]{10009,53,4,6} ,new int[]{10001,54,4,6}});
		configs.add(new Object[]{20080,20319, new int[]{10011,53,4,6} ,new int[]{10014,55,4,6} ,new int[]{10010,54,4,6} ,new int[]{10003,55,4,6}});
		configs.add(new Object[]{20320,20559, new int[]{10016,53,4,6} ,new int[]{10014,55,4,6} ,new int[]{10010,54,4,6} ,new int[]{10003,55,4,6}});
		configs.add(new Object[]{20560,20799, new int[]{10011,54,4,6} ,new int[]{10014,56,4,6} ,new int[]{10002,55,4,6} ,new int[]{10001,56,4,6}});
		configs.add(new Object[]{20800,21039, new int[]{10001,55,4,6} ,new int[]{10004,57,4,6} ,new int[]{10014,56,4,6} ,new int[]{10003,57,4,6}});
		configs.add(new Object[]{21040,21279, new int[]{10016,55,4,6} ,new int[]{10004,57,4,6} ,new int[]{10014,56,4,6} ,new int[]{10015,57,4,6}});
		configs.add(new Object[]{21280,21519, new int[]{10001,56,4,6} ,new int[]{10002,58,4,6} ,new int[]{10011,57,4,6} ,new int[]{10003,58,4,6}});
		configs.add(new Object[]{21520,21759, new int[]{10003,57,4,6} ,new int[]{10014,59,4,6} ,new int[]{10009,58,4,6} ,new int[]{10011,59,4,6}});
		configs.add(new Object[]{21760,21999, new int[]{10003,58,4,6} ,new int[]{10014,60,4,6} ,new int[]{10017,59,4,6} ,new int[]{10011,60,4,6}});
		configs.add(new Object[]{22000,22239, new int[]{10001,59,4,7} ,new int[]{10004,61,4,6} ,new int[]{10014,60,4,6} ,new int[]{10003,61,4,6}});
		configs.add(new Object[]{22240,22559, new int[]{10015,60,4,6} ,new int[]{10017,62,4,7} ,new int[]{10014,61,4,7} ,new int[]{10000,62,4,6}});
		configs.add(new Object[]{22560,22879, new int[]{10011,61,4,6} ,new int[]{10004,63,4,7} ,new int[]{10009,62,4,6} ,new int[]{10003,63,4,6}});
		configs.add(new Object[]{22880,23199, new int[]{10012,61,4,6} ,new int[]{10004,63,4,6} ,new int[]{10014,62,4,7} ,new int[]{10011,63,4,7}});
		configs.add(new Object[]{23200,23519, new int[]{10005,62,4,7} ,new int[]{10017,64,4,7} ,new int[]{10014,63,4,7} ,new int[]{10003,64,4,7}});
		configs.add(new Object[]{23520,23839, new int[]{10011,63,4,7} ,new int[]{10004,65,4,7} ,new int[]{10014,64,4,7} ,new int[]{10013,65,4,7}});
		configs.add(new Object[]{23840,24159, new int[]{10012,63,4,7} ,new int[]{10004,65,4,7} ,new int[]{10014,64,4,7} ,new int[]{10013,65,4,7}});
		configs.add(new Object[]{24160,24479, new int[]{10013,64,4,7} ,new int[]{10010,66,4,7} ,new int[]{10009,65,4,7} ,new int[]{10001,66,4,7}});
		configs.add(new Object[]{24480,24799, new int[]{10005,65,4,7} ,new int[]{10004,66,4,7} ,new int[]{10002,66,4,7} ,new int[]{10015,67,4,7}});
		configs.add(new Object[]{24800,25119, new int[]{10012,65,4,7} ,new int[]{10004,67,4,7} ,new int[]{10009,66,4,7} ,new int[]{10001,67,4,7}});
		configs.add(new Object[]{25120,25439, new int[]{10001,66,5,7} ,new int[]{10014,68,5,7} ,new int[]{10002,67,5,7} ,new int[]{10003,68,5,7}});
		configs.add(new Object[]{25440,25759, new int[]{10001,67,5,7} ,new int[]{10010,69,5,7} ,new int[]{10004,68,5,7} ,new int[]{10005,69,5,7}});
		configs.add(new Object[]{25760,26079, new int[]{10005,68,5,7} ,new int[]{10002,70,5,7} ,new int[]{10014,69,5,7} ,new int[]{10016,70,5,7}});
		configs.add(new Object[]{26080,26399, new int[]{10001,68,5,7} ,new int[]{10002,70,5,7} ,new int[]{10004,69,5,7} ,new int[]{10003,70,5,7}});
		configs.add(new Object[]{26400,26719, new int[]{10013,69,5,7} ,new int[]{10009,71,5,7} ,new int[]{10017,70,5,7} ,new int[]{10005,71,5,7}});
		configs.add(new Object[]{26720,27039, new int[]{10001,70,5,7} ,new int[]{10014,71,5,7} ,new int[]{10002,71,5,7} ,new int[]{10016,71,5,7}});
		configs.add(new Object[]{27040,27839, new int[]{10001,70,5,7} ,new int[]{10009,72,5,7} ,new int[]{10002,71,5,7} ,new int[]{10016,72,5,7}});
		configs.add(new Object[]{27840,28639, new int[]{10000,71,5,7} ,new int[]{10002,73,5,7} ,new int[]{10017,72,5,7} ,new int[]{10001,73,5,8}});
		configs.add(new Object[]{28640,29439, new int[]{10011,72,5,7} ,new int[]{10014,74,5,7} ,new int[]{10010,73,5,8} ,new int[]{10003,74,5,7}});
		configs.add(new Object[]{29440,30239, new int[]{10011,72,5,7} ,new int[]{10009,74,5,8} ,new int[]{10004,73,5,7} ,new int[]{10003,74,5,7}});
		configs.add(new Object[]{30240,31039, new int[]{10013,73,5,8} ,new int[]{10014,75,5,7} ,new int[]{10002,74,5,7} ,new int[]{10001,75,5,7}});
		configs.add(new Object[]{31040,31839, new int[]{10001,74,5,8} ,new int[]{10002,76,5,8} ,new int[]{10014,75,5,8} ,new int[]{10012,76,5,8}});
		configs.add(new Object[]{31840,32639, new int[]{10001,75,5,8} ,new int[]{10004,77,5,8} ,new int[]{10011,76,5,8} ,new int[]{10003,77,5,8}});
		configs.add(new Object[]{32640,34239, new int[]{10003,76,5,8} ,new int[]{10014,78,5,8} ,new int[]{10009,77,5,8} ,new int[]{10011,78,5,8}});
		configs.add(new Object[]{34240,35999, new int[]{10003,77,5,8} ,new int[]{10014,79,5,8} ,new int[]{10017,78,5,8} ,new int[]{10011,79,5,8}});
		configs.add(new Object[]{36000,37999, new int[]{10001,78,5,8} ,new int[]{10004,80,5,8} ,new int[]{10014,79,5,8} ,new int[]{10003,80,5,8}});
		configs.add(new Object[]{38000,39999, new int[]{10011,79,5,8} ,new int[]{10014,80,5,8} ,new int[]{10002,80,5,8} ,new int[]{10001,80,5,8}});
		configs.add(new Object[]{40000,43999, new int[]{10005,80,5,8} ,new int[]{10002,80,5,8} ,new int[]{10014,80,5,8} ,new int[]{10003,80,5,8}});
		configs.add(new Object[]{44000,47999, new int[]{10001,80,5,8} ,new int[]{10004,80,5,8} ,new int[]{10011,80,5,8} ,new int[]{10005,80,5,8}});
		configs.add(new Object[]{48000,65000, new int[]{10001,80,5,8} ,new int[]{10004,80,5,8} ,new int[]{10002,80,5,8} ,new int[]{10003,80,5,8}});

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
		hero.setSkill1Level(1);
		hero.setSkill2Level(1);
		hero.setSkill3Level(1);
		hero.setSkill4Level(0);
		hero.setEquip1(true);
		hero.setEquip2(true);
		hero.setEquip3(true);
		hero.setEquip4(true);
		hero.setEquip5(true);
		hero.setEquip6(true);
		return hero;
	}

	private Power getRolePower(int roleId, int serverId, int myPower, int index) {

		PowerDao powerDao = ContextLoader.getCurrentWebApplicationContext().getBean(PowerDao.class);

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
