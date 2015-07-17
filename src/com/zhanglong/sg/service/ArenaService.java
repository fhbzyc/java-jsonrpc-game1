package com.zhanglong.sg.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import websocket.handler.Broadcast;

import com.zhanglong.sg.dao.AchievementDao;
import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.ArenaLogDao;
import com.zhanglong.sg.dao.BaseActivityDao;
import com.zhanglong.sg.dao.KillRankDao;
import com.zhanglong.sg.dao.MailDao;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.dao.SkillDao;
import com.zhanglong.sg.entity.ArenaLog;
import com.zhanglong.sg.entity.BaseActivity;
import com.zhanglong.sg.entity.BattleLog;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.KillRank;
import com.zhanglong.sg.entity.Mail;
import com.zhanglong.sg.entity.RankLog;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.model.ArenaPlayerModel;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
public class ArenaService extends BaseService {

    private static int MAXCHALLENGER_NUM = 5;
    private static long BATTLE_MAX_TIME = 240l * 1000l;

	@Resource
	private AchievementDao achievementDao;

    @Resource
    private ArenaDao arenaDao;

    @Resource
    private ArenaLogDao arenaLogDao;

    @Resource
    private PowerDao powerDao;

    @Resource
    private ServerDao serverDao;

    @Resource
    private BaseActivityDao baseActivityDao;

    @Resource
    private MailDao mailDao;

	@Resource
	private SkillDao skillDao;

	@Resource
	private KillRankDao killRankDao;

    /**
     * 
     * @return
     * @throws Exception
     */
    public Object list() throws Exception {

        int roleId = this.roleId();
        int serverId = this.serverId();

        Role role = this.roleDao.findOne(roleId);
        if (role.level() < 10) {
  //      	return this.returnError(this.lineNum(), "不到10级不能开竞技场");
        }

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

        long time= dateNumModel.arenaTime - System.currentTimeMillis();
        if (time < 0) {
            time = 0;
        }
        time /= 1000;

        HashMap<String, Object> playinfo = new HashMap<String,Object>();

		int myIndex = this.arenaDao.getIndex(roleId, serverId);

		if (role.rank == 0) {
			role.rank = myIndex + 1;
		}

		List<Integer> roleIds = this.arenaDao.getList(serverId);

		List<Integer> roleIds2 = new ArrayList<Integer>();

		int start = myIndex - 5;
		if (start < 0) {
			start = 0;
		}

		List<Integer> ranks = new ArrayList<Integer>();

		if (myIndex <= 20) {
			for (int i = start; i < myIndex; i++) {
				roleIds2.add(roleIds.get(i));
				ranks.add(i + 1);
			}
		} else {
			Random random = new Random();

			HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
			
			while (true) {
				
				if (map.size() >= 5) {
					break;
				}

				int index = (int)(myIndex * (random.nextInt(46) + 50) / 100);
				
				if (map.get(index) != null) {
					continue;
				}

				map.put(index, true);

				roleIds2.add(roleIds.get(index));
				ranks.add(index + 1);
			}
		}

		roleIds2.add(roleId);

		List<ArenaPlayerModel> players = this.arenaDao.getPlayers(roleIds2, serverId);

		ArenaPlayerModel me = new ArenaPlayerModel();

		for (int i = 0 ; i < players.size() ; i++) {
			ArenaPlayerModel arenaPlayerModel = players.get(i);

			if (arenaPlayerModel.roleId == roleId) {
				me = arenaPlayerModel;
			} else {
				players.get(i).rank = ranks.get(i);
			}
		}

		players.remove(me);

		List<Object> heros = new ArrayList<Object>();
		heros.add(null);
		heros.add(null);
		heros.add(null);
		heros.add(null);
		
		List<Hero> heroList = this.heroDao.findAll(roleId);
		

		for (Hero hero : heroList) {
			if (hero.getIsBattle()) {
				if (hero.getPosition() > 0) {
					heros.set(hero.getPosition() - 1, hero.toArray());
				}
			}
		}

        playinfo.put("maxrank", role.rank);
        playinfo.put("playrank", myIndex + 1);
        playinfo.put("challenge", dateNumModel.arenaBattleNum);
        playinfo.put("challengetime", time);

        Result result = new Result();

        result.setValue("hero", heros);
        result.setValue("enemyList", players);
        result.setValue("playinfo", playinfo);
        result.setValue("gold1", this.gold1(roleId));
        result.setValue("gold2", this.gold2(roleId));

        return this.success(result.toMap());
    }

