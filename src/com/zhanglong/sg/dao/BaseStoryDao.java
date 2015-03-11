package com.zhanglong.sg.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity.BaseStory;
import com.zhanglong.sg.utils.Utils;

@Repository
public class BaseStoryDao extends BaseDao {

	private static List<BaseStory> copys;
	
	@SuppressWarnings("unchecked")
	public List<BaseStory> findAll() {

		if (copys == null) {
			Session session = this.getSessionFactory().getCurrentSession();
			copys = session.createCriteria(BaseStory.class).list();
		}
		return copys;
    }

	public BaseStory findOne(int storyId, int storyType) {
		for (BaseStory baseStory : findAll()) {
			if (baseStory.getId() == storyId && baseStory.getType() == storyType) {
				return baseStory;
			}
		}
		return null;
	}

	public List<BaseStory> copys() {
		
		List<BaseStory> list = new ArrayList<BaseStory>();
		
		for (BaseStory baseStory : findAll()) {
			if (baseStory.getType() == BaseStory.COPY_TYPE) {
				list.add(baseStory);
			}
		}
		return list;
	}

	public List<BaseStory> heroCopys() {
		
		List<BaseStory> list = new ArrayList<BaseStory>();
		
		for (BaseStory baseStory : findAll()) {
			if (baseStory.getType() == BaseStory.HERO_COPY_TYPE) {
				list.add(baseStory);
			}
		}
		return list;
	}

	public List<BaseStory> specialCopys() {
		
		List<BaseStory> list = new ArrayList<BaseStory>();
		
		for (BaseStory baseStory : findAll()) {
			if (baseStory.getType() == BaseStory.SPECIAL_COPY_TYPE) {
				list.add(baseStory);
			}
		}
		return list;
	}

    /**
     * 掉落配置
     * [[id, num , rate]]
     * @param baseStory
     * @return
     */
    public int[][] itemIds(BaseStory baseStory) throws Throwable {

        ObjectMapper mapper = new ObjectMapper();
        int[][] items = mapper.readValue(baseStory.getItems(), int[][].class);
        return items;
    }

    /**
     * 随机 times 次 掉 N道具
     * @param baseStory
     * @param times
     * @return
     * @throws Throwable
     */
    public ArrayList<ArrayList<int[]>> randomItems(int roleId, BaseStory baseStory, int times) throws Throwable {

        int[][] ite = this.itemIds(baseStory);
        int[] must = ite[0];

        ArrayList<ArrayList<int[]>> result = new ArrayList<ArrayList<int[]>>();
        int num = 0;
        int heroN = 0;

        for (int i = 1 ; i <= times ; i++) {
            ArrayList<int[]> item = randomItems(baseStory);
            if (i == 10) {
                if (num == 0) {
                    item = new ArrayList<int[]>();
                    item.add(must);
                } else if (num == 1) {
                    Random random = new Random();
                    int r = random.nextInt(10);
                    if (r < 8) {
                        item = new ArrayList<int[]>();
                        item.add(must);
                    }
                }
            } 

            boolean find = false;
            for (int[] js : item) {
                if (js[0] == must[0]) {
                    find = true;
                }
            }
            if (find) {
                num++;
            }

            result.add(item);
        }

        BaseItemDao baseItemDao = Utils.getApplicationContext().getBean(BaseItemDao.class);
        // 双倍灵魂石掉落
        if (isSoulDoubleTime()) {
            for (ArrayList<int[]> items : result) {

            	for (int[] is : items) {
                	int type = baseItemDao.findOne(is[0]).getType();
                    if (type == 6) {
                    	is[1] *= 2;
                    }
				}
            }
        }

        return result;
    }

