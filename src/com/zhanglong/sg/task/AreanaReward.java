package com.zhanglong.sg.task;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.RewardDao;
import com.zhanglong.sg.entity.Server;

public class AreanaReward {
//
//	@Override
//	public void run(Context context, SchedulerEvent arg1) throws Throwable {
//
//		EntityManager em = context.getEntityManager();
//		Query query = em.createNativeQuery("SELECT * FROM `servers` WHERE `state` > ?", Server.class);
//		query.setParameter(1, 0);
//		List<Server> server = query.getResultList();
//
//		ArenaModel arenaModel = new ArenaModel();
//
//		for (Server serverTable : server) {
//			arenaModel.setLastRank(serverTable.getId());
//			this.mail(context, serverTable.getId());
//		}
//	}
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void mail(Context context, int serverId) throws Throwable {
//
//    	ArenaModel arenaModel = new ArenaModel();
//        ArrayList<int[]> configs = arenaModel.rewardConfig();
//
//        ArrayList<String> list = arenaModel.getLastRank(serverId);
//
//        long time = System.currentTimeMillis();
//
//        EntityManager em = context.getEntityManager();
//
//        for (int[] config : configs) {
//
//			RewardModel rewardModel = new RewardModel();
//			int[] itemId = new int[]{config[7]};
//			int[] itemNum = new int[]{config[8]};
//			String json = rewardModel.getJson(itemId, itemNum, config[5], config[3], null, config[6]);
//
//			int max = config[2];
//			if (max > list.size()) {
//				max = list.size();
//			}
//			
//			if (max < config[1]) {
//				break;
//			}
//
//			String sql = "";
//
//			String from_name = "游戏管理员";
//			String title = "竞技场每日排名奖励";
//
//			int parameters = 1;
//
//			for (int rank = config[1] ; rank <= max; rank++) {
//
//				String roleId = list.get(rank - 1);
//
//	            Pattern p = Pattern.compile("[a-zA-Z]"); 
//	            Matcher m = p.matcher(roleId);
//	            boolean find = m.find();
//
//				if (!find) {
//					continue;
//				}
//
//	        	String content = "截止今天21：00，你的竞技场排名为：" + rank + "名。\n" + 
//	        			"鉴于您在竞技场的优秀表现。竞技场管理处将授予您以下奖励。\n\n" + 
//	        			"竞技场教官：吕小布";
//
//				if (sql.equals("")) {
//					sql = String.format("INSERT INTO `mail`(`attach_status`,`status`,`to_name`,`type`,`attchment`,`content`,`from_name`,`send_time`,`title`,`to_id`) VALUES (0,0,'',1,?,'%s','%s','%s','%s','%s')",
//							content, from_name, time, title, roleId);
//				} else {
//					sql += String.format(",(0,0,'',1,?,'%s','%s','%s','%s','%s')", content, from_name, time, title, roleId);
//				}
//
//				parameters++;
//
//				if (parameters >= 1000) {
//					// 1000条拼成一条
//					Query query = em.createNativeQuery(sql, Object.class);
//
//					for (int i = 1 ; i < parameters; i++) {
//
//						query.setParameter(i, json);
//					}
//
//					query.executeUpdate();
//					
//					parameters = 1;
//					sql = "";
//				}
//			}
//
//			if (sql.equals("")) {
//				continue;
//			}
//
//			Query query = em.createNativeQuery(sql, Object.class);
//
//			for (int i = 1 ; i < parameters; i++) {
//
//				query.setParameter(i, json);
//			}
//
//	        query.executeUpdate();
//		}
//    }
}
