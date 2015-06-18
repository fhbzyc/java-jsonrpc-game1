package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zhanglong.sg.dao.BaseHeroDao;
import com.zhanglong.sg.dao.BaseHeroEquipDao;
import com.zhanglong.sg.entity2.BaseHeroEquip;
import com.zhanglong.sg.entity2.BaseSkill;
import com.zhanglong.sg.utils.SpringContextUtils;

@Entity
@IdClass(HeroPK.class)
@Table(name = "role_hero")
public class Hero implements Serializable {

    private static final long serialVersionUID = 8990364525534029860L;

    public static int[] EXP = new int[]{25,55,95,155,235,335,465,615,795,975,1185,1405,1625,1855,2095,2365,2645,2935,3225,3525,3835,4155,4485,4825,5175,5535,5905,
		6285,6675,7075,7605,8285,9115,10095,11195,12595,14295,16295,18595,21195,24195,27495,31195,35295,39795,44795,50195,56095,62495,69495,
		76995,85095,93795,103095,113095,124095,135095,147095,160095,174095,189095,204095,220095,237095,255095,275095,296095,318095,341095,365095,
		391095,418095,446095,476095,507095,540095,575095,611095,649095,689095,731095,775095,821095,869095,920095,973095,1028095,1085095,1144095,1205095};

    @Id
    @Column(name = "role_id" , nullable = false , columnDefinition = "int default 0")
	private int aRoleId;

    @Id
    @Column(name = "hero_id" , nullable = false , columnDefinition = "int default 0")
	private int heroId;

    @Column(name = "hero_exp" , nullable = false , columnDefinition = "int default 0")
    private int exp;

    @Column(name = "hero_str" , nullable = false , columnDefinition = "int default 0")
    private int str;

    @Column(name = "hero_int" , nullable = false , columnDefinition = "int default 0")
    private int INT;

    @Column(name = "hero_dex" , nullable = false , columnDefinition = "int default 0")
    private int dex;

    @Column(name = "hero_class" , nullable = false , columnDefinition = "int default 0")
    private int CLASS;

    @Column(name = "hero_star" , nullable = false , columnDefinition = "int default 0")
    private int star;

    @Column(name = "hero_is_battle" , nullable = false , columnDefinition = "boolean default false")
    private boolean isBattle;

    @Column(name = "hero_position" , nullable = false , columnDefinition = "smallint default 0")
    private int position;

    @Column(name = "hero_skill1_level" , nullable = false , columnDefinition = "int default 0")
    private int skill1Level;

    @Column(name = "hero_skill2_level" , nullable = false , columnDefinition = "int default 0")
    private int skill2Level;

    @Column(name = "hero_skill3_level" , nullable = false , columnDefinition = "int default 0")
    private int skill3Level;

    @Column(name = "hero_skill4_level" , nullable = false , columnDefinition = "int default 0")
    private int skill4Level;