    /**
     * 玩家的防守阵容
     * @param roleId
     * @return
     * @throws Exception
     */
    public Object hero(int roleId) throws Exception {

		List<Object> heros = new ArrayList<Object>();
		heros.add(null);
		heros.add(null);
		heros.add(null);
		heros.add(null);

		List<Hero> heroList = this.heroDao.findAll(roleId);

		for (Hero hero : heroList) {
			if (hero.getIsBattle()) {
				if (hero.getPosition() > 0) {
					heros.set(hero.getPosition() - 1, hero.toArray());
				}
			}
		}

        Result result = new Result();

        result.setValue("hero", heros);
        return this.success(result);
    }

    /**
     * 设置上阵武将
     * @param heroId1
     * @param heroId2
     * @param heroId3
     * @param heroId4
     * @return
     * @throws Exception
     */
    public Object setDefense(int heroId1, int heroId2, int heroId3, int heroId4) throws Exception {

        int roleId = this.roleId();

        Result result = new Result();

        List<Hero> list = this.heroDao.findAll(roleId);

        for (Hero hero : list) {
    	  
      	    int id = hero.getHeroId();

		    if (id != heroId1 && id != heroId2 && id != heroId3 && id != heroId4) {
		    	hero.setIsBattle(false);
		    	hero.setPosition(0);
            	this.heroDao.update(hero, result);
		    } else {

		    	hero.setIsBattle(true);

		    	if (id == heroId1) {
		    		hero.setPosition(1);
		    	} else if (id == heroId2) {
		    		hero.setPosition(2);
		    	} else if (id == heroId3) {
		    		hero.setPosition(3);
		    	} else if (id == heroId4) {
		    		hero.setPosition(4);
		    	}

                this.heroDao.update(hero, result);
    	    }
        }

        ArrayList<Object> heros = new ArrayList<Object>();

        for (Hero hero : list) {
        	
        	int id = hero.getHeroId();
        	if (id == heroId1 || id == heroId2 || id == heroId3 || id == heroId4) {
        		heros.add(hero.toArray());
        	}
	    }

        result.setValue("hero", heros);
        return this.success(result.toMap());
    }

    /**
     * 清除冷却时间
     * @return
     * @throws Exception
     */
    public Object reset() throws Exception {

        int roleId = this.roleId();

        int gold = 50;

        Role role = this.roleDao.findOne(roleId);

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

        if (dateNumModel.arenaBattleNum >= MAXCHALLENGER_NUM) {

            if (role.vip < 2) {
            	return this.returnError(2, "需要VIP等级为2时才可购买【竞技场】挑战次数，现在就成为VIP2？");

            } else if (dateNumModel.buyArenaNum >= role.getVip() - 1) {
            	return this.returnError(2, "升级VIP等级可提升【竞技场】购买次数，前去充值？");

            } else {

                gold = this.gold2(roleId);

                if (role.getGold() < gold) {

                    return this.returnError(this.lineNum(), "元宝不足，前去充值？");
                } else {
                     this.addGold2(roleId);
                }
            }

        } else {

            if (role.vip < 1) {
                return this.returnError(this.lineNum(), "VIP才可清除冷却时间，现在就成为VIP？");
            }

            gold = this.gold1(roleId);

            if (role.getGold() < gold) {
            	return this.returnError(this.lineNum(), "元宝不足，前去充值？");
            } else {
            	this.addGold1(roleId);
            }
        }

        Result result = new Result();

        this.roleDao.subGold(role, gold, "购买竞技场挑战次数", FinanceLog.STATUS_ARENA_BUY, result);
    //    this.roleDao.update(role, result);

        HashMap<String, Object> playinfo = new HashMap<String,Object>();
        playinfo.put("challenge", this.dateNumDao.findOne(roleId).arenaBattleNum);
        playinfo.put("challengetime", 0);

        result.setValue("playinfo", playinfo);
        result.setValue("good1", this.gold1(roleId));
        result.setValue("good2", this.gold2(roleId));
        return this.success(result.toMap());
    }

