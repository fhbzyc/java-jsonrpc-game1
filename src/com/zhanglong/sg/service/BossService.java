package com.zhanglong.sg.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import websocket.handler.BossBroadcast;
import websocket.handler.EchoHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhanglong.sg.dao.BaseBossDao;
import com.zhanglong.sg.dao.BossDamageDao;
import com.zhanglong.sg.dao.BossKillerDao;
import com.zhanglong.sg.dao.MailDao;
import com.zhanglong.sg.dao.ServerDao;
import com.zhanglong.sg.entity.BossDamage;
import com.zhanglong.sg.entity.BossKiller;
import com.zhanglong.sg.entity.FinanceLog;
import com.zhanglong.sg.entity.Mail;
import com.zhanglong.sg.entity.Role;
import com.zhanglong.sg.entity.Server;
import com.zhanglong.sg.entity2.BaseBoss;
import com.zhanglong.sg.model.DateNumModel;
import com.zhanglong.sg.model.Reward;
import com.zhanglong.sg.protocol.Response;
import com.zhanglong.sg.result.ErrorResult;
import com.zhanglong.sg.result.Result;
import com.zhanglong.sg.utils.Utils;

@Service
public class BossService extends BaseService {

	@Resource
	private BaseBossDao baseBossDao;

	@Resource
	private BossDamageDao bossDamageDao;

	@Resource
	private BossKillerDao bossKillerDao;

	@Resource
	private MailDao mailDao;

	@Resource
	private ServerDao serverDao;

	public Object boss() throws Exception {

		int hp = this.baseBossDao.getHp(this.serverId());

		BaseBoss baseBoss = this.baseBossDao.findOne();

		Result result = new Result();
		result.setValue("boss", baseBoss);
		result.setValue("boss_hp", hp);

		BossKiller bossKiller = this.bossKillerDao.findOne(this.serverId());
		if (bossKiller != null) {
			result.setValue("killer_name", bossKiller.getName());
		}

		int roleId = this.roleId();
		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		int num = 1 - dateNumModel.boss;
		result.setValue("num", num);
		int buyNum = 0;
		Role role = this.roleDao.findOne(roleId);
		if (role.vip >= 5) {
			buyNum = 2 - dateNumModel.buyBoss;
		}
		result.setValue("buy", buyNum);

		BossDamage bossDamage = this.bossDamageDao.findOne(roleId);
		if (bossDamage != null && !bossDamage.getHeros().equals("")) {
			ObjectMapper objectMapper = new ObjectMapper();
			result.setValue("heros", objectMapper.readValue(bossDamage.getHeros(), int[].class));
		} else {
			result.setValue("heros", new int[]{});
		}

		result.setValue("top3", this.bossDamageDao.top3(this.serverId()));
		return this.success(result);
	}

	/**
	 * 
	 * @param heroId1
	 * @param heroId2
	 * @param heroId3
	 * @param heroId4
	 * @return
	 * @throws Exception
	 */
	public Object battleBegin(int heroId1, int heroId2, int heroId3, int heroId4) throws Exception {

		ArrayList<Integer> ids = new ArrayList<Integer>();
		if (heroId1 != 0) {
			ids.add(heroId1);
		}
		if (heroId2 != 0) {
			ids.add(heroId2);
		}
		if (heroId3 != 0) {
			ids.add(heroId3);
		}
		if (heroId4 != 0) {
			ids.add(heroId4);
		}

		int hp = this.baseBossDao.getHp(this.serverId());

		if (hp <= 0) {
			return this.returnError(this.lineNum(), "世界BOSS已被击毙");
		}

		Result result = new Result();

		int roleId = this.roleId();
		BossDamage bossDamage = this.bossDamageDao.findOne(roleId);
		if (bossDamage == null) {
			
			Role role = this.roleDao.findOne(roleId);
			
			bossDamage = new BossDamage();
			bossDamage.setDate(Integer.valueOf(Utils.date()));
			bossDamage.setRoleId(roleId);
			bossDamage.setName(role.getName());
			bossDamage.setAvatar(role.getAvatar());
			bossDamage.setServerId(role.getServerId());

			ObjectMapper objectMapper = new ObjectMapper();
			bossDamage.setHeros(objectMapper.writeValueAsString(ids));

			this.bossDamageDao.save(bossDamage);
			result.setValue("heros", new int[]{});
		} else {
			ObjectMapper objectMapper = new ObjectMapper();
			int[] heros = objectMapper.readValue(bossDamage.getHeros(), int[].class);

			for (int heroId : heros) {
				ids.add(heroId);
			}
			if (heroId1 != 0) {
				ids.add(heroId1);
			}
			if (heroId2 != 0) {
				ids.add(heroId2);
			}
			if (heroId3 != 0) {
				ids.add(heroId3);
			}
			if (heroId4 != 0) {
				ids.add(heroId4);
			}
			bossDamage.setHeros(objectMapper.writeValueAsString(ids));
			result.setValue("heros", ids);
		}

		BaseBoss baseBoss = this.baseBossDao.findOne();
		
		result.setValue("boss", baseBoss);
		result.setValue("boss_hp", hp);

		EchoHandler.bossPut(this.getHandler());

		return this.success(result);
	}

