package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import me.gall.sgp.node.pojo.app.Item;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.MissionDao;
import com.zhanglong.sg.entity.BaseDailyTask;
import com.zhanglong.sg.entity.BaseHeroShop;
import com.zhanglong.sg.entity.BaseItem;
import com.zhanglong.sg.entity.BaseItemShop;
import com.zhanglong.sg.entity.BaseMission;
import com.zhanglong.sg.entity.ItemTable;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.service.HeroService;
import com.zhanglong.sg.service.ItemService;
import com.zhanglong.sg.service.MissionService;
import com.zhanglong.sg.service.ShopService;
import com.zhanglong.sg.service.UserService;
import com.zhanglong.sg.service.RoleService;
import com.zhanglong.sg.service.StoryService;
import com.zhanglong.sg.utils.Utils;

public class test {

	public static void main(String[] args) throws Throwable {
		
////		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
////		login userImpl = (login)ac.getBean("login");
////
////		userImpl.register("12345", "123");
//		
//		String time = "2014-12-12 13:55:36.0";
//
//		
//		 time = time.substring(0, 19);
//		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		 Date date = format.parse(time);
//		 date.getTime();  
//		System.out.print( date.getTime());
//		return;
		
		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
		MissionService missionService = (MissionService) ac.getBean("missionService");

//		SessionFactory sessionFactory = (SessionFactory) ac.getBean("sessionFactory");
//		Session session = sessionFactory.openSession();
//		
//		List<Item> list = session.createCriteria(Item.class).list();
//		
//		Transaction transation = session.beginTransaction();
//		
//		for (Item object : list) {
//
//			int type = 1;
//			
//			if (object.getStoreId().equals("pk_store") ) {
//				type = 2;
//			}
//
//			
//			int moneyType = 1;
//			if (object.getPriceUnit().equals("UNIT_DIAMOND")) {
//				moneyType = 2;
//			}
//			
//			if (object.getStoreId().equals("pk_store")) {
//				moneyType = 3;
//			}
//			
//			BaseItemShop baseMission = new BaseItemShop();
//			
//			BaseItem baseItem = (BaseItem) session.get(BaseItem.class, Integer.valueOf(object.getCustomId()));
//
//			
//			baseMission.setBaseItem(baseItem);
//			baseMission.setMoneyType(moneyType);
//			baseMission.setPrice(object.getPrice());
//			baseMission.setType(type);
//			baseMission.setNum(object.getAmount());
//
//
//			session.save(baseMission);
//		}
//		transation.commit();
		
		

		Object o = missionService.taskList();
	//	Object o = storyService.endBattle("", 3, true, 88, new int[]{}, new int[]{}, "");
		
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.print(objectMapper.writeValueAsString(o));
	}
}
