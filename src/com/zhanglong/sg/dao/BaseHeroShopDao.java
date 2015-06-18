package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity2.BaseHeroShop;

@Repository
public class BaseHeroShopDao extends BaseDao2 {

	private static long myTime = 0l;

	private static List<BaseHeroShop> list;

	@Resource
	private BaseHeroDao baseHeroDao;

	@SuppressWarnings("unchecked")
	public List<BaseHeroShop> findAll() {

		long time = System.currentTimeMillis();
		if (BaseHeroShopDao.list == null ||  time - BaseHeroShopDao.myTime > 5l * 60l * 1000l) {

			BaseHeroShopDao.myTime = time;

			Session session = this.getBaseSessionFactory().getCurrentSession();
			BaseHeroShopDao.list = session.createCriteria(BaseHeroShop.class).addOrder(Order.asc("id")).list();
		}

		return BaseHeroShopDao.list;
	}

	public List<BaseHeroShop> findAll(String type) {

		List<BaseHeroShop> list = this.findAll();

		List<BaseHeroShop> result = new ArrayList<BaseHeroShop>();
		for (BaseHeroShop baseHeroShop : list) {
			if (baseHeroShop.getType().equals(type)) {
				result.add(baseHeroShop);
			}
		}

		return result;
	}

    public int[] coinRandom() {

		List<BaseHeroShop> list = this.findAll("coin");

		List<int[]> randomList = new ArrayList<int[]>();

        Random random = new Random();
        int r = random.nextInt(100);

		int q = 0;

        if (r < 50) {
        	q = 1;
        } else if (r < 79) {
        	q = 2;
        } else if (r < 89) {
        	q = 3;
        } else if (r < 94) {
        	q = 4;
        } else if (r < 98) {
        	q = 5;
        } else {
        	q = 6;
        }

		int w = 0;
		for (BaseHeroShop item : list) {

			if (q == item.getQuality()) {
	        	w += item.getWeight();
	        	randomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
			}
		}

        return randomFromList(randomList);
    }

    public int[] goldRandom() {

		List<BaseHeroShop> list = this.findAll("gold");

        Random random = new Random();
        int r = random.nextInt(100);

        List<int[]> randomList = new ArrayList<int[]>();

		int q = 0;

        if (r < 20) {
        	q = 2;
        } else if (r < 50) {
        	q = 3;
        } else if (r < 65) {
        	q = 4;
        } else if (r < 80) {
        	q = 5;
        } else if (r < 88) {
        	q = 6;
        } else {
        	q = 7;
        }

        int w = 0;
        for (BaseHeroShop item : list) {
        	
			if (q == item.getQuality()) {
	        	w += item.getWeight();
	        	randomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
			}
		}

        return randomFromList(randomList);
    }

    private int[] randomFromList(List<int[]> randomList) {

    	Random random = new Random();

    	int[] last = randomList.get(randomList.size() - 1);
    	
        int r = random.nextInt(last[0]);

        int[] result = new int[2];
        for (int[] rate : randomList) {
            if (r < rate[0]) {
                result[0] = rate[1];
                int n = rate[3] - rate[2];
                result[1] = random.nextInt(n + 1) + rate[2];
                return result;
            }
        }

        result[0] = last[1];
        result[1] = random.nextInt(last[3] - last[2]) + last[2];
        return result;
    }

    public int[] randomGeneral() {

		ArrayList<int[]> goldRandom6 = new ArrayList<int[]>();

		List<BaseHeroShop> list = this.findAll("gold");

		int g6 = 0;

		for (BaseHeroShop item : list) {
			if (item.getQuality() == 6) {
				g6 += item.getWeight();
				goldRandom6.add(new int[]{g6 , Integer.valueOf(item.getItemId()) , item.getMinNum() , item.getMaxNum()});
			}
		}

    	return randomFromList(goldRandom6);
    }

    public int[] randomGeneral(int star) throws Exception {

		ArrayList<int[]> goldRandom6 = new ArrayList<int[]>();

		List<BaseHeroShop> list = this.findAll("gold");

		int g6 = 0;

		for (BaseHeroShop item : list) {
			if (item.getQuality() == 6 && this.baseHeroDao.findOne(item.getItemId()).getStar() == star) {
				g6 += item.getWeight();
				goldRandom6.add(new int[]{g6 , Integer.valueOf(item.getItemId()) , item.getMinNum() , item.getMaxNum()});
			}
		}

    	return randomFromList(goldRandom6);
    }
}
