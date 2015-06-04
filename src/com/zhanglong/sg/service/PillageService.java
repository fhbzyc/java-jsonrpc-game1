package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.BattleLogDao;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.entity.BattleLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.model.PlayerModel;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;

@Service
public class PillageService extends BaseService {

	@Resource
	private PowerDao powerDao;

	@Resource
	private BattleLogDao battleLogDao;
	
	/**
	 * 可掠夺列表
	 * @return
	 * @throws Exception
	 */
	public Object list(int itemId) throws Exception {

		int roleId = this.roleId();

        int serverId = this.serverId();

		Role me = this.roleDao.findOne(roleId);

		if (me.level() < 25) {
			return this.returnError(this.lineNum(), "掠夺功能25级后开放");
		}
		
		List<Role> roles = this.roleDao.get10player(roleId, Integer.valueOf(serverId), me.level(), itemId);

		if (roles.size() < 10) {
			List<Role> roles2 = this.roleDao.get100player(roleId, Integer.valueOf(serverId), me.level());
			for (Role role2 : roles2) {
				boolean find = false;
				for (Role role : roles) {
					if (role.getRoleId().equals(role2.getRoleId())) {
						find = true;
					}
				}
				if (!find) {
					roles.add(role2);
					if (roles.size() >= 10) {
						break;
					}
				}
			}
		}

		List<Integer> roleIds = new ArrayList<Integer>();
		for (Role role : roles) {
			roleIds.add(role.getRoleId());
		}

		List<PlayerModel> players = this.roleDao.getPlayers(roles);

		if (players.size() < 10) {
			this.players(players, me.level());
		}

		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		
		Result result = new Result();
		result.setValue("players", players);
		result.setValue("num", me.pillageNum);
		result.setValue("buy_num", this.buyNum(me.vip, dateNumModel.getPillageBuyNum()));
		result.setValue("gold", this.gold(dateNumModel.getPillageBuyNum()));

		return this.success(result.toMap());
	}

	/**
	 * 
	 * @param playerId
	 * @param heroId1
	 * @param heroId2
	 * @param heroId3
	 * @param heroId4
	 * @param power
	 * @return
	 * @throws Exception
	 */
	public Object BattleBegin(int playerId, int heroId1, int heroId2, int heroId3, int heroId4, int power) throws Exception {

        int roleId = this.roleId();

        List<Hero> heros = this.heroDao.findAll(roleId);
        List<Hero> hero2 = new ArrayList<Hero>();

        boolean find1 = false;
        boolean find2 = false;
        boolean find3 = false;
        boolean find4 = false;
        for (Hero hero : heros) {
            if (heroId1 != 0 && (int)hero.getHeroId() == heroId1) {
                find1 = true;
                hero2.add(hero);
            }
            if (heroId2 != 0 && (int)hero.getHeroId() == heroId2) {
                find2 = true;
                hero2.add(hero);
            }
            if (heroId3 != 0 && (int)hero.getHeroId() == heroId3) {
                find3 = true;
                hero2.add(hero);
            }
            if (heroId4 != 0 && (int)hero.getHeroId() == heroId4) {
                find4 = true;
                hero2.add(hero);
            }
        }

        if (heroId1 != 0 && !find1 || heroId2 != 0 && !find2 || heroId3 != 0 && !find3 || heroId4 != 0 && !find4) {
            return this.returnError(this.lineNum(), "未拥有的武将");
        }

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        if (role.pillageNum <= 0) {
        	return this.returnError(this.lineNum(), "掠夺次数不足");
        } else {
        	role.pillageNum -= 1;
        	this.roleDao.update(role, result);
        }

        this.powerDao.save(role, power, hero2);

        BattleLog battleLog = new BattleLog();
        battleLog.setRoleId(roleId);
        battleLog.setHeroId1(heroId1);
        battleLog.setHeroId2(heroId2);
        battleLog.setHeroId3(heroId3);
        battleLog.setHeroId4(heroId4);
        battleLog.setStoryType(5);
        battleLog.setStoryId(0);
        battleLog.setRoleId2(playerId);

        battleLog = this.battleLogDao.create(battleLog);

        result.setValue("battle_id", battleLog.getId());
        result.setValue("num", role.pillageNum);
        return this.success(result.toMap());
	}

	public Object battleEnd(int battleId, boolean win, int itemId) throws Exception {

        int roleId = this.roleId();

        BattleLog battleLog = this.battleLogDao.findOne(battleId);
        if (battleLog == null || battleLog.getStoryType() != 5) {
            return this.returnError(this.lineNum(), "参数出错");
        }

        Result result = new Result();

        battleLog.setEndTime((int)(System.currentTimeMillis() / 1000));

        if (!win) {
        	battleLog.setBattleResult(BattleLog.BATTLE_LOG_LOST);
        	this.battleLogDao.update(battleLog);
            return this.success(result.toMap());
        }

    	battleLog.setBattleResult(BattleLog.BATTLE_LOG_WIN);
    	this.battleLogDao.update(battleLog);

    	// 是否掠夺成功
    	int playerId = battleLog.getRoleId2();

    	Role role = this.roleDao.findOne(roleId);
    	int lv2 = 0;
    	if (!this.roleDao.isPlayer(playerId)) {
    		Object[] objects = this.configs().get(playerId - 1);
    		lv2 = (Integer)objects[1];
    	} else {
    		Role role2 = this.roleDao.findOne(playerId);
    		lv2 = role2.level();

    		int nextTime = (int)(System.currentTimeMillis() / 1000l) + 4 * 3600;
    		role2.setPillageTime(nextTime);
    	}

    	double f = ((double)lv2 - (double)role.level() + 10d) * 0.015 + 0.15;

    	Random random = new Random();
    	if (random.nextFloat() < f) {

        	this.itemDao.addItem(role.getRoleId(), itemId, 1, result);
    	}

        // 掠夺日常任务
        this.dailyTaskDao.addPillage(role, result);

    	return this.success(result.toMap());
	}

	/**
	 * 
	 * @param n
	 * @return
	 * @throws Exception
	 */
	public Object buy() throws Exception {

		int roleId = this.roleId();

		Role role = this.roleDao.findOne(roleId);
		
		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

		int buyNum = this.buyNum(role.vip, dateNumModel.getPillageBuyNum());

		if (buyNum <= 0) {
			return this.returnError(this.lineNum(), "购买次数不足");
		}

    	int gold = this.gold(dateNumModel.getPillageBuyNum());

		if (role.gold < gold) {
			return this.returnError(2, ErrorResult.NotEnoughGold);
		}

		Result result = new Result();
		this.roleDao.subGold(role, gold, "购买掠夺次数", 0, result);
		role.pillageNum += 5;

		// this.roleDao.update(role, result);

		dateNumModel.pillageBuyNum += 1;
		this.dateNumDao.save(roleId, dateNumModel);

		result.setValue("buy_num", this.buyNum(role.vip, dateNumModel.getPillageBuyNum()));
		result.setValue("gold", this.gold(dateNumModel.getPillageBuyNum()));
		return this.success(result.toMap());
	}

	/**
	 * 
	 * @param hour
	 * @return
	 * @throws Exception
	 */
	public Object protect(String tokenS, int hour) throws Exception {

		int roleId = this.roleId();

		int gold = 10;
		long time = 4l * 3600l * 1000l;
		if (hour == 12) {
			gold = 30;
			time = 12l * 3600l * 1000l;
		}

		Role role = this.roleDao.findOne(roleId);
		if (role.gold < gold) {
			return this.returnError(2, ErrorResult.NotEnoughGold);
		}
		
		Result result = new Result();
		this.roleDao.subGold(role, gold, "免疫掠夺", 0, result);
		role.pillageTime = (int)((time + System.currentTimeMillis()) / 1000);

		// this.roleDao.update(role, result);

		result.setValue("protect_time", time);
		return this.success(result.toMap());
	}

	public void addP() {
		this.roleDao.addPillage(10);
	}

	private int buyNum(int vip, int todayNum) {

		if (vip <= 5) {
			return 0;
		}
		return vip - 4 - todayNum;
	}

	private int gold(int num) {
    	int n = num / 2;
    	int gold = (int)(50 * Math.pow(2, n));
    	if (gold > 800) {
    		gold = 800;
    	}
    	return gold;
	}

	private void players(List<PlayerModel> players, int level) throws Exception {

		List<Object[]> configs = this.configs();

		List<Object[]> configs2 = new ArrayList<Object[]>();
		for (Object[] objects : configs) {
			int lv = (Integer)objects[1];
			if (lv <= level + 10 && lv >= level - 10) {
				configs2.add(objects);
			}
		}

		Collections.shuffle(configs2);

		configs2 = configs2.subList(0, 10 - players.size());

    	String[] firstName = RoleService.FirstName;
    	String[] lastName = RoleService.LastName;

    	ArenaDao arenaDao = new ArenaDao();
    	
		for (Object[] objects : configs2) {

			int rId = (Integer)objects[0];

			PlayerModel player = new PlayerModel();
			player.roleId = rId;
			player.avatar = rId % 9;
			player.level = (Integer)objects[1];
	    	player.name = firstName[(rId * 2) % firstName.length] + lastName[(rId * 3) % lastName.length];

	    	int[] h1 = (int[])objects[2];
	    	int[] h2 = (int[])objects[3];
	    	int[] h3 = (int[])objects[4];
	    	int[] h4 = (int[])objects[5];

	    	player.generalList.add(arenaDao.toHero(1, h1));
	    	player.generalList.add(arenaDao.toHero(1, h2));
	    	player.generalList.add(arenaDao.toHero(1, h3));
	    	player.generalList.add(arenaDao.toHero(1, h4));
	    	
	    	players.add(player);
		}
	}

