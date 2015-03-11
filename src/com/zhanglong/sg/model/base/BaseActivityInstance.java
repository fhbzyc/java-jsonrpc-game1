package com.zhanglong.sg.model.base;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

import com.zhanglong.sg.dao.DailyTaskDao;
import com.zhanglong.sg.utils.Utils;

public class BaseActivityInstance {
//
//	private static long myTime = 0l;
//
//    private static List<DailyTask> activityList;
//
//    public static List<DailyTask> getActivityList() {
//
//		if (activityList == null || System.currentTimeMillis() - myTime > 5 * 60 * 1000) {
//			myTime = System.currentTimeMillis();
//			update();
//		}
//
//		List<DailyTask> list = new ArrayList<DailyTask>();
//		for (DailyTask dailyTask : activityList) {
//			DailyTask temp = new DailyTask();
//			BeanUtils.copyProperties(dailyTask, temp);
//			list.add(temp);
//		}
//
//        return list;
//    }
//
//	private static void update() {
//
//		DailyTaskDao dailyTaskDao = new DailyTaskDao();
//		dailyTaskDao.setSessionFactory(Utils.getSessionFactory());
//
//    	Session session = Utils.getSessionFactory().getCurrentSession();
//    	Criteria criteria  = session.createCriteria(DailyTask.class);
// //   	activityList = criteria.add(Restrictions.eq("available", DailyTask.STATUS_TASK_AVAILABLE)).add(Restrictions.eq("pre_task_id", "activity")).list();
//    }
}
