package com.zhanglong.sg.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utils {

    public static String date() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
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
}
