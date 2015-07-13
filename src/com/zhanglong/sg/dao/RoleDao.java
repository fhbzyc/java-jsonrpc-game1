package com.zhanglong.sg.dao;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.model.PlayerModel;
import com.zhanglong.sg.result.Result;

@Repository
public class RoleDao extends BaseDao {

	@Resource
	private MissionDao missionDao;

	public static int[] LEVEL_ACTION_VALUE = new int[]{10,10,10,10,20,20,20,20,30,30,30,30,40,40,40,40,40,40,55,55,55,65,65,65,65,65,75,75,75,75,75,85,85,85,85,85,90,90,90,90,90,95,95,95,95,95,100,100,100,100,100,105,105,105,105,105,110,110,110,110,110,115,115,115,115,115,120,120,120,120,120,125,125,125,125,125,130,130,130,130,130,135,135,135,135,135,140,140,140,140};

	public Role findOne(int roleId) {
		Session session = this.getSessionFactory().getCurrentSession();
		Object object = session.get(Role.class, roleId);
		if (object == null) {
			return null;
		}
		return (Role)object;
	}

	public int countByName(String name, int serverId) {

		String sql = "SELECT COUNT(*) FROM role WHERE role_name = ? AND server_id = ? ";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, name);
		query.setParameter(1, serverId);

