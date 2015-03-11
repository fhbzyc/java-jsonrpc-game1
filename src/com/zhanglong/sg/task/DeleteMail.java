package com.zhanglong.sg.task;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.transaction.annotation.Transactional;

public class DeleteMail {
//
//	@Override
//	public void run(Context context, SchedulerEvent arg1) throws Throwable {
//		this.delete(context);
//	}
//
//    @Transactional(rollbackFor = Throwable.class)
//	public void delete(Context context) {
//
//		String sql = "DELETE FROM `mail` WHERE (`attchment` IS NULL OR `attchment` = '') AND `send_time` < ? ";
//
//		EntityManager em = context.getEntityManager();
//		Query query = em.createNativeQuery(sql, Object.class);
//		query.setParameter(1, System.currentTimeMillis() - 30l * 86400l * 1000l);
//		query.executeUpdate();
//	}
}