    /**
     * 
     * @param baseStory
     * @return
     * @throws Throwable
     */
    private ArrayList<int[]> randomItems(BaseStory baseStory) throws Throwable {

        int[][] li = this.itemIds(baseStory);
        ArrayList<int[]> items = new ArrayList<int[]>();
        for (int[] l : li) {
            items.add(l);
        }

        Random random = new Random();

        int r = random.nextInt(100);
        int count = 0;
        if (r < 40) {
            count = 1;
        } else if (r < 71) {
            count = 2;
        } else if ( r < 88) {
            count = 3;
        } else if ( r < 95) {
            count = 4;
        }

        int rate = 0;
        for (int[] is : items) {
            rate += is[2];
            is[2] = rate;
        }

        ArrayList<int[]> result = new ArrayList<int[]>();
        for (int i = 0 ; i < count ; i++) {

            r = random.nextInt(items.get(items.size() - 1)[2]);

            for (int j = 0 ; j < items.size() ; j++) {

                if (r < items.get(j)[2]) {

                    result.add(new int[]{items.get(j)[0] , items.get(j)[1]});

                    ArrayList<int[]> newItems = new ArrayList<int[]>();
                    for (int k = 0 ; k < items.size() ; k++) {
                        if (k != j) {

                            int[] arr = items.get(k);

                            if (k > j) {
                                if (j == 0) {
                                    arr[2] -= items.get(j)[2];
                                } else {
                                    arr[2] -= items.get(j)[2] - items.get(j - 1)[2];
                                }
                            }
                            
                            newItems.add(arr);
                        }
                    }

                    items = (ArrayList<int[]>) newItems.clone();
                    break;
                }
            }
        }

        // 双倍掉落
        if (isDoubleTime(baseStory)) {
            for (int[] item : result) {
                item[1] *= 2;
            }
        }

        BaseItemDao baseItemDao = Utils.getApplicationContext().getBean(BaseItemDao.class);

        // 双倍灵魂石掉落
        if (this.isSoulDoubleTime()) {
            for (int[] item : result) {
                if (baseItemDao.findOne(item[0]).getType() == 6) {
                    item[1] *= 2;
                }
            }
        }

        return result;
    }

    /**
     * 扫荡掉落
     * @return
     */
    public int[][] decodeWipeOutItems(BaseStory baseStory) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            int[][] items = mapper.readValue(baseStory.getWipeOutItems(), int[][].class);

            if (this.isDoubleTime(baseStory)) {
                for (int[] item : items) {
                    item[1] *= 2;
                }
            }

            return items;

        } catch (Exception e) {
            return new int[][]{};
            // TODO: handle exception
        }
    }

    private boolean isDoubleTime(BaseStory baseStory) {
        
        String baseStoryType = "";
        if (baseStory.getType() == BaseStory.COPY_TYPE) {
            baseStoryType = "activity_copy_double";
        } else if (baseStory.getType() == BaseStory.HERO_COPY_TYPE) {
            baseStoryType = "activity_hero_copy_double";
        }

//        ArrayList<DailyTask> activityList = BaseActivityInstance.getActivityList();
//        for (DailyTask baseActivity : activityList) {
//
//            if (baseActivity.getType().equals(baseStoryType)) {
//
//                long time = System.currentTimeMillis();
//                if (time >= baseActivity.getStartTime() && time < baseActivity.getEndTime()) {
//                    Integer storyId = baseActivity.getCampaignId();
//                    if (storyId != null && (int)storyId == (int)baseStory.getId()) {
//                        return true;
//                    }
//                }
//            }
//        }
        return false;
    }

    /**
     * 双倍灵魂石活动
     * @return
     */
    public boolean isSoulDoubleTime() {

//        String baseStoryType = "activity_soul_double";
//        ArrayList<DailyTask> activityList = BaseActivityInstance.getActivityList();
//        for (DailyTask baseActivity : activityList) {
//
//            if (baseActivity.getType().equals(baseStoryType)) {
//                
//                long time = System.currentTimeMillis();
//                if (time >= baseActivity.getStartTime() && time < baseActivity.getEndTime()) {
//
//                    return true;
//                }
//            }
//        }
        return false;
    }
}
