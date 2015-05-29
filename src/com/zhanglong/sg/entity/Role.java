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
@Table(name = "role" , indexes = {@Index(columnList="role_exp" , unique = false) , @Index(columnList="user_id" , unique = false)})
public class Role implements Serializable {

    private static final long serialVersionUID = 521595721195578568L;

    public static int ActionCoolTime = 6 * 60; // 毫秒

    public static int ANum = 59; // 体力最大值 - 等级
    
	// 经验对应等级
	public static int[] EXP = new int[]{6,12,24,36,60,90,140,220,300,400,530,660,790,920,1050,1180,1310,1440,1570,1700,1830,1980,2140,2320,2510,2710,2950,3230,3530,3880,4280,4730,5230,5840,6560,7390,8490,9690,10890,12390,13890,15590,17290,18990,20790,22790,24790,26990,29190,31490,33790,36090,38590,41290,43990,46790,49790,52990,56290,59590,62890,66390,69890,73590,77390,81390,85590,89890,94390,99090,103790,108490,113190,117890,122590,127290,131990,136690,141490,147490};

	public static int[] VIP_GOLD = new int[]{10 , 100 , 300 , 500 , 1000 , 2000 , 3000 , 5000 , 7000 , 10000 , 15000 , 20000 , 40000 , 80000 , 150000};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_coin" , nullable = false , columnDefinition = "int default 0")
    public int coin;

    @Column(name = "role_gold" , nullable = false , columnDefinition = "int default 0")
    public int gold;

    @Column(name = "role_exp" , nullable = false , columnDefinition = "int default 0")
    public int exp;

    @Column(name = "role_ap" , nullable = false , columnDefinition = "int default 0")
    public int ap;

    @Column(name = "role_ap_time" , nullable = false , columnDefinition = "int default 0")
    public int apTime;

    @Column(name = "role_vip" , nullable = false , columnDefinition = "int default 0")
    public int vip;

    @Column(name = "role_bar_gold_time" , nullable = false , columnDefinition = "int default 0")
    public int barGoldTime;

//    @Column(name = "role_bar_coin_time" , nullable = false , columnDefinition = "bigint default 0")
//    public long barCoinTime;

    @Column(name = "role_progress" , nullable = false , columnDefinition = "int default 0")
    public int progress;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(20) default ''" , length = 20)
    public String name;

    @Column(name = "role_avatar" , nullable = false , columnDefinition = "int default 0")
    public int avatar;

    @Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
    public int serverId;

    @Column(name = "user_id" , nullable = false , columnDefinition = "int default 0")
    public int userId;

    @Column(name = "role_money3" , nullable = false , columnDefinition = "int default 0")
    public int money3;

    @Column(name = "role_money4" , nullable = false , columnDefinition = "int default 0")
    public int money4;

    @Column(name = "role_level" , nullable = false , columnDefinition = "int default 0")
    public int level;

    @Column(name = "role_create_time" , nullable = false , columnDefinition = "timestamp")
    public Timestamp createTime;

    @Column(name = "role_enable" , nullable = false , columnDefinition = "boolean default false")
    public boolean enable;

    @Column(name = "role_string" , nullable = false , columnDefinition = "varchar(255) default ''" , length = 255)
    public String str;

    @Column(name = "role_count_gold" , nullable = false , columnDefinition = "int default 0")
    public int countGold;

    @Column(name = "role_pillage_time" , nullable = false , columnDefinition = "int default 0")
    public int pillageTime;

    @Column(name = "role_pillage_num" , nullable = false , columnDefinition = "int default 0")
    public int pillageNum;

    @Column(name = "role_rank" , nullable = false , columnDefinition = "int default 0")
    public int rank;

    @Column(name = "role_win_num" , nullable = false , columnDefinition = "int default 0")
    public int winNum;

    @Column(name = "role_card_time" , nullable = false , columnDefinition = "int default 0")
    public int cardTime;

