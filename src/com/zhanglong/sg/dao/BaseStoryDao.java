package com.zhanglong.sg.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.model.DateNumModel;

@Repository
public class BaseStoryDao extends BaseDao2 {

	@Resource
	private BaseItemDao baseItemDao;
	
	@Resource
	private DateNumDao dateNumDao;

	private static List<BaseStory> copys;
	
	@SuppressWarnings("unchecked")
	public List<BaseStory> findAll() {

		if (copys == null) {
			Session session = this.getBaseSessionFactory().getCurrentSession();
			copys = session.createCriteria(BaseStory.class).addOrder(Order.asc("id")).list();
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
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public int[][] itemIds(BaseStory baseStory) throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper mapper = new ObjectMapper();
        int[][] items = mapper.readValue(baseStory.getItems(), int[][].class);
        return items;
    }

    /**
     * 随机 times 次 掉 N道具
     * @param baseStory
     * @param times
     * @return
     * @throws Exception
     */
    public ArrayList<ArrayList<int[]>> randomItems(int roleId, BaseStory baseStory, int times) throws Exception {

        int[][] ite = itemIds(baseStory);
        int[] must = ite[0];

        boolean doubleTime = isDoubleTime(baseStory);
        if (doubleTime) {
        	must[1] *= 2;
        }

        ArrayList<ArrayList<int[]>> result = new ArrayList<ArrayList<int[]>>();

        int num = 0;
        int heroN = 0;

        long saveTime = 0l;

        DateNumModel dateNumModel = null;
        
        if (baseStory.getType() == BaseStory.HERO_COPY_TYPE) {
            dateNumModel = this.dateNumDao.findOne(roleId);

            if (System.currentTimeMillis() - dateNumModel.dropItemTime < 600l * 1000l) {
                saveTime = dateNumModel.dropItemTime;
                heroN = dateNumModel.dropItemNum;
            }
        }

        for (int i = 1 ; i <= times ; i++) {
            ArrayList<int[]> item = randomItems(baseStory, doubleTime);
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
            } else if (baseStory.getType() == BaseStory.HERO_COPY_TYPE) {
                boolean find = false;
                for (int[] js : item) {
                    if (js[0] == must[0]) {
                        find = true;
                    }
                }
                if (!find) {

                    if (heroN == 2) {
                        Random random = new Random();
                        int r = random.nextInt(10);
                        if (r < 8) {
                        	// 两次没给魂石 80% 给魂石
                            item = new ArrayList<int[]>();
                            item.add(must);
                        }

                    } else if (heroN >= 3) {
                        item = new ArrayList<int[]>();
                        item.add(must);
                    }
                }

                long time = System.currentTimeMillis();
                if (time - saveTime >= 600l * 1000l) {
                    saveTime = time;
                }

                find = false;
                for (int[] js : item) {
                    if (js[0] == must[0]) {
                        find = true;
                    }
                }
                if (find) {
                    heroN = 0;
                } else {
                	heroN++;
                }

                dateNumModel.dropItemNum = heroN;
                dateNumModel.dropItemTime = saveTime;
                this.dateNumDao.save(roleId, dateNumModel);
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

        // 双倍掉落
        if (isDoubleTime(baseStory)) {
            for (ArrayList<int[]> items : result) {
            	for (int[] is : items) {
                   is[1] *= 2;
				}
            }
        } else if (isSoulDoubleTime()) {
            for (ArrayList<int[]> items : result) {

            	for (int[] is : items) {
                	int type = this.baseItemDao.findOne(is[0]).getType();
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
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	private ArrayList<int[]> randomItems(BaseStory baseStory, boolean doubleTime) throws Exception {

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
        
//        String baseStoryType = "";
//        if (baseStory.getType() == BaseStory.COPY_TYPE) {
//            baseStoryType = "copy_double";
//        } else if (baseStory.getType() == BaseStory.HERO_COPY_TYPE) {
//            baseStoryType = "hero_copy_double";
//        }

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
