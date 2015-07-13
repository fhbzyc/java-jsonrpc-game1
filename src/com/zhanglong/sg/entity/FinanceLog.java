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
    public static final int STATUS_CHAT = 73;
    public static final int STATUS_TOUZIJIHUA = 74; // 投资计划
    public static final int STATUS_ACH_GET = 75; // 成就获得

    public static final int STATUS_BUY_ACH = 76; // 成就消费

    public static final int STATUS_SHOP_BUY_MONEY5 = 77;

    public static final int STATUS_RESET_BOSS = 78;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
