package com.zhanglong.sg.dao;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Role;
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
		Query query = session.createQuery(sql);
		query.setParameter(1, name);
		query.setParameter(2, serverId);

		return ((Integer) query.list().iterator().next()).intValue();
	}

	public List<Role> expTop20(int serverId) {

        // String sql = "SELECT * FROM role WHERE server_id = ? ORDER BY role_exp DESC LIMIT ? OFFSET ?";

		Session session = this.getSessionFactory().getCurrentSession();
		return session.createCriteria(Role.class).addOrder(Order.desc("exp")).setMaxResults(20).list();
	}

	public int countExpAfter(int exp, int serverId) {

		String sql = "SELECT COUNT(*) AS n FROM role WHERE role_exp > ? AND server_id = ?";

		Session session = this.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(sql);
		query.setParameter(1, exp);
		query.setParameter(2, serverId);

		return ((Integer) query.list().iterator().next()).intValue();
	}
	
	
	public Role create(int userId, int serverId) {

		Role role = new Role();
		role.setServerId(serverId);
		role.setName("");
		role.setAp(0);
		role.setApTime(0l);
		role.setAvatar(1);
		role.setBarCoinTime(0l);
		role.setBarGoldTime(0l);
		role.setCoin(0);
		role.setExp(0);
		role.setGold(0);
		role.setProgress(0);
		role.setVip(0);
		role.setUserId(userId);

		Session session = this.getSessionFactory().getCurrentSession();
		session.save(role);
		return role;
	}

	public void update(Role role, Result result) {
		Session session = this.getSessionFactory().getCurrentSession();
		session.update(role);
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
    }

    public void subCoin(Role role, int coin, String desc, int status) {

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

    public void subGold(Role role, int gold, String desc, int status) {

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
    }

	public void addMoney3(Role role, int money3, Result result) {

		result.addRandomItem(new int[]{3 , money3});

		role.setGold(role.getMoney3() + money3);
	}

	public void subMoney3(Role role, int money3) {

		role.setGold(role.getMoney3() - money3);
	}

	public void addMoney4(Role role, int money4, Result result) {

		result.addRandomItem(new int[]{4 , money4});

		role.setGold(role.getMoney4() + money4);
	}

	public void subMoney4(Role role, int money4) {

		role.setGold(role.getMoney4() - money4);
	}

    public void addExp(Role role, int exp, Result result) {

    	int oldExp = role.getExp();
    	int oldLevel = role.level();
    	int oldPhysicalStrength = role.getPhysicalStrength();

        int maxExp = Role.EXP[Role.EXP.length - 1];
        int newExp = oldExp + exp;

    	if (newExp > maxExp) {
    		newExp = maxExp;
    	}
    	
    	role.setExp(newExp);

    	int newLevel = role.level();
    	
        if (newLevel > oldLevel) {

        	// 升级送体力
        	int addPS = 0;
        	for (int i = newLevel; i > oldLevel ; i--) {
        		addPS += LEVEL_ACTION_VALUE[i - 2];
			}

        	role.setPhysicalStrength(oldPhysicalStrength + addPS);

        	result.addPhysicalStrength(addPS);

        	// 检查升级主线任务
        	try {
        		missionDao.checkLevel(role, result);
			} catch (Throwable e) {

			}
        }
        


//        if (oldExp != this.exp) {
//        	this.changeExp = true;
//        }	
    }

    public int[] getVipLevelupGold(int gold) {
	
		int[] arr = new int[]{10 , 100 , 300 , 500 , 1000 , 2000 , 3000 , 5000 , 7000 , 10000 , 15000 , 20000 , 40000 , 80000 , 150000};
		
		for(int i = 0 ; i < arr.length ; i++) {
			if (gold < arr[i]) {
				return new int[]{i, arr[i]};
			}
		}

		return new int[]{arr.length, arr[arr.length - 1]};
    }

    public Role getByServerId(int userId, int serverId) {

		Session session = this.getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Role.class);
		List<Role> list = criteria.add(Restrictions.eq("userId", userId)).add(Restrictions.eq("serverId", serverId)).setMaxResults(1).list();
		if (list.size() == 0) {
			return null;
		}
		return list.get(0);
    }
}
