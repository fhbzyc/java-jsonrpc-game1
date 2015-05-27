package com.zhanglong.sg.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import websocket.handler.EchoHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity.RankLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Mail;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.ArenaPlayerModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.service.RoleService;
import com.zhanglong.sg.utils.Utils;

@Repository
public class ArenaDao extends BaseDao {

	private static String RedisKey = "ARENA1_";

	@Resource
	private RedisTemplate<String, Integer> redisTemplate;

	@Resource
	private RoleDao roleDao;

	@Resource
	private MailDao mailDao;

	@Resource
	private HeroDao heroDao;

	public ArenaDao() {
	}

	public void changeIndex(int roleId, int loserId, int serverId) throws JsonParseException, JsonMappingException, IOException {

		// 交换位置
		int myIndex = this.getIndex(roleId, serverId);
		int newIndex = this.getIndex(loserId, serverId);
		this.redisTemplate.opsForList().set(ArenaDao.redisKey(serverId), myIndex, loserId);
		this.redisTemplate.opsForList().set(ArenaDao.redisKey(serverId), newIndex, roleId);

		Role role = this.roleDao.findOne(roleId);

		int oldRank = role.getRank();
		int newRank = newIndex + 1;
        if (newIndex < myIndex) {
        	role.rank = newRank;

    	    // 历史排名上升发奖励
            try {
                String reward = this.getRankUpReward(newRank, oldRank);
                if (reward != null) {

                	String content = 
"主公好棒！您刚才在竞技场比武中大显神威，完美地刷新了您在竞技场中的最好成绩！\n"+
"您现在的排名：" + newRank + "名\n"+
"比之前提升了：" + (oldRank - newRank) + "名\n"+
"竞技场管理处决定授予您下列奖励：\n\n"+
"竞技场教官：吕小布";

                    Mail mail = new Mail();
                    mail.setFromName("GM");
                    mail.setAttachment(reward);
                    mail.setTitle("竞技场最高排名奖励");
                    mail.setContent(content);
                    mail.setStatus(0);
                    mail.setRoleId(roleId);
                    this.mailDao.create(mail);
                }
    		} catch (Exception e) {
    		}
        }

    	role.winNum += 1;
    	this.roleDao.update(role, new Result());

		if (newIndex == 0) {

    		String msgs = "恭喜" + role.name + "在竞技场中登顶第1名!";
    		Result r = new Result();
    		r.setValue("msgs", msgs);
    		String msg = Response.marshalSuccess(0, r.toMap());
    		EchoHandler.broadcast(serverId, msg);
		}
	}

	public List<Integer> getList(int serverId) {

		List<Integer> list = this.redisTemplate.opsForList().range(redisKey(serverId), 0, -1);

		if (list.size() == 0) {

			for ( int i = 1 ; i <= 10000 ; i++) {
				this.redisTemplate.opsForList().rightPush(redisKey(serverId), i);
			}
			list = this.redisTemplate.opsForList().range(redisKey(serverId), 0, -1);
		}

		return list;
	}

	/**
	 * 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public RankLog getLastRank(int serverId) throws JsonParseException, JsonMappingException, IOException {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(RankLog.class);
		@SuppressWarnings("unchecked")
		List<RankLog> list = criteria.add(Restrictions.eq("serverId", serverId)).addOrder(Order.desc("id")).list();
		
		RankLog rank = null;
		if (list != null) {
			rank = list.get(0);
		}
		return rank;
	}

	/**
	 * 每日9点记录最后的排名
	 * @throws Exception 
	 * @throws Throwable 
	 */
	public void setLastRank(int serverId) throws Exception {

		List<Integer> list = getList(serverId).subList(0, 20);
		List<ArenaPlayerModel> players = this.getPlayers(list, serverId);

		ObjectMapper objectMapper = new ObjectMapper();
		RankLog rankLog = new RankLog();
		rankLog.setData(objectMapper.writeValueAsString(players));
		rankLog.setRank(objectMapper.writeValueAsString(getList(serverId)));
		rankLog.setServerId(serverId);
		rankLog.setDate(Integer.valueOf(Utils.date()));
		this.getSessionFactory().getCurrentSession().save(rankLog);

	}

	public static String redisKey(int serverId) {
		return RedisKey + serverId;
	}

