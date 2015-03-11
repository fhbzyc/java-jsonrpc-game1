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
import com.zhanglong.sg.utils.Utils;

@Entity
@IdClass(HeroPK.class)
@Table(name = "role_hero")
public class Hero implements Serializable {

    private static final long serialVersionUID = 8990364525534029860L;

    public static int[] EXP = new int[]{25,55,95,155,235,335,465,615,795,975,1185,1405,1625,1855,2095,2365,2645,2935,3225,3525,3835,4155,4485,4825,5175,5535,5905,
		6285,6675,7075,7605,8285,9115,10095,11195,12595,14295,16295,18595,21195,24195,27495,31195,35295,39795,44795,50195,56095,62495,69495,
		76995,85095,93795,103095,113095,124095,135095,147095,160095,174095,189095,204095,220095,237095,255095,275095,296095,318095,341095,365095,
		391095,418095,446095,476095,507095,540095,575095,611095,649095,689095,731095,775095,821095,869095,920095,973095,1028095,1085095,1144095,1205095};

    public static int UN_BATTLE = 0;
    public static int ON_BATTLE = 1;

    @Id
    @Column(name = "role_id")
	private Integer aRoleId;

    @Id
    @Column(name = "hero_id" , nullable = false , columnDefinition = "int(11) default 0")
	private Integer heroId;

	public void setHeroId(Integer heroId) {
		this.heroId = heroId;
	}

	public Integer getHeroId() {
	   return this.heroId;
	}

	public void setARoleId(Integer roleId) {
		this.aRoleId = roleId;
	}

	public Integer getARoleId() {
		return this.aRoleId;
	}
    
