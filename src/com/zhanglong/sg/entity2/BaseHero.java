package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "base_hero")
public class BaseHero implements Serializable {

	private static final long serialVersionUID = 4439990055411229847L;

	@Id
	@Column(name = "hero_id" , nullable = false , columnDefinition = "int default 0")
	private Integer id;

	@Column(name = "hero_name" , nullable = false )
	private String name;

	@Column(name = "hero_star" , nullable = false , columnDefinition = "int default 0")
	private Integer star;

    @ManyToOne
    @JoinColumn(name="hero_skill1",referencedColumnName="skill_id")
    private BaseSkill skill1;

    @ManyToOne
    @JoinColumn(name="hero_skill2",referencedColumnName="skill_id")
    private BaseSkill skill2;

    @ManyToOne
    @JoinColumn(name="hero_skill3",referencedColumnName="skill_id")
    private BaseSkill skill3;

    @ManyToOne
    @JoinColumn(name="hero_skill4",referencedColumnName="skill_id")
    private BaseSkill skill4;

	public BaseHero() {
		
	}

    public BaseHero(int baseId, String name, int star, int skill1, int skill2, int skill3, int skill4) {
        this.id = baseId;
        this.name = name;
        this.star = star;
        
        BaseSkill s1 = new BaseSkill();
        s1.setId(skill1);
        BaseSkill s2 = new BaseSkill();
        s2.setId(skill2);
        BaseSkill s3 = new BaseSkill();
        s3.setId(skill3);
        BaseSkill s4 = new BaseSkill();
        s4.setId(skill4);
        
        this.setSkill1(s1);
        this.setSkill2(s2);
        this.setSkill3(s3);
        this.setSkill4(s4);
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

    public void setSkill1(BaseSkill skill) {
    	this.skill1 = skill;
    }

    public void setSkill2(BaseSkill skill) {
    	this.skill2 = skill;
    }

    public void setSkill3(BaseSkill skill) {
    	this.skill3 = skill;
    }

    public void setSkill4(BaseSkill skill) {
    	this.skill4 = skill;
    }

	public Integer getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public Integer getStar() {
		return this.star;
	}

    public BaseSkill getSkill1() {
    	return this.skill1;
    }

    public BaseSkill getSkill2() {
    	return this.skill2;
    }

    public BaseSkill getSkill3() {
    	return this.skill3;
    }

    public BaseSkill getSkill4() {
    	return this.skill4;
    }
}