    /**
     * 
     * @param rank
     * @param playerId
     * @param heroId1
     * @param herId2
     * @param heroId3
     * @param heroId4
     * @param power
     * @return
     * @throws Exception
     */
    public Object battleBegin(int rank, int playerId, int heroId1, int heroId2, int heroId3, int heroId4, int power) throws Exception {

        int myRoleId = this.roleId();

        List<Hero> heros = new ArrayList<Hero>();
        if (heroId1 > 0) {
        	heros.add(heroDao.findOne(myRoleId, heroId1));
        }
        if (heroId2 > 0) {
        	heros.add(heroDao.findOne(myRoleId, heroId2));
        }
        if (heroId3 > 0) {
        	heros.add(heroDao.findOne(myRoleId, heroId3));
        }
        if (heroId4 > 0) {
        	heros.add(heroDao.findOne(myRoleId, heroId4));
        }

        Role role = this.roleDao.findOne(myRoleId);

        this.powerDao.save(role, power, heros);

        int serverId = this.serverId();

        List<Integer> list = this.arenaDao.getList(serverId);

        Integer heRoleId = list.get(rank - 1);
        if (heRoleId != playerId) {
            return this.returnError(this.lineNum(), "排名有变动");
        }

		ArenaLog log = this.arenaLogDao.findByRoleId2(playerId);
        if (log != null && log.getBattleResult() == BattleLog.BATTLE_LOG_INIT && System.currentTimeMillis() - log.getBeginTime() < BATTLE_MAX_TIME) {
            return this.returnError(this.lineNum(), "该玩家正在被其他人挑战");
        }

        DateNumModel dateNumModel = this.dateNumDao.findOne(myRoleId);

        if (System.currentTimeMillis() < dateNumModel.arenaTime) {
            return this.returnError(this.lineNum(), "挑战时间未到");
        } else if (dateNumModel.arenaBattleNum >= MAXCHALLENGER_NUM) {
            return this.returnError(this.lineNum(), "挑战次数不足");
        } else {
        	dateNumModel.arenaBattleNum += 1;
        	dateNumModel.arenaTime = System.currentTimeMillis() + 600l * 1000l;

            this.dateNumDao.save(myRoleId, dateNumModel);
        }

        ArenaLog arenaLogTable = new ArenaLog();
        arenaLogTable.setBattleResult(BattleLog.BATTLE_LOG_INIT);
        arenaLogTable.setBeginTime(System.currentTimeMillis());
        arenaLogTable.setRoleId1(myRoleId);
        arenaLogTable.setRoleId2(heRoleId);
        arenaLogTable.setRank1(this.arenaDao.getIndex(myRoleId, serverId) + 1);
        arenaLogTable.setRank2(this.arenaDao.getIndex(heRoleId, serverId) + 1);
        arenaLogTable.setData("");

        // 我方的头像 名字

        arenaLogTable.setAvatar1(role.getAvatar());
        arenaLogTable.setName1(role.getName());

        // 对手的头像 名字
        ArenaPlayerModel arenaPlayerModel = new ArenaPlayerModel();
        arenaPlayerModel.roleId = heRoleId;
        arenaPlayerModel.rank = rank;

        Result result = new Result();

        if (!this.roleDao.isPlayer(heRoleId)) {
        	this.arenaDao.toPlayer(arenaPlayerModel, serverId);
        	result.setValue("he_combo_skill", this.skillDao.comboSkills(heRoleId));
        } else {
    		ArrayList<Object> heHeros = new ArrayList<Object>();
    		heHeros.add(null);
    		heHeros.add(null);
    		heHeros.add(null);
    		heHeros.add(null);
    		
    		List<Hero> heroList = this.heroDao.findAll(heRoleId);

    		for (Hero hero : heroList) {
    			if (hero.getIsBattle()) {
    				if (hero.getPosition() > 0) {
    					heHeros.set(hero.getPosition() - 1, hero.toArray());
    				}
    			}
    		}
    		arenaPlayerModel.generalList = heHeros;

    		result.setValue("he_combo_skill", new int[]{});
        }

        arenaLogTable.setAvatar2(arenaPlayerModel.avatar);
        arenaLogTable.setName2(arenaPlayerModel.name);

        this.arenaLogDao.create(arenaLogTable);

        result.setValue("hero", arenaPlayerModel.generalList);
        result.setValue("battle_id", arenaLogTable.getId());
        return this.success(result.toMap());
    }
    