    @Column(name = "hero_equip1" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip1;

    @Column(name = "hero_equip2" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip2;

    @Column(name = "hero_equip3" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip3;

    @Column(name = "hero_equip4" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip4;

    @Column(name = "hero_equip5" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip5;

    @Column(name = "hero_equip6" , nullable = false , columnDefinition = "boolean default false")
    private boolean equip6;

    // 这个是专属神器
    // @JsonSerialize(include = Inclusion.NON_NULL)
    @Column(name = "hero_equip7" , nullable = false , columnDefinition = "int default 0")
    private int equip7;

    @Column(name = "hero_equip1_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip1Exp;

    @Column(name = "hero_equip2_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip2Exp;

    @Column(name = "hero_equip3_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip3Exp;

    @Column(name = "hero_equip4_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip4Exp;

    @Column(name = "hero_equip5_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip5Exp;

    @Column(name = "hero_equip6_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip6Exp;

    @Column(name = "hero_equip7_exp" , nullable = false , columnDefinition = "int default 0")
    private int equip7Exp;

    @Column(name = "hero_level" , nullable = false , columnDefinition = "int default 0")
    private int level;

    @Transient
    private Integer hp;

    @Transient
    private Float cd;

    public Hero() {
    }

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public int getHeroId() {
	   return this.heroId;
	}

	public void setARoleId(int roleId) {
		this.aRoleId = roleId;
	}

	public int getARoleId() {
		return this.aRoleId;
	}

    public void setExp(int exp) {
        this.exp = exp;
    }
    
    public void setStr(int str) {
        this.str = str;
    }
    
    public void setINT(int INT) {
        this.INT = INT;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public void setCLASS(int CLASS) {
        this.CLASS = CLASS;
    }
    
    public void setStar(int star) {
        this.star = star;
    }
    
    public void setIsBattle(boolean isBattle) {

        this.isBattle = isBattle;
    }

    public void setSkill1Level(int eneralSkill1Level) {
        this.skill1Level = eneralSkill1Level;
    }

    public void setSkill2Level(int eneralSkill2Level) {
        this.skill2Level = eneralSkill2Level;
    }

    public void setSkill3Level(int eneralSkill3Level) {
        this.skill3Level = eneralSkill3Level;
    }

    public void setSkill4Level(int eneralSkill4Level) {
        this.skill4Level = eneralSkill4Level;
    }

    public void setEquip1(boolean equip) {

    	this.equip1 = equip;
    }

    public void setEquip2(boolean equip) {

    	this.equip2 = equip;
    }

    public void setEquip3(boolean equip) {

    	this.equip3 = equip;
    }

    public void setEquip4(boolean equip) {

    	this.equip4 = equip;
    }

    public void setEquip5(boolean equip) {

    	this.equip5 = equip;
    }

    public void setEquip6(boolean equip) {

    	this.equip6 = equip;
    }

    public int getExp() {
        return this.exp;
    }

    public int getStr() {
        return this.str;
    }

    public int getINT() {
        return this.INT;
    }

    public int getDex() {
        return this.dex;
    }

    public int getCLASS() {
        return this.CLASS;
    }

    public int getStar() {
        return this.star;
    }

    public boolean getIsBattle() {
        return this.isBattle;
    }

    public int getSkill1Level() {
        return this.skill1Level;
    }

    public int getSkill2Level() {
        return this.skill2Level;
    }

    public int getSkill3Level() {
        return this.skill3Level;
    }

    public int getSkill4Level() {
        return this.skill4Level;
    }

    public boolean getEquip1() {
        return this.equip1;
    }

    public boolean getEquip2() {
        return this.equip2;
    }

    public boolean getEquip3() {
        return this.equip3;
    }

    public boolean getEquip4() {
        return this.equip4;
    }

    public boolean getEquip5() {
        return this.equip5;
    }

    public boolean getEquip6() {
        return this.equip6;
    }

	public void setLevel(int level) {
		this.level = level;
	}

    public int getLevel() {
    	return this.level;
    }

	public int getEquip7() {
		return equip7;
	}

	public void setEquip7(int equip7) {
		this.equip7 = equip7;
	}

	public int getEquip1Exp() {
		return equip1Exp;
	}

	public void setEquip1Exp(int equip1Exp) {
		this.equip1Exp = equip1Exp;
	}

	public int getEquip2Exp() {
		return equip2Exp;
	}

	public void setEquip2Exp(int equip2Exp) {
		this.equip2Exp = equip2Exp;
	}

	public int getEquip3Exp() {
		return equip3Exp;
	}

	public void setEquip3Exp(int equip3Exp) {
		this.equip3Exp = equip3Exp;
	}

	public int getEquip4Exp() {
		return equip4Exp;
	}

	public void setEquip4Exp(int equip4Exp) {
		this.equip4Exp = equip4Exp;
	}

	public int getEquip5Exp() {
		return equip5Exp;
	}

	public void setEquip5Exp(int equip5Exp) {
		this.equip5Exp = equip5Exp;
	}

	public int getEquip6Exp() {
		return equip6Exp;
	}

	public void setEquip6Exp(int equip6Exp) {
		this.equip6Exp = equip6Exp;
	}

	public int getEquip7Exp() {
		return equip7Exp;
	}

	public void setEquip7Exp(int equip7Exp) {
		this.equip7Exp = equip7Exp;
	}

    public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int level() {
    	for (int i = 0 ; i < EXP.length ; i++) {
			if (this.getExp() <= EXP[i]) {
				return i + 1;
			}
		}

    	return EXP.length;
    }

    /**
     * 武将可用点数
     * @return
     */
    @Transient
    @JsonIgnore
    public int getAvailablePoint() {
    	return 3 * this.getLevel() - this.getStr() - this.getINT() - this.getDex();
    }

    /**
     * 武将可用技能点
     * @return
     */
    @Transient
    @JsonIgnore
    public int getAvailableSkillPoint() {
    	return 2 + 4 + 2 * (this.getLevel() - 1) - this.getSkill1Level() - this.getSkill2Level() - this.getSkill3Level() - this.getSkill4Level();
    }

    @Transient
    public Integer getHp() {
		return this.hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
	}

    @Transient
    public Float getCd() {
		return this.cd;
	}

	public void setCd(Float cd) {
		this.cd = cd;
	}

	/**
     * 武将可用技能点
     * @return
	 * @throws Exception 
     */
    @Transient
    public Object[] toArray() throws Exception {

        int expIndex = this.getLevel() - 2;
     	int baseExp = 0;
     	if (expIndex >= 0) {
     		baseExp = Hero.EXP[expIndex];
     	}

     	int equip1 = this.getEquip1() ? 1 : 0;
     	int equip2 = this.getEquip2() ? 1 : 0;
     	int equip3 = this.getEquip3() ? 1 : 0;
     	int equip4 = this.getEquip4() ? 1 : 0;
     	int equip5 = this.getEquip5() ? 1 : 0;
     	int equip6 = this.getEquip6() ? 1 : 0;

     	Object[] array = new Object[19];
        array[0] = this.getHeroId();
        array[1] = this.getStr();
        array[2] = this.getINT();
        array[3] = this.getDex();
        array[4] = this.getExp() - baseExp;
        array[5] = this.getLevel();
        array[6] = this.getCLASS();
        array[7] = this.getStar();
        array[8] = this.getIsBattle();
        array[9] = new int[]{this.getSkill1Level() , this.getSkill2Level() , this.getSkill3Level() , this.getSkill4Level()};
        array[10] = new int[]{equip1 , equip2 , equip3 , equip4 , equip5 , equip6};
        array[11] = this.getAvailablePoint(); // 可用的属性点
        array[12] = this.getAvailableSkillPoint(); // 可用的技能点

        BaseHeroEquipDao baseHeroEquipDao = (BaseHeroEquipDao) SpringContextUtils.getBean(BaseHeroEquipDao.class);

        BaseHeroEquip equip = baseHeroEquipDao.findByHeroId(this.getHeroId()).get(this.getCLASS());

        array[13] = new int[]{equip.getEquip1().getBaseId() , equip.getEquip2().getBaseId() , equip.getEquip3().getBaseId() , equip.getEquip4().getBaseId() , equip.getEquip5().getBaseId() , equip.getEquip6().getBaseId()};

        BaseHeroDao baseHeroDao = (BaseHeroDao) SpringContextUtils.getBean(BaseHeroDao.class);

        BaseSkill baselSkill1 = baseHeroDao.findOne(this.getHeroId()).getSkill1();
        BaseSkill baselSkill2 = baseHeroDao.findOne(this.getHeroId()).getSkill2();
        BaseSkill baselSkill3 = baseHeroDao.findOne(this.getHeroId()).getSkill3();
        BaseSkill baselSkill4 = baseHeroDao.findOne(this.getHeroId()).getSkill4();
         
        int coin1 = baselSkill1.getBaseCoin() + (this.getSkill1Level() - 1) * baselSkill1.getLevelupCoin();
        int coin2 = baselSkill2.getBaseCoin() + (this.getSkill2Level() - 1) * baselSkill2.getLevelupCoin();
        int coin3 = baselSkill3.getBaseCoin() + (this.getSkill3Level() - 1) * baselSkill3.getLevelupCoin();
        int coin4 = baselSkill4.getBaseCoin() + (this.getSkill4Level() - 1) * baselSkill4.getLevelupCoin();

        array[14] = new int[]{coin1 , coin2 , coin3 , coin4};
        array[15] = this.getHp();
        array[16] = this.getCd();
        array[17] = new int[]{this.getEquip1Exp() , this.getEquip2Exp() , this.getEquip3Exp() , this.getEquip4Exp() , this.getEquip5Exp() , this.getEquip6Exp() , this.getEquip7Exp()};
        array[18] = this.getEquip7();
        return array;
    }
}
