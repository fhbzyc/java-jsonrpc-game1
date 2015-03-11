package com.zhanglong.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SampleReflect {

	public static Object createObject(String javaName) {
	     try {
	         return Class.forName(javaName).newInstance();
	     } catch (InstantiationException e) {
	         e.printStackTrace();
	     } catch (IllegalAccessException e) {
	         e.printStackTrace();
	     } catch (ClassNotFoundException e) {
	         e.printStackTrace();
	     }
	     return null;
	}

	public static Object invokeMethod(Object obj, String methodName, Object[] params, Class[] clazz) {
		try {
			Method method = obj.getClass().getMethod(methodName, clazz);
			return method.invoke(obj, params);
	    } catch (IllegalArgumentException e) {
	         e.printStackTrace();
	    } catch (IllegalAccessException e) {
	         e.printStackTrace();
	    } catch (InvocationTargetException e) {
	         e.printStackTrace();
	    } catch (SecurityException e) {
	         e.printStackTrace();
	    } catch (NoSuchMethodException e) {
	         e.printStackTrace();
	    }
		return null;
	}
}