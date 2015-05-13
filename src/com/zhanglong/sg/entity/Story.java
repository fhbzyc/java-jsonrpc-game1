package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.zhanglong.sg.utils.Utils;

@Entity
@IdClass(StoryPK.class)
@Table(name = "role_story")
public class Story implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2906858527128718492L;
    
    public static int COPY_TYPE = 1;
    public static int HERO_COPY_TYPE = 2;

    @Id
    @Column(name = "role_id")  
    private Integer aRoleId;

    @Id
    @Column(name = "story_id" , nullable = false , columnDefinition = "int default 0")
    private Integer storyId;

    @Id
    @Column(name = "story_type" , nullable = false , columnDefinition = "smallint default 0")
    private Integer type;

    @Column(name = "story_star" , nullable = false , columnDefinition = "smallint default 0")
    private Integer star;

    @Column(name = "story_num" , nullable = false , columnDefinition = "int default 0")
    private Integer num;

    @Column(name = "story_buy_num" , nullable = false , columnDefinition = "int default 0")
    private Integer buyNum;

    @Column(name = "story_date" , nullable = false , columnDefinition = "int default 0")
    private Integer date;

    public Story() {
    }

    public void setARoleId(Integer roleId) {
        this.aRoleId = roleId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getARoleId() {
        return this.aRoleId;
    }

    public Integer getStoryId() {
        return this.storyId;
    }

    public Integer getType() {
        return this.type;
    }

    public Integer getStar() {
        return this.star;
    }

    public Integer getNum() {
        return this.num;
    }

    public Integer getBuyNum() {
    	return this.buyNum;
    }

    public Integer getDate() {
        return this.date;
    }

    /**
    *
    * @return
    */
    @Transient
    public void init() {
    	
    	Integer dat = Integer.valueOf(Utils.date());
    	if (this.date != null) {
    		if ((int)dat == (int)this.date) {
    			return;
    		}
    	}

		this.setNum(0);
		this.setBuyNum(0);
		this.setDate(dat);
    }

    /**
     * 精英关卡充值需要的gold
     * @return
     */
    @Transient
    public int needGold() {
    	this.init();
    	int n = this.getBuyNum() / 2;
    	int gold = (int)(25 * Math.pow(2, n));
    	if (gold > 400) {
    		gold = 400;
    	}
    	return gold;
    }
}