    /**
     * 提交结果
     * @param battleId
     * @param win
     * @param battleData
     * @return
     * @throws Exception
     */
    public Object battleEnd(int battleId, boolean win, String battleData) throws Exception {

        int roleId = this.roleId();

        ArenaLog log = this.arenaLogDao.findOne(battleId);

        if (log == null) {
            return this.returnError(this.lineNum(), "参数出错,没有这场战斗");
        } else if (log.getBattleResult() != BattleLog.BATTLE_LOG_INIT) {
            return this.returnError(this.lineNum(), "不能重复提交结果");
        }

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        
        // 竞技场每日任务
        this.dailyTaskDao.pk(role, result);

        result.setValue("win", false);
        result.setValue("overtime", false);

        log.setData(battleData);
        log.setEndTime(System.currentTimeMillis());

        if (!win) {
            log.setBattleResult(BattleLog.BATTLE_LOG_LOST);

            this.arenaLogDao.update(log);
            return this.success(result.toMap());
        }

        long time = System.currentTimeMillis();
        if (time - log.getBeginTime() > BATTLE_MAX_TIME) {

        	result.setValue("overtime", true);
        	return this.success(result.toMap());
        }

        log.setBattleResult(BattleLog.BATTLE_LOG_WIN);
        log.setData(battleData);
        log.setEndTime(System.currentTimeMillis());

        this.arenaLogDao.update(log);

        // 更改排名
        int serverId = this.serverId();
        
        int oldRank = role.getRank();
        this.arenaDao.changeIndex(roleId, log.getRoleId2(), serverId);

        role = this.roleDao.findOne(roleId);
        if (role.getRank() < oldRank) {

            // 刷新成就
    		this.achievementDao.setNum(role.getRoleId(), BaseAchievement.TYPE_PK, 0, role.getRank(), result);
        }

        int wins = this.arenaLogDao.wins(roleId);

        if (wins == 10) {
    		String msgs = role.name + "在竞技场大杀四方,快来人终结他吧";
    		Result r = new Result();
    		r.setValue("msgs", msgs);
    		String msg = Response.marshalSuccess(0, r.toMap());
    		Broadcast broadcast = new Broadcast();
    		broadcast.send(roleId, serverId, msg);
        } else if (wins == 3) {
    		String msgs = role.name + "在竞技场获得3连胜,上升势头强劲";
    		Result r = new Result();
    		r.setValue("msgs", msgs);
    		String msg = Response.marshalSuccess(0, r.toMap());
    		Broadcast broadcast = new Broadcast();
    		broadcast.send(roleId, serverId, msg);
        }

        result.setValue("win", true);
        result.setValue("newrank", log.getRank2());
        result.setValue("oldrank", log.getRank1());
        return this.success(result.toMap());
    }