	/**
	 * 排名
	 * @param roleId
	 * @param serverId
	 * @return
	 */
	public int getIndex(int roleId, int serverId) {

		List<Integer> list = this.getList(serverId);
		for (int i = 0 ; i < list.size() ; i++) {
			if (list.get(i) == roleId) {
				return i;
			}
		}

		redisTemplate.opsForList().rightPush(redisKey(serverId), roleId);

		return list.size();
	}

	/**
	 * 
	 * @param roleIds
	 * @return
	 * @throws Exception 
	 */
	public List<ArenaPlayerModel> getPlayers(List<Integer> roleIds, int serverId) throws Exception {

		List<Integer> copy = new ArrayList<Integer>();

		for (int i = 0 ; i < roleIds.size() ; i++) {
			int roleId = roleIds.get(i);

            if (roleId > 20000) {
            	copy.add(roleId);
            }
		}

		ArrayList<ArenaPlayerModel> result = new ArrayList<ArenaPlayerModel>();

		if (copy.size() == 0) {
			
			for (int i = 0; i < roleIds.size(); i++) {
				ArenaPlayerModel player = new ArenaPlayerModel();
				player.roleId = roleIds.get(i);
				this.toPlayer(player, serverId);
				result.add(player);
			}

			return result;
		}

		String sql = "SELECT * FROM role WHERE role_id IN(";

		for (int i = 0 ; i < copy.size() ; i++) {
			if (i == 0) {
				sql += "?";
			} else {
				sql += ",?";
			}
		}
		sql += ")";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		for (int i = 0 ; i < copy.size() ; i++) {
			query.setParameter(i, copy.get(i));
		}

		query.addEntity(Role.class);
		@SuppressWarnings("unchecked")
		List<Role> roles = query.list();

		sql = "SELECT * FROM role_hero WHERE role_id IN(";

		for (int i = 0 ; i < copy.size() ; i++) {
			if (i == 0) {
				sql += "?";
			} else {
				sql += ",?";
			}
		}
		sql += ") AND hero_is_battle = true";

		query = session.createSQLQuery(sql);

		for (int i = 0 ; i < copy.size() ; i++) {
			query.setParameter(i, copy.get(i));
		}

		query.addEntity(Hero.class);
		@SuppressWarnings("unchecked")
		List<Hero> heros = query.list();

		for (int i = 0 ; i < roleIds.size() ; i++) {
			
			ArenaPlayerModel player = new ArenaPlayerModel();

			int roleId = roleIds.get(i);
			
			player.roleId = roleId;

            if (roleId <= 20000) {
            	this.toPlayer(player, serverId);

            } else {
            	for (Role role : roles) {

            		if (role.getRoleId().equals(roleId)) {
    					player.avatar = role.getAvatar();
    					player.level = role.level();
    					player.name = role.getName();
    					player.wins = role.winNum;
            		}
				}

            	for (Hero hero : heros) {
            		if (hero.getARoleId() == roleId) {
            			player.generalList.add(hero.toArray());
            		}
				}
            }

            result.add(player);
		}

		return result;
	}

