package com.zhanglong.sg.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import websocket.handler.EchoHandler;

import com.zhanglong.sg.entity2.BaseBoss;
import com.zhanglong.sg.model.Boss;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BaseBossDao extends BaseDao2 {

	private static HashMap<Integer, Boss> bosses = new HashMap<Integer, Boss>();

	public static HashMap<Integer, Integer> damages = new HashMap<Integer, Integer>();

	private static int date;

	public HashMap<Integer, Integer> getDamagesMap() {
		if (BaseBossDao.date != Integer.valueOf(Utils.date())) {
			BaseBossDao.date = Integer.valueOf(Utils.date());
			BaseBossDao.damages = new HashMap<Integer, Integer>();
		}
		return BaseBossDao.damages;
	}

	public BaseBoss findOne() throws Exception {

		Session session = this.getBaseSessionFactory().getCurrentSession();

		@SuppressWarnings("unchecked")
		List<BaseBoss> list = session.createCriteria(BaseBoss.class).addOrder(Order.asc("id")).list();
		if (list.size() != 0) {
			return list.get(0);
		}

		throw new Exception("世界BOSS配置出错");
	}

	public int getHp(int serverId) throws Exception {
		Boss boss = bosses.get(serverId);
		int date = Integer.valueOf(Utils.date());

		if (boss == null || boss.getDate() != date) {

			EchoHandler.bossClear(serverId);

			BaseBoss baseBoss = this.findOne();

			boss = new Boss();

			boss.setDate(date);
			boss.setHp(baseBoss.getHp());

			BaseBossDao.bosses.put(serverId, boss);

			this.getDamagesMap();
		}
		return boss.hp;
	}

	public void subHp(int serverId, int roleId, int damage) throws Exception {
		Boss boss = bosses.get(serverId);
		int date = Integer.valueOf(Utils.date());
		if (boss == null || boss.getDate() != date) {

			EchoHandler.bossClear(serverId);

			BaseBoss baseBoss = this.findOne();

			boss = new Boss();
			boss.setDate(date);
			boss.setHp(baseBoss.getHp());
			BaseBossDao.bosses.put(serverId, boss);

			this.getDamagesMap();
		}
		boss.hp -= damage;
		if (boss.hp < 0) {
			boss.hp = 0;
		}

		Integer myDamage = BaseBossDao.damages.get(roleId);
		if (myDamage == null) {
			BaseBossDao.damages.put(roleId, damage);
		} else {
			BaseBossDao.damages.put(roleId, myDamage + damage);
		}
	}
}