    /**
     * 排名变化
     * @param page
     * @return
     * @throws Exception
     */
    public Object changeLogs(int page) throws Exception {

        int roleId = this.roleId();

        if (page < 1) {
            page = 1;
        }

        List<ArenaLog> list = this.arenaLogDao.getByRoleId(roleId, page);

        ArrayList<Object[]> logs = new ArrayList<Object[]>();

        for (ArenaLog arenaLog : list) {
            Object[] temp = new Object[]{arenaLog.getId() , 0 , 0 , 0 , "" , arenaLog.getEndTime() , false};
            if (arenaLog.getRoleId1().equals(roleId)) {
                // 我是攻方

                if (arenaLog.getAvatar2() != null) {
                    temp[3] = arenaLog.getAvatar2();
                }
                if (arenaLog.getName2() != null) {
                    temp[4] = arenaLog.getName2();
                }

                if (arenaLog.getBattleResult() == BattleLog.BATTLE_LOG_WIN) {
                    temp[6] = true;
                    temp[1] = arenaLog.getRank2(); // 旧排名
                    temp[2] = arenaLog.getRank1(); // 新排名
                } else {
                    temp[1] = arenaLog.getRank1();
                    temp[2] = arenaLog.getRank1();
                }

            } else {

                if (arenaLog.getAvatar1() != null) {
                    temp[3] = arenaLog.getAvatar1();
                }
                if (arenaLog.getName1() != null) {
                    temp[4] = arenaLog.getName1();
                }

                if (arenaLog.getBattleResult() == BattleLog.BATTLE_LOG_LOST) {
                    temp[6] = true;
                    temp[1] = arenaLog.getRank2(); // 旧排名
                    temp[2] = arenaLog.getRank2(); // 新排名
                } else {
                    temp[1] = arenaLog.getRank1(); // 旧排名
                    temp[2] = arenaLog.getRank2(); // 新排名
                }
            }

            logs.add(temp);
        }

        Result result = new Result();
        result.setValue("logs", logs);
        return this.success(result.toMap());
    }

    /**
     * 查看战斗数据
     * @param logId
     * @return
     * @throws Exception
     */
    public Object battleData(int logId) throws Exception {

        ArenaLog log = this.arenaLogDao.findOne(logId);

        if (log == null) {
            return this.returnError(this.lineNum(), "参数出错,没有这场战斗");
        } else if (log.getData().equals(null)) {
            return this.returnError(this.lineNum(), "没有战斗数据");
        }

        return this.success(log.getData());
    }