	public Role() {
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public void setBarGoldTime(int barGoldTime) {
    	this.barGoldTime = barGoldTime;
    }

    public void setProgress(int progress) {
    	this.progress = progress;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public void setAvatar(int avatar) {
    	this.avatar = avatar;
    }

    /************************************** 上面 set 下面 get **************************************/

    public Integer getRoleId() {
        return this.roleId;
    }

    public int getCoin() {
        return this.coin;
    }

    public int getGold() {
        return this.gold;
    }

    public int getExp() {
        return this.exp;
    }

    public int getVip() {
    	return this.vip;
    }

    public int getBarGoldTime() {
    	return this.barGoldTime;
    }

    public int getProgress() {
    	return this.progress ;
    }

    public String getName() {
    	return this.name;
    }

    public int getAvatar() {
    	return this.avatar;
    }

    public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getApTime() {
		return apTime;
	}

	public void setApTime(int apTime) {
		this.apTime = apTime;
	}

	public int getAp() {
		return ap;
	}

	public void setAp(int ap) {
		this.ap = ap;
	}

	public int getMoney3() {
		return money3;
	}

	public void setMoney3(int money3) {
		this.money3 = money3;
	}

	public int getMoney4() {
		return money4;
	}

	public void setMoney4(int money4) {
		this.money4 = money4;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public int getCountGold() {
		return countGold;
	}

	public void setCountGold(int countGold) {
		this.countGold = countGold;
	}

	public int getPillageTime() {
		return pillageTime;
	}

	public void setPillageTime(int pillageTime) {
		this.pillageTime = pillageTime;
	}

	public int getPillageNum() {
		return pillageNum;
	}

	public void setPillageNum(int pillageNum) {
		this.pillageNum = pillageNum;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public int getCardTime() {
		return cardTime;
	}

	public void setCardTime(int cardTime) {
		this.cardTime = cardTime;
	}

	/**
	 * 经验对应等级
	 * @return
	 */
	@Transient
	public int level() {
    	return toLevel(this.getExp());
	}

	@Transient
	public int ap() {
        int num = ((int)(System.currentTimeMillis() / 1000l) - this.apTime) / ActionCoolTime;

        if (num > this.maxPhysicalStrength()) {
            num = this.maxPhysicalStrength();
        }

        return num + this.ap;
	}

	@Transient
    private int maxPhysicalStrength() {
        return level() + ANum;
    }

	@Transient
    public void setNewAp(int newAp) {

        int actionExtra = 0;
        if (newAp > this.maxPhysicalStrength()) {
        	actionExtra = newAp - this.maxPhysicalStrength();
        	newAp = this.maxPhysicalStrength();
        }

        int unixTime = (int)(System.currentTimeMillis() / 1000l);

        int num = (unixTime - this.apTime) / ActionCoolTime;

        int time2 = unixTime - num * ActionCoolTime - this.apTime;

   //     long time2 = num * ActionCoolTime + this.apTime;

        this.apTime = unixTime - (newAp + 1) * ActionCoolTime + time2;

        this.ap = actionExtra;

//        long coolTime = 0;
//        if (physicalStrength < this.maxPhysicalStrength()) {
//        	coolTime = ActionCoolTime - time2;
//        }
//
//        return coolTime;
    }

	public static int toLevel(int exp) {
    	for (int i = 0 ; i < EXP.length ; i++) {
			if (exp < EXP[i]) {
				return i + 1;
			}
		}
    	return EXP.length;
	}

    /**
     * 点金手最大次数
     * @return
     */
    public int maxGetCoinTimes() {
		int vip = this.vip;
    	if (vip <= 0) {
			return 2;
		} else if (vip == 1) {
			return 5;
		} else if (vip == 2) {
			return 20;
		} else if (vip == 3) {
			return 30;
		} else if (vip == 4) {
			return 40;
		} else if (vip == 5) {
			return 50;
		} else if (vip == 6) {
			return 60;
		} else if (vip == 7) {
			return 70;
		} else if (vip == 8) {
			return 80;
		} else if (vip == 9) {
			return 90;
		} else if (vip == 10) {
			return 100;
		} else if (vip == 11) {
			return 120;
		} else if (vip == 12) {
			return 150;
		} else if (vip == 13) {
			return 200;
		} else if (vip == 14) {
			return 250;
		} else {
			return 300;
		}
	}

    public int[] vip() {
    	int next = this.countGold;
    	if (this.vip < Role.VIP_GOLD.length) {
    		next = Role.VIP_GOLD[this.vip];
    	}
		return new int[]{this.vip , this.countGold , next};
    }

    /**
     * 体力回复时间
     * @return
     */
    public int apCoolTime() {

        if (this.ap() >= this.maxPhysicalStrength()) {
            return 0;
        } else {

            int unixTime = (int)(System.currentTimeMillis() / 1000l);
            int num = (unixTime - this.apTime) / ActionCoolTime;
            int time2 = unixTime - num * ActionCoolTime - this.apTime;

        	return ActionCoolTime - time2;
        }
    }

    public int maxBuyPsNum() {
    	return this.vip + 1;
    }
}