	public ArrayList<int[]> rewardConfig() {
		ArrayList<int[]> arrayList = new ArrayList<int[]>();
		arrayList.add(new int[]{1 , 1 , 1 , 550 , 550 , 100000 , 800 , 4202 , 12});
		arrayList.add(new int[]{2 , 2 , 2 , 500 , 500 , 95000 , 775 , 4202 , 8});
		arrayList.add(new int[]{3 , 3 , 3 , 450 , 450 , 90000 , 750 , 4202 , 6});
		arrayList.add(new int[]{4 , 4 , 4 , 360 , 360 , 85000 , 725 , 4202 , 6});
		arrayList.add(new int[]{5 , 5 , 5 , 330 , 330 , 80000 , 700 , 4202 , 5});
		arrayList.add(new int[]{6 , 6 , 6 , 300 , 300 , 75000 , 680 , 4202 , 5});
		arrayList.add(new int[]{7 , 7 , 7 , 270 , 270 , 70000 , 660 , 4202 , 4});
		arrayList.add(new int[]{8 , 8 , 8 , 250 , 250 , 65000 , 640 , 4202 , 4});
		arrayList.add(new int[]{9 , 9 , 9 , 230 , 230 , 60000 , 620 , 4202 , 3});
		arrayList.add(new int[]{10 , 10 , 10 , 210 , 210 , 55000 , 600 , 4202 , 3});
		arrayList.add(new int[]{11 , 11 , 20 , 200 , 120 , 50000 , 590 , 4201 , 8});
		arrayList.add(new int[]{12 , 21 , 30 , 190 , 110 , 45000 , 575 , 4201 , 7});
		arrayList.add(new int[]{13 , 31 , 40 , 180 , 100 , 40000 , 560 , 4201 , 6});
		arrayList.add(new int[]{14 , 41 , 50 , 170 , 80 , 35000 , 545 , 4201 , 5});
		arrayList.add(new int[]{15 , 51 , 70 , 160 , 80 , 30000 , 530 , 4201 , 4});
		arrayList.add(new int[]{16 , 71 , 100 , 150 , 80 , 27500 , 515 , 4200 , 10});
		arrayList.add(new int[]{17 , 101 , 200 , 145 , 30 , 25000 , 500 , 4200 , 9});
		arrayList.add(new int[]{18 , 201 , 300 , 140 , 30 , 22500 , 475 , 4200 , 8});
		arrayList.add(new int[]{19 , 301 , 400 , 135 , 30 , 20000 , 450 , 4200 , 7});
		arrayList.add(new int[]{20 , 401 , 500 , 130 , 30 , 18000 , 425 , 4200 , 6});
		arrayList.add(new int[]{21 , 501 , 700 , 125 , 30 , 16000 , 400 , 4200 , 5});
		arrayList.add(new int[]{22 , 701 , 1000 , 120 , 30 , 14000 , 350 , 4200 , 4});
		arrayList.add(new int[]{23 , 1001 , 2000 , 110 , 20 , 12000 , 300 , 4200 , 3});
		arrayList.add(new int[]{24 , 2001 , 3000 , 100 , 20 , 10000 , 260 , 4200 , 3});
		arrayList.add(new int[]{25 , 3001 , 4000 , 90 , 20 , 9000 , 220 , 4200 , 2});
		arrayList.add(new int[]{26 , 4001 , 5000 , 80 , 10 , 8000 , 180 , 4200 , 2});
		arrayList.add(new int[]{27 , 5001 , 7000 , 70 , 10 , 7000 , 140 , 4200 , 1});
		arrayList.add(new int[]{28 , 7001 , 10000 , 60 , 5 , 5000 , 100 , 4200 , 1});
		arrayList.add(new int[]{29 , 10001 , 15000 , 50 , 5 , 3000 , 60 , 4200 , 1});
		arrayList.add(new int[]{30 , 15001 , 20000 , 40 , 5 , 2000 , 40 , 4200 , 1});
		return arrayList;
	}

	public String getRankUpReward(int newRank, int oldRank) throws JsonProcessingException {

		ArrayList<int[]> arrayList = this.rewardConfig();

		if (newRank > arrayList.get(arrayList.size() - 1)[2]) {
			return null;
		}

		int newIndex = 0;
		for (int i = 0; i < arrayList.size() ; i++) {
			if (arrayList.get(i)[1] <= newRank && arrayList.get(i)[2] >= newRank) {
				newIndex = i;
			}
		}

		int oldIndex = 0;
		for (int i = 0; i < arrayList.size() ; i++) {
			if (arrayList.get(i)[1] <= oldRank && arrayList.get(i)[2] >= oldRank) {
				oldIndex = i;
			}
		}

		int gold = 0;
		if (newIndex == oldIndex) {
			gold = arrayList.get(newIndex)[4];
		} else {

			for(int i = newIndex ; i <= oldIndex ; i++) {
				gold += arrayList.get(i)[3];
			}
		}
		
		Reward reward = new Reward();
		reward.setGold(gold);

		ObjectMapper objectMapper = new ObjectMapper();

		return objectMapper.writeValueAsString(reward);
	}

	public void addMoney(int money, Result result) {

//		Arena arenaTable = this.getArenaTable();
//		arenaTable.setMoney(arenaTable.getMoney() + money);
//		this.saveArenaTable(arenaTable);
//
//		result.addRandomItem(new int[]{3 , money});
//		result.setArenaMoney(arenaTable.getMoney());
	}

	public void subMoney(int money, Result result) {
//		Arena arenaTable = this.getArenaTable();
//		arenaTable.setMoney(arenaTable.getMoney() - money);
//		this.saveArenaTable(arenaTable);
//
//		result.setArenaMoney(arenaTable.getMoney());
	}

