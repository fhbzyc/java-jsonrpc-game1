package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "base_boss")
public class BaseBoss implements Serializable , Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "boss_id" , nullable = false , columnDefinition = "int default 0")
	private Integer id;

	@Column(name = "boss_name" , nullable = false )
	private String name;

	@Column(name = "boss_level" , nullable = false , columnDefinition = "int default 0")
	private int level;

    @Column(name = "boss_hp" , nullable = false , columnDefinition = "int default 0")
    private int hp;

    @Column(name = "boss_skill1_level" , nullable = false , columnDefinition = "int default 0")
    private int skill1Level;

    @Column(name = "boss_skill2_level" , nullable = false , columnDefinition = "int default 0")
    private int skill2Level;

    @Column(name = "boss_skill3_level" , nullable = false , columnDefinition = "int default 0")
    private int skill3Level;

    @Column(name = "boss_skill4_level" , nullable = false , columnDefinition = "int default 0")
    private int skill4Level;

    @Column(name = "boss_atk" , nullable = false , columnDefinition = "int default 0")
    private int atk;

    @Column(name = "boss_def" , nullable = false , columnDefinition = "int default 0")
    private int def;

    @Column(name = "boss_magic" , nullable = false , columnDefinition = "int default 0")
    private int magic;

    // 魔抗
    @Column(name = "boss_mr" , nullable = false , columnDefinition = "int default 0")
    private int mr;

    @Column(name = "boss_crtical" , nullable = false , columnDefinition = "int default 0")
    private int crtical;

    @Column(name = "boss_dex" , nullable = false , columnDefinition = "int default 0")
    private int dex;

    @Column(name = "boss_vampire" , nullable = false , columnDefinition = "int default 0")
    private int vampire;

    @Column(name = "boss_other" , nullable = false , columnDefinition = "text")
    private String other;

	public BaseBoss() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getSkill1Level() {
		return skill1Level;
	}

	public void setSkill1Level(int skill1Level) {
		this.skill1Level = skill1Level;
	}

	public int getSkill2Level() {
		return skill2Level;
	}

	public void setSkill2Level(int skill2Level) {
		this.skill2Level = skill2Level;
	}

	public int getSkill3Level() {
		return skill3Level;
	}

	public void setSkill3Level(int skill3Level) {
		this.skill3Level = skill3Level;
	}

	public int getSkill4Level() {
		return skill4Level;
	}

	public void setSkill4Level(int skill4Level) {
		this.skill4Level = skill4Level;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getMagic() {
		return magic;
	}

	public void setMagic(int magic) {
		this.magic = magic;
	}

	public int getMr() {
		return mr;
	}

	public void setMr(int mr) {
		this.mr = mr;
	}

	public int getCrtical() {
		return crtical;
	}

	public void setCrtical(int crtical) {
		this.crtical = crtical;
	}

	public int getDex() {
		return dex;
	}

	public void setDex(int dex) {
		this.dex = dex;
	}

	public int getVampire() {
		return vampire;
	}

	public void setVampire(int vampire) {
		this.vampire = vampire;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	@Override
	public BaseBoss clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (BaseBoss) super.clone();
	}
}
