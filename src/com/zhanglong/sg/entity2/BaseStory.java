package com.zhanglong.sg.entity2;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(BaseStoryPK.class)
@Table(name = "base_story")
public class BaseStory implements Serializable {

    public static int COPY_TYPE = 1;
    public static int HERO_COPY_TYPE = 2;
    public static int SPECIAL_COPY_TYPE = 3;

	private static final long serialVersionUID = -1227382473763042694L;

	@Id
	@Column(name = "story_id" , nullable = false , columnDefinition = "int default 0")
    private Integer id;

	@Id
	@Column(name = "story_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer type;

	@Column(name = "story_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String name;

	@Column(name = "story_team_exp" , nullable = false , columnDefinition = "int default 0")
    private Integer teamExp;

	@Column(name = "story_exp" , nullable = false , columnDefinition = "int default 0")
    private Integer exp;

	@Column(name = "story_coin" , nullable = false , columnDefinition = "int default 0")
    private Integer coin;

	@Column(name = "story_unlock_level" , nullable = false , columnDefinition = "int default 0")
    private Integer unlockLevel; // 等级限制

	@Column(name = "story_items" , nullable = false , columnDefinition = "text")
    private String items; // 掉落道具

	@Column(name = "story_wipe_out_items" , nullable = false , columnDefinition = "text")
    private String wipeOutItems; // 扫荡掉落

	public BaseStory() {
	}

    public BaseStory(int id, int type, String name, int teamExp, int exp, int coin, int unlockLevel, String items, String wipeOutItems) {
        this.setId(id);
        this.setType(type);
        this.setName(name);
        this.setTeamExp(teamExp);
        this.setExp(exp);
        this.setCoin(coin);
        this.setUnlockLevel(unlockLevel);
        this.setItems(items);
        this.setWipeOutItems(wipeOutItems);
    }

	public Integer getId() {
        return this.id;
    }

    public Integer getType() {
    	return this.type;
    }

    public String getName() {
    	return this.name;
    }

    public Integer getTeamExp() {
    	return this.teamExp;
    }

    public Integer getCoin() {
    	return this.coin;
    }

    public Integer getExp() {
    	return this.exp;
    }

    public Integer getUnlockLevel() {
    	return this.unlockLevel;
    }

    public String getItems() {
    	return this.items;
    }

    public String getWipeOutItems() {
    	return this.wipeOutItems;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public void setType(Integer type) {
        this.type = type;
    }

    public void setName(String name) {
    	this.name = name;
    }

    public void setTeamExp(Integer teamExp) {
        this.teamExp = teamExp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public void setCoin(Integer coin) {
        this.coin = coin;
    }

    public void setUnlockLevel(Integer unlockLevel) {
        this.unlockLevel = unlockLevel;
    }

    public void setItems(String items) {
    	this.items = items;
    }

    public void setWipeOutItems(String wipeOutItems) {
    	this.wipeOutItems = wipeOutItems;
    }
}