		return ((BigInteger) query.list().iterator().next()).intValue();
	}

	public List<Role> expTop20(int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Role> list = session.createCriteria(Role.class)
				.add(Restrictions.eq("serverId", serverId))
				.addOrder(Order.desc("exp"))
				.setMaxResults(20).list();
		return list;
	}

	public List<Role> killTop20(int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Role> list = session.createCriteria(Role.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.gt("killNum", 0))
				.addOrder(Order.desc("killNum"))
				.setMaxResults(20).list();
		return list;
	}

	public int countExpAfter(int exp, int serverId) {

		String sql = "SELECT COUNT(*) AS n FROM role WHERE role_exp > ? AND server_id = ?";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter(0, exp);
		query.setParameter(1, serverId);

		return ((BigInteger) query.list().iterator().next()).intValue();
	}

	public long countKillAfter(int killNum, int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Long> list = session.createCriteria(Role.class)
				.add(Restrictions.eq("serverId", serverId))
				.add(Restrictions.gt("killNum", killNum))
				.setProjection(Projections.projectionList().add(Projections.count("roleId")))
				.list();
		return list.get(0);
	}

	public int addPillage(int num) {

		String sql = "UPDATE role SET role_pillage_num = role_pillage_num + " + num + " WHERE role_level >= 25 AND role_pillage_num < 20";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		return query.executeUpdate();
	}

	public Role create(int userId, int serverId) {

		Role role = new Role();
		role.setServerId(serverId);
		role.setName("");
		role.setAp(0);
		role.setApTime(0);
		role.setAvatar(1);
		role.setCoin(0);
		role.setExp(0);
		role.setGold(0);
		role.setProgress(0);
		role.setVip(0);
		role.setUserId(userId);
		role.setCreateTime(new Timestamp(System.currentTimeMillis()));
		role.setEnable(true);
		role.setLevel(1);
		role.setPillageNum(10);
		role.setStr("");

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(role);
		return role;
	}

	public void update(Role role, Result result) {
		role.level = role.level();
//		role.vip = role.vip()[0];
//		Session session = this.getSessionFactory().getCurrentSession();
//		session.update(role);
		result.setMoney(role.getCoin(), role.getGold());
	}

	public void addCoin(Role role, int coin, String desc, int status, Result result) {

    	result.addRandomItem(new int[]{1 , coin});

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getCoin());
    	finance.setMoneyType(1);
    	finance.setDesc(desc);
    	finance.setStatus(status);

    	role.setCoin(role.getCoin() + coin);
    	
    	finance.setNewMoney(role.getCoin());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);

    	result.setMoney(role.coin, role.gold);
    }

    public void subCoin(Role role, int coin, String desc, int status, Result result) {

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getCoin());
    	finance.setMoneyType(1);
    	finance.setDesc(desc);
    	finance.setStatus(status);

    	role.setCoin(role.getCoin() - coin);

    	finance.setNewMoney(role.getCoin());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);

    	result.setMoney(role.coin, role.gold);
    }

    public void addGold(Role role, int gold, String desc, int status, Result result) {

    	result.addRandomItem(new int[]{2 , gold});

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getGold());
    	finance.setMoneyType(2);
    	finance.setDesc(desc);
    	finance.setStatus(status);

    	role.setGold(role.getGold() + gold);

    	finance.setNewMoney(role.getGold());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
    }

    public void subGold(Role role, int gold, String desc, int status, Result result) {

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getGold());
    	finance.setMoneyType(2);
    	finance.setDesc(desc);
    	finance.setStatus(status);

    	role.setGold(role.getGold() - gold);

    	finance.setNewMoney(role.getGold());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);

    	result.setMoney(role.coin, role.gold);
    }

	public void addMoney3(Role role, int money3, String desc, int status, Result result) {

		result.addRandomItem(new int[]{3 , money3});

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getMoney3());
    	finance.setMoneyType(3);
    	finance.setDesc(desc);
    	finance.setStatus(status);

		role.setMoney3(role.getMoney3() + money3);
		
		result.setValue("money3", role.money3);

    	finance.setNewMoney(role.getMoney3());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

	public void subMoney3(Role role, int money3, String desc, int status) {

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getMoney3());
    	finance.setMoneyType(3);
    	finance.setDesc(desc);
    	finance.setStatus(status);

		role.setMoney3(role.getMoney3() - money3);

    	finance.setNewMoney(role.getMoney3());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

	public void addMoney4(Role role, int money4, String desc, int status, Result result) {

		result.addRandomItem(new int[]{4 , money4});

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getMoney4());
    	finance.setMoneyType(4);
    	finance.setDesc(desc);
    	finance.setStatus(status);

		role.setMoney4(role.getMoney4() + money4);
		result.setValue("money4", role.money4);

    	finance.setNewMoney(role.getMoney4());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

	public void subMoney4(Role role, int money4, String desc, int status) {

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.getMoney4());
    	finance.setMoneyType(4);
    	finance.setDesc(desc);
    	finance.setStatus(status);

		role.setMoney4(role.getMoney4() - money4);

    	finance.setNewMoney(role.getMoney4());

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

	public void addMoney5(Role role, int money5, String desc, int status, Result result) {

		result.addRandomItem(new int[]{5 , money5});

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.money5);
    	finance.setMoneyType(5);
    	finance.setDesc(desc);
    	finance.setStatus(status);

		role.money5 += money5;
		result.setValue("money5", role.money5);

    	finance.setNewMoney(role.money5);

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

	public void subMoney5(Role role, int money5, String desc, int status) {

    	FinanceLog finance = new FinanceLog();
    	finance.setRoleId(role.getRoleId());
    	finance.setOldMoney(role.money5);
    	finance.setMoneyType(5);
    	finance.setDesc(desc);
    	finance.setStatus(status);

    	role.money5 -= money5;

    	finance.setNewMoney(role.money5);

    	FinanceLogDao financeLogDao = new FinanceLogDao();
    	financeLogDao.setSessionFactory(this.getSessionFactory());

    	financeLogDao.create(finance);
	}

    public void addExp(Role role, int exp, Result result) {

    	int oldExp = role.getExp();
    	int oldLevel = role.level();
   // 	int oldPhysicalStrength = role.getPhysicalStrength();

        int maxExp = Role.EXP[Role.EXP.length - 1];
        int newExp = oldExp + exp;

    	if (newExp > maxExp) {
    		newExp = maxExp;
    	}
    	
    	role.setExp(newExp);
    	result.setTeam(newExp);

    	int newLevel = role.level();
    	
        if (newLevel > oldLevel) {

        	// 升级送体力
        	int ap = 0;
        	for (int i = newLevel; i > oldLevel ; i--) {
        		ap += LEVEL_ACTION_VALUE[i - 2];
			}

        	this.addAp(role, ap, result);

        	result.addAp(ap);
        	result.setTeam(newExp);

        	// 检查升级主线任务
        	try {
        		this.missionDao.checkLevel(role, result);
        		this.missionDao.newMission(role, result);

			} catch (Throwable e) {

			}
        }

        role.level = newLevel;
    }

	public void addAp(Role role, int ap, Result result) {
		int newAp = ap + role.ap();
		role.setNewAp(newAp);
		result.setAp(newAp, role.apCoolTime());
    }

	public void subAp(Role role, int ap, Result result) {
		int newAp = role.ap() - ap;
		role.setNewAp(newAp);
		result.setAp(newAp, role.apCoolTime());
    }

    public Role getByServerId(int userId, int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Role.class);
		@SuppressWarnings("unchecked")
		List<Role> list = criteria.add(Restrictions.eq("userId", userId)).add(Restrictions.eq("serverId", serverId)).setMaxResults(1).list();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
    }

	/**
	 * 
	 * @param roleId
	 * @param serverId
	 * @param mylevel
	 * @param itemId
	 * @param isAfter
	 * @return
	 */
	public List<Role> get10player(int roleId, int serverId, int mylevel, int itemId, boolean isAfter) {

		int num = 10;

		String sql = "SELECT role.* FROM role JOIN role_items ON role.role_id = role_items.role_id WHERE role_items.item_id = ? AND role.server_id = ? AND role.role_pillage_time < ? AND role.role_level BETWEEN ? AND ? ORDER BY role.role_level LIMIT 100";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		query.setParameter(1 - 1, itemId);
		query.setParameter(2 - 1, serverId);
		query.setParameter(3 - 1, System.currentTimeMillis() / 1000);
		if (isAfter) {
			query.setParameter(4 - 1, mylevel - 1);
			query.setParameter(5 - 1, mylevel + num);
		} else {
			query.setParameter(4 - 1, mylevel - num);
			query.setParameter(5 - 1, mylevel - 2);
		}

		query.addEntity(Role.class);
		@SuppressWarnings("unchecked")
		List<Role> list = query.list();
		int index = -1;
		for (int i = 0 ; i < list.size() ; i++) {
			if (list.get(i).getRoleId().equals(roleId)) {
				index = i;
			}
		}

		if (index >= 0) {
			list.remove(index);
		}

		if (list.size() > 0) {
			Collections.shuffle(list);
			int max = 9;
			if (max > list.size()) {
				max = list.size();
			}
			list = list.subList(0, max);
		}

		return list;
	}

	public List<PlayerModel> getPlayers(List<Role> roles) throws Exception {

		List<PlayerModel> result = new ArrayList<PlayerModel>();

		if (roles.size() == 0) {
			return result;
		}


//		String sql = "SELECT * FROM role_arena WHERE role_id IN(";
//
//		for (int i = 0 ; i < roles.size() ; i++) {
//			if (i == 0) {
//				sql += "?";
//			} else {
//				sql += ",?";
//			}
//		}
//		sql += ")";
//
//		Session session = this.getSessionFactory().getCurrentSession();
//		SQLQuery query = session.createSQLQuery(sql);
//
//		for (int i = 0 ; i < roles.size() ; i++) {
//			query.setParameter(i, roles.get(i).getRoleId());
//		}
//
//		query.addEntity(Arena.class);
//		@SuppressWarnings("unchecked")
//		List<Arena> arenaTables = query.list();

		String sql = "SELECT * FROM role_hero WHERE role_id IN(";

		for (int i = 0 ; i < roles.size() ; i++) {
			if (i == 0) {
				sql += "?";
			} else {
				sql += ",?";
			}
		}
		sql += ") AND hero_is_battle = true";

		Session session = this.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);

		for (int i = 0 ; i < roles.size() ; i++) {
			query.setParameter(i, roles.get(i).getRoleId());
		}

		query.addEntity(Hero.class);
		@SuppressWarnings("unchecked")
		List<Hero> heros = query.list();

		for (int i = 0 ; i < roles.size() ; i++) {

			Role role = roles.get(i);

			PlayerModel player = new PlayerModel();
			player.avatar = role.getAvatar();
			player.level = role.getLevel();
			player.name = role.getName();
			player.roleId = role.getRoleId();
			player.generalList.add(null);
			player.generalList.add(null);
			player.generalList.add(null);
			player.generalList.add(null);

			for (Hero hero : heros) {
				if ((int)hero.getARoleId() == (int)role.getRoleId()) {
					if (hero.getPosition() > 0) {
						player.generalList.set(hero.getPosition() - 1, hero.toArray());
					}
				}
			}

//			for (Arena arena : arenaTables) {
//				if (arena.getRoleId().equals(role.getRoleId())) {
//					ArrayList<Object> heros = new ArrayList<Object>();
//					heros.add(null);
//					heros.add(null);
//					heros.add(null);
//					heros.add(null);
//
//					for (Hero hero : heroTables) {
//						if (hero.getARoleId() == role.getRoleId()) {
//							if ((int)hero.getHeroId() == (int)arena.getGeneralId1()) {
//								heros.set(0, hero.toArray());
//							} else if ((int)hero.getHeroId() == (int)arena.getGeneralId2()) {
//								heros.set(1, hero.toArray());
//							} else if ((int)hero.getHeroId() == (int)arena.getGeneralId3()) {
//								heros.set(2, hero.toArray());
//							} else if ((int)hero.getHeroId() == (int)arena.getGeneralId4()) {
//								heros.set(3, hero.toArray());
//							}
//						}
//					}
//
//					player.generalList = heros;
//				}
//			}

            result.add(player);
		}

		return result;
	}

	public boolean isPlayer(int roleId) {
		if (roleId > 20000) {
			return true;
		} else {
			return false;
		}
	}
}
