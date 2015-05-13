package com.zhanglong.sg.service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.jsonrpc4j.JsonRpcService;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.dao.BaseHeroEquipDao;
import com.zhanglong.sg.dao.BaseHeroShopDao;
import com.zhanglong.sg.dao.BaseMakeItemDao;
import com.zhanglong.sg.entity.BaseHero;
import com.zhanglong.sg.entity.BaseHeroEquip;
import com.zhanglong.sg.entity.BaseHeroShop;
import com.zhanglong.sg.entity.BaseItem;
import com.zhanglong.sg.entity.BaseMakeItem;
import com.zhanglong.sg.entity.BaseSkill;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Item;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
@JsonRpcService("/hero")
public class HeroService extends BaseService {

    @Resource
    private BaseHeroShopDao baseHeroShopDao;

    @Resource
    private BaseHeroEquipDao baseHeroEquipDao;

    @Resource
    private BaseMakeItemDao baseMakeItemDao;

    
    public static int TYPE_COIN_RANDOM = 1;
    public static int TYPE_GOLD_RANDOM = 2;

    /**
     * 
     * @return
     * @throws Throwable
     */
    public Object list() throws Throwable {

        int roleId = this.roleId();

        List<Hero> queryList = this.heroDao.findAll(roleId);

        Result result = new Result();

        for (Hero hero : queryList) {
            result.addHero(hero);
        }

        return this.success(result.toMap());
    }

    /**
     * 
     * @param heroId
     * @param skillIndex
     * @return
     * @throws Throwable
     */
    public Object skillLevelUp(int heroId, int skillIndex) throws Throwable {

    	int roleId = this.roleId();

        if (skillIndex < 1 || skillIndex > 4) {
            return this.returnError(this.lineNum(), "参数出错, skillIndex 出错");
        }

        BaseHero baselGeneral = this.baseHeroDao.findOne(heroId);

        Hero hero = this.heroDao.findOne(roleId, heroId);
        if (hero == null) {
            return this.returnError(this.lineNum(), "未拥有这个英雄  generalId: " + heroId);
        }

        // 这里还要验证一个技能点数   // 1级两个技能点 
        if (hero.getAvailableSkillPoint() <= 0) {
            return this.returnError(this.lineNum(), "技能点不足");
        }

        BaseSkill baselSkill = null;
        // 验证阶级 0 , 1 , 3 , 6
        if (skillIndex == 1) {
        	baselSkill = baselGeneral.getSkill1();

        } else if(skillIndex == 2) {

        	if (hero.getCLASS() < 1) {
        		return this.returnError(this.lineNum(), "英雄 阶级不够");
        	} else {
        		baselSkill = baselGeneral.getSkill2();
        	}
            
        } else if(skillIndex == 3) {

        	if (hero.getCLASS() < 3) {
        		return this.returnError(this.lineNum(), "英雄 阶级不够");
        	} else {
        		baselSkill = baselGeneral.getSkill3();
        	}

        } else if(skillIndex == 4) {

        	if (hero.getCLASS() < 6) {
        		return this.returnError(this.lineNum(), "英雄 阶级不够");
        	} else {
        		baselSkill = baselGeneral.getSkill4();
        	}
        }

        if (baselSkill == null) {
            return this.returnError(this.lineNum(), "参数出错, baselSkill 不存在");
        }

        int skillLevel = 0;
        if(skillIndex == 1) {
            skillLevel = hero.getSkill1Level();
            hero.setSkill1Level(skillLevel + 1);

        } else if(skillIndex == 2) {
            skillLevel = hero.getSkill2Level();
            hero.setSkill2Level(skillLevel + 1);

        } else if(skillIndex == 3) {
            skillLevel = hero.getSkill3Level();
            hero.setSkill3Level(skillLevel + 1);

        } else if(skillIndex == 4) {
            skillLevel = hero.getSkill4Level();
            hero.setSkill4Level(skillLevel + 1);
        }

        if (skillLevel >= hero.getLevel()) {
            return this.returnError(this.lineNum(), "技能等级不能高于角色等级");
        }

        if (skillLevel >= baselSkill.getMaxLevel()) {
            return this.returnError(this.lineNum(), "技能已是最大等级");
        }

        int coin = baselSkill.getBaseCoin() + (skillLevel - 1) * baselSkill.getLevelupCoin();

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);

        if (role.getCoin() < coin) {
        	return this.returnError(this.lineNum(), "铜钱不足");
        } else {
        	this.roleDao.subCoin(role, coin, baselGeneral.getName() + ">升级技能<" + skillIndex + ">, level(" + skillLevel + " -> " + (skillLevel + 1) +")", FinanceLog.STATUS_GEN_SKILL_LEVELUP);
        	this.roleDao.update(role, result);
        }

        this.heroDao.update(hero, result);

        // 升级技能日常任务
        dailyTaskDao.addSkill(role, result);

