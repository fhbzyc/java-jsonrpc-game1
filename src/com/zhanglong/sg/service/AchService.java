package com.zhanglong.sg.service;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.AchievementDao;
import com.zhanglong.sg.dao.ArenaDao;
import com.zhanglong.sg.dao.BaseAchievementDao;
import com.zhanglong.sg.dao.StoryDao;
import com.zhanglong.sg.entity.Achievement;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Hero;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Story;
import com.zhanglong.sg.entity2.BaseAchievement;
import com.zhanglong.sg.entity2.BaseStory;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;

@Service
public class AchService extends BaseService {

	@Resource
	private BaseAchievementDao baseAchievementDao;

	@Resource
	private AchievementDao achievementDao;

    @Resource
    private ArenaDao arenaDao;

    @Resource
    private StoryDao storyDao;

    /**
     * 成就列表
     * @return
     * @throws Exception
     */
    public Object list() throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        int rank = 1 + this.arenaDao.getIndex(roleId, this.serverId());

        List<Hero> heros = this.heroDao.findAll(roleId);

        List<Achievement> list = this.achievementDao.findAll(roleId);

        List<BaseAchievement> achievements = this.baseAchievementDao.findAll();

        Result result = new Result();

        for (BaseAchievement baseAchievement : achievements) {

			if (baseAchievement.getType().equals(BaseAchievement.TYPE_CRUSADE)) {
				baseAchievement.setNum(role.crusadeNum);
			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_HERO_CLASS)) {

				int n = 0;
				for (Hero hero : heros) {
					if (hero.getCLASS() >= baseAchievement.getTarget()) {
						n++;
					}
				}

				baseAchievement.setNum(n);

			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_HERO_Id)) {

				for (Hero hero : heros) {
					if (hero.getHeroId() == baseAchievement.getTarget()) {
						baseAchievement.setNum(1);
					}
				}

			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_KILL_NUM)) {

				baseAchievement.setNum(role.killNum);

			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_PK)) {

				baseAchievement.setNum(rank);

			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_VIP)) {
				baseAchievement.setNum(role.vip);

			} else if (baseAchievement.getType().equals(BaseAchievement.TYPE_STAR)) {
				
				List<Story> copys = this.storyDao.findAll(roleId);
				int n = 0;
				for (Story story : copys) {
					if (story.getType() == BaseStory.HERO_COPY_TYPE) {
						n += story.getStar();
					}
				}
				baseAchievement.setNum(n);
			}

			boolean find = false;
			for (Achievement achievement : list) {
				if (achievement.getAchievementId() == baseAchievement.getId() && achievement.getComplete()) {
					find = true;
					break;
				}
			}
			if (find) {
				continue;
			}
			result.addAchievement(baseAchievement);
        }

        HashMap<String, Object> r = result.toMap();
        if (r.get("ach") == null) {
        	r.put("ach", new Object[]{});
        }

        return this.success(r);
    }

    /**
     * 
     * @param achId
     * @return
     * @throws Exception
     */
    public Object complete(int achId) throws Exception {

    	int roleId = this.roleId();

        Role role = this.roleDao.findOne(roleId);

        BaseAchievement baseAch = this.baseAchievementDao.findOne(achId);

        ObjectMapper mapper = new ObjectMapper();
        Reward reward = mapper.readValue(baseAch.getReward(), Reward.class);
        
        List<Achievement> list = this.achievementDao.findAll(roleId);

        Result result = new Result();

        for (Achievement ach : list) {
            if (ach.getAchievementId() == achId) {

                if (ach.getComplete()) {
                	return this.success(new Result().toMap());
                }

                switch (baseAch.getMoneyType()) {
				case 1:
                	if (role.coin < baseAch.getMoney()) {
                		return this.returnError(this.lineNum(), "铜币不足");
                	}
					this.roleDao.subCoin(role, baseAch.getMoney(), "购买成就<" + baseAch.getName() + ">(" + baseAch.getId() + ")", FinanceLog.STATUS_BUY_ACH, result);
					break;
				case 2:
                	if (role.gold < baseAch.getMoney()) {
                		return this.returnError(2, ErrorResult.NotEnoughGold);
                	}
					this.roleDao.subGold(role, baseAch.getMoney(), "购买成就<" + baseAch.getName() + ">(" + baseAch.getId() + ")", FinanceLog.STATUS_BUY_ACH, result);
					break;
				case 3:
                	if (role.money3 < baseAch.getMoney()) {
                		return this.returnError(this.lineNum(), "荣誉不足");
                	}
					this.roleDao.subMoney3(role, baseAch.getMoney(), "购买成就<" + baseAch.getName() + ">(" + baseAch.getId() + ")", FinanceLog.STATUS_BUY_ACH);
					result.setValue("money3", role.money3);
					break;
				case 4:
                	if (role.money4 < baseAch.getMoney()) {
                		return this.returnError(this.lineNum(), "军功不足");
                	}
					this.roleDao.subMoney4(role, baseAch.getMoney(), "购买成就<" + baseAch.getName() + ">(" + baseAch.getId() + ")", FinanceLog.STATUS_BUY_ACH);
					result.setValue("money4", role.money4);
					break;
				default:
					break;
				}

                ach.setComplete(true);
                this.achievementDao.update(ach);

                List<BaseAchievement> baseAchs = this.baseAchievementDao.findAll();
				for (BaseAchievement nextAch : baseAchs) {

					if (nextAch.getParentId() == ach.getAchievementId()) {
						boolean find = false;
						for (Achievement ach2 : list) {
							if (ach2.getAchievementId() == nextAch.getId()) {
								find = true;
								break;
							}
						}
						if (!find) {
							Achievement newAch = new Achievement();
							newAch.setARoleId(roleId);
							newAch.setAchievementId(nextAch.getId());
							newAch.setType(nextAch.getType());
							this.achievementDao.save(newAch);
						}
					}
				}

				this.rewardDao.get(role, reward, "成就领取<" + baseAch.getName() + ">", FinanceLog.STATUS_ACH_GET, result);
                break;
            }
        }

        return this.success(result.toMap());
    }
}