    /**
     * 三个排行一起发
     * @return
     * @throws Exception
     */
    public Object allRank() throws Exception {

        int roleId = this.roleId();
        int serverId = this.serverId();

        // 实时排行
        List<Integer> list = this.arenaDao.getList(serverId);

        int max = 20;
        if (list.size() < max) {
            max = list.size();
        }

        ArrayList<Integer> newList = new ArrayList<Integer>();
        for (int i = 0 ; i < max ; i++) {
        	newList.add(list.get(i));
        }

        HashSet<Integer> set = new HashSet<Integer>();
        for (Integer id : newList) {
        	set.add(id);
		}

    	// 等级排行
        List<Role> levelList = this.roleDao.expTop20(serverId);

        for (Role role : levelList) {
        	set.add(role.getRoleId());
		}

        List<Integer> roleIds = new ArrayList<Integer>();
        for (Integer id : set) {
        	roleIds.add(id);
		}

        List<ArenaPlayerModel> players = this.arenaDao.getPlayers(roleIds, serverId);

        ArrayList<ArenaPlayerModel> nowPlayers = new ArrayList<ArenaPlayerModel>();
        for (int i = 0 ; i < newList.size() ; i++) {
        	for (ArenaPlayerModel player : players) {
        		if (player.roleId == newList.get(i))  {
        			player.rank = i + 1;
        			nowPlayers.add(player);
        			break;
        		}
			}
		}
        
        HashMap<String, Object> nowMap = new HashMap<String, Object>();
        nowMap.put("players", nowPlayers);
        nowMap.put("myrank", this.arenaDao.getIndex(roleId, serverId) + 1);

        ArrayList<ArenaPlayerModel> levelPlayers = new ArrayList<ArenaPlayerModel>();
        for (int i = 0 ; i < levelList.size() ; i++) {
        	for (ArenaPlayerModel player : players) {
        		if (player.roleId == levelList.get(i).getRoleId())  {
        			ArenaPlayerModel newPlayer = (ArenaPlayerModel) player.clone();
        			newPlayer.rank = i + 1;
        			levelPlayers.add(newPlayer);
        			break;
        		}
			}
		}

        HashMap<String, Object> levelMap = new HashMap<String, Object>();
        levelMap.put("players", levelPlayers);

        int myLevelIndex = 0;
        for (int index = 0 ; index < levelList.size() ; index++) {

            if (levelList.get(index).getRoleId().equals(roleId)) {
            	myLevelIndex = index + 1;
            }
        }

        if (myLevelIndex == 0) {
        	Role role = this.roleDao.findOne(roleId);
        	myLevelIndex = this.roleDao.countExpAfter(role.getExp(), serverId) + 1;
        }

        levelMap.put("myrank", myLevelIndex);

        RankLog rankLog = this.arenaDao.getLastRank(serverId);
        ObjectMapper objectMapper = new ObjectMapper();

        List<ArenaPlayerModel> yesterdayPlayers = new ArrayList<ArenaPlayerModel>();
        if (rankLog != null) {
        	yesterdayPlayers = objectMapper.readValue(rankLog.getData(),  new TypeReference<List<ArenaPlayerModel>>(){});
        }

        List<Integer> yesterdayRank = new ArrayList<Integer>();
        if (rankLog != null) {
        	yesterdayRank = objectMapper.readValue(rankLog.getRank(),  new TypeReference<List<Integer>>(){});
        }

        int myRank = 0;
        for (int i = 0 ; i < yesterdayRank.size() ; i++) {
			if (roleId == yesterdayRank.get(i)) {
				myRank = i + 1;
			}
		}

        HashMap<String, Object> yesterDayMap = new HashMap<String, Object>();
        yesterDayMap.put("players", yesterdayPlayers);
        yesterDayMap.put("myrank", myRank);

        Result result = new Result();
        result.setValue("now", nowMap);
        result.setValue("yesterday", yesterDayMap);
        result.setValue("level", levelMap);
        return this.success(result.toMap());
    }

    /**
     * 杀怪排行榜
     * @return
     * @throws Exception
     */
    public Object killRank() throws Exception {

        int roleId = this.roleId();
        int serverId = this.serverId();

    	// 等级排行
        List<KillRank> list = this.killRankDao.top20(serverId);

//        List<Integer> roleIds = new ArrayList<Integer>();
//        for (KillRank r : list) {
//        	roleIds.add(r.getRoleId());
//		}

        long myRank = 0;
        List<HashMap<String, Object>> newPlayers = new ArrayList<HashMap<String, Object>>();
        for (int i = 0 ; i < list.size() ; i++) {

        	if (list.get(i).getRoleId() == roleId) {
        		myRank = i + 1;
        	}

        	HashMap<String, Object> player = new HashMap<String, Object>();
        	player.put("rank", i + 1);
        	player.put("roleId", list.get(i).getRoleId());
        	player.put("name", list.get(i).getName());
        	player.put("avatar", list.get(i).getAvatar());
        	player.put("level", list.get(i).getLevel());
        	player.put("kill", list.get(i).getNum());
			newPlayers.add(player);
		}

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("players", newPlayers);

        if (myRank == 0) {
        	Role role = this.roleDao.findOne(roleId);
        	myRank = this.killRankDao.countAfter(this.killRankDao.findOne(role).getNum(), serverId) + 1;
        }

        map.put("myrank", myRank);

        Result result = new Result();
        result.setValue("rank", map);
        return this.success(result.toMap());
    }