	public Object battleEnd() throws Exception {

		int roleId = this.roleId();
		EchoHandler.bossRemove(this.serverId(), roleId);

		BossDamage bossDamage = this.bossDamageDao.findOne(roleId);

		Integer damage = BaseBossDao.damages.get(roleId);
		if (damage != null) {
			
			if (damage > bossDamage.getDamage()) {
				bossDamage.setDamage(damage);
				this.bossDamageDao.save(bossDamage);
			}
		}

		Result result = new Result();
		result.setValue("damage", bossDamage.getDamage());
		return this.success(result);
	}

	public Object subHp(int hp) throws Exception {

		int serverId = this.serverId();

		int bossHp = this.baseBossDao.getHp(serverId);
		if (bossHp > 0) {

			this.baseBossDao.subHp(serverId, this.roleId(), hp);

			int newHp = this.baseBossDao.getHp(serverId);
			
			Result result = new Result();
			result.setValue("boss_hp", newHp);
			String msg = Response.marshalSuccess(0, result.toMap());

			BossBroadcast bossBroadcast = new BossBroadcast();
			bossBroadcast.send(serverId, msg);

			if (newHp <= 0) {

				int roleId = this.roleId();
				Role role = this.roleDao.findOne(roleId);
				
				String content ="主公，你对世界BOSS造成了最后的致命一击，由于你的出色贡献。\n" +
"世界BOSS联盟将给予你以下奖励：\n\n" +
"世界BOSS盟主：曹操";

				ObjectMapper objectMapper = new ObjectMapper();

				Reward reward = new Reward();
				reward.setCoin(30000);
				reward.setMoney5(300);
				reward.setItem_id(new int[]{4231});
				reward.setItem_num(new int[]{5});

                Mail mail = new Mail();
                mail.setFromName("GM");
                mail.setAttachment(objectMapper.writeValueAsString(reward));
                mail.setTitle("世界BOSS最后一击奖励！");
                mail.setContent(content);
                mail.setStatus(0);
                mail.setRoleId(roleId);
                this.mailDao.create(mail);

                BossKiller bossKiller = new BossKiller();
                bossKiller.setRoleId(roleId);
                bossKiller.setName(role.getName());
                bossKiller.setServerId(serverId);
                this.bossKillerDao.save(bossKiller);
			}
		}

		return this.success(true);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public Object rank() throws Exception {

		int serverId = this.serverId();

		int day = Integer.valueOf(Utils.date());
		
		Date date = new Date();
		int hour = date.getHours();
		if (hour < 20) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			String str = simpleDateFormat.format(new Date(System.currentTimeMillis() - 86400l * 1000l));
			day = Integer.valueOf(str);
		}

		List<BossDamage> list = this.bossDamageDao.findAll(day, serverId);

		Result result = new Result();
		result.setValue("boss_rank", list);

		int roleId = this.roleId();

		BossDamage bossDamage = this.bossDamageDao.findOne(roleId, day);
		if (bossDamage == null) {
			result.setValue("myrank", 0);
		} else {
			int c = this.bossDamageDao.countAfter(bossDamage.getDamage(), serverId);
			result.setValue("myrank", c + 1);
		}

		return this.success(result);
	}

	public Object reset() throws Exception {

		int roleId = this.roleId();
		DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		if (dateNumModel.buyBoss >= 2 && dateNumModel.boss >= 1) {
			return this.returnError(this.lineNum(), "次数已用尽"); 
		}

		int gold = 100;
		Result result = new Result();
		Role role = this.roleDao.findOne(roleId);
		if (dateNumModel.boss >= 1) {
			if (role.vip >= 5) {
				
				if (role.gold < gold) {
					return this.returnError(this.lineNum(), ErrorResult.NotEnoughGold); 
				} else {
					this.roleDao.subGold(role, gold, "第<" + dateNumModel.boss + ">次重置BOSS", FinanceLog.STATUS_RESET_BOSS, result);
					dateNumModel.buyBoss++;
				}
			}
		}

		//DateNumModel dateNumModel = this.dateNumDao.findOne(roleId);
		dateNumModel.boss = 1;
		this.dateNumDao.save(roleId, dateNumModel);

		result.setValue("num", 0);
		result.setValue("buy", 2 - dateNumModel.buyBoss);

		BossDamage bossDamage = this.bossDamageDao.findOne(roleId);
		if (bossDamage == null) {

			bossDamage = new BossDamage();
			bossDamage.setDate(Integer.valueOf(Utils.date()));
			bossDamage.setRoleId(roleId);
			bossDamage.setName(role.getName());
			bossDamage.setAvatar(role.getAvatar());
			bossDamage.setServerId(role.getServerId());
			bossDamage.setHeros("[]");

			this.bossDamageDao.save(bossDamage);
		} else {
			bossDamage.setHeros("[]");
			this.bossDamageDao.update(bossDamage);
		}

		this.bossDamageDao.save(bossDamage);
		result.setValue("heros", new int[]{});

		BaseBossDao.damages.put(roleId, 0);

		return this.success(result);
	}

