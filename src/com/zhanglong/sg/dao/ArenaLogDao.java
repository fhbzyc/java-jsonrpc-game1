package com.zhanglong.sg.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.ArenaLog;
import com.zhanglong.sg.entity.BattleLog;

@Repository
public class ArenaLogDao extends BaseDao {

    public ArenaLog findOne(int id) {

        Session session = this.getSessionFactory().getCurrentSession();
        return (ArenaLog) session.get(ArenaLog.class, id);
    }

    public ArenaLog findByRoleId2(int roleId) {

        Session session = this.getSessionFactory().getCurrentSession();
        @SuppressWarnings("unchecked")
		List<ArenaLog> list = session.createCriteria(ArenaLog.class)
		.add(Restrictions.eq("roleId2", roleId))
		.addOrder(Order.desc("id"))
		.setMaxResults(1).list();

        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public void create(ArenaLog arenaLog) {

        Session session = this.getSessionFactory().getCurrentSession();
        session.save(arenaLog);
    }

    public void update(ArenaLog arenaLog) {

        Session session = this.getSessionFactory().getCurrentSession();
        session.update(arenaLog);
    }

	public List<ArenaLog> getByRoleId(int roleId, int page) {

        Session session = this.getSessionFactory().getCurrentSession();

        @SuppressWarnings("unchecked")
		List<ArenaLog> list = session.createCriteria(ArenaLog.class)
        .add(Restrictions.ne("battleResult", BattleLog.BATTLE_LOG_INIT))
        .add(Restrictions.or(Restrictions.eq("roleId1", roleId), Restrictions.eq("roleId2", roleId)))
        .addOrder(Order.desc("id"))
        .setFirstResult((page - 1) * 40)
        .setMaxResults(40).list();

        return list;
    }

	public int wins(int roleId) {

        Session session = this.getSessionFactory().getCurrentSession();

        @SuppressWarnings("unchecked")
		List<ArenaLog> logs = session.createCriteria(ArenaLog.class)
        .add(Restrictions.or(Restrictions.eq("roleId1", roleId), Restrictions.eq("roleId2", roleId)))
        .addOrder(Order.desc("id"))
        .setMaxResults(100).list();

        int wins = 0;
        for (ArenaLog log : logs) {
			if (log.getRoleId1() == roleId && log.getBattleResult() == BattleLog.BATTLE_LOG_WIN) {
				wins++;
			} else {
				break;
			}
		}
        return wins;
    }
}
