package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.jsonrpc4j.JsonRpcService;
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
@JsonRpcService("/pillage")
public class PillageService extends BaseService {

	@Resource
	private PowerDao powerDao;

	@Resource
	private BattleLogDao battleLogDao;
	
	/**
	 * 可掠夺列表
	 * @return
	 * @throws Throwable
	 */
	public Object list(int itemId) throws Throwable {

		int roleId = this.roleId();

        int serverId = this.serverId();

		Role me = this.roleDao.findOne(roleId);

		if (me.level() < 25) {
			return this.returnError(this.lineNum(), "掠夺功能25级后开放");
		}
		
		List<Role> roles = this.roleDao.get10player(roleId, Integer.valueOf(serverId), me.getLevel(), itemId);

		if (roles.size() < 10) {
			List<Role> roles2 = this.roleDao.get100player(roleId, Integer.valueOf(serverId), me.getLevel());
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

		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		
		Result result = new Result();
		result.setValue("players", players);
		result.setValue("num", me.pillageNum);
		result.setValue("buy_num", buyNum(me.vip, dateNumModel.getPillageBuyNum()));

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
	 * @throws Throwable
	 */
	public Object BattleBegin(int playerId, int heroId1, int heroId2, int heroId3, int heroId4, int power) throws Throwable {

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

	@Transactional(rollbackFor = Throwable.class)
	public Object battleEnd(int battleId, boolean win, int itemId) throws Throwable {

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

    	// 是否发材料
    	int playerId = battleLog.getRoleId2();

    	Role role = this.roleDao.findOne(roleId);
    	Role role2 = this.roleDao.findOne(playerId);
    	
    	double f = ((double)role2.getLevel() - (double)role.getLevel() + 10d) * 0.015 + 0.3;

    	Random random = new Random();
    	if (random.nextFloat() < f) {

        	this.itemDao.addItem(role.getRoleId(), itemId, 1, result);
    	}

    	return this.success(result.toMap());
	}

	/**
	 * 
	 * @param n
	 * @return
	 * @throws Throwable
	 */
	public Object buy(int n) throws Throwable {
		
		int roleId = this.roleId();

		Role role = this.roleDao.findOne(roleId);
		
		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

		int buyNum = buyNum(role.vip, dateNumModel.getPillageBuyNum());

		if (buyNum < n) {
			return this.returnError(this.lineNum(), "购买次数不足");
		}

		if (role.gold < 10 * n) {
			return this.returnError(2, ErrorResult.NotEnoughGold);
		}

		this.roleDao.subGold(role, 10 * n, "购买掠夺次数", 0);
		role.pillageNum += n;

		Result result = new Result();

		this.roleDao.update(role, result);

		dateNumModel.setPillageBuyNum(dateNumModel.getPillageBuyNum() + n);
		this.dateNumDao.save(roleId, dateNumModel);

		result.setValue("buy_num", buyNum(role.vip, dateNumModel.getPillageBuyNum()));
		return this.success(result.toMap());
	}

	/**
	 * 
	 * @param hour
	 * @return
	 * @throws Throwable
	 */
	public Object protect(String tokenS, int hour) throws Throwable {

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
		this.roleDao.subGold(role, gold, "免疫掠夺", 0);
		role.pillageTime = (int)((time + System.currentTimeMillis()) / 1000);

		Result result = new Result();
		this.roleDao.update(role, result);

		result.setValue("protect_time", time);
		return this.success(result.toMap());
	}

	public void addP() {
		this.roleDao.addPillage(20);
	}

	private int buyNum(int vip, int todayNum) {

		if (vip <= 5) {
			return 0;
		}
		return (vip - 5) * 10 - todayNum;
	}
}
