package com.zhanglong.sg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.result.ErrorResult;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.ArenaLogDao;
import com.zhanglong.sg.dao.PowerDao;
import com.zhanglong.sg.entity.ArenaLog;
import com.zhanglong.sg.entity.Arena;
import com.zhanglong.sg.entity.BattleLog;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.ArenaPlayerModel;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.result.Result;

@Service
@JsonRpcService("/arena")
public class ArenaService extends BaseClass {

    private static int MAXCHALLENGER_NUM = 5;
    private static long BATTLE_MAX_TIME = 240l * 1000l;

    @Resource
    private ArenaDao arenaDao;

    @Resource
    private ArenaLogDao arenaLogDao;

    @Resource
    private PowerDao powerDao;

    /**
     * 刷新5个对手
     * @return
     * @throws Throwable
     */
    public Object list() throws Throwable {

        int roleId = this.roleId();
        int serverId = this.serverId();

        Role role = this.roleDao.findOne(roleId);
        if (role.level() < 10 ) {
  //      	throw new Throwable("不到10级不能开竞技场");
        }

        Arena arenaTable = this.arenaDao.findOne(roleId, serverId);

        long time= arenaTable.getBattleTime() - System.currentTimeMillis();
        if (time < 0) {
            time = 0;
        }
        time /= 1000;

        HashMap<String, Object> playinfo = new HashMap<String,Object>();

		int myIndex = this.arenaDao.getIndex(roleId, serverId);

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

		ArrayList<ArenaPlayerModel> players = this.arenaDao.getPlayers(roleIds2, serverId);

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

		ArrayList<Object> heros = new ArrayList<Object>();
		for (Object object : me.generalList) {
			Object[] objects = (Object[])object;
			if ((Integer)objects[0] == (int)arenaTable.getGeneralId1()) {
				heros.add(object);
			}
		}
		for (Object object : me.generalList) {
			Object[] objects = (Object[])object;
			if ((Integer)objects[0] == (int)arenaTable.getGeneralId2()) {
				heros.add(object);
			}
		}
		for (Object object : me.generalList) {
			Object[] objects = (Object[])object;
			if ((Integer)objects[0] == (int)arenaTable.getGeneralId3()) {
				heros.add(object);
			}
		}
		for (Object object : me.generalList) {
			Object[] objects = (Object[])object;
			if ((Integer)objects[0] == (int)arenaTable.getGeneralId4()) {
				heros.add(object);
			}
		}

        playinfo.put("maxrank", arenaTable.getRank());
        playinfo.put("playrank", myIndex + 1);
        playinfo.put("challenge", arenaTable.getBattleNum());
        playinfo.put("challengetime", time);

        Result result = new Result();

        result.setValue("hero", heros);
        result.setValue("enemyList", players);
        result.setValue("playinfo", playinfo);
        result.setValue("gold1", this.gold1(roleId));
        result.setValue("gold2", this.gold2(roleId));

        //Utils.addCustomEvent("jingJiChang", "jinRu", new SimpleDateFormat("HH").format(new Date()), roleId);

        return result.toMap();
    }

