package com.zhanglong.sg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class Utils {

	private static SessionFactory sessionFactory;

    public static String date() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static void addCustomEvent(String eventId, HashMap<String, String> eventData, String sgpPlayerId) {
    }

    public static void addCustomEvent(String eventId, String key, String value, String sgpPlayerId) {
    }

    /**
     * 根据概率随机一个值 返回的是数组的下标
     * @param array
     * @return
     */
    public static int randomGetOne(int[] array) {

    	int count = 0;
    	int[] newArray = new int[array.length];

    	for (int i = 0 ; i < array.length ; i++) {
    		count += array[i];
    		newArray[i] = count;
		}

    	Random random = new Random();
		int r = random.nextInt(newArray[newArray.length - 1]);

		for (int i = 0; i < newArray.length; i++) {
			if (r < newArray[i]) {
				return i;
			}
		}

		return newArray.length - 1;
	}

//	public static SessionFactory getSessionFactory() {
//		if (sessionFactory == null) {
//			ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("beans.xml");
//			sessionFactory = (SessionFactory)ac.getBean("sessionFactory");
//		}
//		return sessionFactory;
//	}
//
//	public static void setSessionFactory(SessionFactory s) {
//		sessionFactory = s;
//	}
//
//	public static RedisTemplate getRedisTemplate() {
//		return new RedisTemplate();
//	}

    private static ClassPathXmlApplicationContext classPathXmlApplicationContext;

	public static ApplicationContext getApplicationContext() {

		if (classPathXmlApplicationContext == null) {
			classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
		}

		return classPathXmlApplicationContext;
	}
}
