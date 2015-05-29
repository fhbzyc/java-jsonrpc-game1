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

@Entity
@Table(name = "role_orders" , indexes = {@Index(columnList="role_id" , unique = false)})
public class Order implements Serializable {

    private static final long serialVersionUID = -3091972353824838296L;

    public static int STATUS_CREATE  = 0;
    public static int STATUS_PAY     = 1;
    public static int STATUS_SUCCESS = 2;

    public static int MOON_CARD = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "role_id" , nullable = false , columnDefinition = "int default 0")
    private int roleId;

    @Column(name = "user_id" , nullable = false , columnDefinition = "int default 0")
    private int userId;

    @Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
    private int serverId;

    @Column(name = "order_type" , nullable = false , columnDefinition = "smallint default 0")
	private int type;

    @Column(name = "order_gold" , nullable = false , columnDefinition = "int default 0")
    private int gold;

    @Column(name = "order_add_gold" , nullable = false , columnDefinition = "int default 0")
    private int addGold;

    @Column(name = "order_money" , nullable = false , columnDefinition = "int default 0")
    private int money;

    @Column(name = "order_status" , nullable = false , columnDefinition = "smallint default 0")
    private int status;

    @Column(name = "platform_id" , nullable = false , columnDefinition = "int default 0")
    private int platformId;

    @Column(name = "order_time" , columnDefinition = "timestamp")
    private Timestamp time;

    public Order() {
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getAddGold() {
		return addGold;
	}

	public void setAddGold(int addGold) {
		this.addGold = addGold;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