    /**
     * 设置上阵武将
     * @param tokenS
     * @param heroId1
     * @param heroId2
     * @param heroId3
     * @param heroId4
     * @return
     * @throws Throwable
     */
    public HashMap<String, Object> setDefense(String tokenS, int heroId1, int heroId2, int heroId3, int heroId4) throws Throwable {

        int roleId = this.roleId();

        Result result = new Result();
        ArrayList<Object> heros = new ArrayList<Object>();
        
        List<Hero> generalList = this.heroDao.findAll(roleId);

        for (Hero general : generalList) {
    	  
      	    int id = general.getHeroId();

      	    if (general.getIsBattle() != Hero.UN_BATTLE) {

    		     if (id != heroId1 && id != heroId2 && id != heroId3 && id != heroId4) {
                    general.setIsBattle(Hero.UN_BATTLE);
                    this.heroDao.update(general, result);
    		    }
    	    } else {
    		    if (id == heroId1 || id == heroId2 || id == heroId3 || id == heroId4) {
                    general.setIsBattle(Hero.ON_BATTLE);
                    this.heroDao.update(general, result);
    		    }
    	    }
        }

        for (Hero hero : generalList) {
        	if (hero.getHeroId() == heroId1) {
        		heros.add(hero.toArray());
        	}
	    }

        for (Hero hero : generalList) {
        	if (hero.getHeroId() == heroId2) {
        		heros.add(hero.toArray());
        	}
	    }
        
        for (Hero hero : generalList) {
        	if (hero.getHeroId() == heroId3) {
        		heros.add(hero.toArray());
        	}
	    }
        
        for (Hero hero : generalList) {
        	if (hero.getHeroId() == heroId4) {
        		heros.add(hero.toArray());
        	}
	    }
        
        Arena arenaTable = this.arenaDao.findOne(roleId, this.serverId());
        arenaTable.setGeneralId1(heroId1);
        arenaTable.setGeneralId2(heroId2);
        arenaTable.setGeneralId3(heroId3);
        arenaTable.setGeneralId4(heroId4);
        this.arenaDao.update(arenaTable);

        result.setValue("hero", heros);
        return result.toMap();
    }