	public void updateRank() throws Exception {

		int date = Integer.valueOf(Utils.date());

		ObjectMapper objectMapper = new ObjectMapper();
		
		List<int[]> configs = this.rewards();
		
		List<Server> servers = this.serverDao.findAll();
		for (Server server : servers) {
			int serverId = server.getId();

			List<BossDamage> list = this.bossDamageDao.findAll(date, serverId);

			for (int i = 0 ; i < list.size() ; i++) {
				int rand = i + 1;

				for (int[] config : configs) {

					if (rand >= config[0] && rand <= config[1]) {
						int[] itemId = new int[]{config[4]};
						int[] itemNum = new int[]{config[5]};

						Reward reward = new Reward();
						reward.setCoin(config[2]);
						reward.setMoney5(config[3]);
						reward.setItem_id(itemId);
						reward.setItem_num(itemNum);

						String json = objectMapper.writeValueAsString(reward);
						
						Mail mail = new Mail();
						mail.setAttachment(json);
						mail.setRoleId(list.get(i).getRoleId());
						mail.setFromName("GM");
						mail.setTitle("世界BOSS伤害奖励!");
						mail.setContent("主公你好，你在对世界BOSS的战斗中造成的伤害为：" + list.get(i).getDamage() + "，由于你的英勇表现。\n" + 
			    			"世界BOSS联盟将给于你以下奖励。\n\n" + 
			    			"世界BOSS盟主：曹操");
						this.mailDao.create(mail);

						break;
					}
				}
			}
		}
	}

	private List<int[]> rewards() {

		List<int[]> arrayList = new ArrayList<int[]>();
		arrayList.add(new int[]{1 , 1 , 60000 , 500 , 4231 , 12});
		arrayList.add(new int[]{2 , 2 , 50000 , 455 , 4231 , 10});
		arrayList.add(new int[]{3 , 3 , 49000 , 400 , 4231 , 9});
		arrayList.add(new int[]{4 , 4 , 48000 , 390 , 4231 , 7});
		arrayList.add(new int[]{5 , 5 , 47000 , 380 , 4231 , 6});
		arrayList.add(new int[]{6 , 6 , 46000 , 370 , 4231 , 5});
		arrayList.add(new int[]{7 , 7 , 45000 , 360 , 4231 , 4});
		arrayList.add(new int[]{8 , 8 , 44000 , 350 , 4231 , 4});
		arrayList.add(new int[]{9 , 9 , 43000 , 340 , 4231 , 3});
		arrayList.add(new int[]{10 , 10 , 42000 , 330 , 4231 , 3});
		arrayList.add(new int[]{11 , 20 , 40500 , 325 , 4231 , 2});
		arrayList.add(new int[]{21 , 30 , 39000 , 320 , 4231 , 2});
		arrayList.add(new int[]{31 , 40 , 37500 , 315 , 4231 , 2});
		arrayList.add(new int[]{41 , 50 , 36000 , 310 , 4231 , 1});
		arrayList.add(new int[]{51 , 70 , 34500 , 305 , 4231 , 1});
		arrayList.add(new int[]{71 , 100 , 33000 , 300 , 4231 , 1});
		arrayList.add(new int[]{101 , 200 , 31500 , 295 , 4231 , 1});
		arrayList.add(new int[]{201 , 300 , 29500 , 285 , 4231 , 1});
		arrayList.add(new int[]{301 , 400 , 27500 , 275 , 4231 , 1});
		arrayList.add(new int[]{401 , 500 , 25500 , 265 , 4231 , 1});
		arrayList.add(new int[]{501 , 700 , 23500 , 255 , 4231 , 1});
		arrayList.add(new int[]{701 , 1000 , 21500 , 245 , 4231 , 1});
		arrayList.add(new int[]{1001 , 1200 , 19500 , 235 , 4231 , 1});
		arrayList.add(new int[]{1201 , 1400 , 16500 , 220 , 4231 , 1});
		arrayList.add(new int[]{1401 , 1600 , 13500 , 205 , 4231 , 1});
		arrayList.add(new int[]{1601 , 1800 , 10500 , 190 , 4231 , 1});
		arrayList.add(new int[]{1801 , 2000 , 7500 , 175 , 4231 , 1});
		arrayList.add(new int[]{2001 , 3000 , 4500 , 160 , 4231 , 1});
		arrayList.add(new int[]{3001 , 4000 , 4000 , 145 , 4231 , 1});
		arrayList.add(new int[]{4001 , 5000 , 3500 , 130 , 4231 , 1});
		arrayList.add(new int[]{5001 , 6000 , 3000 , 115 , 4231 , 1});
		arrayList.add(new int[]{6001 , 7000 , 2500 , 100 , 4231 , 1});
		arrayList.add(new int[]{7001 , 8000 , 2000 , 85 , 4231 , 1});
		arrayList.add(new int[]{8001 , 15000 , 2000 , 70 , 4231 , 1});

		return arrayList;
	}
}
