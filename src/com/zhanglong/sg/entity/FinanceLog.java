package com.zhanglong.sg.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "role_finance_log" , indexes = {@Index(columnList="role_id" , unique = false)})
public class FinanceLog implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2907187428171488415L;

    @Transient
    public static final int STATUS_GEN_SKILL_LEVELUP = 11;
    public static final int STATUS_GEN_STAR_UP = 12;
    public static final int STATUS_RANDOM_GEN_ONE_TIMES_COIN = 13;
    public static final int STATUS_RANDOM_GEN_ONE_TIMES_GOLD = 14;
    public static final int STATUS_RANDOM_GEN_TEN_TIMES_COIN = 15;
    public static final int STATUS_RANDOM_GEN_TEN_TIMES_GOLD = 16;
    public static final int STATUS_RESET_SKILL = 17;
    public static final int STATUS_RESET_POINT = 18;
    public static final int STATUS_RANDOM_SOUL = 19;
    public static final int STATUS_EQUIP_MAKE = 20;
    public static final int STATUS_ITEM_SELL = 21;
//    public static final int STATUS_MAIL_GET_COIN = 22;
//    public static final int STATUS_MAIL_GET_GOLD = 23;
    public static final int STATUS_SIGN_GET_COIN = 24;
    public static final int STATUS_SIGN_GET_GOLD = 25;
    public static final int STATUS_PAY_GET_GOLD = 26;
    public static final int STATUS_SHOP_REFRESH = 27;
    public static final int STATUS_SHOP_BUY_COIN = 28;
    public static final int STATUS_SHOP_BUY_GOLD = 29;
    public static final int STATUS_STORY_GET_COIN = 30;
    public static final int STATUS_WIPE_OUT_GET = 31;
    public static final int STATUS_BUY_PHYSICAL_STRENGTHP = 32;
//    public static final int STATUS_TASK_GET_COIN = 33;
//    public static final int STATUS_TASK_GET_GOLD = 34;
//    public static final int STATUS_DAILY_TASK_GET_COIN = 35;
//    public static final int STATUS_DAILY_TASK_GET_GOLD = 36;
    public static final int STATUS_GOLD_BUY_COIN = 37;