        return this.success(result.toMap());
    }

    /**
     * 加属性点
     * @param tokenS
     * @param generalId
     * @param points
     * @return
     * @throws Throwable
     */
    public Object addPoint(int heroId, int[] points) throws Throwable {

    	int roleId = this.roleId();

        if (points.length != 3) {
            return this.returnError(this.lineNum(), "Invalid argument");
        }
        int STR = points[0];
        int INT = points[1];
        int DEX = points[2];

        if (STR < 0 || INT < 0 || DEX < 0) {
        	return this.returnError(this.lineNum(), "Invalid argument");
        }

        Hero hero = this.heroDao.findOne(roleId, heroId);
        if (hero == null) {
            return this.returnError(this.lineNum(), "未拥有这个英雄  heroId: " + heroId);
        }

        if (STR + INT + DEX > hero.getAvailablePoint()) {
            return this.returnError(this.lineNum(), "参数出错 ,属性点超过最大值");
        }

        hero.setStr(hero.getStr() + STR);
        hero.setINT(hero.getINT() + INT);
        hero.setDex(hero.getDex() + DEX);

        Result result = new Result();
        this.heroDao.update(hero, result);
        return this.success(result.toMap());
    }

    /**
     * 升星
     * @param heroId
     * @throws Throwable
     */
    public Object starUp(int heroId) throws Throwable {

    	int roleId = this.roleId();

        Hero general = this.heroDao.findOne(roleId, heroId);
        if (general == null) {
            return this.returnError(this.lineNum(), "未拥有这个英雄  heroId: " + heroId);
        }

        if (general.getStar() >= 5) {
            return this.returnError(this.lineNum(), "已经最大星");
        }

        int coin = 0;
        int num = 0;
        if (general.getStar() <= 1) {
            num = 20;
            coin = 35000;
        } else if (general.getStar() == 2) {
            num = 50;
            coin = 120000;
        } else if (general.getStar() == 3) {
            num = 100;
            coin = 300000;
        } else {
            num = 150;
            coin = 800000;
        }

        Role role = this.roleDao.findOne(roleId);

        if (role.getCoin() < coin) {
        	return returnError(lineNum(), "铜钱不足");
        }

        Item item = this.itemDao.findOneByItemId(roleId, heroId - 6000);

        if (item == null) {
            return this.returnError(this.lineNum(), "灵魂石数量不足 数量为 0");
        }
        if (item.getNum() < num) {
            return this.returnError(this.lineNum(), "灵魂石数量不足 当前数量: " + item.getNum() + " , 还差: " + (num - item.getNum()));
        }

        Result result = new Result();

        this.roleDao.subCoin(role, coin, this.baseHeroDao.findOne(heroId).getName() + " 升星<" + general.getStar() + " -> " + (general.getStar() + 1) + ">", FinanceLog.STATUS_GEN_STAR_UP);
        this.roleDao.update(role, result);

        this.itemDao.subItem(item, num, result);

        general.setStar(general.getStar() + 1);
        this.heroDao.update(general, result);
        return this.success(result.toMap());
    }

    /**
     * 升阶
     * @param heroId
     * @return
     * @throws Throwable
     */
    public Object classUp(int heroId) throws Throwable {

    	int roleId = this.roleId();

        Hero general = this.heroDao.findOne(roleId, heroId);
        if (general == null) {
            return this.returnError(this.lineNum(), "未拥有这个英雄  generalId: " + heroId);
        }

        if (general.getCLASS() >= baseHeroEquipDao.findByHeroId(heroId).size()) {
            return this.returnError(this.lineNum(), "已经是最高阶了");
        }

        if (!general.getEquip1()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        } else if (!general.getEquip2()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        } else if (!general.getEquip3()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        } else if (!general.getEquip4()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        } else if (!general.getEquip5()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        } else if (!general.getEquip6()) {
            return this.returnError(this.lineNum(), "身上装备数量不够不能升阶");
        }

        int newClass = general.getCLASS() + 1;

        general.setEquip1(false);
        general.setEquip2(false);
        general.setEquip3(false);
        general.setEquip4(false);
        general.setEquip5(false);
        general.setEquip6(false);
        general.setCLASS(newClass);

        Result result = new Result();
        this.heroDao.update(general, result);

        Role role = this.roleDao.findOne(roleId);
        
        // 进阶主线任务
        missionDao.checkHeroClass(role, newClass, 1, result);

        return this.success(result.toMap());
    }

    /**
     * 更换装备
     * @param heroId
     * @param equipId
     * @param index
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object equipment(int heroId, int equipId, int index) throws Throwable {

    	int roleId = this.roleId();

        if (index < 1 || index > 6) {
            return this.returnError(this.lineNum(), "装备位置参数不正确");
        }

        Hero hero = this.heroDao.findOne(roleId, heroId);
        if (hero == null) {
            return this.returnError(this.lineNum(), "未拥有这个英雄  generalId: " + heroId);
        }

        Item equip = this.itemDao.findOne(equipId);
        if (equip == null) {
            return this.returnError(this.lineNum(), "未拥有这个道具 itemId: " + equipId);
        }

        BaseHeroEquip equips = this.baseHeroEquipDao.findByHeroId(heroId).get(hero.getCLASS());

        BaseItem baseEquip = this.baseItemDao.findOne(equip.getItemId());

        if (index == 1) {
            if (hero.getEquip1()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip1().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip1(true);
        } else if (index == 2) {
            if (hero.getEquip2()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip2().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip2(true);
        } else if (index == 3) {
            if (hero.getEquip3()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip3().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip3(true);
        } else if (index == 4) {
            if (hero.getEquip4()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip4().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip4(true);
        } else if (index == 5) {
            if (hero.getEquip5()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip5().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip5(true);
        } else if (index == 6) {
            if (hero.getEquip6()) {
                return this.returnError(this.lineNum(), "位置已有装备");
            } else if ((int)equips.getEquip6().getBaseId() != (int)baseEquip.getBaseId()) {
            	return this.returnError(this.lineNum(), "装备不匹配");
            }
            hero.setEquip6(true);
        }

        Result result = new Result();

        this.itemDao.subItem(equip, 1, result);
        this.heroDao.update(hero, result);

        return this.success(result.toMap());
    }

    /**
     * 酒馆随机一次武将
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object randomGeneral(int type) throws Throwable {

    	int roleId = this.roleId();

        if (type != TYPE_COIN_RANDOM && type != TYPE_GOLD_RANDOM) {
            return this.returnError(this.lineNum(), "参数出错");
        }

        int coin = 10000;
        int gold = 288; // 需要花费的元宝

        int itemId = 0;
        int num = 1;

        List<Hero> heros = this.heroDao.findAll(roleId);
        if (heros.size() == 1) {
        	itemId = 10009;
        } else if (heros.size() == 2) {
        	itemId = 10012;
        } else {
            // 随机一个将出来
            int[] rate;
            if (type == TYPE_COIN_RANDOM) {
            	rate = this.baseHeroShopDao.coinRandom();
            } else {
            	rate = this.baseHeroShopDao.goldRandom();
            }
            itemId = rate[0];
            num = rate[1];
        }

        String desc = "金币";
        if (type == TYPE_GOLD_RANDOM) {
        	desc = "元宝";
        } 

        desc += "酒馆单次招募,获得<";
        if (itemId >= 10000) {
        	desc += this.baseHeroDao.findOne(itemId).getName();
        } else {

        	desc += this.baseItemDao.findOne(itemId).getName();
        }
        desc += ">";

        Role role = this.roleDao.findOne(roleId);

        DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);

        if (type == TYPE_COIN_RANDOM) {

        	if (dateNumModel.getBarCoinNum() >= 6 || dateNumModel.barTime > System.currentTimeMillis()) {
                if (role.getCoin() < coin) {
                	return returnError(lineNum(), "铜钱不足");
                } else {
                	roleDao.subCoin(role, coin, desc, FinanceLog.STATUS_RANDOM_GEN_ONE_TIMES_COIN);
                }
        	} else {
        		// 酒馆每日免费刷新次数加一
        		dateNumModel.setBarCoinNum(dateNumModel.getBarCoinNum() + 1);

        		if (dateNumModel.getBarCoinNum() >= 6) {
        	        Date date = new SimpleDateFormat("yyyyMMdd").parse(Utils.date());
        	        dateNumModel.barTime = date.getTime() + 24l * 3600l * 1000l;
        		} else {
        			dateNumModel.barTime = System.currentTimeMillis() + 60l * 5l * 1000l;
        		}

        		this.dateNumDao.save(roleId, dateNumModel);
        	}
        } else {

        	if (System.currentTimeMillis() / 1000 < role.getBarGoldTime()) {
                if (role.getGold() < gold) {
                	return this.success(ErrorResult.NotEnoughGold);
                } else {
                	this.roleDao.subGold(role, gold, desc, FinanceLog.STATUS_RANDOM_GEN_ONE_TIMES_GOLD);

                }
        	} else {
            	// 酒馆免费元宝刷新冷却时间重置为1天
        		role.barGoldTime = (int)(System.currentTimeMillis() / 1000l + 86400l);
        	}
        }

        Result result = new Result();
        this.roleDao.update(role, result);
        
        if (itemId >= 10000) {
        	boolean find = false;
            for (Hero gen : heros) {
                if (gen.getHeroId() == itemId) {
                	find = true;
                	break;
                }
            }
            if (!find) {
            	this.heroDao.create(role, itemId, result);

            	int serverId = role.getServerId();

            	// 滚动消息
            	if (this.baseHeroDao.findOne(itemId).getStar() >= 3) {
          //  		message.saveMessage(Role.name + "通过点将招募到了[" + this.baseHeroDao.findOne(itemId).getName() + "]", serverId);
            	}
            	
            } else {

            	this.itemDao.addItem(roleId, itemId - 6000, this.soulNumByStar(itemId), result);
            }
        } else {
        	this.itemDao.addItem(roleId, itemId, num, result);
        }

        // 酒馆寻宝日常任务
        this.dailyTaskDao.addBar(role, 1, result);

        long time = System.currentTimeMillis();
        long bar_gold_time = (long)role.barGoldTime * 1000l - time;

        result.setValue("bar_coin_times", dateNumModel.getBarCoinNum());
        result.setValue("bar_gold_time", bar_gold_time > 0 ? bar_gold_time : 0);
        result.setValue("bar_coin_time", dateNumModel.barTime > time ? dateNumModel.barTime - time : 0);
        return this.success(result.toMap());
    }

    /**
     * 酒馆随机十次武将
     * @param tokenS
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object random10Times(int type) throws Throwable {

    	int roleId = this.roleId();

        if (type != TYPE_COIN_RANDOM && type != TYPE_GOLD_RANDOM) {
            return this.returnError(this.lineNum(), "参数出错");
        }

        int coin = 90000;
        int gold = 2590; // 需要花费的元宝

        Role role = this.roleDao.findOne(roleId);
        if (type == TYPE_COIN_RANDOM) {
            if (role.getCoin() < coin) {
            	return returnError(lineNum(), "铜钱不足");
            }
        } else {
            if (role.getGold() < gold) {
            	return this.success(ErrorResult.NotEnoughGold);
            }
        }

        int start = 0;
        int times = 10; // 10次

        int[][] randomResult = new int[times][];

        if (type == TYPE_GOLD_RANDOM) {

        	List<Hero> generals = this.heroDao.findAll(roleId);

        	boolean find = false;
        	for (Hero generalTable : generals) {
				if (this.baseHeroDao.findOne(generalTable.getHeroId()).getStar() == 3) {
					find = true;
					break;
				}
			}

    		// 元宝10连抽  第一次花元宝必给一个3星英雄
    		if (!find) {
    			int genId = this.baseHeroShopDao.randomGeneral(3)[0];
    			randomResult[0] = new int[]{genId , 1};
    			
    			start = 1;
    		}
        }

        int heroNum = 0;
        for(int i = start ; i < times ; i++) {

            if (type == TYPE_COIN_RANDOM) {
            	randomResult[i] = this.baseHeroShopDao.coinRandom();
            } else {

            	int[] temp = this.baseHeroShopDao.goldRandom();
            	if (temp[0] >= 10000) {

            		if (this.baseHeroDao.findOne(temp[0]).getStar() >=2) {
                    	if (heroNum >= 2) {
                    		i--;
                    		continue;
                    	} else {
                    		heroNum++;
                    	}
            		}
            	}

            	randomResult[i] = temp;
            }
        }

        // 元宝十连抽必给英雄
        if (type == TYPE_GOLD_RANDOM && times == 10) {
        	boolean giveGeneral = false;
        	for (int[] is : randomResult) {
				if (is[0] >= 10000) {
					if (this.baseHeroDao.findOne(is[0]).getStar() >= 2) {
						giveGeneral = true;
					}
				}
			}
        	if (!giveGeneral) {
        		Random random = new Random();
        		int index = random.nextInt(times);
        		int genId = this.baseHeroShopDao.randomGeneral(2)[0];
        		randomResult[index] = new int[]{genId , 1};
        	}
        }

        Result result = new Result();

        String desc = "金币";
        if (type == TYPE_GOLD_RANDOM) {
        	desc = "元宝";
        } 
        desc += "酒馆十连抽,获得<";
        for (int[] is : randomResult) {
			if (is[0] >= 10000) {
				int generalId = is[0];
				
        		Hero general = this.heroDao.findOne(roleId, generalId);
        		if (general != null) {

        			int itemId = generalId - 6000;
        			this.itemDao.addItem(roleId, itemId, this.soulNumByStar(generalId), result);
    				desc += "," + this.baseItemDao.findOne(itemId).getName() + "x" + is[1];
        			
        		} else {
	                // 发武将
        			this.heroDao.create(role, generalId, result);
	                desc += "," + this.baseHeroDao.findOne(generalId).getName();

	                int serverId = role.getServerId();
	                
	            	// 滚动消息
	            	if (this.baseHeroDao.findOne(generalId).getStar() >= 3) {
	        //    		message.saveMessage(Role.name + "招募到了[" + this.baseHeroDao.findOne(generalId).getName() + "]", serverId);
	            	}
        		}

			} else {
				// 发道具
				this.itemDao.addItem(roleId, is[0], is[1], result);
				desc += "," + this.baseItemDao.findOne(is[0]).getName() + "x" + is[1];
			}
		}
        desc += ">";

        if (type == TYPE_COIN_RANDOM) {
        	this.roleDao.subCoin(role, coin, desc, FinanceLog.STATUS_RANDOM_GEN_TEN_TIMES_COIN);
        } else {
        	this.roleDao.subGold(role, gold, desc, FinanceLog.STATUS_RANDOM_GEN_TEN_TIMES_GOLD);
        }

        this.roleDao.update(role, result);
        
        // 酒馆寻宝日常任务
        dailyTaskDao.addBar(role, times, result);

        return this.success(result.toMap());
    }

    /**
     * 用灵魂石招呼武将
     * @param tokenS
     * @param heroId
     * @return
     * @throws Throwable
     */
    public Object useSoulStone(int heroId) throws Throwable {

        int roleId = this.roleId();

        if (this.heroDao.findOne(roleId, heroId) != null) {
            return this.returnError(this.lineNum(), "出错,已有这个武将");
        }

        this.itemDao.setSessionFactory(this.heroDao.getSessionFactory());
        Item item = itemDao.findOneByItemId(roleId, heroId - 6000);
        if (item == null) {
            return this.returnError(this.lineNum(), "出错,没有灵魂石");
        }

        int soulNum = 80;
        if (this.baseHeroDao.findOne(heroId).getStar() == 2) {
        	soulNum = 30;
        } else if (this.baseHeroDao.findOne(heroId).getStar() == 1) {
        	soulNum = 10;
        }

        int itemNum = item.getNum();
        if (itemNum < soulNum) {
            return this.returnError(this.lineNum(), "出错,灵魂石数量不足");
        }

        Role role = this.roleDao.findOne(roleId);

        Result result = new Result();
        this.heroDao.create(role, heroId, result);
        this.itemDao.subItem(item, soulNum, result);
        return this.success(result.toMap());
    }

    /**
     * 重置技能
     * @param tokenS
     * @param generalId
     * @return
     * @throws Throwable
     */
    public Object resetSkill(int generalId) throws Throwable {

    	int roleId = this.roleId();

        Hero general = this.heroDao.findOne(roleId, generalId);
        if (general == null) {
            return this.returnError(this.lineNum(), "出错,没有这个武将");
        }

        int gold = 50;
        
        int coin = 0;

        BaseSkill baselSkill = this.baseHeroDao.findOne(generalId).getSkill1();
        for (int i = 1 ; i < general.getSkill1Level() ; i++) {
        	coin += baselSkill.getBaseCoin() + (i - 1) * baselSkill.getLevelupCoin();
        }
        
        baselSkill = this.baseHeroDao.findOne(generalId).getSkill2();
        for (int i = 1 ; i < general.getSkill2Level() ; i++) {
        	coin += baselSkill.getBaseCoin() + (i - 1) * baselSkill.getLevelupCoin();
        }
        
        baselSkill = this.baseHeroDao.findOne(generalId).getSkill3();
        for (int i = 1 ; i < general.getSkill3Level() ; i++) {
        	coin += baselSkill.getBaseCoin() + (i - 1) * baselSkill.getLevelupCoin();
        }
        
        baselSkill = this.baseHeroDao.findOne(generalId).getSkill4();
        for (int i = 1 ; i < general.getSkill4Level() ; i++) {
        	coin += baselSkill.getBaseCoin() + (i - 1) * baselSkill.getLevelupCoin();
        }

        coin *= 0.5;

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        if (role.getGold() < gold) {
        	return this.success(ErrorResult.NotEnoughGold);
        } else {
        	this.roleDao.addCoin(role, coin, "重置技能退铜钱", FinanceLog.STATUS_RESET_SKILL, result);
        	this.roleDao.subGold(role, gold, "重置技能<" + this.baseHeroDao.findOne(generalId).getName() + ">", FinanceLog.STATUS_RESET_SKILL);
        	this.roleDao.update(role, result);
        }

        general.setSkill1Level(1);
        general.setSkill2Level(1);
        general.setSkill3Level(1);
        general.setSkill4Level(1);

        this.heroDao.update(general, result);

        return this.success(result.toMap());
    }

    /**
     * 重置属性点
     * @param tokenS
     * @param generalId
     * @return
     * @throws Throwable
     */
    public Object resetPoint(int generalId) throws Throwable {

    	int roleId = this.roleId();

        Hero general = this.heroDao.findOne(roleId, generalId);
        if (general == null) {
            return this.returnError(this.lineNum(), "出错,没有这个武将");
        }

        int gold = 20;
        
        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        if (role.getGold() < gold) {
        	return this.success(ErrorResult.NotEnoughGold);
        } else {
        	this.roleDao.subGold(role, gold, "重置属性点<" + this.baseHeroDao.findOne(generalId).getName() + ">", FinanceLog.STATUS_RESET_POINT);
        	this.roleDao.update(role, result);
        }

        general.setStr(0);
        general.setINT(0);
        general.setDex(0);

        this.heroDao.update(general, result);

        return this.success(result.toMap());
    }

    /**
     * 抽灵魂石
     * @param tokenS
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object randomSoul() throws Throwable {

    	int roleId = this.roleId();

        int gold = 400;

        Role role = this.roleDao.findOne(roleId);
        if (role.getVip() < 11) {

            return this.returnError(com.zhanglong.sg.result.Error.ERROR_BUY_OVER_NUM, "需要VIP11可解锁魂石商店，前去充值？");
        } else if (role.getGold() < gold) {
            return this.success(ErrorResult.NotEnoughGold);
        }

        List<BaseHeroShop> soulList = this.baseHeroShopDao.findAll("soul");
        if (soulList == null || soulList.size() < 5) {

        	return this.returnError(this.lineNum(), "魂石商店配置出错!");
        }

        int sBaseNum = 4; // 前4个是灵魂石

        int[] soulRateArray = new int[sBaseNum];
        int[] itemRateArray = new int[soulList.size() - sBaseNum];
        
        for (int i = 0; i < soulList.size(); i++) {
			if (i < sBaseNum) {
				soulRateArray[i] = soulList.get(i).getWeight();
			} else {
				itemRateArray[i - sBaseNum] = soulList.get(i).getWeight();
			}
		}

        int countNum = 6; // 随机6个道具包括武将 

        Random random = new Random();
        
    	int[][] itemIds = new int[countNum][]; // [[itemId , num]...]

    	// 第一个是武将或魂石
    	int soulIndex = Utils.randomGetOne(soulRateArray);
    	BaseHeroShop item = soulList.get(soulIndex);
    	int num = random.nextInt(item.getMaxNum() - item.getMinNum() + 1) + item.getMinNum();
    	itemIds[0] = new int[]{item.getItemId() , num};

    	for (int i = 1; i < countNum; i++) {

    		int itemIndex = Utils.randomGetOne(itemRateArray);
    		itemIndex += sBaseNum;

    		item = soulList.get(itemIndex);

    		num = random.nextInt(item.getMaxNum() - item.getMinNum() + 1) + item.getMinNum();
    		itemIds[i] = new int[]{item.getItemId() , num};
		}
    	
    	Result result = new Result();

    	String desc = "抽取魂石花费<" + gold + ">元宝获得";
    	for (int[] idAndNum : itemIds) {
    		if (idAndNum[0] >= 10000) {
    			int heroId = idAndNum[0];
				Hero general = this.heroDao.findOne(roleId, heroId);
				if (general == null) {
					this.heroDao.create(role, heroId, result);
					desc += "<"+this.baseHeroDao.findOne(heroId).getName() + "x1>,";
				} else {
					int itemId = heroId - 6000;
					int itemNum = this.soulNumByStar(heroId);

					desc += "<"+ this.baseItemDao.findOne(itemId).getName() + "x"+ itemNum + ">,";
					this.itemDao.addItem(roleId, itemId, itemNum, result);
				}
    		} else {
    			desc += "<"+ this.baseItemDao.findOne(idAndNum[0]).getName() + "x"+ idAndNum[1] + ">,";
    			this.itemDao.addItem(roleId, idAndNum[0], idAndNum[1], result);
    		}
		}

    	this.roleDao.subGold(role, gold, desc, FinanceLog.STATUS_RANDOM_SOUL);
    	this.roleDao.update(role, result);

    	// 酒馆寻宝日常任务
    	dailyTaskDao.addBar(role, 1, result);

    	return this.success(result.toMap());
    }

    /**
     * 
     * @return
     * @throws Throwable
     */
	public Object showAll() throws Throwable {

		List<BaseHero> list = baseHeroDao.findAll();

        Comparator<BaseHero> comparator = new Comparator<BaseHero>(){  
        	public int compare(BaseHero s1, BaseHero s2) {  
        		return s1.getId() - s2.getId();
			}  
        };  

        Collections.sort(list, comparator);

		return this.success(list);
	}


    /**
     * 锻造
     * @param heroId
     * @param index
     * @param ids
     * @param nums
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object addEquipExp(int heroId, int index, int[] ids, int[] nums) throws Throwable {

    	int roleId = this.roleId();

        Result result = new Result();

        int exp = 0;
        for (int i = 0; i < ids.length; i++) {

			if (nums[i] <= 0) {
				return this.returnError(this.lineNum(), "参数出错");
			}

			int num = nums[i];
			
			int itemId = ids[i];
			Item item = this.itemDao.findOne(itemId);

			if (item == null || !item.getRoleId().equals(roleId)) {
				return this.returnError(this.lineNum(), "未拥有的道具");
			}
			
			BaseItem baseItem = this.baseItemDao.findOne(item.getItemId());
			if (baseItem.getType() != 2 && baseItem.getType() != 3  && baseItem.getType() != 5) {
				return this.returnError(this.lineNum(), baseItem.getName() + ">并非锻造材料");
			}

			if (item.getNum() < num) {
				return this.returnError(this.lineNum(), baseItem.getName() + ">数量不足");
			} else {
				this.itemDao.subItem(item, num, result);
			}

			exp += baseItem.getExp() * num;
		}

        int coin = exp * 200;

        Role role = this.roleDao.findOne(roleId);
        if (role.coin < coin) {
        	return this.returnError(this.lineNum(), "铜币不足");
        }

    	Hero hero = this.heroDao.findOne(roleId, heroId);

        if (index == 1) {
            if (hero.getEquip1() == false) {
                return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip1Exp(hero.getEquip1Exp() + exp);
			}
        } else if (index == 2) {
            if (hero.getEquip2() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip2Exp(hero.getEquip2Exp() + exp);
			}
        } else if (index == 3) {
            if (hero.getEquip3() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip3Exp(hero.getEquip3Exp() + exp);
			}
        } else if (index == 4) {
            if (hero.getEquip4() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip4Exp(hero.getEquip4Exp() + exp);
			}
        } else if (index == 5) {
            if (hero.getEquip5() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip5Exp(hero.getEquip5Exp() + exp);
			}
        } else if (index == 6) {
            if (hero.getEquip6() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip6Exp(hero.getEquip6Exp() + exp);
			}
        } else if (index == 7) {
            if (hero.getEquip7() == 0) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else {
				hero.setEquip7Exp(hero.getEquip7Exp() + exp);
			}
        } else {
        	return this.returnError(this.lineNum(), "参数出错");
        }

        this.roleDao.subCoin(role, coin, hero.getCLASS() + "阶<" + this.baseHeroDao.findOne(heroId).getName() + ">锻造<" + index , FinanceLog.STATUS_COIN_EQUIP_EXP);
        this.roleDao.update(role, result);

        this.heroDao.update(hero, result);

    	return this.success(result.toMap());
    }

    /**
     * 元宝锻造
     * @param heroId
     * @param index
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object addEquipExp2(int heroId, int index) throws Throwable {

    	int roleId = this.roleId();

    	Hero hero = this.heroDao.findOne(roleId, heroId);

    	int equipId = 0;
    	if (index == 7) {
    		equipId = hero.getEquip7();
    	} else {

    		equipId = this.equipId(hero, index);
    	}

        if (equipId == 0) {
            return this.returnError(this.lineNum(), "没装备你锻的个什么劲");
        }

    	BaseItem baseItem = this.baseItemDao.findOne(equipId);
    	
    	int exp = 0;
        if (index == 1) {
            if (hero.getEquip1() == false) {
                return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip1Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip1Exp();
				hero.setEquip1Exp(baseItem.maxExp());
			}
        } else if (index == 2) {
            if (hero.getEquip2() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip2Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip2Exp();
				hero.setEquip2Exp(baseItem.maxExp());
			}
        } else if (index == 3) {
            if (hero.getEquip3() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip3Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip3Exp();
				hero.setEquip3Exp(baseItem.maxExp());
			}
        } else if (index == 4) {
            if (hero.getEquip4() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip4Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip4Exp();
				hero.setEquip4Exp(baseItem.maxExp());
			}
        } else if (index == 5) {
            if (hero.getEquip5() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip5Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip5Exp();
				hero.setEquip5Exp(baseItem.maxExp());
			}
        } else if (index == 6) {
            if (hero.getEquip6() == false) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip6Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip6Exp();
				hero.setEquip6Exp(baseItem.maxExp());
			}
        } else if (index == 7) {
            if (hero.getEquip7() == 0) {
            	return this.returnError(this.lineNum(), "装备没配");
            } else if (hero.getEquip7Exp() >= baseItem.maxExp()) {
            	return this.returnError(this.lineNum(), "经验已最大");
            } else {
            	exp = baseItem.maxExp() - hero.getEquip7Exp();
				hero.setEquip7Exp(baseItem.maxExp());
			}
        } else {
        	return this.returnError(this.lineNum(), "参数出错");
        }

        Role role = this.roleDao.findOne(roleId);
        if (role.gold < exp) {
        	return this.success(ErrorResult.NotEnoughGold);
        }

        this.roleDao.subGold(role, exp, hero.getCLASS() + "阶<" + baseHeroDao.findOne(heroId).getName() + ">锻造<" + index , FinanceLog.STATUS_COIN_EQUIP_EXP);

        Result result = new Result();

        this.roleDao.update(role, result);

        this.heroDao.update(hero, result);

    	return this.success(result.toMap());
    }

    /**
     * 
     * @param heroId
     * @param weaponId
     * @return
     * @throws Throwable
     */
    @Transactional(rollbackFor = Throwable.class)
    public Object exclusiveWeapon(int heroId, int weaponId) throws Throwable {

    	int roleId = this.roleId();

    	Hero hero = this.heroDao.findOne(roleId, heroId);

    	if (hero == null) {
    		return this.returnError(this.lineNum(), "参数错误");
    	}

    	int myId = hero.getEquip7();
    	if (myId >= 9900) {
    		return this.returnError(this.lineNum(), "已经是最高级的橙色专属武器");
    	}

    	Item weapon = this.itemDao.findOne(weaponId);

    	if (weapon == null) {
    		return this.returnError(this.lineNum(), "参数错误");
    	}

    	int itemId = weapon.getItemId();
    	
    	if (itemId <= myId) {
    		return this.returnError(this.lineNum(), "参数出错");
    	}

    	int id1 = hero.getHeroId() - 100;
    	int id2 = hero.getHeroId() - 200;
    	int id3 = hero.getHeroId() - 300;

    	if (itemId !=id1 && itemId != id2 && itemId != id3) {
    		return this.returnError(this.lineNum(), "不是他的专属武器不能装备");
    	}

    	Result result = new Result();

    	this.itemDao.subItem(weapon, 1, result);

    	hero.setEquip7(itemId);
    	this.heroDao.update(hero, result);

    	if (myId > 0 && hero.getEquip7Exp() > 10) {
    		// 返还材料
    		int material = 4204;
    		this.itemDao.addItem(roleId, material, hero.getEquip7Exp() / 10, result);
    		this.itemDao.addItem(roleId, id1, 1, result);
    	}

    	return this.success(result.toMap());
    }

    /**
     * 专属武器进阶
     * @param heroId
     * @return
     * @throws Throwable
     */
    public Object equipClassup(int heroId) throws Throwable {

    	int roleId = this.roleId();

    	Hero hero = this.heroDao.findOne(roleId, heroId);

    	if (hero == null) {
    		return this.returnError(this.lineNum(), "参数出错");
    	}

    	if (hero.getEquip7() == 0) {
    		return this.returnError(this.lineNum(), "参数出错");
    	}
    	
    	if (hero.getEquip7() >= 9900) {
    		return this.returnError(this.lineNum(), "橙色不能进阶了");
    	}

    	int itemId = hero.getEquip7() + 100;

        BaseItem baseItem = this.baseItemDao.findOne(itemId);

        Result result = new Result();

        Role role = this.roleDao.findOne(roleId);
        if (role.coin < baseItem.getMakeCoin()) {
        	return this.returnError(this.lineNum(), "铜币不足");
        } else {
        	this.roleDao.subCoin(role, baseItem.getMakeCoin(), "专属进阶<" + baseItem.getName() + ">", FinanceLog.STATUS_EQUIP_MAKE);
        	this.roleDao.update(role, result);
        }

	    List<BaseMakeItem> items = this.baseMakeItemDao.findByItemId(itemId);

        boolean find = false;
		for (BaseMakeItem baseMakeItem : items) {
			if (baseMakeItem.getPk().getBaseItem().getBaseId() == itemId) {
				find = true;

				int materialId = baseMakeItem.getPk().getMaterial().getBaseId();

				Item item = this.itemDao.findOneByItemId(roleId, materialId);
				if (item == null) {
					return this.returnError(this.lineNum(), "缺少合成材料:" + this.baseItemDao.findOne(materialId).getName());
				} else if (item.getNum() < baseMakeItem.getNum()) {
					return this.returnError(this.lineNum(), "合成材料:" + this.baseItemDao.findOne(materialId).getName() + " , 数量不足");
				} else {
					this.itemDao.subItem(item, baseMakeItem.getNum(), result);
				}
			}
		}

		if (!find) {
			return this.returnError(this.lineNum(), "专属合成配置出错");
		}

    	if (hero.getEquip7Exp() > 10) {
    		// 返还材料
    		int material = 4204;
    		this.itemDao.addItem(roleId, material, hero.getEquip7Exp() / 10, result);
    	}

		hero.setEquip7(itemId);
		hero.setEquip7Exp(0);
		this.heroDao.update(hero, result);

		return this.success(result.toMap());
    }

    private int equipId(Hero hero, int index) throws Exception {

    	List<BaseHeroEquip> baseHeroEquips = this.baseHeroEquipDao.findByHeroId(hero.getHeroId());
    	BaseHeroEquip baseHeroEquip = baseHeroEquips.get(hero.getCLASS());
    	switch (index) {
		case 1:
			return baseHeroEquip.getEquip1().getBaseId();
		case 2:
			return baseHeroEquip.getEquip2().getBaseId();
		case 3:
			return baseHeroEquip.getEquip3().getBaseId();
		case 4:
			return baseHeroEquip.getEquip4().getBaseId();
		case 5:
			return baseHeroEquip.getEquip5().getBaseId();
		case 6:
			return baseHeroEquip.getEquip6().getBaseId();
		default:
			throw new Exception("索引出错");
		}
    }

	private int soulNumByStar(int heroId) {
		return 18;
	}
}
