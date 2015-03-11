package com.zhanglong.sg.entity;

import java.io.Serializable;

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

    public static long ActionCoolTime = 6 * 60 * 1000; // 毫秒

    public static int ANum = 59; // 体力最大值 - 等级
    
	// 经验对应等级
	public static int[] EXP = new int[]{25,50,75,105,135,165,215,295,395,525,655,785,915,1045,1175,1305,1435,1565,1695,1825,1955,2105,2265,2445,2635,2835,3075,3355,3655,4005,4405,4855,5355,5965,6685,7515,8615,9815,11015,12515,14015,15715,17415,19115,20915,22915,24915,27115,29315,31615,33915,36215,38715,41415,44115,46915,49915,53115,56415,59715,63015,66515,70015,73715,77515,81515,85715,90015,94515,99215,103915,108615,113315,118015,122715,127415,132115,136815,141615,147615,155615,165615,177615,191615,207615,225615,245615,267615,291615,317615};

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "role_coin" , nullable = false , columnDefinition = "int default 0")
    private Integer coin;

    @Column(name = "role_gold" , nullable = false , columnDefinition = "int default 0")
    private Integer gold;

    @Column(name = "role_exp" , nullable = false , columnDefinition = "int default 0")
    private Integer exp;

    @Column(name = "role_ap" , nullable = false , columnDefinition = "int default 0")
    private Integer ap;

    @Column(name = "role_ap_time" , nullable = false , columnDefinition = "bigint(14) default 0")
    private Long apTime;

    @Column(name = "role_vip" , nullable = false , columnDefinition = "int default 0")
    private Integer vip;

//    @Column(name = "role_date" , nullable = false , columnDefinition = "int default 0")
//    private Integer date;

//    @Column(name = "role_buy_coin_num" , nullable = false , columnDefinition = "int default 0")
//    private Integer buyCoinNum;

//    @Column(name = "role_bar_coin_num" , nullable = false , columnDefinition = "int default 0")
//    private Integer barCoinNum;

    @Column(name = "role_bar_gold_time" , nullable = false , columnDefinition = "bigint(14) default 0")
    private Long barGoldTime;

    @Column(name = "role_bar_coin_time" , nullable = false , columnDefinition = "bigint(14) default 0")
    private Long barCoinTime;

    @Column(name = "role_progress" , nullable = false , columnDefinition = "int default 0")
    private Integer progress;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(20) default ''" , length = 20)
    private String name;

    @Column(name = "role_avatar" , nullable = false , columnDefinition = "int default 0")
    private Integer avatar;

    @Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
    private Integer serverId;

    @Column(name = "user_id" , nullable = false , columnDefinition = "int default 0")
    private Integer userId;

    @Column(name = "role_money3" , nullable = false , columnDefinition = "int default 0")
    private Integer money3;

    @Column(name = "role_money4" , nullable = false , columnDefinition = "int default 0")
    private Integer money4;

	public Role() {
    }

    public Role(Integer roleId) {
        this.setRoleId(roleId);
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setVip(Integer vip) {
        this.vip = vip;
    }

    public void setBarGoldTime(Long barGoldTime) {
    	this.barGoldTime = barGoldTime;
    }

    public void setBarCoinTime(Long barCoinTime) {
    	this.barCoinTime = barCoinTime;
    }

    public void setProgress(Integer progress) {
    	this.progress = progress;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public void setAvatar(Integer avatar) {
    	this.avatar = avatar;
    }

    /************************************** 上面 set 下面 get **************************************/
    
    
    public Integer getRoleId() {
        return this.roleId;
    }

    public Integer getCoin() {
        return this.coin;
    }

    public Integer getGold() {
        return this.gold;
    }

    public Integer getExp() {
        return this.exp;
    }

    public Integer getVip() {
    	return this.vip;
    }

    public Long getBarGoldTime() {
    	return this.barGoldTime;
    }

    public Long getBarCoinTime() {
    	return this.barCoinTime;
    }
    
    public Integer getProgress() {
    	return this.progress ;
    }

    public String getName() {
    	return this.name;
    }

    public Integer getAvatar() {
    	return this.avatar;
    }

    public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Long getApTime() {
		return apTime;
	}

	public void setApTime(Long apTime) {
		this.apTime = apTime;
	}

	public Integer getAp() {
		return ap;
	}

	public void setAp(Integer ap) {
		this.ap = ap;
	}

	public Integer getMoney3() {
		return money3;
	}

	public void setMoney3(Integer money3) {
		this.money3 = money3;
	}

	public Integer getMoney4() {
		return money4;
	}

	public void setMoney4(Integer money4) {
		this.money4 = money4;
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
	public int getPhysicalStrength() {
        int num = (int)((System.currentTimeMillis() - this.apTime) / ActionCoolTime);

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
    public void setPhysicalStrength(int physicalStrength) {

        int actionExtra = 0;
        if (physicalStrength > this.maxPhysicalStrength()) {
        	actionExtra = physicalStrength - this.maxPhysicalStrength();
        	physicalStrength = this.maxPhysicalStrength();
        }

        long unixTime = System.currentTimeMillis();

        long num = (unixTime - this.apTime) / ActionCoolTime;

        long time2 = unixTime - num * ActionCoolTime - this.apTime;

   //     long time2 = num * ActionCoolTime + this.apTime;

        this.apTime = unixTime - (physicalStrength + 1) * ActionCoolTime + time2;

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

    /**
     * 体力回复时间
     * @return
     */
    public long psCoolTime() {

        if (this.getPhysicalStrength() >= this.maxPhysicalStrength()) {
            return 0;
        } else {

            long unixTime = System.currentTimeMillis();
            long num = (unixTime - this.apTime) / ActionCoolTime;
            long time2 = unixTime - num * ActionCoolTime - this.apTime;

        	return ActionCoolTime - time2;
        }
    }

    public int maxBuyPsNum() {
    	return this.vip + 1;
    }
}