    // 清除时间的
    private int gold1(int roleId) {

    	int gold = 50;

        int dayNum = 0;


        
		int n = dayNum / 2;
		gold = (int)(50 * Math.pow(2, n));
		if (gold > 400) {
		    gold = 400;
		}
		
		return gold;
    }

    private void addGold1(int roleId) throws JsonParseException, JsonMappingException, IOException {

    	DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

    	dateNumModel.setArenaCd(dateNumModel.getArenaCd() + 1);
    	dateNumModel.arenaTime = System.currentTimeMillis();

    	this.dateNumDao.save(roleId, dateNumModel);
    }

    // 重置次数的
    private int gold2(int roleId) {

    	int gold = 50;


        int dayNum = 0;


        
		int n = dayNum / 2;
		gold = (int)(50 * Math.pow(2, n));
		if (gold > 400) {
		    gold = 400;
		}
		
		return gold;
    }

    private void addGold2(int roleId) throws JsonParseException, JsonMappingException, IOException {

    	DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
    	dateNumModel.buyArenaNum += 1;
    	dateNumModel.arenaBattleNum = 0;
    	dateNumModel.arenaTime = System.currentTimeMillis();

    	this.dateNumDao.save(roleId, dateNumModel);
    }

	public void sendMail() {

		List<Server> list = this.serverDao.findAll();
		for (Server server : list) {
			
			int serverId = server.getId();
			try {

				// ArenaDao arenaDao = ContextLoader.getCurrentWebApplicationContext().getBean(ArenaDao.class);
				//arenaDao.setLastRank(session, server.getId());

				List<Integer> intList = this.arenaDao.getList(serverId);
				
				
				intList = intList.subList(0, 20);
				List<ArenaPlayerModel> players = this.arenaDao.getPlayers(intList, server.getId());

				ObjectMapper objectMapper = new ObjectMapper();
				RankLog rankLog = new RankLog();
				rankLog.setData(objectMapper.writeValueAsString(players));
			//	rankLog.setRank(objectMapper.writeValueAsString(arenaDao.getList(server.getId())));
				rankLog.setServerId(server.getId());
				rankLog.setDate(Integer.valueOf(Utils.date()));
				this.arenaDao.setLastRank(serverId);

				this.sendMail(server.getId());

				List<BaseActivity> activities = this.baseActivityDao.findAll(serverId);
				for (BaseActivity baseAct : activities) {
					
					if (baseAct.getType().equals("kill_rank")) {
						HashMap<Integer, Reward> rewards = objectMapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});

						List<KillRank> roles = this.killRankDao.top20(serverId);

						for (int i = 0 ; i < 10 ; i++) {

							int r = i + 1;

							Reward reward = rewards.get(r);
							if (reward != null) {

								Mail mail = new Mail();
								mail.setAttachment(objectMapper.writeValueAsString(reward));
								mail.setFromName("GM");
								mail.setRoleId(roles.get(i).getRoleId());
								mail.setTitle("【横扫妖怪榜】奖励！");
								mail.setContent("恭喜主公在【横扫妖怪榜】中奋勇杀怪，获得的名次是：" + r + "名；\n"+
"特授予你以下奖励，请笑纳；\n\n"+
"客服：萌小乔");
					            this.mailDao.create(mail);
					        }
						}
					}

					if (baseAct.getType().equals("pk_rank")) {

						HashMap<Integer, Reward> rewards = objectMapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});

						for (int i = 0 ; i < intList.size() ; i++) {
							
							int r = i + 1;

							Reward reward = rewards.get(r);
							if (reward != null) {

								Mail mail = new Mail();
								mail.setAttachment(objectMapper.writeValueAsString(reward));
								mail.setFromName("GM");
								mail.setRoleId(intList.get(i));
								mail.setTitle("【冲榜活动】奖励!");
								mail.setContent("恭喜主公在竞技场大展神威，获得了令人侧目的成绩，你在竞技场的排名为：" + r + "名：\n"+
"竞技场管理处将授予你以下奖励。\n\n"+
"竞技场教官：吕小布");
					            this.mailDao.create(mail);
					        }
						}
						//break;
					}

