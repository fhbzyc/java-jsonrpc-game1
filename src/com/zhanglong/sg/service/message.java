package com.zhanglong.sg.service;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.entity.Arena;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;


/**
 * 滚动消息
 * @author Speed
 *
 */
public class message extends BaseClass {
//
//	/**
//	 * 滚动消息
//	 * @param tokenS
//	 * @return
//	 * @throws Throwable
//	 */
//	public Object msg(String tokenS) throws Throwable {
//
//		String roleId = this.verifyToken(tokenS);
//
//		long time = System.currentTimeMillis();
//
//		String sql = "SELECT * FROM message WHERE create_time > ? AND channel_id = ? ORDER BY id DESC LIMIT 40";
//
//        EntityManager em = this.context.getEntityManager();
//        Query query = em.createNativeQuery(sql, Message.class);
//        query.setParameter(1, time - 60 * 5 * 1000);
//
//        Token T = TokenInstance.getToken(this.context.getRedisHandle());
//        String serverId = T.getServerId(tokenS);
//
//        query.setParameter(2, serverId);
//
//        RoleModel Role = new RoleModel(roleId);
//
//		Result result = new Result();
//		result.setPhysicalStrength(Role.getPhysicalStrength(), Role.mpCoolTime());
//
//		HashMap<String, Object> r = result.returnMap();
//		r.put("msgs", query.getResultList());
//		// 英雄招募冷却时间
//        r.put("bar_gold_time", Role.barGoldTime > time ? Role.barGoldTime - time : 0);
//        // 普通招募冷却时间
//        r.put("bar_coin_time", Role.barCoinTime > time ? Role.barCoinTime - time : 0);
//
//        long time2 = 0l;
//        if (Role.getLevel() >= 10) {
//            // 竞技场时间
//            ArenaModel arenaModel = new ArenaModel(roleId);
//            Arena arenaTable = arenaModel.getArenaTable();
//
//            time2 = arenaTable.getBattleTime() - System.currentTimeMillis();
//            if (time2 < 0) {
//            	time2 = 0;
//            }
//            time2 /= 1000;
//            r.put("challengetime", time2);
//        }
//
//		return r;
//	}
//
//	public static void saveMessage(String content, int serverId) {
//		me.gall.sgp.sdk.entity.app.Message message = new me.gall.sgp.sdk.entity.app.Message();
//		message.setCreateTime(System.currentTimeMillis());
//		message.setTitle("");
//		message.setContent(content);
//		message.setChannelId(String.valueOf(serverId));
//
//		SgpJpaRepository<me.gall.sgp.sdk.entity.app.Message, String> jpaRepository = Utils.getContext().getSgpJpaRepository(me.gall.sgp.sdk.entity.app.Message.class);
//		jpaRepository.save(message);
//	}
}
