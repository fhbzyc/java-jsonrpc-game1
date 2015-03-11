package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.zhanglong.sg.entity.BaseHeroShop;

@Repository
public class BaseHeroShopDao extends BaseDao {

	private static List<BaseHeroShop> list;

	@Resource
	private BaseHeroDao baseHeroDao;

	@SuppressWarnings("unchecked")
	public List<BaseHeroShop> findAll() {

		if (BaseHeroShopDao.list == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			BaseHeroShopDao.list = session.createCriteria(BaseHeroShop.class).list();
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

		int w = 0;
		ArrayList<int[]> coinRandomList = new ArrayList<int[]>();

        Random random = new Random();
        int r = random.nextInt(100);

		for (BaseHeroShop item : list) {
			
			int q = item.getQuality();

	        if (r < 50 && q == 1) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        } else if (r < 79 && q == 2) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        } else if (r < 89 && q == 3) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        } else if (r < 94 && q == 4) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        } else if (r < 98 && q == 5) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        } else if (q == 6) {
	        	w += item.getWeight();
	            coinRandomList.add(new int[]{w , item.getItemId() , item.getMinNum() , item.getMaxNum()});
	        }
		}

        return randomFromList(coinRandomList);
    }

    public int[] goldRandom() {

		List<BaseHeroShop> list = this.findAll("gold");

        Random random = new Random();
        int r = random.nextInt(100);

        ArrayList<int[]> goldRandomList = new ArrayList<int[]>();

        int w = 0;
        
        for (BaseHeroShop baseHeroShop : list) {
        	
        	int q = baseHeroShop.getQuality();

            if (r < 20 && q == 2) {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            } else if (r < 50 && q == 3) {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            } else if (r < 65 && q == 4) {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            } else if (r < 80 && q == 5) {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            } else if (r < 88 && q == 6) {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            } else {
            	w += baseHeroShop.getWeight();
            	goldRandomList.add(new int[]{w , Integer.valueOf(baseHeroShop.getItemId()) , baseHeroShop.getMinNum() , baseHeroShop.getMaxNum()});
            }
		}

        return randomFromList(goldRandomList);
    }

    private int[] randomFromList(ArrayList<int[]> randomList) {

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

    public int[] randomGeneral(int star) throws Throwable {

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