					if (baseAct.getType().equals("lv_rank")) {

						HashMap<Integer, Reward> rewards = objectMapper.readValue(baseAct.getReward(), new TypeReference<Map<Integer, Reward>>(){});
						
						List<Role> roles = this.roleDao.expTop20(serverId);

						for (int i = 0 ; i < 10 ; i++) {

							int r = i + 1;

							Reward reward = rewards.get(r);
							if (reward != null) {

								Mail mail = new Mail();
								mail.setAttachment(objectMapper.writeValueAsString(reward));
								mail.setFromName("GM");
								mail.setRoleId(roles.get(i).getRoleId());
								mail.setTitle("【冲级活动】奖励！");
								mail.setContent("恭喜主公战队等级快速提升，并且在【冲级活动】中获得好的名次：" + r + "名；\n"+
"特授予你以下奖励。\n\n"+
"客服：萌小乔");
					            this.mailDao.create(mail);
					        }
						}
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void sendMail(int serverId) throws IOException {

		ArrayList<int[]> configs = this.arenaDao.rewardConfig();

        List<Integer> list = new ArrayList<Integer>();

        Session session = this.arenaDao.getSessionFactory().getCurrentSession();

		Criteria criteria = session.createCriteria(RankLog.class);
		@SuppressWarnings("unchecked")
		List<RankLog> rankList = criteria.add(Restrictions.eq("serverId", serverId)).addOrder(Order.desc("id")).list();

		ObjectMapper objectMapper = new ObjectMapper();

		if (rankList.size() != 0) {
			list = objectMapper.readValue(rankList.get(0).getRank(),  new TypeReference<List<Integer>>(){});
		}

		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		for (int[] config : configs) {

			int[] itemId = new int[]{config[7]};
			int[] itemNum = new int[]{config[8]};

			Reward reward = new Reward();
			reward.setCoin(config[5]);
			reward.setGold(config[3]);
			reward.setMoney3(config[6]);
			reward.setItem_id(itemId);
			reward.setItem_num(itemNum);

			String json = objectMapper.writeValueAsString(reward);

			int max = config[2];
			if (max > list.size()) {
				max = list.size();
			}
			
			if (max < config[1]) {
				break;
			}

			String sql = "";

			String from_name = "游戏管理员";
			String title = "竞技场每日排名奖励";

			int parameters = 0;

			for (int rank = config[1] ; rank <= max; rank++) {

				int roleId = list.get(rank - 1);

				if (roleId < 20000) {
					continue;
				}

		    	String content = "截止今天21：00，你的竞技场排名为：" + rank + "名。\n" + 
		    			"鉴于您在竞技场的优秀表现。竞技场管理处将授予您以下奖励。\n\n" + 
		    			"竞技场教官：吕小布";

				if (sql.equals("")) {
					sql = String.format("INSERT INTO role_mail(mail_status,mail_attchment,mail_content,mail_from_name,mail_time,mail_title,role_id) VALUES (0,'%s','%s','%s','%s','%s','%s')",
							json, content, from_name, time, title, roleId);
				} else {
					sql += String.format(",(0,'%s','%s','%s','%s','%s','%s')", json, content, from_name, time, title, roleId);
				}

				parameters++;

				if (parameters >= 500) {
					// 1000条拼成一条
					SQLQuery query = session.createSQLQuery(sql);

					query.executeUpdate();
	
					sql = "";
					parameters = 0;
				}
			}

			if (sql.equals("")) {
				continue;
			}

			SQLQuery query = session.createSQLQuery(sql);

		    query.executeUpdate();
		}
	}
}