	public void toPlayer(ArenaPlayerModel player, int serverId) throws Exception {

		int rank = player.roleId;
		player.avatar = rank % 9;

		player.wins = 20 - (rank % 18);

    	String[] firstName = RoleService.FirstName;
    	String[] lastName = RoleService.LastName;

    	player.name = firstName[rank % firstName.length] + lastName[rank % lastName.length];

		ArrayList<Object[]> configs = new ArrayList<Object[]>();

		configs.add(new Object[]{1 , 1 , new int[]{10004,25,3,3} ,new int[]{10016,25,3,3} ,new int[]{10012,25,3,3} ,new int[]{10014,25,3,3}});
		configs.add(new Object[]{2 , 2 , new int[]{10002,24,3,3} ,new int[]{10005,24,3,3} ,new int[]{10012,24,3,3} ,new int[]{10014,24,3,3}});
		configs.add(new Object[]{3 , 3 , new int[]{10010,23,3,3} ,new int[]{10016,23,3,3} ,new int[]{10012,23,3,3} ,new int[]{10014,23,3,3}});
		configs.add(new Object[]{4 , 4 , new int[]{10002,22,3,3} ,new int[]{10005,22,3,3} ,new int[]{10012,22,3,3} ,new int[]{10009,22,3,3}});
		configs.add(new Object[]{5 , 5 , new int[]{10010,21,3,3} ,new int[]{10016,21,3,3} ,new int[]{10012,21,3,3} ,new int[]{10014,21,3,3}});
		configs.add(new Object[]{6 , 6 , new int[]{10002,20,3,2} ,new int[]{10011,20,3,2} ,new int[]{10012,20,3,2} ,new int[]{10009,20,3,2}});
		configs.add(new Object[]{7 , 7 , new int[]{10004,19,3,2} ,new int[]{10016,19,2,2} ,new int[]{10012,19,3,2} ,new int[]{10014,19,3,2}});
		configs.add(new Object[]{8 , 8 , new int[]{10002,18,3,2} ,new int[]{10005,18,3,2} ,new int[]{10012,18,3,2} ,new int[]{10014,18,3,2}});
		configs.add(new Object[]{9 , 9 , new int[]{10002,17,3,2} ,new int[]{10011,17,3,2} ,new int[]{10012,17,3,2} ,new int[]{10009,17,3,2}});
		configs.add(new Object[]{10 , 10 , new int[]{10010,17,3,2} ,new int[]{10016,17,3,2} ,new int[]{10012,17,3,2} ,new int[]{10014,17,3,2}});
		configs.add(new Object[]{11 , 20 , new int[]{10002,17,3,2} ,new int[]{10011,16,3,2} ,new int[]{10012,16,3,2} ,new int[]{10009,17,3,2}});
		configs.add(new Object[]{21 , 30 , new int[]{10004,16,3,2} ,new int[]{10016,16,2,2} ,new int[]{10012,16,3,2} ,new int[]{10014,16,3,2}});
		configs.add(new Object[]{31 , 40 , new int[]{10010,16,3,2} ,new int[]{10016,16,2,2} ,new int[]{10012,16,3,2} ,new int[]{10014,16,3,2}});
		configs.add(new Object[]{41 , 50 , new int[]{10004,15,3,2} ,new int[]{10016,15,2,2} ,new int[]{10012,15,3,2} ,new int[]{10014,15,3,2}});
		configs.add(new Object[]{51 , 70 , new int[]{10002,15,3,2} ,new int[]{10011,15,3,2} ,new int[]{10012,15,3,2} ,new int[]{10009,15,3,2}});
		configs.add(new Object[]{71 , 100 , new int[]{10010,14,3,2} ,new int[]{10016,14,2,2} ,new int[]{10012,14,3,2} ,new int[]{10014,14,3,2}});
		configs.add(new Object[]{101 , 200 , new int[]{10002,14,2,2} ,new int[]{10011,14,3,2} ,new int[]{10012,14,3,2} ,new int[]{10009,14,3,2}});
		configs.add(new Object[]{201 , 300 , new int[]{10004,14,3,2} ,new int[]{10016,14,2,2} ,new int[]{10012,14,3,2} ,new int[]{10014,14,2,2}});
		configs.add(new Object[]{301 , 400 , new int[]{10010,13,3,1} ,new int[]{10016,13,2,1} ,new int[]{10012,13,3,1} ,new int[]{10014,13,3,1}});
		configs.add(new Object[]{401 , 500 , new int[]{10002,13,2,1} ,new int[]{10011,13,3,1} ,new int[]{10004,13,3,1} ,new int[]{10009,13,3,1}});
		configs.add(new Object[]{501 , 700 , new int[]{10004,13,3,1} ,new int[]{10016,13,2,1} ,new int[]{10012,13,3,1} ,new int[]{10014,13,2,1}});
		configs.add(new Object[]{701 , 1000 , new int[]{10002,13,2,1} ,new int[]{10011,13,3,1} ,new int[]{10004,13,3,1} ,new int[]{10009,13,3,1}});
		configs.add(new Object[]{1001 , 2000 , new int[]{10010,12,2,1} ,new int[]{10016,12,3,1} ,new int[]{10012,12,3,1} ,new int[]{10014,12,2,1}});
		configs.add(new Object[]{2001 , 3000 , new int[]{10004,12,2,1} ,new int[]{10016,12,2,1} ,new int[]{10012,12,2,1} ,new int[]{10014,12,2,1}});
		configs.add(new Object[]{3001 , 4000 , new int[]{10004,12,2,1} ,new int[]{10016,12,2,1} ,new int[]{10012,12,2,1} ,new int[]{10014,12,2,1}});
		configs.add(new Object[]{4001 , 5000 , new int[]{10010,13,2,1} ,new int[]{10011,13,2,0} ,new int[]{10004,10,2,1} ,new int[]{10014,11,3,0}});
		configs.add(new Object[]{5001 , 7000 , new int[]{10002,8,1,0} ,new int[]{10011,10,1,0} ,new int[]{10012,9,1,1} ,new int[]{10009,8,1,0}});
		configs.add(new Object[]{7001 , 10000 , new int[]{10002,8,1,0} ,new int[]{10011,6,1,0} ,new int[]{10012,8,1,0} ,new int[]{10009,6,1,0}});   
		configs.add(new Object[]{10001 , 15000 , new int[]{10010,7,1,0} ,new int[]{10011,8,1,0} ,new int[]{10012,6,1,0} ,new int[]{10014,8,1,0}});
		configs.add(new Object[]{15001 , 20000 , new int[]{10002,6,1,0} ,new int[]{10011,8,1,0} ,new int[]{10012,8,1,0} ,new int[]{10009,8,1,0}});

		int maxLevel = 0;
		
		for (Object[] objects : configs) {
			int min = (Integer)objects[0];
			int max = (Integer)objects[1];

			if (rank >= min && rank <= max) {

				int[] g1 = (int[])objects[2];
				int[] g2 = (int[])objects[3];
				int[] g3 = (int[])objects[4];
				int[] g4 = (int[])objects[5];

				
				maxLevel = g1[1];
				if (g2[1] > maxLevel) {
					maxLevel = g2[1];
				}
				if (g3[1] > maxLevel) {
					maxLevel = g3[1];
				}
				if (g4[1] > maxLevel) {
					maxLevel = g4[1];
				}

				player.generalList.add(this.toHero(rank, g1));
				player.generalList.add(this.toHero(rank, g2));
				player.generalList.add(this.toHero(rank, g3));
				player.generalList.add(this.toHero(rank, g4));
				player.level = maxLevel;
				return ;
			}
		}
	}

	public Object[] toHero(int rank, int[] config) throws Exception {

		int heroId = config[0];
		int level = config[1];
		int star = config[2];
		int CLASS = config[3];

		Hero hero = new Hero();
		hero.setHeroId(heroId);
		hero.setStr(level);
		hero.setDex(level);
		hero.setINT(level);
		hero.setExp(Hero.EXP[level - 1] - 1);
		hero.setLevel(level);
		hero.setCLASS(CLASS);
		hero.setStar(star);
		hero.setSkill1Level(1);
		hero.setSkill2Level(1);
		hero.setSkill3Level(1);
		hero.setSkill4Level(1);
		if (rank < 7000) {
			hero.setEquip1(true);
			hero.setEquip2(true);
			hero.setEquip3(true);
			hero.setEquip4(true);
			hero.setEquip5(true);
			hero.setEquip6(true);
        }
        return hero.toArray();
	}
}