//    public static final int STATUS_ACTIVITY_PAY_COIN = 38;
//    public static final int STATUS_ACTIVITY_PAY_GOLD = 39;
    public static final int STATUS_MAIL_GET = 40;
    public static final int STATUS_ACTIVITY_PAY = 41;
    public static final int STATUS_DAILY_TASK_GET = 42;
    public static final int STATUS_BUY_WIPE_OUT = 43;
    public static final int STATUS_CODE_GET = 44;
    public static final int STATUS_TASK_GET = 45;
    public static final int STATUS_ARENA_BUY = 46;
    public static final int STATUS_UPDATE_NAME = 47;
    public static final int STATUS_BATTLE_IN_WORLD = 48;
    public static final int STATUS_GOLD_WIPE_OUT = 49; // 扫荡

    public static final int STATUS_SHOP_BUY_MONEY3 = 70;
    public static final int STATUS_SHOP_BUY_MONEY4 = 71;

    public static final int STATUS_COIN_EQUIP_EXP = 72;

    public static final String statusName(int status) {
        switch (status) {
//            case STATUS_GEN_SKILL_LEVELUP: return "技能升级";
//            case STATUS_GEN_STAR_UP: return "升星";
//            case STATUS_RANDOM_GEN_ONE_TIMES_COIN: return "酒馆钱币单抽";
//            case STATUS_RANDOM_GEN_ONE_TIMES_GOLD: return "酒馆元宝单抽";
//            case STATUS_RANDOM_GEN_TEN_TIMES_COIN: return "酒馆钱币十连抽";
//            case STATUS_RANDOM_GEN_TEN_TIMES_GOLD: return "酒馆元宝十连抽";
//            case STATUS_RESET_SKILL: return "技能重置";
//            case STATUS_RESET_POINT: return "属性点重置";
//            case STATUS_RANDOM_SOUL: return "魂石商店";
//            case STATUS_EQUIP_MAKE: return "装备合成";
//            case STATUS_ITEM_SELL: return "出售道具";
//
//            case STATUS_SIGN_GET_COIN: return "签到获得";
//            case STATUS_SIGN_GET_GOLD: return "签到获得";
//            case STATUS_PAY_GET_GOLD: return "付费充值";
//            case STATUS_SHOP_REFRESH: return "刷新商店";
//            case STATUS_SHOP_BUY_COIN: return "商店购买道具";
//            case STATUS_SHOP_BUY_GOLD: return "商店购买道具";
//            case STATUS_STORY_GET_COIN: return "关卡掉落";
//            case STATUS_WIPE_OUT_GET: return "扫荡掉落";
//            case STATUS_BUY_PHYSICAL_STRENGTHP: return "购买体力";
//            case STATUS_TASK_GET_COIN: return "任务领取";
//            case STATUS_TASK_GET_GOLD: return "任务领取";
//
//            case STATUS_GOLD_BUY_COIN: return "点金手";
//
//            case STATUS_MAIL_GET: return "邮件领取";
//            case STATUS_ACTIVITY_PAY: return "充值活动";
//            case STATUS_DAILY_TASK_GET: return "每日任务领取";
//            case STATUS_BUY_WIPE_OUT: return "购买关卡挑战次数";
//            case STATUS_CODE_GET: return "兑换码领取";
//            case STATUS_TASK_GET: return "任务领取";
	
	        case STATUS_GEN_SKILL_LEVELUP: return "shengJiJiNeng";
	        case STATUS_GEN_STAR_UP: return "shengXing";
	        case STATUS_RANDOM_GEN_ONE_TIMES_COIN: return "jiuGuanTongQian1Ci";
	        case STATUS_RANDOM_GEN_ONE_TIMES_GOLD: return "jiuGuanYuanBao1Ci";
	        case STATUS_RANDOM_GEN_TEN_TIMES_COIN: return "jiuGuanTongQian10Ci";
	        case STATUS_RANDOM_GEN_TEN_TIMES_GOLD: return "jiuGuanYuanBao10Ci";
	        case STATUS_RESET_SKILL: return "jiNengChongZhi";
	        case STATUS_RESET_POINT: return "shuXingDianChongZhi";
	        case STATUS_RANDOM_SOUL: return "hunShiShangDian";
	        case STATUS_EQUIP_MAKE: return "zhuangBeiHeCheng";
	        case STATUS_ITEM_SELL: return "chuShouDaoJu";
	        case STATUS_SIGN_GET_COIN: return "qianDaoJiangLi";
	        case STATUS_SIGN_GET_GOLD: return "qianDaoJiangLi";
	        case STATUS_PAY_GET_GOLD: return "chongZhi";
	        case STATUS_SHOP_REFRESH: return "shuaXinShangDian";
	        case STATUS_SHOP_BUY_COIN: return "shangDianGouWu";
	        case STATUS_SHOP_BUY_GOLD: return "shangDianGouWu";
	        case STATUS_STORY_GET_COIN: return "guanKaDiaoLuo";
	        case STATUS_WIPE_OUT_GET: return "saoDangDiaoLuo";
	        case STATUS_BUY_PHYSICAL_STRENGTHP: return "GouMaiTiLi";
	        case STATUS_GOLD_BUY_COIN: return "dianJinShou";
	        case STATUS_MAIL_GET: return "youJianHuoDe";
	        case STATUS_ACTIVITY_PAY: return "chongZhiHuoDong";
	        case STATUS_DAILY_TASK_GET: return "renWuJiangLi";
	        case STATUS_BUY_WIPE_OUT: return "gouMaiGuanKaCiShu";
	        case STATUS_CODE_GET: return "duiHuanMaLingQv";
	        case STATUS_TASK_GET: return "renWuJiangLi";
	        case STATUS_ARENA_BUY: return "jingJiChang";
	        case STATUS_BATTLE_IN_WORLD: return "taoFaTianXia";
        }
        return "";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    @Column(name = "finance_id")
    private Integer id;

    // 1 coin 2 gold
    @Column(name = "finance_money_type" , nullable = false , columnDefinition = "smallint default 0")
    private Integer moneyType;
    
    @Column(name = "finance_old_money" , nullable = false , columnDefinition = "int default 0")
    private Integer oldMoney;

    @Column(name = "finance_new_money" , nullable = false , columnDefinition = "int default 0")
    private Integer newMoney;

    @Column(name = "finance_status" , nullable = false , columnDefinition = "smallint default 0")
    private Integer status;

    @Column(name = "finance_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp time;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "finance_desc" , nullable = false , length = 2000)
    private String desc;

    public FinanceLog() {
    }

    public FinanceLog(int roleId) {
        this.setRoleId(roleId);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }

    public void setOldMoney(Integer oldMoney) {
        this.oldMoney = oldMoney;
    }

    public void setNewMoney(Integer newMoney) {
        this.newMoney = newMoney;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    /*********************************  get ****************************************/
    public Integer getId() {
        return this.id;
    }

    public Integer getMoneyType() {
        return this.moneyType;
    }

    public Integer getOldMoney() {
        return this.oldMoney;
    }

    public Integer getNewMoney() {
        return this.newMoney;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public String getDesc() {
        return this.desc;
    }

    public Timestamp getTime() {
        return this.time;
    }

    public Integer getStatus() {
        return this.status;
    }
}