    @Column(name = "hero_exp" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer exp;

    @Column(name = "hero_str" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer str;

    @Column(name = "hero_int" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer INT;

    @Column(name = "hero_dex" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer dex;

    @Column(name = "hero_point" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer point;

    @Column(name = "hero_class" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer CLASS;

    @Column(name = "hero_star" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer star;

    @Column(name = "hero_is_battle" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer isBattle;

    @Column(name = "hero_skill1_level" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer skill1Level;

    @Column(name = "hero_skill2_level" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer skill2Level;

    @Column(name = "hero_skill3_level" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer skill3Level;

    @Column(name = "hero_skill4_level" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer skill4Level;

//    @Column(name = "hero_skill5_level" , nullable = false , columnDefinition = "int(11) default 0")
//    private Integer skill5Level;

    @Column(name = "hero_equip1" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip1;

    @Column(name = "hero_equip2" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip2;

    @Column(name = "hero_equip3" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip3;

    @Column(name = "hero_equip4" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip4;

    @Column(name = "hero_equip5" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip5;

    @Column(name = "hero_equip6" , nullable = false , columnDefinition = "tinyint(1) default 0")
    private Integer equip6;

    @Transient
    private Integer hp;

    @Transient
    private Float cd;

    public Hero() {
    }

    public Hero(int aRoleId, Integer heroId, Integer star) {
        this.setARoleId(aRoleId);
        this.setHeroId(heroId);
        this.setStar(star);
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }
    
    public void setStr(Integer str) {
        this.str = str;
    }
    
    public void setINT(Integer INT) {
        this.INT = INT;
    }

    public void setDex(Integer dex) {
        this.dex = dex;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public void setCLASS(Integer CLASS) {
        this.CLASS = CLASS;
    }
    
    public void setStar(Integer star) {
        this.star = star;
    }
    
    public void setIsBattle(Integer isBattle) {
    	if (isBattle != 0) {
    		isBattle = 1;
    	}
        this.isBattle = isBattle;
    }

    public void setSkill1Level(Integer eneralSkill1Level) {
        this.skill1Level = eneralSkill1Level;
    }

    public void setSkill2Level(Integer eneralSkill2Level) {
        this.skill2Level = eneralSkill2Level;
    }

    public void setSkill3Level(Integer eneralSkill3Level) {
        this.skill3Level = eneralSkill3Level;
    }

    public void setSkill4Level(Integer eneralSkill4Level) {
        this.skill4Level = eneralSkill4Level;
    }

//    public void setSkill5Level(Integer eneralSkill5Level) {
//        this.skill5Level = eneralSkill5Level;
//    }

    public void setEquip1(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip1 = equip;
    }

    public void setEquip2(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip2 = equip;
    }

    public void setEquip3(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip3 = equip;
    }

    public void setEquip4(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip4 = equip;
    }

    public void setEquip5(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip5 = equip;
    }

    public void setEquip6(Integer equip) {
    	if (equip != 0) {
    		equip = 1;
    	}
    	this.equip6 = equip;
    }

    public Integer getExp() {
        return this.exp;
    }

    public Integer getStr() {
        return this.str;
    }

    public Integer getINT() {
        return this.INT;
    }

    public Integer getDex() {
        return this.dex;
    }

    public Integer getPoint() {
        return this.point;
    }

    public Integer getCLASS() {
        return this.CLASS;
    }

    public Integer getStar() {
        return this.star;
    }

    public Integer getIsBattle() {
        return this.isBattle;
    }

    public Integer getSkill1Level() {
        return this.skill1Level;
    }

    public Integer getSkill2Level() {
        return this.skill2Level;
    }

    public Integer getSkill3Level() {
        return this.skill3Level;
    }

    public Integer getSkill4Level() {
        return this.skill4Level;
    }

//    public Integer getSkill5Level() {
//        return this.skill5Level;
//    }

    public Integer getEquip1() {
        return this.equip1;
    }

    public Integer getEquip2() {
        return this.equip2;
    }

    public Integer getEquip3() {
        return this.equip3;
    }

    public Integer getEquip4() {
        return this.equip4;
    }

    public Integer getEquip5() {
        return this.equip5;
    }

    public Integer getEquip6() {
        return this.equip6;
    }

    /**
     * 武将等级
     * @return
     */
    @Transient
    @JsonIgnore
    public int getLevel() {

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
    	return this.point + 3 * this.getLevel() - this.getStr() - this.getINT() - this.getDex();
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
     * @throws Throwable 
     */
    @Transient
    public Object[] toArray() throws Throwable {

        int expIndex = this.getLevel() - 2;
     	int baseExp = 0;
     	if (expIndex >= 0) {
     		baseExp = Hero.EXP[expIndex];
     	}

     	Object[] array = new Object[15];
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
        array[10] = new int[]{this.getEquip1() , this.getEquip2() , this.getEquip3() , this.getEquip4() , this.getEquip5() , this.getEquip6()};
        array[11] = this.getAvailablePoint(); // 可用的属性点
        array[12] = this.getAvailableSkillPoint(); // 可用的技能点

        BaseHeroEquipDao baseHeroEquipDao = Utils.getApplicationContext().getBean(BaseHeroEquipDao.class);

        BaseHeroEquip equip = baseHeroEquipDao.findByHeroId(this.getHeroId()).get(this.getCLASS());

        array[13] = new int[]{equip.getEquip1().getBaseId() , equip.getEquip2().getBaseId() , equip.getEquip3().getBaseId() , equip.getEquip4().getBaseId() , equip.getEquip5().getBaseId() , equip.getEquip6().getBaseId()};

        BaseHeroDao baseHeroDao = Utils.getApplicationContext().getBean(BaseHeroDao.class);

        BaseSkill baselSkill1 = baseHeroDao.findOne(this.getHeroId()).getSkill1();
        BaseSkill baselSkill2 = baseHeroDao.findOne(this.getHeroId()).getSkill2();
        BaseSkill baselSkill3 = baseHeroDao.findOne(this.getHeroId()).getSkill3();
        BaseSkill baselSkill4 = baseHeroDao.findOne(this.getHeroId()).getSkill4();
         
        int coin1 = baselSkill1.getBaseCoin() + (this.getSkill1Level() - 1) * baselSkill1.getLevelupCoin();
        int coin2 = baselSkill2.getBaseCoin() + (this.getSkill2Level() - 1) * baselSkill2.getLevelupCoin();
        int coin3 = baselSkill3.getBaseCoin() + (this.getSkill3Level() - 1) * baselSkill3.getLevelupCoin();
        int coin4 = baselSkill4.getBaseCoin() + (this.getSkill4Level() - 1) * baselSkill4.getLevelupCoin();

        array[14] = new int[]{coin1 , coin2 , coin3 , coin4};
        return array;
    }

    @Transient
    public Object[] toArray2() throws Throwable {

        int expIndex = this.getLevel() - 2;
     	int baseExp = 0;
     	if (expIndex >= 0) {
     		baseExp = Hero.EXP[expIndex];
     	}

     	Object[] array = new Object[15];
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
        array[10] = new int[]{this.getEquip1() , this.getEquip2() , this.getEquip3() , this.getEquip4() , this.getEquip5() , this.getEquip6()};
        array[11] = this.getAvailablePoint(); // 可用的属性点
        array[12] = this.getAvailableSkillPoint(); // 可用的技能点

        BaseHeroEquipDao baseHeroEquipDao = Utils.getApplicationContext().getBean(BaseHeroEquipDao.class);

        BaseHeroEquip equip = baseHeroEquipDao.findByHeroId(this.getHeroId()).get(this.getCLASS());

        array[13] = new int[]{equip.getEquip1().getBaseId() , equip.getEquip2().getBaseId() , equip.getEquip3().getBaseId() , equip.getEquip4().getBaseId() , equip.getEquip5().getBaseId() , equip.getEquip6().getBaseId()};

        BaseHeroDao baseHeroDao = Utils.getApplicationContext().getBean(BaseHeroDao.class);

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
        return array;
    }
}