    /**
     * 清除冷却时间
     * @return
     * @throws Throwable
     */
    public Object resetChallengerTime() throws Throwable {

        int roleId = this.roleId();

        int serverId = this.serverId();
        
        Result result = new Result();

        Arena arenaTable = arenaDao.findOne(roleId, serverId);

        int gold = 50;

        Role role = this.roleDao.findOne(roleId);
        
        arenaTable.setBattleTime(System.currentTimeMillis());
        if (arenaTable.getBattleNum() >= MAXCHALLENGER_NUM) {

            if (role.getVip() < 3) {
                com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
                error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
                error.setMessage("需要VIP等级为3时才可购买【竞技场】挑战次数，现在就成为VIP3？");
                return new ErrorResult(error);
            } else if (arenaTable.getBuyNum() >= role.getVip() - 2) {
                com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
                error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
                error.setMessage("升级VIP等级可提升【竞技场】购买次数，前去充值？");
                return new ErrorResult(error);
            } else {

                gold = this.gold2(roleId);

                if (role.getGold() < gold) {
                    com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
                    error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
                    error.setMessage("元宝不足，前去充值？");
                    return new ErrorResult(error);
                } else {
                     this.addGold2(roleId);
                }

                arenaTable.setBuyNum(arenaTable.getBuyNum() + 1);
            }

            arenaTable.setBattleNum(0);

        } else {

            if (role.getVip() < 3) {
                com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
                error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
                error.setMessage("VIP才可清除冷却时间，现在就成为VIP？");
                return new ErrorResult(error);
            }

            gold = this.gold1(roleId);

            if (role.getGold() < gold) {
                com.zhanglong.sg.result.Error error = new com.zhanglong.sg.result.Error();
                error.setCode(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM);
                error.setMessage("元宝不足，前去充值？");
                return new ErrorResult(error);
            } else {
            	this.addGold1(roleId);
            }

        }

        roleDao.subGold(role, gold, "购买竞技场挑战次数", FinanceLog.STATUS_ARENA_BUY);
        roleDao.update(role, result);

        this.arenaDao.update(arenaTable);

        HashMap<String, Object> playinfo = new HashMap<String,Object>();
        playinfo.put("challenge", arenaTable.getBattleNum());
        playinfo.put("challengetime", 0);

        result.setValue("playinfo", playinfo);
        result.setValue("good1", this.gold1(roleId));
        result.setValue("good2", this.gold2(roleId));
        return result.toMap();
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
     * @throws Throwable
     */
    public Object battleBegin(int rank, int playerId, int heroId1, int heroId2, int heroId3, int heroId4, int power) throws Throwable {

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
            throw new Throwable("排名有变动");
        }

		ArenaLog log = this.arenaLogDao.findByRoleId2(playerId);
        if (log != null && log.getBattleResult() == BattleLog.BATTLE_LOG_INIT && System.currentTimeMillis() - log.getBeginTime() < BATTLE_MAX_TIME) {
            throw new Throwable("该玩家正在被其他人挑战");
        }

        Arena arenaTable = this.arenaDao.findOne(myRoleId, serverId);
        if (System.currentTimeMillis() < arenaTable.getBattleTime()) {
            throw new Throwable("挑战时间未到");
        } else if (arenaTable.getBattleNum() >= MAXCHALLENGER_NUM) {
            throw new Throwable("挑战次数不足");
        } else {
            arenaTable.setBattleNum(arenaTable.getBattleNum() + 1);
            arenaTable.setBattleTime(System.currentTimeMillis() + (long)600 * 1000);
            this.arenaDao.update(arenaTable);
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
        this.arenaDao.toPlayer(arenaPlayerModel, serverId);

        arenaLogTable.setAvatar2(arenaPlayerModel.avatar);
        arenaLogTable.setName2(arenaPlayerModel.name);

        this.arenaLogDao.create(arenaLogTable);

        Result result = new Result();
        result.setValue("hero", arenaPlayerModel.generalList);
        result.setValue("battle_id", arenaLogTable.getId());
        return result.toMap();
    }
    
    /**
     * 提交结果
     * @param battleId
     * @param win
     * @param battleData
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object battleEnd(int battleId, boolean win, String battleData) throws Throwable {

        int roleId = this.roleId();

        ArenaLog log = this.arenaLogDao.findOne(battleId);

        if (log == null) {
            throw new Throwable("参数出错,没有这场战斗");
        } else if (log.getBattleResult() != BattleLog.BATTLE_LOG_INIT) {
            throw new Throwable("不能重复提交结果");
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
            return result.toMap();
        }

        long time = System.currentTimeMillis();
        if (time - log.getBeginTime() > BATTLE_MAX_TIME) {

        	result.setValue("overtime", true);
        	return result.toMap();
        }

        log.setBattleResult(BattleLog.BATTLE_LOG_WIN);
        log.setData(battleData);
        log.setEndTime(System.currentTimeMillis());

        this.arenaLogDao.update(log);

        // 更改排名
        int serverId = this.serverId();
        this.arenaDao.changeIndex(roleId, log.getRoleId2(), serverId);

//        // 滚动消息
//        String sql = "SELECT * FROM `role_arena_log` WHERE `role_id1` = ? AND `arena_log_result` = ? ORDER BY `arena_log_id` LIMIT 1";
//
//        EntityManager em = this.context.getEntityManager();
//        Query query = em.createNativeQuery(sql, ArenaLog.class);
//
//        query.setParameter(1, roleId);
//        query.setParameter(2, BattleLog.BATTLE_LOG_LOST);
//
//        @SuppressWarnings("unchecked")
//		List<ArenaLog> lostLog = query.getResultList();
//
//        int lostId = 0;
//        if (lostLog.size() > 0) {
//            lostId = lostLog.get(0).getId();
//        }
//
//        sql = "SELECT * FROM `role_arena_log` WHERE `role_id1` = ? AND `arena_log_result` = ? AND `arena_log_id` > ?";
//
//        query = em.createNativeQuery(sql, ArenaLog.class);
//        query.setParameter(1, roleId);
//        query.setParameter(2, BattleLog.BATTLE_LOG_WIN);
//        query.setParameter(3, lostId);
//
//        lostLog = query.getResultList();
//
//        if (lostLog.size() == 3) {
//
//            SgpPlayerServiceImpl sgpPlayerServiceImpl = this.context.getSgpService(SgpPlayerServiceImpl.class);
//            SgpPlayer player = sgpPlayerServiceImpl.getSgpPlayerById(roleId);
//
//            message.saveMessage(player.getName() + "在竞技场获得3连胜,上升势头强劲 ", Integer.valueOf(player.getCustomId()));
//        } else if (lostLog.size() == 10) {
//
//            SgpPlayerServiceImpl sgpPlayerServiceImpl = this.context.getSgpService(SgpPlayerServiceImpl.class);
//            SgpPlayer player = sgpPlayerServiceImpl.getSgpPlayerById(roleId);
//
//            message.saveMessage(player.getName() + "在竞技场大杀四方,快来人终结他吧 ", Integer.valueOf(player.getCustomId()));
//        }

        result.setValue("win", true);
        result.setValue("newrank", log.getRank2());
        result.setValue("oldrank", log.getRank1());
        return result.toMap();
    }

    /**
     * 排名变化
     * @param page
     * @return
     * @throws Throwable
     */
    public Object changeLogs(int page) throws Throwable {

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
        return result.toMap();
    }

    /**
     * 查看战斗数据
     * @param logId
     * @return
     * @throws Throwable
     */
    public String battleData(int logId) throws Throwable {

        ArenaLog log = this.arenaLogDao.findOne(logId);

        if (log == null) {
            throw new Throwable("参数出错,没有这场战斗");
        } else if (log.getData().equals(null)) {
            throw new Throwable("没有战斗数据");
        }

        return log.getData();
    }

//    /**
//     * 实时排名
//     * @param tokenS
//     * @return
//     * @throws Throwable
//     */
////    public HashMap<String, Object> rank(String tokenS) throws Throwable {
////
////        int roleId = this.roleId();
////
////        ArenaModel arenaModel = new ArenaModel(roleId);
////        List<String> list = arenaModel.getList();
////
////        int max = 20;
////        if (list.size() < max) {
////            max = list.size();
////        }
////
////        ArrayList<String> newList = new ArrayList<String>();
////        for (int i = 0 ; i < max ; i++) {
////        	newList.add(list.get(i));
////        }
////        
////        ArrayList<ArenaPlayerModel> players = arenaModel.getPlayers(newList);
////
////        int i = 0;
////        for (ArenaPlayerModel arenaPlayerModel : players) {
////			i++;
////			arenaPlayerModel.rank = i;
////		}
////
////        Result result = new Result();
////        HashMap<String, Object> map = result.returnMap();
////        map.put("players", players);
////        map.put("myrank", arenaModel.getIndex(roleId) + 1);
////        return map;
////    }    
////
////    /**
////     * 上次排名
////     * @param tokenS
////     * @return
////     * @throws Throwable
////     */
////    public HashMap<String, Object> lastRank(String tokenS) throws Throwable {
////
////        int roleId = this.roleId();
////
////        Token T = TokenInstance.getToken(this.context.getRedisHandle());
////        String serverId = T.getServerId(tokenS);
////        
////        ArenaModel Arena = new ArenaModel(roleId);
////        List<String> list = Arena.getLastRank(Integer.valueOf(serverId));
////
////        int max = 20;
////        if (list.size() < max) {
////            max = list.size();
////        }
////
////        int myIndex = list.size() - 1;
////        for (int i = 0 ; i < list.size() ; i++) {
////            if (list.get(i).equals(roleId)) {
////                myIndex = i;
////                break;
////            }
////        }
////
////        ArenaPlayerModel[] players = Arena.getLastPlayerRank();
////
////        int i = 0;
////        for (ArenaPlayerModel arenaPlayerModel : players) {
////			i++;
////			arenaPlayerModel.rank = i;
////		}
////
////        Result result = new Result();
////        HashMap<String, Object> map = result.returnMap();
////        map.put("players", players);
////        map.put("myrank", myIndex + 1);
////        return map;
////    }
////
////    /**
////     * 等级排行
////     * @param tokenS
////     * @return
////     * @throws Throwable
////     */
////    public Object levelRank(String tokenS) throws Throwable {
////
////        int roleId = this.roleId();
////
////        Token T = TokenInstance.getToken(this.context.getRedisHandle());
////        String serverId = T.getServerId(tokenS);
////
////        String sql = "SELECT * FROM role WHERE server_id = ? ORDER BY role_exp DESC LIMIT ? OFFSET ?";
////
////        EntityManager em = this.context.getEntityManager();
////        Query query = em.createNativeQuery(sql, Role.class);
////
////        query.setParameter(1, serverId);
////        query.setParameter(2, 20);
////        query.setParameter(3, 0);
////
////        @SuppressWarnings("unchecked")
////        List<Role> list = query.getResultList();
////
////        List<String> roleIds = new ArrayList<String>();
////        for (Role roleTable : list) {
////        	roleIds.add(roleTable.getRoleId());
////		}
////
////        ArenaModel arenaModel = new ArenaModel(roleId);
////        ArrayList<ArenaPlayerModel> playerList = arenaModel.getPlayers(roleIds);
////
////        int myIndex = 0;
////
////        for (int index = 0 ; index < playerList.size() ; index++) {
////
////            if (playerList.get(index).roleId.equals(roleId)) {
////            	myIndex = index + 1;
////            }
////        }
////
////        if (myIndex == 0) {
////    		query = em.createNativeQuery("SELECT COUNT(*) AS n FROM `role` WHERE `role_exp` > ? ");
////    		
////    		RoleModel Role = new RoleModel(roleId);
////    		query.setParameter(1, Role.exp);
////
////    		@SuppressWarnings("unchecked")
////    		List<Object> data = query.getResultList();
////    		Object dat = data.get(0);
////    		BigInteger c = (BigInteger)dat;
////    		c.intValue();
////    		myIndex = c.intValue() + 1;
////        }
////
////        int i = 0;
////        for (ArenaPlayerModel arenaPlayerModel : playerList) {
////			i++;
////			arenaPlayerModel.rank = i;
////		}
////
////		Result result = new Result();
////		HashMap<String, Object> map = result.returnMap();
////		map.put("players", playerList);
////		map.put("myrank", myIndex);
////		return map;
////    }
//    
    /**
     * 三个排行一起发
     * @return
     * @throws Throwable
     */
    public Object allRank() throws Throwable {

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

        // 昨天排行
        List<Integer> yesterdayList = this.arenaDao.getLastRank(serverId);
        int myYesterdayIndex = yesterdayList.size();
        for (int i = 0 ; i < yesterdayList.size() ; i++) {
            if (yesterdayList.get(i).equals(roleId)) {
            	myYesterdayIndex = i + 1;
                break;
            }
        }

        if (yesterdayList.size() > 0) {
        	yesterdayList = yesterdayList.subList(0, 20);
        }

        for (Integer id : yesterdayList) {
			set.add(id);
		}

        List<Integer> roleIds = new ArrayList<Integer>();
        for (Integer id : set) {
        	roleIds.add(id);
		}

        ArrayList<ArenaPlayerModel> players = this.arenaDao.getPlayers(roleIds, serverId);

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

        ArrayList<ArenaPlayerModel> yesterdayPlayers = new ArrayList<ArenaPlayerModel>();
        for (int i = 0 ; i < yesterdayList.size() ; i++) {
        	for (ArenaPlayerModel player : players) {
        		if (player.roleId == yesterdayList.get(i)) {
        			ArenaPlayerModel newPlayer = (ArenaPlayerModel) player.clone();
        			newPlayer.rank = i + 1;
        			yesterdayPlayers.add(newPlayer);
        			break;
        		}
			}
		}

        HashMap<String, Object> yesterDayMap = new HashMap<String, Object>();
        yesterDayMap.put("players", yesterdayPlayers);
        yesterDayMap.put("myrank", myYesterdayIndex);

        Result result = new Result();
        result.setValue("now", nowMap);
        result.setValue("yesterday", yesterDayMap);
        result.setValue("level", levelMap);
        return result.toMap();
    }

//    // 清除时间的
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

    private void addGold1(int roleId) {

    	DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

    	dateNumModel.setArenaCd(dateNumModel.getArenaCd() + 1);

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

    private void addGold2(int roleId) {

    	DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

    	dateNumModel.setArenaNum(dateNumModel.getArenaNum() + 1);

    	this.dateNumDao.save(roleId, dateNumModel);
    }
}
