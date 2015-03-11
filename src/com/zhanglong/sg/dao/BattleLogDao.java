package com.zhanglong.sg.dao;

import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BattleLog;

@Repository
public class BattleLogDao extends BaseDao {

	public BattleLogDao() {
	}

	public BattleLog create(BattleLog battleLog) {

		int unixTime = (int)(System.currentTimeMillis() / 1000l);

		battleLog.setBattleResult(BattleLog.BATTLE_LOG_INIT);
        battleLog.setBeginTime(unixTime);
        battleLog.setEndTime(unixTime);
        if (battleLog.getData() == null) {
        	battleLog.setData("");
        }

        this.getSessionFactory().getCurrentSession().save(battleLog);

        return battleLog;
	}

	public void update(BattleLog battleLog) {

        this.getSessionFactory().getCurrentSession().update(battleLog);
	}

	public BattleLog findOne(int id) {

        return (BattleLog) this.getSessionFactory().getCurrentSession().get(BattleLog.class, id);
	}
}