	private List<Object[]> configs() {
		List<Object[]> configs = new ArrayList<Object[]>();
		configs.add(new Object[]{1, 80, new int[]{10001,77,5,8,77,57,37,12} ,new int[]{10017,80,5,9,80,60,40,12} ,new int[]{10015,79,5,9,79,59,39,12} ,new int[]{10003,80,5,9,80,60,40,12}});
		configs.add(new Object[]{2, 80, new int[]{10012,77,5,8,77,57,37,12} ,new int[]{10010,80,5,9,80,60,40,12} ,new int[]{10009,79,5,9,79,59,39,12} ,new int[]{10015,80,5,9,80,60,40,12}});
		configs.add(new Object[]{3, 80, new int[]{10000,77,5,8,77,57,37,12} ,new int[]{10009,80,5,9,80,60,40,12} ,new int[]{10010,79,5,9,79,59,39,12} ,new int[]{10003,80,5,9,80,60,40,12}});
		configs.add(new Object[]{4, 80, new int[]{10011,77,5,8,77,57,37,12} ,new int[]{10014,80,5,9,80,60,40,12} ,new int[]{10010,79,5,9,79,59,39,12} ,new int[]{10005,80,5,9,80,60,40,12}});
		configs.add(new Object[]{5, 79, new int[]{10012,76,5,8,76,56,36,12} ,new int[]{10010,78,5,9,78,58,38,12} ,new int[]{10009,79,5,9,79,59,39,12} ,new int[]{10015,79,5,8,79,59,39,12}});
		configs.add(new Object[]{6, 79, new int[]{10000,76,5,8,76,56,36,12} ,new int[]{10009,78,5,9,78,58,38,12} ,new int[]{10010,79,5,9,79,59,39,12} ,new int[]{10003,79,5,9,79,59,39,12}});
		configs.add(new Object[]{7, 79, new int[]{10011,76,5,8,76,56,36,12} ,new int[]{10014,78,5,9,78,58,38,12} ,new int[]{10010,79,5,9,79,59,39,12} ,new int[]{10005,79,5,9,79,59,39,12}});
		configs.add(new Object[]{8, 79, new int[]{10012,76,5,8,76,56,36,12} ,new int[]{10015,78,5,9,78,58,38,12} ,new int[]{10002,79,5,9,79,59,39,12} ,new int[]{10013,79,5,9,79,59,39,12}});
		configs.add(new Object[]{9, 78, new int[]{10013,75,5,8,75,55,35,12} ,new int[]{10017,77,4,8,77,57,37,12} ,new int[]{10001,76,5,8,76,56,36,12} ,new int[]{10014,78,4,8,78,58,38,12}});
		configs.add(new Object[]{10, 78, new int[]{10002,75,5,8,75,55,35,12} ,new int[]{10015,77,4,8,77,57,37,12} ,new int[]{10003,76,5,8,76,56,36,12} ,new int[]{10014,78,4,8,78,58,38,12}});
		configs.add(new Object[]{11, 78, new int[]{10017,75,5,8,75,55,35,12} ,new int[]{10011,77,4,8,77,57,37,12} ,new int[]{10000,76,5,8,76,56,36,12} ,new int[]{10015,78,4,8,78,58,38,12}});
		configs.add(new Object[]{12, 78, new int[]{10001,75,5,8,75,55,35,12} ,new int[]{10017,77,4,8,77,57,37,12} ,new int[]{10015,76,5,8,76,56,36,12} ,new int[]{10003,78,4,8,78,58,38,12}});
		configs.add(new Object[]{13, 77, new int[]{10014,74,4,8,74,54,34,12} ,new int[]{10016,76,5,8,76,56,36,12} ,new int[]{10005,75,4,8,75,55,35,12} ,new int[]{10010,77,5,8,77,57,37,12}});
		configs.add(new Object[]{14, 77, new int[]{10015,74,4,8,74,54,34,12} ,new int[]{10009,76,5,8,76,56,36,12} ,new int[]{10012,75,4,8,75,55,35,12} ,new int[]{10013,77,5,8,77,57,37,12}});
		configs.add(new Object[]{15, 77, new int[]{10020,74,4,8,74,54,34,12} ,new int[]{10009,76,5,8,76,56,36,12} ,new int[]{10016,75,4,8,75,55,35,12} ,new int[]{10010,77,5,8,77,57,37,12}});
		configs.add(new Object[]{16, 77, new int[]{10003,74,4,8,74,54,34,12} ,new int[]{10002,76,5,8,76,56,36,12} ,new int[]{10005,75,4,8,75,55,35,12} ,new int[]{10015,77,5,8,77,57,37,12}});
		configs.add(new Object[]{17, 76, new int[]{10017,73,4,8,73,53,33,12} ,new int[]{10010,75,4,8,75,55,35,12} ,new int[]{10012,74,5,8,74,54,34,12} ,new int[]{10015,76,4,8,76,56,36,12}});
		configs.add(new Object[]{18, 76, new int[]{10001,73,4,8,73,53,33,12} ,new int[]{10002,75,4,8,75,55,35,12} ,new int[]{10011,74,5,8,74,54,34,12} ,new int[]{10014,76,4,8,76,56,36,12}});
		configs.add(new Object[]{19, 76, new int[]{10003,73,4,8,73,53,33,12} ,new int[]{10009,75,4,8,75,55,35,12} ,new int[]{10010,74,5,8,74,54,34,12} ,new int[]{10015,76,4,8,76,56,36,12}});
		configs.add(new Object[]{20, 76, new int[]{10016,73,4,8,73,53,33,12} ,new int[]{10014,75,4,8,75,55,35,12} ,new int[]{10012,74,5,8,74,54,34,12} ,new int[]{10002,76,4,8,76,56,36,12}});
		configs.add(new Object[]{21, 75, new int[]{10009,72,4,8,72,52,32,12} ,new int[]{10002,74,4,8,74,54,34,12} ,new int[]{10015,73,4,8,73,53,33,12} ,new int[]{10016,75,4,8,75,55,35,12}});
		configs.add(new Object[]{22, 75, new int[]{10004,72,4,8,72,52,32,12} ,new int[]{10010,74,4,8,74,54,34,12} ,new int[]{10003,73,4,8,73,53,33,12} ,new int[]{10005,75,4,8,75,55,35,12}});
		configs.add(new Object[]{23, 75, new int[]{10002,72,4,8,72,52,32,12} ,new int[]{10004,74,4,8,74,54,34,12} ,new int[]{10003,73,4,8,73,53,33,12} ,new int[]{10011,75,4,8,75,55,35,12}});
		configs.add(new Object[]{24, 75, new int[]{10004,72,4,8,72,52,32,12} ,new int[]{10010,74,4,8,74,54,34,12} ,new int[]{10009,73,4,8,73,53,33,12} ,new int[]{10016,75,4,8,75,55,35,12}});
		configs.add(new Object[]{25, 75, new int[]{10005,72,4,8,72,52,32,12} ,new int[]{10010,74,4,8,74,54,34,12} ,new int[]{10011,73,4,8,73,53,33,12} ,new int[]{10014,75,4,8,75,55,35,12}});
		configs.add(new Object[]{26, 74, new int[]{10020,71,4,8,71,51,31,12} ,new int[]{10018,73,4,8,73,53,33,12} ,new int[]{10015,72,4,8,72,52,32,12} ,new int[]{10005,74,4,8,74,54,34,12}});
		configs.add(new Object[]{27, 74, new int[]{10010,71,4,8,71,51,31,12} ,new int[]{10016,73,4,8,73,53,33,12} ,new int[]{10014,72,4,8,72,52,32,12} ,new int[]{10005,74,4,8,74,54,34,12}});
		configs.add(new Object[]{28, 74, new int[]{10013,71,4,8,71,51,31,12} ,new int[]{10009,73,4,8,73,53,33,12} ,new int[]{10015,72,4,8,72,52,32,12} ,new int[]{10012,74,4,8,74,54,34,12}});
		configs.add(new Object[]{29, 74, new int[]{10010,71,4,8,71,51,31,12} ,new int[]{10009,73,4,8,73,53,33,12} ,new int[]{10020,72,4,8,72,52,32,12} ,new int[]{10016,74,4,8,74,54,34,12}});
		configs.add(new Object[]{30, 73, new int[]{10011,70,4,8,70,50,30,12} ,new int[]{10014,72,3,8,72,52,32,12} ,new int[]{10010,71,4,8,71,51,31,12} ,new int[]{10005,73,4,8,73,53,33,12}});
		configs.add(new Object[]{31, 73, new int[]{10012,70,4,8,70,50,30,12} ,new int[]{10015,72,3,8,72,52,32,12} ,new int[]{10002,71,4,8,71,51,31,12} ,new int[]{10013,73,4,8,73,53,33,12}});
		configs.add(new Object[]{32, 73, new int[]{10011,70,4,8,70,50,30,12} ,new int[]{10014,72,3,8,72,52,32,12} ,new int[]{10009,71,4,8,71,51,31,12} ,new int[]{10001,73,4,8,73,53,33,12}});
		configs.add(new Object[]{33, 73, new int[]{10015,70,4,8,70,50,30,12} ,new int[]{10002,72,3,8,72,52,32,12} ,new int[]{10014,71,4,8,71,51,31,12} ,new int[]{10003,73,4,8,73,53,33,12}});
		configs.add(new Object[]{34, 72, new int[]{10012,69,4,7,69,49,29,12} ,new int[]{10015,71,5,8,71,51,31,12} ,new int[]{10002,70,4,8,70,50,30,12} ,new int[]{10013,72,4,8,72,52,32,12}});
		configs.add(new Object[]{35, 72, new int[]{10001,69,4,7,69,49,29,12} ,new int[]{10017,71,5,8,71,51,31,12} ,new int[]{10015,70,4,8,70,50,30,12} ,new int[]{10003,72,4,8,72,52,32,12}});
		configs.add(new Object[]{36, 72, new int[]{10012,69,4,7,69,49,29,12} ,new int[]{10010,71,5,8,71,51,31,12} ,new int[]{10009,70,4,8,70,50,30,12} ,new int[]{10015,72,4,8,72,52,32,12}});
		configs.add(new Object[]{37, 72, new int[]{10000,69,4,7,69,49,29,12} ,new int[]{10009,71,5,8,71,51,31,12} ,new int[]{10010,70,4,8,70,50,30,12} ,new int[]{10003,72,4,8,72,52,32,12}});
		configs.add(new Object[]{38, 71, new int[]{10001,68,4,7,68,48,38,6} ,new int[]{10017,70,4,7,70,50,40,6} ,new int[]{10015,69,3,7,69,49,39,6} ,new int[]{10003,71,4,8,71,51,41,6}});
		configs.add(new Object[]{39, 71, new int[]{10012,68,4,7,68,48,38,6} ,new int[]{10010,70,4,7,70,50,40,6} ,new int[]{10009,69,3,7,69,49,39,6} ,new int[]{10015,71,4,8,71,51,41,6}});
		configs.add(new Object[]{40, 71, new int[]{10000,68,4,7,68,48,38,6} ,new int[]{10009,70,4,7,70,50,40,6} ,new int[]{10010,69,3,7,69,49,39,6} ,new int[]{10003,71,4,8,71,51,41,6}});
		configs.add(new Object[]{41, 71, new int[]{10011,68,4,7,68,48,38,6} ,new int[]{10014,70,4,7,70,50,40,6} ,new int[]{10010,69,3,7,69,49,39,6} ,new int[]{10005,71,4,8,71,51,41,6}});
		configs.add(new Object[]{42, 70, new int[]{10003,66,4,7,66,46,36,6} ,new int[]{10002,69,4,7,69,49,39,6} ,new int[]{10005,65,5,7,65,45,35,6} ,new int[]{10015,70,4,8,70,50,40,6}});
		configs.add(new Object[]{43, 70, new int[]{10013,68,4,7,68,48,38,6} ,new int[]{10017,69,4,7,69,49,39,6} ,new int[]{10001,68,5,7,68,48,38,6} ,new int[]{10014,70,4,8,70,50,40,6}});
		configs.add(new Object[]{44, 70, new int[]{10002,67,4,7,67,47,37,6} ,new int[]{10015,69,4,7,69,49,39,6} ,new int[]{10003,68,5,7,68,48,38,6} ,new int[]{10014,70,4,8,70,50,40,6}});
		configs.add(new Object[]{45, 70, new int[]{10017,67,4,7,67,47,37,6} ,new int[]{10011,69,4,7,69,49,39,6} ,new int[]{10000,68,5,7,68,48,38,6} ,new int[]{10015,70,4,8,70,50,40,6}});
		configs.add(new Object[]{46, 69, new int[]{10011,67,4,7,67,47,37,6} ,new int[]{10017,68,4,7,68,48,38,6} ,new int[]{10015,67,3,7,67,47,37,6} ,new int[]{10000,65,3,7,65,45,35,6}});
		configs.add(new Object[]{47, 69, new int[]{10017,66,4,7,66,46,36,6} ,new int[]{10001,68,4,7,68,48,38,6} ,new int[]{10003,66,3,7,66,46,36,6} ,new int[]{10015,66,3,7,66,46,36,6}});
		configs.add(new Object[]{48, 69, new int[]{10017,67,4,7,66,46,36,6} ,new int[]{10001,68,4,7,68,48,38,6} ,new int[]{10003,66,3,7,66,46,36,6} ,new int[]{10015,66,3,7,66,46,36,6}});
		configs.add(new Object[]{49, 69, new int[]{10010,66,4,7,66,46,36,6} ,new int[]{10012,68,4,7,68,48,38,6} ,new int[]{10015,65,3,7,65,45,35,6} ,new int[]{10009,66,3,7,66,46,36,6}});
		configs.add(new Object[]{50, 69, new int[]{10009,68,4,7,68,48,38,6} ,new int[]{10000,68,4,7,68,48,38,6} ,new int[]{10003,64,3,7,64,44,34,6} ,new int[]{10010,65,3,7,65,45,35,6}});
		configs.add(new Object[]{51, 68, new int[]{10009,66,4,7,66,46,36,6} ,new int[]{10020,67,3,7,67,47,37,6} ,new int[]{10010,65,4,7,65,45,35,6} ,new int[]{10016,64,3,7,64,44,34,6}});
		configs.add(new Object[]{52, 68, new int[]{10002,68,4,7,68,48,38,6} ,new int[]{10003,67,3,7,67,47,37,6} ,new int[]{10015,64,4,7,64,44,34,6} ,new int[]{10005,65,3,7,65,45,35,6}});
		configs.add(new Object[]{53, 68, new int[]{10017,67,4,7,67,47,37,6} ,new int[]{10013,67,3,7,67,47,37,6} ,new int[]{10014,66,4,7,66,46,36,6} ,new int[]{10001,65,3,7,65,45,35,6}});
		configs.add(new Object[]{54, 68, new int[]{10015,68,4,7,68,48,38,6} ,new int[]{10002,67,3,7,67,47,37,6} ,new int[]{10014,65,4,7,65,45,35,6} ,new int[]{10003,66,3,7,66,46,36,6}});
		configs.add(new Object[]{55, 67, new int[]{10001,65,4,7,65,45,35,6} ,new int[]{10002,66,3,7,66,46,36,6} ,new int[]{10011,64,4,7,64,44,34,6} ,new int[]{10014,65,4,7,65,45,35,6}});
		configs.add(new Object[]{56, 67, new int[]{10001,65,4,7,65,45,35,6} ,new int[]{10002,66,3,7,66,46,36,6} ,new int[]{10011,64,4,7,64,44,34,6} ,new int[]{10014,65,4,7,65,45,35,6}});
		configs.add(new Object[]{57, 67, new int[]{10003,64,4,7,64,44,34,6} ,new int[]{10009,66,3,7,66,46,36,6} ,new int[]{10010,64,4,7,64,44,34,6} ,new int[]{10015,66,4,7,66,46,36,6}});
		configs.add(new Object[]{58, 67, new int[]{10016,65,4,7,65,45,35,6} ,new int[]{10014,66,3,7,66,46,36,6} ,new int[]{10012,66,4,7,66,46,36,6} ,new int[]{10002,66,4,7,66,46,36,6}});
		configs.add(new Object[]{59, 67, new int[]{10014,64,4,7,64,44,34,6} ,new int[]{10016,66,3,7,66,46,36,6} ,new int[]{10005,65,4,7,65,45,35,6} ,new int[]{10010,65,4,7,65,45,35,6}});
		configs.add(new Object[]{60, 66, new int[]{10002,65,4,7,65,45,35,6} ,new int[]{10004,65,3,7,65,45,35,6} ,new int[]{10003,64,3,7,64,44,34,6} ,new int[]{10011,64,4,7,64,44,34,6}});
		configs.add(new Object[]{61, 66, new int[]{10004,66,4,7,66,46,36,6} ,new int[]{10010,65,3,7,65,45,35,6} ,new int[]{10009,64,3,7,64,44,34,6} ,new int[]{10016,65,4,7,65,45,35,6}});
		configs.add(new Object[]{62, 66, new int[]{10005,65,4,7,65,45,35,6} ,new int[]{10010,65,3,7,65,45,35,6} ,new int[]{10011,64,3,7,64,44,34,6} ,new int[]{10014,65,4,7,65,45,35,6}});
		configs.add(new Object[]{63, 66, new int[]{10017,64,4,7,64,44,34,6} ,new int[]{10010,65,3,7,65,45,35,6} ,new int[]{10012,65,3,7,65,45,35,6} ,new int[]{10015,66,4,7,66,46,36,6}});
		configs.add(new Object[]{64, 65, new int[]{10002,65,4,7,65,45,35,6} ,new int[]{10009,64,3,7,64,44,34,6} ,new int[]{10016,64,3,7,64,44,34,6} ,new int[]{10015,65,3,7,65,45,35,6}});
		configs.add(new Object[]{65, 65, new int[]{10010,64,4,7,64,44,34,6} ,new int[]{10004,64,3,7,64,44,34,6} ,new int[]{10005,64,3,7,64,44,34,6} ,new int[]{10003,64,3,7,64,44,34,6}});
		configs.add(new Object[]{66, 65, new int[]{10004,65,4,7,65,45,35,6} ,new int[]{10002,64,3,7,64,44,34,6} ,new int[]{10011,64,3,7,64,44,34,6} ,new int[]{10003,65,3,7,65,45,35,6}});
		configs.add(new Object[]{67, 65, new int[]{10010,65,4,7,65,45,35,6} ,new int[]{10004,64,3,7,64,44,34,6} ,new int[]{10016,65,3,7,65,45,35,6} ,new int[]{10009,64,3,7,64,44,34,6}});
		configs.add(new Object[]{68, 64, new int[]{10018,64,4,6,64,44,34,6} ,new int[]{10020,63,4,6,63,43,33,6} ,new int[]{10005,62,4,6,62,42,32,6} ,new int[]{10015,64,4,7,64,44,34,6}});
		configs.add(new Object[]{69, 64, new int[]{10016,62,4,6,62,42,32,6} ,new int[]{10010,63,4,6,63,43,33,6} ,new int[]{10005,62,4,6,62,42,32,6} ,new int[]{10014,64,4,7,64,44,34,6}});
		configs.add(new Object[]{70, 64, new int[]{10009,62,4,6,62,42,32,6} ,new int[]{10013,63,4,7,63,43,33,6} ,new int[]{10012,64,4,6,64,44,34,6} ,new int[]{10015,64,4,7,64,44,34,6}});
		configs.add(new Object[]{71, 64, new int[]{10009,61,4,6,61,41,31,6} ,new int[]{10010,64,4,7,64,44,34,6} ,new int[]{10016,65,4,6,65,45,35,6} ,new int[]{10020,64,4,6,64,44,34,6}});
		configs.add(new Object[]{72, 63, new int[]{10011,61,4,6,61,41,31,6} ,new int[]{10014,62,4,6,62,42,32,6} ,new int[]{10009,61,4,6,61,41,31,6} ,new int[]{10001,63,4,6,63,43,33,6}});
		configs.add(new Object[]{73, 63, new int[]{10015,62,4,6,62,42,32,6} ,new int[]{10002,62,4,6,62,42,32,6} ,new int[]{10014,61,4,6,61,41,31,6} ,new int[]{10003,63,4,6,63,43,33,6}});
		configs.add(new Object[]{74, 63, new int[]{10020,60,4,6,60,40,30,6} ,new int[]{10018,62,4,6,62,42,32,6} ,new int[]{10015,61,4,6,61,41,31,6} ,new int[]{10005,63,4,6,63,43,33,6}});
		configs.add(new Object[]{75, 63, new int[]{10010,61,4,6,61,41,31,6} ,new int[]{10016,62,4,6,62,42,32,6} ,new int[]{10014,61,4,6,61,41,31,6} ,new int[]{10005,63,4,6,63,43,33,6}});
		configs.add(new Object[]{76, 63, new int[]{10013,60,4,6,60,40,30,6} ,new int[]{10009,62,4,6,62,42,32,6} ,new int[]{10015,61,4,6,61,41,31,6} ,new int[]{10012,63,4,6,63,43,33,6}});
		configs.add(new Object[]{77, 62, new int[]{10016,60,4,6,60,40,30,6} ,new int[]{10014,61,3,6,61,41,31,6} ,new int[]{10010,60,4,6,60,40,30,6} ,new int[]{10005,62,3,6,62,42,32,6}});
		configs.add(new Object[]{78, 62, new int[]{10009,61,4,6,61,41,31,6} ,new int[]{10015,61,3,6,61,41,31,6} ,new int[]{10013,60,4,6,60,40,30,6} ,new int[]{10012,62,3,6,62,42,32,6}});
		configs.add(new Object[]{79, 62, new int[]{10009,60,4,6,60,40,30,6} ,new int[]{10020,61,3,6,61,41,31,6} ,new int[]{10010,60,4,6,60,40,30,6} ,new int[]{10016,62,3,6,62,42,32,6}});
		configs.add(new Object[]{80, 62, new int[]{10002,60,4,6,60,40,30,6} ,new int[]{10015,61,3,6,61,41,31,6} ,new int[]{10003,60,4,6,60,40,30,6} ,new int[]{10014,62,3,6,62,42,32,6}});
		configs.add(new Object[]{81, 61, new int[]{10012,58,3,6,58,38,28,3} ,new int[]{10010,60,3,6,60,40,30,3} ,new int[]{10009,59,4,6,59,39,29,3} ,new int[]{10015,61,3,6,61,41,31,3}});
		configs.add(new Object[]{82, 61, new int[]{10000,59,3,6,59,39,29,3} ,new int[]{10009,60,3,6,60,40,30,3} ,new int[]{10010,59,4,6,59,39,29,3} ,new int[]{10003,61,3,6,61,41,31,3}});
		configs.add(new Object[]{83, 61, new int[]{10011,60,3,6,60,40,30,3} ,new int[]{10014,60,3,6,60,40,30,3} ,new int[]{10010,59,4,6,59,39,29,3} ,new int[]{10005,61,3,6,61,41,31,3}});
		configs.add(new Object[]{84, 61, new int[]{10012,60,3,6,60,40,30,3} ,new int[]{10015,60,3,6,60,40,30,3} ,new int[]{10002,59,4,6,59,39,29,3} ,new int[]{10013,61,3,6,61,41,31,3}});
		configs.add(new Object[]{85, 61, new int[]{10011,59,3,6,59,39,29,3} ,new int[]{10014,60,3,6,60,40,30,3} ,new int[]{10009,59,4,6,59,39,29,3} ,new int[]{10001,61,3,6,61,41,31,3}});
		configs.add(new Object[]{86, 60, new int[]{10003,57,3,6,57,37,27,3} ,new int[]{10002,59,3,6,59,39,29,3} ,new int[]{10005,58,3,6,58,38,28,3} ,new int[]{10015,60,3,6,60,40,30,3}});
		configs.add(new Object[]{87, 60, new int[]{10013,57,3,6,57,37,27,3} ,new int[]{10017,59,3,6,59,39,29,3} ,new int[]{10001,58,3,6,58,38,28,3} ,new int[]{10014,60,3,6,60,40,30,3}});
		configs.add(new Object[]{88, 60, new int[]{10002,57,3,6,57,37,27,3} ,new int[]{10015,59,3,6,59,39,29,3} ,new int[]{10003,58,3,6,58,38,28,3} ,new int[]{10014,60,3,6,60,40,30,3}});
		configs.add(new Object[]{89, 60, new int[]{10017,57,3,6,57,37,27,3} ,new int[]{10011,59,3,6,59,39,29,3} ,new int[]{10000,58,3,6,58,38,28,3} ,new int[]{10015,60,3,6,60,40,30,3}});
		configs.add(new Object[]{90, 60, new int[]{10001,59,3,6,59,39,29,3} ,new int[]{10017,59,3,6,59,39,29,3} ,new int[]{10015,58,3,6,58,38,28,3} ,new int[]{10003,60,3,6,60,40,30,3}});
		configs.add(new Object[]{91, 59, new int[]{10014,57,3,6,57,47,42,0} ,new int[]{10016,56,2,6,56,46,41,0} ,new int[]{10002,58,1,6,58,48,43,0} ,new int[]{10012,57,2,6,57,47,42,0}});
		configs.add(new Object[]{92, 59, new int[]{10016,58,2,6,58,48,43,0} ,new int[]{10014,57,2,6,57,47,42,0} ,new int[]{10010,57,2,6,57,47,42,0} ,new int[]{10005,57,3,6,57,47,42,0}});
		configs.add(new Object[]{93, 59, new int[]{10009,56,2,6,56,46,41,0} ,new int[]{10015,56,2,6,56,46,41,0} ,new int[]{10013,58,2,6,58,48,43,0} ,new int[]{10012,56,2,6,56,46,41,0}});
		configs.add(new Object[]{94, 59, new int[]{10009,57,2,6,57,47,42,0} ,new int[]{10020,55,2,6,55,45,40,0} ,new int[]{10010,56,2,6,56,46,41,0} ,new int[]{10016,57,2,6,57,47,42,0}});
		configs.add(new Object[]{95, 59, new int[]{10002,58,2,6,58,48,43,0} ,new int[]{10003,57,3,6,57,47,42,0} ,new int[]{10015,57,2,6,57,47,42,0} ,new int[]{10005,58,3,6,58,48,43,0}});
		configs.add(new Object[]{96, 58, new int[]{10010,56,2,6,56,46,41,0} ,new int[]{10005,55,3,6,55,45,40,0} ,new int[]{10014,57,3,6,57,47,42,0} ,new int[]{10011,56,1,6,56,46,41,0}});
		configs.add(new Object[]{97, 58, new int[]{10010,57,2,6,57,47,42,0} ,new int[]{10017,57,2,6,57,47,42,0} ,new int[]{10015,58,2,6,58,48,43,0} ,new int[]{10012,57,1,6,57,47,42,0}});
		configs.add(new Object[]{98, 58, new int[]{10002,58,1,6,58,48,43,0} ,new int[]{10001,58,3,6,58,48,43,0} ,new int[]{10014,56,3,6,56,46,41,0} ,new int[]{10011,58,1,6,58,48,43,0}});
		configs.add(new Object[]{99, 58, new int[]{10009,56,1,6,56,46,41,0} ,new int[]{10003,57,3,6,57,47,42,0} ,new int[]{10015,57,2,6,57,47,42,0} ,new int[]{10010,56,2,6,56,46,41,0}});
		configs.add(new Object[]{100, 57, new int[]{10004,53,3,6,53,43,38,0} ,new int[]{10010,54,2,5,54,44,39,0} ,new int[]{10003,54,3,5,54,44,39,0} ,new int[]{10005,57,3,5,57,47,42,0}});
		configs.add(new Object[]{101, 57, new int[]{10002,56,3,6,56,46,41,0} ,new int[]{10004,53,2,5,53,43,38,0} ,new int[]{10003,55,3,5,55,45,40,0} ,new int[]{10011,56,3,5,56,46,41,0}});
		configs.add(new Object[]{102, 57, new int[]{10004,55,3,6,55,45,40,0} ,new int[]{10010,55,2,5,55,45,40,0} ,new int[]{10009,54,3,5,54,44,39,0} ,new int[]{10016,55,3,5,55,45,40,0}});
		configs.add(new Object[]{103, 57, new int[]{10005,57,3,6,57,47,42,0} ,new int[]{10010,54,2,5,54,44,39,0} ,new int[]{10011,55,2,5,55,45,40,0} ,new int[]{10014,54,3,5,54,44,39,0}});
		configs.add(new Object[]{104, 56, new int[]{10009,53,3,6,53,43,38,0} ,new int[]{10013,55,2,5,55,45,40,0} ,new int[]{10012,54,3,5,54,44,39,0} ,new int[]{10015,55,3,5,55,45,40,0}});
		configs.add(new Object[]{105, 56, new int[]{10009,53,3,6,53,43,38,0} ,new int[]{10010,54,2,5,54,44,39,0} ,new int[]{10016,55,3,5,55,45,40,0} ,new int[]{10020,56,3,5,56,46,41,0}});
		configs.add(new Object[]{106, 56, new int[]{10002,54,3,6,54,44,39,0} ,new int[]{10015,52,2,5,52,42,37,0} ,new int[]{10017,54,3,5,54,44,39,0} ,new int[]{10005,54,3,5,54,44,39,0}});
		configs.add(new Object[]{107, 56, new int[]{10017,55,3,6,55,45,40,0} ,new int[]{10014,56,3,5,56,46,41,0} ,new int[]{10001,55,3,5,55,45,40,0} ,new int[]{10013,53,3,5,53,43,38,0}});
		configs.add(new Object[]{108, 56, new int[]{10009,54,3,6,54,44,39,0} ,new int[]{10002,55,2,5,55,45,40,0} ,new int[]{10015,56,3,5,56,46,41,0} ,new int[]{10016,52,3,5,52,42,37,0}});
		configs.add(new Object[]{109, 55, new int[]{10016,52,2,5,52,42,37,0} ,new int[]{10010,53,2,5,53,43,38,0} ,new int[]{10005,53,3,5,53,43,38,0} ,new int[]{10014,53,3,5,53,43,38,0}});
		configs.add(new Object[]{110, 55, new int[]{10009,51,3,5,51,41,36,0} ,new int[]{10013,54,2,5,54,44,39,0} ,new int[]{10012,54,3,5,54,44,39,0} ,new int[]{10015,54,3,5,54,44,39,0}});
		configs.add(new Object[]{111, 55, new int[]{10009,50,3,5,50,40,35,0} ,new int[]{10010,52,2,5,52,42,37,0} ,new int[]{10016,52,3,5,52,42,37,0} ,new int[]{10020,54,3,5,54,44,39,0}});
		configs.add(new Object[]{112, 55, new int[]{10002,52,3,5,52,42,37,0} ,new int[]{10015,54,2,5,54,44,39,0} ,new int[]{10017,53,3,5,53,43,38,0} ,new int[]{10005,53,3,5,53,43,38,0}});
		configs.add(new Object[]{113, 55, new int[]{10017,54,3,5,54,44,39,0} ,new int[]{10014,53,3,5,53,43,38,0} ,new int[]{10001,52,3,5,52,42,37,0} ,new int[]{10013,52,3,5,52,42,37,0}});
		configs.add(new Object[]{114, 54, new int[]{10015,54,2,5,54,44,39,0} ,new int[]{10012,52,2,5,52,42,37,0} ,new int[]{10013,53,2,5,53,43,38,0} ,new int[]{10002,54,2,5,54,44,39,0}});
		configs.add(new Object[]{115, 54, new int[]{10014,52,3,5,52,42,37,0} ,new int[]{10011,54,2,5,54,44,39,0} ,new int[]{10001,52,3,5,52,42,37,0} ,new int[]{10009,54,2,5,54,44,39,0}});
		configs.add(new Object[]{116, 54, new int[]{10002,53,2,5,53,43,38,0} ,new int[]{10015,53,2,5,53,43,38,0} ,new int[]{10003,53,3,5,53,43,38,0} ,new int[]{10014,53,3,5,53,43,38,0}});
		configs.add(new Object[]{117, 54, new int[]{10018,54,3,5,54,44,39,0} ,new int[]{10020,52,2,5,52,42,37,0} ,new int[]{10005,54,3,5,54,44,39,0} ,new int[]{10015,52,2,5,52,42,37,0}});
		configs.add(new Object[]{118, 53, new int[]{10017,52,3,5,52,42,37,0} ,new int[]{10014,50,3,5,50,40,35,0} ,new int[]{10001,52,3,5,52,42,37,0} ,new int[]{10013,52,3,5,52,42,37,0}});
		configs.add(new Object[]{119, 53, new int[]{10009,51,3,5,51,41,36,0} ,new int[]{10002,52,2,5,52,42,37,0} ,new int[]{10015,51,3,5,51,41,36,0} ,new int[]{10016,53,3,5,53,43,38,0}});
		configs.add(new Object[]{120, 53, new int[]{10004,52,3,5,52,42,37,0} ,new int[]{10010,51,2,5,51,41,36,0} ,new int[]{10003,50,3,5,50,40,35,0} ,new int[]{10005,52,3,5,52,42,37,0}});
		configs.add(new Object[]{121, 53, new int[]{10002,51,3,5,51,41,36,0} ,new int[]{10004,50,2,5,50,40,35,0} ,new int[]{10003,52,3,5,52,42,37,0} ,new int[]{10011,51,3,5,51,41,36,0}});
		configs.add(new Object[]{122, 53, new int[]{10002,50,2,5,50,40,35,0} ,new int[]{10009,52,2,5,52,42,37,0} ,new int[]{10004,50,2,5,50,40,35,0} ,new int[]{10015,51,2,5,51,41,36,0}});
		configs.add(new Object[]{123, 52, new int[]{10002,50,2,5,50,40,35,0} ,new int[]{10015,52,2,5,52,42,37,0} ,new int[]{10003,52,3,5,52,42,37,0} ,new int[]{10014,50,3,5,50,40,35,0}});
		configs.add(new Object[]{124, 52, new int[]{10018,52,3,5,52,42,37,0} ,new int[]{10020,51,2,5,51,41,36,0} ,new int[]{10005,51,3,5,51,41,36,0} ,new int[]{10015,51,2,5,51,41,36,0}});
		configs.add(new Object[]{125, 52, new int[]{10016,51,2,5,51,41,36,0} ,new int[]{10010,50,2,5,50,40,35,0} ,new int[]{10005,50,3,5,50,40,35,0} ,new int[]{10014,50,3,5,50,40,35,0}});
		configs.add(new Object[]{126, 52, new int[]{10009,52,3,5,52,42,37,0} ,new int[]{10013,52,2,5,52,42,37,0} ,new int[]{10012,52,3,5,52,42,37,0} ,new int[]{10015,49,3,5,49,39,34,0}});
		configs.add(new Object[]{127, 52, new int[]{10009,51,3,5,51,41,36,0} ,new int[]{10010,51,2,5,51,41,36,0} ,new int[]{10016,50,3,5,50,40,35,0} ,new int[]{10020,50,3,5,50,40,35,0}});
		configs.add(new Object[]{128, 52, new int[]{10002,50,3,5,50,40,35,0} ,new int[]{10015,49,2,5,49,39,34,0} ,new int[]{10017,51,3,5,51,41,36,0} ,new int[]{10005,51,3,5,51,41,36,0}});
		configs.add(new Object[]{129, 51, new int[]{10009,50,2,5,50,40,35,0} ,new int[]{10003,49,3,5,49,39,34,0} ,new int[]{10015,50,2,5,50,40,35,0} ,new int[]{10010,49,2,5,49,39,34,0}});
		configs.add(new Object[]{130, 51, new int[]{10014,48,3,5,48,38,33,0} ,new int[]{10016,50,2,5,50,40,35,0} ,new int[]{10002,51,2,5,51,41,36,0} ,new int[]{10012,48,2,5,48,38,33,0}});
		configs.add(new Object[]{131, 51, new int[]{10016,49,2,5,49,39,34,0} ,new int[]{10014,49,2,5,49,39,34,0} ,new int[]{10010,50,2,5,50,40,35,0} ,new int[]{10005,49,3,5,49,39,34,0}});
		configs.add(new Object[]{132, 51, new int[]{10009,49,2,5,49,39,34,0} ,new int[]{10015,48,2,5,48,38,33,0} ,new int[]{10013,49,2,5,49,39,34,0} ,new int[]{10012,50,2,5,50,40,35,0}});
		configs.add(new Object[]{133, 51, new int[]{10009,49,2,5,49,39,34,0} ,new int[]{10020,49,2,5,49,39,34,0} ,new int[]{10010,50,2,5,50,40,35,0} ,new int[]{10016,49,2,5,49,39,34,0}});
		configs.add(new Object[]{134, 50, new int[]{10010,50,2,5,50,40,35,0} ,new int[]{10004,49,3,5,49,39,34,0} ,new int[]{10005,47,3,5,47,37,32,0} ,new int[]{10003,49,3,5,49,39,34,0}});
		configs.add(new Object[]{135, 50, new int[]{10004,48,2,5,48,38,33,0} ,new int[]{10002,50,3,5,50,40,35,0} ,new int[]{10011,48,3,5,48,38,33,0} ,new int[]{10003,48,3,5,48,38,33,0}});
		configs.add(new Object[]{136, 50, new int[]{10010,49,2,5,49,39,34,0} ,new int[]{10004,49,3,5,49,39,34,0} ,new int[]{10016,48,3,5,48,38,33,0} ,new int[]{10009,49,3,5,49,39,34,0}});
		configs.add(new Object[]{137, 50, new int[]{10010,49,2,5,49,39,34,0} ,new int[]{10005,48,3,5,48,38,33,0} ,new int[]{10014,49,3,5,49,39,34,0} ,new int[]{10011,50,2,5,50,40,35,0}});
		configs.add(new Object[]{138, 50, new int[]{10010,49,2,5,49,39,34,0} ,new int[]{10017,49,2,5,49,39,34,0} ,new int[]{10015,47,2,5,47,37,32,0} ,new int[]{10012,49,2,5,49,39,34,0}});
		configs.add(new Object[]{139, 50, new int[]{10002,50,2,5,50,40,35,0} ,new int[]{10001,50,3,5,50,40,35,0} ,new int[]{10014,48,3,5,48,38,33,0} ,new int[]{10011,50,2,5,50,40,35,0}});
		configs.add(new Object[]{140, 49, new int[]{10013,46,2,5,46,36,31,0} ,new int[]{10017,48,2,5,48,38,33,0} ,new int[]{10001,46,3,5,46,36,31,0} ,new int[]{10014,49,3,5,49,39,34,0}});
		configs.add(new Object[]{141, 49, new int[]{10002,45,2,5,45,35,30,0} ,new int[]{10015,46,2,5,46,36,31,0} ,new int[]{10003,45,3,5,45,35,30,0} ,new int[]{10014,47,3,5,47,37,32,0}});
		configs.add(new Object[]{142, 49, new int[]{10017,48,2,5,48,38,33,0} ,new int[]{10011,49,2,5,49,39,34,0} ,new int[]{10000,48,2,5,48,38,33,0} ,new int[]{10015,46,2,5,46,36,31,0}});
		configs.add(new Object[]{143, 49, new int[]{10001,47,3,5,47,37,32,0} ,new int[]{10017,47,2,5,47,37,32,0} ,new int[]{10015,49,2,5,49,39,34,0} ,new int[]{10003,48,3,5,48,38,33,0}});
		configs.add(new Object[]{144, 49, new int[]{10012,46,2,5,46,36,31,0} ,new int[]{10010,48,2,5,48,38,33,0} ,new int[]{10009,47,2,5,47,37,32,0} ,new int[]{10015,49,2,5,49,39,34,0}});
		configs.add(new Object[]{145, 49, new int[]{10000,48,2,5,48,38,33,0} ,new int[]{10009,47,2,5,47,37,32,0} ,new int[]{10010,48,2,5,48,38,33,0} ,new int[]{10003,48,3,5,48,38,33,0}});
		configs.add(new Object[]{146, 48, new int[]{10003,46,3,5,46,36,31,0} ,new int[]{10009,47,2,5,47,37,32,0} ,new int[]{10010,46,2,5,46,36,31,0} ,new int[]{10015,46,2,5,46,36,31,0}});
		configs.add(new Object[]{147, 48, new int[]{10016,45,2,5,45,35,30,0} ,new int[]{10014,45,3,5,45,35,30,0} ,new int[]{10012,45,2,5,45,35,30,0} ,new int[]{10002,47,2,5,47,37,32,0}});
		configs.add(new Object[]{148, 48, new int[]{10014,46,2,5,46,36,31,0} ,new int[]{10016,46,2,5,46,36,31,0} ,new int[]{10005,44,3,5,44,34,29,0} ,new int[]{10010,46,2,5,46,36,31,0}});
		configs.add(new Object[]{149, 48, new int[]{10015,45,2,5,45,35,30,0} ,new int[]{10009,47,2,5,47,37,32,0} ,new int[]{10012,45,2,5,45,35,30,0} ,new int[]{10013,48,2,5,48,38,33,0}});
		configs.add(new Object[]{150, 48, new int[]{10020,44,2,5,44,34,29,0} ,new int[]{10009,48,2,5,48,38,33,0} ,new int[]{10016,46,2,5,46,36,31,0} ,new int[]{10010,49,2,5,49,39,34,0}});
		configs.add(new Object[]{151, 48, new int[]{10003,45,3,5,45,35,30,0} ,new int[]{10002,47,2,5,47,37,32,0} ,new int[]{10005,47,3,5,47,37,32,0} ,new int[]{10015,48,2,5,48,38,33,0}});
		configs.add(new Object[]{152, 47, new int[]{10002,45,3,5,45,35,30,0} ,new int[]{10004,47,2,5,47,37,32,0} ,new int[]{10003,45,3,5,45,35,30,0} ,new int[]{10011,46,3,5,46,36,31,0}});
		configs.add(new Object[]{153, 47, new int[]{10004,46,3,5,46,36,31,0} ,new int[]{10010,45,2,5,45,35,30,0} ,new int[]{10009,44,3,5,44,34,29,0} ,new int[]{10016,47,3,5,47,37,32,0}});
		configs.add(new Object[]{154, 47, new int[]{10005,45,3,5,45,35,30,0} ,new int[]{10010,46,2,5,46,36,31,0} ,new int[]{10011,45,2,5,45,35,30,0} ,new int[]{10014,46,3,5,46,36,31,0}});
		configs.add(new Object[]{155, 47, new int[]{10017,44,2,5,44,34,29,0} ,new int[]{10010,47,2,5,47,37,32,0} ,new int[]{10012,46,2,5,46,36,31,0} ,new int[]{10015,45,2,5,45,35,30,0}});
		configs.add(new Object[]{156, 47, new int[]{10001,45,3,5,45,35,30,0} ,new int[]{10002,46,2,5,46,36,31,0} ,new int[]{10011,47,2,5,47,37,32,0} ,new int[]{10014,45,3,5,45,35,30,0}});
		configs.add(new Object[]{157, 46, new int[]{10002,46,1,4,46,36,31,0} ,new int[]{10015,41,2,4,41,31,26,0} ,new int[]{10003,43,3,4,43,33,28,0} ,new int[]{10014,40,3,4,40,30,25,0}});
		configs.add(new Object[]{158, 46, new int[]{10018,43,3,4,43,33,28,0} ,new int[]{10020,44,2,4,44,34,29,0} ,new int[]{10005,41,3,4,41,31,26,0} ,new int[]{10015,39,2,4,39,29,24,0}});
		configs.add(new Object[]{159, 46, new int[]{10016,45,2,4,45,35,30,0} ,new int[]{10010,43,2,4,43,33,28,0} ,new int[]{10005,44,3,4,44,34,29,0} ,new int[]{10014,40,3,4,40,30,25,0}});
		configs.add(new Object[]{160, 46, new int[]{10009,45,3,4,45,35,30,0} ,new int[]{10013,43,2,4,43,33,28,0} ,new int[]{10012,43,3,4,43,33,28,0} ,new int[]{10015,42,3,4,42,32,27,0}});
		configs.add(new Object[]{161, 46, new int[]{10009,44,3,4,44,34,29,0} ,new int[]{10010,45,2,4,45,35,30,0} ,new int[]{10016,45,3,4,45,35,30,0} ,new int[]{10020,41,3,4,41,31,26,0}});
		configs.add(new Object[]{162, 45, new int[]{10004,41,3,4,41,31,26,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10003,40,3,4,40,30,25,0} ,new int[]{10005,41,3,4,41,31,26,0}});
		configs.add(new Object[]{163, 45, new int[]{10002,44,3,4,44,34,29,0} ,new int[]{10004,41,2,4,41,31,26,0} ,new int[]{10003,41,3,4,41,31,26,0} ,new int[]{10011,39,3,4,39,29,24,0}});
		configs.add(new Object[]{164, 45, new int[]{10004,43,3,4,43,33,28,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10009,40,3,4,40,30,25,0} ,new int[]{10016,40,3,4,40,30,25,0}});
		configs.add(new Object[]{165, 45, new int[]{10005,45,3,4,45,35,30,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10011,40,1,4,40,30,25,0} ,new int[]{10014,40,3,4,40,30,25,0}});
		configs.add(new Object[]{166, 45, new int[]{10017,42,2,4,42,32,27,0} ,new int[]{10010,41,2,4,41,31,26,0} ,new int[]{10012,41,1,4,41,31,26,0} ,new int[]{10015,41,2,4,41,31,26,0}});
		configs.add(new Object[]{167, 44, new int[]{10018,41,3,4,41,31,26,0} ,new int[]{10020,41,2,4,41,31,26,0} ,new int[]{10005,40,3,4,40,30,25,0} ,new int[]{10015,41,2,4,41,31,26,0}});
		configs.add(new Object[]{168, 44, new int[]{10016,39,2,4,39,29,24,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10005,41,3,4,41,31,26,0} ,new int[]{10014,39,3,4,39,29,24,0}});
		configs.add(new Object[]{169, 44, new int[]{10009,41,3,4,41,31,26,0} ,new int[]{10013,39,2,4,39,29,24,0} ,new int[]{10012,39,3,4,39,29,24,0} ,new int[]{10015,41,3,4,41,31,26,0}});
		configs.add(new Object[]{170, 44, new int[]{10009,40,3,4,40,30,25,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10016,41,3,4,41,31,26,0} ,new int[]{10020,29,3,4,29,19,14,0}});
		configs.add(new Object[]{171, 44, new int[]{10002,39,3,4,39,29,24,0} ,new int[]{10015,40,2,4,40,30,25,0} ,new int[]{10017,40,3,4,40,30,25,0} ,new int[]{10005,31,3,4,31,21,16,0}});
		configs.add(new Object[]{172, 44, new int[]{10017,40,3,4,40,30,25,0} ,new int[]{10014,39,3,4,39,29,24,0} ,new int[]{10001,39,3,4,39,29,24,0} ,new int[]{10013,40,3,4,40,30,25,0}});
		configs.add(new Object[]{173, 44, new int[]{10009,42,3,4,42,32,27,0} ,new int[]{10002,40,2,4,40,30,25,0} ,new int[]{10015,40,3,4,40,30,25,0} ,new int[]{10016,40,3,4,40,30,25,0}});
		configs.add(new Object[]{174, 43, new int[]{10012,40,2,4,40,30,25,0} ,new int[]{10010,41,2,4,41,31,26,0} ,new int[]{10009,39,2,4,39,29,24,0} ,new int[]{10015,40,2,4,40,30,25,0}});
		configs.add(new Object[]{175, 43, new int[]{10000,40,2,4,40,30,25,0} ,new int[]{10009,40,2,4,40,30,25,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10003,40,3,4,40,30,25,0}});
		configs.add(new Object[]{176, 43, new int[]{10011,41,2,4,41,31,26,0} ,new int[]{10014,40,3,4,40,30,25,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10005,41,3,4,41,31,26,0}});
		configs.add(new Object[]{177, 43, new int[]{10012,40,2,4,40,30,25,0} ,new int[]{10015,39,2,4,39,29,24,0} ,new int[]{10002,41,2,4,41,31,26,0} ,new int[]{10013,39,2,4,39,29,24,0}});
		configs.add(new Object[]{178, 43, new int[]{10011,40,2,4,40,30,25,0} ,new int[]{10014,40,3,4,40,30,25,0} ,new int[]{10009,40,2,4,40,30,25,0} ,new int[]{10001,41,3,4,41,31,26,0}});
		configs.add(new Object[]{179, 42, new int[]{10002,40,3,4,40,30,25,0} ,new int[]{10015,41,2,4,41,31,26,0} ,new int[]{10017,31,3,4,31,21,16,0} ,new int[]{10005,39,3,4,39,29,24,0}});
		configs.add(new Object[]{180, 42, new int[]{10017,40,3,4,40,30,25,0} ,new int[]{10014,39,3,4,39,29,24,0} ,new int[]{10001,40,3,4,40,30,25,0} ,new int[]{10013,38,3,4,38,28,23,0}});
		configs.add(new Object[]{181, 42, new int[]{10009,41,3,4,41,31,26,0} ,new int[]{10002,41,2,4,41,31,26,0} ,new int[]{10015,40,3,4,40,30,25,0} ,new int[]{10016,37,3,4,37,27,22,0}});
		configs.add(new Object[]{182, 42, new int[]{10004,39,3,4,39,29,24,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10003,41,3,4,41,31,26,0} ,new int[]{10005,39,3,4,39,29,24,0}});
		configs.add(new Object[]{183, 42, new int[]{10002,41,3,4,41,31,26,0} ,new int[]{10004,39,2,4,39,29,24,0} ,new int[]{10003,39,3,4,39,29,24,0} ,new int[]{10011,40,3,4,40,30,25,0}});
		configs.add(new Object[]{184, 42, new int[]{10002,40,2,4,40,30,25,0} ,new int[]{10009,40,1,4,40,30,25,0} ,new int[]{10004,41,2,4,41,31,26,0} ,new int[]{10015,40,2,4,40,30,25,0}});
		configs.add(new Object[]{185, 42, new int[]{10001,39,3,4,39,29,24,0} ,new int[]{10017,40,2,4,40,30,25,0} ,new int[]{10015,40,2,4,40,30,25,0} ,new int[]{10003,39,3,4,39,29,24,0}});
		configs.add(new Object[]{186, 41, new int[]{10011,39,2,4,39,29,24,0} ,new int[]{10014,37,3,4,37,27,22,0} ,new int[]{10009,39,2,4,39,29,24,0} ,new int[]{10001,37,3,4,37,27,22,0}});
		configs.add(new Object[]{187, 41, new int[]{10015,38,2,4,38,28,23,0} ,new int[]{10002,39,1,4,39,29,24,0} ,new int[]{10014,38,3,4,38,28,23,0} ,new int[]{10003,35,3,4,35,25,20,0}});
		configs.add(new Object[]{188, 41, new int[]{10020,37,2,4,37,27,22,0} ,new int[]{10018,38,3,4,38,28,23,0} ,new int[]{10015,37,2,4,37,27,22,0} ,new int[]{10005,36,3,4,36,26,21,0}});
		configs.add(new Object[]{189, 41, new int[]{10010,39,2,4,39,29,24,0} ,new int[]{10016,37,2,4,37,27,22,0} ,new int[]{10014,39,3,4,39,29,24,0} ,new int[]{10005,35,3,4,35,25,20,0}});
		configs.add(new Object[]{190, 41, new int[]{10013,40,2,4,40,30,25,0} ,new int[]{10009,37,3,4,37,27,22,0} ,new int[]{10015,40,3,4,40,30,25,0} ,new int[]{10012,35,3,4,35,25,20,0}});
		configs.add(new Object[]{191, 40, new int[]{10009,38,1,4,38,28,23,0} ,new int[]{10003,39,3,4,39,29,24,0} ,new int[]{10015,38,2,4,38,28,23,0} ,new int[]{10010,37,2,4,37,27,22,0}});
		configs.add(new Object[]{192, 40, new int[]{10014,37,3,4,37,27,22,0} ,new int[]{10016,38,2,4,38,28,23,0} ,new int[]{10002,37,1,4,37,27,22,0} ,new int[]{10012,36,2,4,36,26,21,0}});
		configs.add(new Object[]{193, 40, new int[]{10016,39,2,4,39,29,24,0} ,new int[]{10014,37,2,4,37,27,22,0} ,new int[]{10010,39,2,4,39,29,24,0} ,new int[]{10005,37,3,4,37,27,22,0}});
		configs.add(new Object[]{194, 40, new int[]{10009,40,2,4,40,30,25,0} ,new int[]{10015,39,2,4,39,29,24,0} ,new int[]{10013,40,2,4,40,30,25,0} ,new int[]{10012,36,2,4,36,26,21,0}});
		configs.add(new Object[]{195, 40, new int[]{10009,40,2,4,40,30,25,0} ,new int[]{10020,40,2,4,40,30,25,0} ,new int[]{10010,40,2,4,40,30,25,0} ,new int[]{10016,37,2,4,37,27,22,0}});
		configs.add(new Object[]{196, 40, new int[]{10002,37,2,4,37,27,22,0} ,new int[]{10015,40,2,4,40,30,25,0} ,new int[]{10003,37,3,4,37,27,22,0} ,new int[]{10014,38,3,4,38,28,23,0}});
		configs.add(new Object[]{197, 39, new int[]{10017,37,2,4,37,27,22,0} ,new int[]{10011,37,1,4,37,27,22,0} ,new int[]{10000,37,2,4,37,27,22,0} ,new int[]{10015,36,2,4,36,26,21,0}});
		configs.add(new Object[]{198, 39, new int[]{10001,38,3,4,38,28,23,0} ,new int[]{10017,36,2,4,36,26,21,0} ,new int[]{10015,37,2,4,37,27,22,0} ,new int[]{10003,35,3,3,35,25,20,0}});
		configs.add(new Object[]{199, 39, new int[]{10012,37,2,4,37,27,22,0} ,new int[]{10010,37,2,4,37,27,22,0} ,new int[]{10009,36,2,4,36,26,21,0} ,new int[]{10015,34,2,3,34,24,19,0}});
		configs.add(new Object[]{200, 39, new int[]{10000,35,2,3,35,25,20,0} ,new int[]{10009,38,2,4,38,28,23,0} ,new int[]{10010,37,2,4,37,27,22,0} ,new int[]{10003,37,3,4,37,27,22,0}});
		configs.add(new Object[]{201, 39, new int[]{10011,36,2,4,36,26,21,0} ,new int[]{10014,37,3,4,37,27,22,0} ,new int[]{10010,38,2,4,38,28,23,0} ,new int[]{10005,36,3,4,36,26,21,0}});
		configs.add(new Object[]{202, 39, new int[]{10012,35,2,4,35,25,20,0} ,new int[]{10015,35,2,3,35,25,20,0} ,new int[]{10002,37,2,4,37,27,22,0} ,new int[]{10013,35,2,3,35,25,20,0}});
		configs.add(new Object[]{203, 38, new int[]{10016,37,2,4,37,27,22,0} ,new int[]{10014,35,3,3,35,25,20,0} ,new int[]{10012,36,2,4,36,26,21,0} ,new int[]{10002,35,1,3,35,25,20,0}});
		configs.add(new Object[]{204, 38, new int[]{10014,35,2,3,35,25,20,0} ,new int[]{10016,36,2,4,36,26,21,0} ,new int[]{10005,37,3,4,37,27,22,0} ,new int[]{10010,37,2,4,37,27,22,0}});
		configs.add(new Object[]{205, 38, new int[]{10015,36,2,4,36,26,21,0} ,new int[]{10009,35,2,3,35,25,20,0} ,new int[]{10012,38,2,4,38,28,23,0} ,new int[]{10013,35,2,3,35,25,20,0}});
		configs.add(new Object[]{206, 38, new int[]{10020,35,2,3,35,25,20,0} ,new int[]{10009,37,2,4,37,27,22,0} ,new int[]{10016,35,2,3,35,25,20,0} ,new int[]{10010,36,2,4,36,26,21,0}});
		configs.add(new Object[]{207, 38, new int[]{10003,37,3,4,37,27,22,0} ,new int[]{10002,37,2,4,37,27,22,0} ,new int[]{10005,37,3,4,37,27,22,0} ,new int[]{10015,35,2,3,35,25,20,0}});
		configs.add(new Object[]{208, 38, new int[]{10013,37,2,4,37,27,22,0} ,new int[]{10017,37,2,4,37,27,22,0} ,new int[]{10001,37,3,4,37,27,22,0} ,new int[]{10014,34,3,3,34,24,19,0}});
		configs.add(new Object[]{209, 38, new int[]{10002,36,2,4,36,26,21,0} ,new int[]{10015,37,2,4,37,27,22,0} ,new int[]{10003,37,3,4,37,27,22,0} ,new int[]{10014,35,3,3,35,25,20,0}});
		configs.add(new Object[]{210, 37, new int[]{10009,34,1,3,34,24,19,0} ,new int[]{10003,33,3,3,33,23,18,0} ,new int[]{10015,35,2,3,35,25,20,0} ,new int[]{10010,34,2,3,34,24,19,0}});
		configs.add(new Object[]{211, 37, new int[]{10014,35,3,3,35,25,20,0} ,new int[]{10016,35,2,3,35,25,20,0} ,new int[]{10002,35,1,3,35,25,20,0} ,new int[]{10012,35,2,3,35,25,20,0}});
		configs.add(new Object[]{212, 37, new int[]{10016,35,2,3,35,25,20,0} ,new int[]{10014,34,2,3,34,24,19,0} ,new int[]{10010,35,2,3,35,25,20,0} ,new int[]{10005,35,3,3,35,25,20,0}});
		configs.add(new Object[]{213, 37, new int[]{10009,35,2,3,35,25,20,0} ,new int[]{10015,35,2,3,35,25,20,0} ,new int[]{10013,36,2,4,36,26,21,0} ,new int[]{10012,34,2,3,34,24,19,0}});
		configs.add(new Object[]{214, 37, new int[]{10009,36,2,4,36,26,21,0} ,new int[]{10020,34,2,3,34,24,19,0} ,new int[]{10010,35,2,3,35,25,20,0} ,new int[]{10016,35,2,3,35,25,20,0}});
		configs.add(new Object[]{215, 37, new int[]{10002,35,2,3,35,25,20,0} ,new int[]{10003,33,3,3,33,23,18,0} ,new int[]{10015,34,2,3,34,24,19,0} ,new int[]{10005,35,3,3,35,25,20,0}});
		configs.add(new Object[]{216, 36, new int[]{10010,33,2,3,33,23,18,0} ,new int[]{10004,34,3,3,34,24,19,0} ,new int[]{10005,30,3,3,30,20,15,0} ,new int[]{10003,30,3,3,30,20,15,0}});
		configs.add(new Object[]{217, 36, new int[]{10004,34,2,3,34,24,19,0} ,new int[]{10002,33,3,3,33,23,18,0} ,new int[]{10011,35,3,3,35,25,20,0} ,new int[]{10003,32,3,3,32,22,17,0}});
		configs.add(new Object[]{218, 36, new int[]{10010,35,2,3,35,25,20,0} ,new int[]{10004,35,3,3,35,25,20,0} ,new int[]{10016,34,3,3,34,24,19,0} ,new int[]{10009,35,3,3,35,25,20,0}});
		configs.add(new Object[]{219, 36, new int[]{10010,35,2,3,35,25,20,0} ,new int[]{10005,34,3,3,34,24,19,0} ,new int[]{10014,36,3,3,36,26,21,0} ,new int[]{10011,34,1,3,34,24,19,0}});
		configs.add(new Object[]{220, 36, new int[]{10010,36,2,3,36,26,21,0} ,new int[]{10017,36,2,4,36,26,21,0} ,new int[]{10015,35,2,3,35,25,20,0} ,new int[]{10012,33,1,3,33,23,18,0}});
		configs.add(new Object[]{221, 36, new int[]{10002,35,1,3,35,25,20,0} ,new int[]{10001,34,3,3,34,24,19,0} ,new int[]{10014,34,3,3,34,24,19,0} ,new int[]{10011,35,1,3,35,25,20,0}});
		configs.add(new Object[]{222, 35, new int[]{10010,32,2,3,32,22,17,0} ,new int[]{10004,33,3,3,33,23,18,0} ,new int[]{10005,32,3,3,32,22,17,0} ,new int[]{10003,33,3,3,33,23,18,0}});
		configs.add(new Object[]{223, 35, new int[]{10004,33,2,3,33,23,18,0} ,new int[]{10002,32,3,3,32,22,17,0} ,new int[]{10011,32,3,3,32,22,17,0} ,new int[]{10003,30,3,3,30,20,15,0}});
		configs.add(new Object[]{224, 35, new int[]{10010,31,2,3,31,21,16,0} ,new int[]{10004,31,3,3,31,21,16,0} ,new int[]{10016,33,3,3,33,23,18,0} ,new int[]{10009,31,3,3,31,21,16,0}});
		configs.add(new Object[]{225, 35, new int[]{10010,30,2,3,30,20,15,0} ,new int[]{10005,30,3,3,30,20,15,0} ,new int[]{10014,33,3,3,33,23,18,0} ,new int[]{10011,32,1,3,32,22,17,0}});
		configs.add(new Object[]{226, 35, new int[]{10010,31,2,3,31,21,16,0} ,new int[]{10017,32,2,3,32,22,17,0} ,new int[]{10015,31,2,3,31,21,16,0} ,new int[]{10012,30,1,3,30,20,15,0}});
		configs.add(new Object[]{227, 35, new int[]{10017,32,2,3,32,22,17,0} ,new int[]{10015,35,2,3,35,25,20,0} ,new int[]{10003,32,3,3,32,22,17,0} ,new int[]{10014,31,3,3,31,21,16,0}});
		configs.add(new Object[]{228, 34, new int[]{10015,33,2,3,33,23,18,0} ,new int[]{10002,33,1,3,33,23,18,0} ,new int[]{10014,32,3,3,32,22,17,0} ,new int[]{10003,31,3,3,31,21,16,0}});
		configs.add(new Object[]{229, 34, new int[]{10020,31,2,3,31,21,16,0} ,new int[]{10018,32,3,3,32,22,17,0} ,new int[]{10015,30,2,3,30,20,15,0} ,new int[]{10005,32,3,3,32,22,17,0}});
		configs.add(new Object[]{230, 34, new int[]{10010,30,2,3,30,20,15,0} ,new int[]{10016,31,2,3,31,21,16,0} ,new int[]{10014,31,3,3,31,21,16,0} ,new int[]{10005,32,3,3,32,22,17,0}});
		configs.add(new Object[]{231, 34, new int[]{10013,31,2,3,31,21,16,0} ,new int[]{10009,30,3,3,30,20,15,0} ,new int[]{10015,32,3,3,32,22,17,0} ,new int[]{10012,33,3,3,33,23,18,0}});
		configs.add(new Object[]{232, 34, new int[]{10010,32,2,3,32,22,17,0} ,new int[]{10009,32,3,3,32,22,17,0} ,new int[]{10020,32,3,3,32,22,17,0} ,new int[]{10016,30,3,3,30,20,15,0}});
		configs.add(new Object[]{233, 33, new int[]{10010,32,2,3,32,22,17,0} ,new int[]{10012,32,2,3,32,22,17,0} ,new int[]{10015,30,2,3,30,20,15,0} ,new int[]{10009,30,2,3,30,20,15,0}});
		configs.add(new Object[]{234, 33, new int[]{10009,32,2,3,32,22,17,0} ,new int[]{10000,30,2,3,30,20,15,0} ,new int[]{10003,31,3,3,31,21,16,0} ,new int[]{10010,30,2,3,30,20,15,0}});
		configs.add(new Object[]{235, 33, new int[]{10015,30,2,3,30,20,15,0} ,new int[]{10012,32,2,3,32,22,17,0} ,new int[]{10013,31,2,3,31,21,16,0} ,new int[]{10002,31,2,3,31,21,16,0}});
		configs.add(new Object[]{236, 33, new int[]{10014,31,3,3,31,21,16,0} ,new int[]{10011,31,2,3,31,21,16,0} ,new int[]{10001,30,3,3,30,20,15,0} ,new int[]{10009,32,2,3,32,22,17,0}});
		configs.add(new Object[]{237, 33, new int[]{10002,32,1,3,32,22,17,0} ,new int[]{10015,32,2,3,32,22,17,0} ,new int[]{10003,30,3,3,30,20,15,0} ,new int[]{10014,30,3,3,30,20,15,0}});
		configs.add(new Object[]{238, 32, new int[]{10015,32,2,3,32,22,17,0} ,new int[]{10002,29,3,3,29,19,14,0} ,new int[]{10005,29,3,3,29,19,14,0} ,new int[]{10017,31,3,3,31,21,16,0}});
		configs.add(new Object[]{239, 32, new int[]{10014,29,3,3,29,19,14,0} ,new int[]{10017,30,3,3,30,20,15,0} ,new int[]{10013,28,3,3,28,18,13,0} ,new int[]{10001,30,3,3,30,20,15,0}});
		configs.add(new Object[]{240, 32, new int[]{10002,30,2,3,30,20,15,0} ,new int[]{10009,28,3,3,28,18,13,0} ,new int[]{10016,29,3,3,29,19,14,0} ,new int[]{10015,30,3,3,30,20,15,0}});
		configs.add(new Object[]{241, 32, new int[]{10010,31,2,3,31,21,16,0} ,new int[]{10004,28,3,3,28,18,13,0} ,new int[]{10005,29,3,3,29,19,14,0} ,new int[]{10003,29,3,3,29,19,14,0}});
		configs.add(new Object[]{242, 32, new int[]{10004,32,2,3,32,22,17,0} ,new int[]{10002,29,3,3,29,19,14,0} ,new int[]{10011,29,3,3,29,19,14,0} ,new int[]{10003,29,3,3,29,19,14,0}});
		configs.add(new Object[]{243, 32, new int[]{10009,30,1,3,30,20,15,0} ,new int[]{10002,32,2,3,32,22,17,0} ,new int[]{10015,31,2,3,31,21,16,0} ,new int[]{10004,31,2,3,31,21,16,0}});
		configs.add(new Object[]{244, 32, new int[]{10017,31,2,3,31,21,16,0} ,new int[]{10001,31,3,3,31,21,16,0} ,new int[]{10003,30,3,3,30,20,15,0} ,new int[]{10015,32,2,3,32,22,17,0}});
		configs.add(new Object[]{245, 31, new int[]{10014,28,3,3,28,18,13,0} ,new int[]{10011,30,2,3,30,20,15,0} ,new int[]{10001,30,3,3,30,20,15,0} ,new int[]{10009,28,2,3,28,18,13,0}});
		configs.add(new Object[]{246, 31, new int[]{10002,29,1,3,29,19,14,0} ,new int[]{10015,29,2,3,29,19,14,0} ,new int[]{10003,29,3,3,29,19,14,0} ,new int[]{10014,29,3,3,29,19,14,0}});
		configs.add(new Object[]{247, 31, new int[]{10018,30,3,3,30,20,15,0} ,new int[]{10020,30,2,3,30,20,15,0} ,new int[]{10005,28,3,3,28,18,13,0} ,new int[]{10015,27,2,3,27,17,12,0}});
		configs.add(new Object[]{248, 31, new int[]{10016,29,2,3,29,19,14,0} ,new int[]{10010,28,2,3,28,18,13,0} ,new int[]{10005,29,3,3,29,19,14,0} ,new int[]{10014,29,3,3,29,19,14,0}});
		configs.add(new Object[]{249, 31, new int[]{10009,27,3,3,27,17,12,0} ,new int[]{10013,30,2,3,30,20,15,0} ,new int[]{10012,30,3,3,30,20,15,0} ,new int[]{10015,30,3,3,30,20,15,0}});
		configs.add(new Object[]{250, 30, new int[]{10003,27,3,3,27,17,12,0} ,new int[]{10009,29,1,3,29,19,14,0} ,new int[]{10010,28,2,3,28,18,13,0} ,new int[]{10015,29,2,3,29,19,14,0}});
		configs.add(new Object[]{251, 30, new int[]{10016,27,2,3,27,17,12,0} ,new int[]{10014,28,3,3,28,18,13,0} ,new int[]{10012,28,2,3,28,18,13,0} ,new int[]{10002,30,1,3,30,20,15,0}});
		configs.add(new Object[]{252, 30, new int[]{10014,27,2,3,27,17,12,0} ,new int[]{10016,27,2,3,27,17,12,0} ,new int[]{10005,28,3,3,28,18,13,0} ,new int[]{10010,29,2,3,29,19,14,0}});
		configs.add(new Object[]{253, 30, new int[]{10015,27,2,3,27,17,12,0} ,new int[]{10009,29,2,3,29,19,14,0} ,new int[]{10012,28,2,3,28,18,13,0} ,new int[]{10013,28,2,3,28,18,13,0}});
		configs.add(new Object[]{254, 30, new int[]{10020,27,2,3,27,17,12,0} ,new int[]{10009,28,2,3,28,18,13,0} ,new int[]{10016,28,2,3,28,18,13,0} ,new int[]{10010,29,2,3,29,19,14,0}});
		configs.add(new Object[]{255, 30, new int[]{10015,27,2,3,27,17,12,0} ,new int[]{10002,27,2,3,27,17,12,0} ,new int[]{10014,28,3,3,28,18,13,0} ,new int[]{10003,30,3,3,30,20,15,0}});
		configs.add(new Object[]{256, 29, new int[]{10011,26,1,3,26,16,11,0} ,new int[]{10017,28,2,3,28,18,13,0} ,new int[]{10015,27,2,3,27,17,12,0} ,new int[]{10000,29,2,3,29,19,14,0}});
		configs.add(new Object[]{257, 29, new int[]{10017,26,2,3,26,16,11,0} ,new int[]{10001,28,3,3,28,18,13,0} ,new int[]{10003,27,3,3,27,17,12,0} ,new int[]{10015,29,2,3,29,19,14,0}});
		configs.add(new Object[]{258, 29, new int[]{10010,26,2,3,26,16,11,0} ,new int[]{10012,28,2,3,28,18,13,0} ,new int[]{10015,27,2,3,27,17,12,0} ,new int[]{10009,29,2,3,29,19,14,0}});
		configs.add(new Object[]{259, 29, new int[]{10009,26,2,3,26,16,11,0} ,new int[]{10000,28,2,3,28,18,13,0} ,new int[]{10003,27,3,3,27,17,12,0} ,new int[]{10010,28,2,3,28,18,13,0}});
		configs.add(new Object[]{260, 29, new int[]{10014,26,3,3,26,16,11,0} ,new int[]{10011,28,2,3,28,18,13,0} ,new int[]{10005,27,3,3,27,17,12,0} ,new int[]{10010,29,2,3,29,19,14,0}});
		configs.add(new Object[]{261, 28, new int[]{10014,25,3,3,25,15,10,0} ,new int[]{10016,27,2,3,27,17,12,0} ,new int[]{10002,26,1,3,26,16,11,0} ,new int[]{10012,28,2,3,28,18,13,0}});
		configs.add(new Object[]{262, 28, new int[]{10016,25,2,3,25,15,10,0} ,new int[]{10014,27,2,3,27,17,12,0} ,new int[]{10010,26,2,3,26,16,11,0} ,new int[]{10005,28,3,3,28,18,13,0}});
		configs.add(new Object[]{263, 28, new int[]{10009,25,2,3,25,15,10,0} ,new int[]{10015,27,2,3,27,17,12,0} ,new int[]{10013,26,2,3,26,16,11,0} ,new int[]{10012,28,2,3,28,18,13,0}});
		configs.add(new Object[]{264, 28, new int[]{10009,25,2,3,25,15,10,0} ,new int[]{10020,27,2,3,27,17,12,0} ,new int[]{10010,26,2,3,26,16,11,0} ,new int[]{10016,28,2,3,28,18,13,0}});
		configs.add(new Object[]{265, 28, new int[]{10002,25,2,3,25,15,10,0} ,new int[]{10003,27,3,3,27,17,12,0} ,new int[]{10015,26,2,3,26,16,11,0} ,new int[]{10005,28,3,3,28,18,13,0}});
		configs.add(new Object[]{266, 28, new int[]{10017,25,2,3,25,15,10,0} ,new int[]{10013,27,2,3,27,17,12,0} ,new int[]{10014,26,3,3,26,16,11,0} ,new int[]{10001,28,3,3,28,18,13,0}});
		configs.add(new Object[]{267, 28, new int[]{10015,25,2,3,25,15,10,0} ,new int[]{10002,27,2,3,27,17,12,0} ,new int[]{10014,26,3,3,26,16,11,0} ,new int[]{10003,28,3,3,28,18,13,0}});
		configs.add(new Object[]{268, 27, new int[]{10012,24,2,3,24,14,9,0} ,new int[]{10002,26,1,3,26,16,11,0} ,new int[]{10014,25,3,3,25,15,10,0} ,new int[]{10016,27,2,3,27,17,12,0}});
		configs.add(new Object[]{269, 27, new int[]{10005,24,3,3,24,14,9,0} ,new int[]{10010,26,2,3,26,16,11,0} ,new int[]{10016,25,2,3,25,15,10,0} ,new int[]{10014,27,2,3,27,17,12,0}});
		configs.add(new Object[]{270, 27, new int[]{10012,24,2,3,24,14,9,0} ,new int[]{10013,26,2,3,26,16,11,0} ,new int[]{10009,25,2,3,25,15,10,0} ,new int[]{10015,27,2,3,27,17,12,0}});
		configs.add(new Object[]{271, 27, new int[]{10016,24,2,3,24,14,9,0} ,new int[]{10010,26,2,3,26,16,11,0} ,new int[]{10009,25,2,3,25,15,10,0} ,new int[]{10020,27,2,3,27,17,12,0}});
		configs.add(new Object[]{272, 27, new int[]{10005,24,3,3,24,14,9,0} ,new int[]{10015,26,2,3,26,16,11,0} ,new int[]{10002,25,2,3,25,15,10,0} ,new int[]{10003,27,3,3,27,17,12,0}});
		configs.add(new Object[]{273, 26, new int[]{10001,24,3,3,23,13,8,0} ,new int[]{10017,25,2,3,25,15,10,0} ,new int[]{10004,24,2,3,24,14,9,0} ,new int[]{10003,26,3,3,26,16,11,0}});
		configs.add(new Object[]{274, 26, new int[]{10012,24,1,3,23,13,8,0} ,new int[]{10010,25,2,3,25,15,10,0} ,new int[]{10015,24,2,3,24,14,9,0} ,new int[]{10003,26,3,3,26,16,11,0}});
		configs.add(new Object[]{275, 26, new int[]{10000,24,2,3,23,13,8,0} ,new int[]{10010,25,2,3,25,15,10,0} ,new int[]{10009,24,1,3,24,14,9,0} ,new int[]{10003,26,3,3,26,16,11,0}});
		configs.add(new Object[]{276, 26, new int[]{10011,24,1,3,23,13,8,0} ,new int[]{10014,25,3,3,25,15,10,0} ,new int[]{10010,24,2,3,24,14,9,0} ,new int[]{10005,26,3,3,26,16,11,0}});
		configs.add(new Object[]{277, 26, new int[]{10012,24,1,3,23,13,8,0} ,new int[]{10015,25,2,3,25,15,10,0} ,new int[]{10010,24,2,3,24,14,9,0} ,new int[]{10017,26,2,3,26,16,11,0}});
		configs.add(new Object[]{278, 26, new int[]{10011,24,1,3,23,13,8,0} ,new int[]{10014,25,3,3,25,15,10,0} ,new int[]{10002,24,1,3,24,14,9,0} ,new int[]{10001,26,3,3,26,16,11,0}});
		configs.add(new Object[]{279, 26, new int[]{10010,24,2,3,23,13,8,0} ,new int[]{10015,25,2,3,25,15,10,0} ,new int[]{10009,24,1,3,24,14,9,0} ,new int[]{10003,26,3,3,26,16,11,0}});
		configs.add(new Object[]{280, 25, new int[]{10005,24,3,3,22,12,7,0} ,new int[]{10020,24,2,3,24,14,9,0} ,new int[]{10018,24,3,3,23,13,8,0} ,new int[]{10015,25,2,3,25,15,10,0}});
		configs.add(new Object[]{281, 25, new int[]{10005,24,3,3,22,12,7,0} ,new int[]{10010,24,2,3,24,14,9,0} ,new int[]{10016,24,2,3,23,13,8,0} ,new int[]{10014,25,3,3,25,15,10,0}});
		configs.add(new Object[]{282, 25, new int[]{10012,24,3,3,22,12,7,0} ,new int[]{10013,24,2,3,24,14,9,0} ,new int[]{10009,24,3,3,23,13,8,0} ,new int[]{10015,25,3,3,25,15,10,0}});
		configs.add(new Object[]{283, 25, new int[]{10016,24,3,3,22,12,7,0} ,new int[]{10010,24,2,3,24,14,9,0} ,new int[]{10009,24,3,3,23,13,8,0} ,new int[]{10020,25,3,3,25,15,10,0}});
		return configs;
	}
}
