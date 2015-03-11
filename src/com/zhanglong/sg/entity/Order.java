package com.zhanglong.sg.entity;

import java.io.Serializable;

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

    public static int STATUS_CREATE = 0;
    public static int STATUS_SUCCESS = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "order_type" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String type;

    @Column(name = "order_gold" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer gold;

    @Column(name = "order_add_gold" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer addGold;

    @Column(name = "order_money" , nullable = false , columnDefinition = "int(11) default 0")
    private Integer money;

    @Column(name = "order_status" , nullable = false , columnDefinition = "tinyint(4) default 0")
    private Integer status;

    @Column(name = "order_time" , columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private String time;

    public Order() {
        
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGold(Integer gold) {
        this.gold = gold;
    }

    public void setAddGold(Integer addGold) {
        this.addGold = addGold;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getId() {
        return this.id;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public String getType() {
        return this.type;
    }

    public Integer getGold() {
        return this.gold;
    }

    public Integer getAddGold() {
        return this.addGold;
    }

    public Integer getMoney() {
        return this.money;
    }

    public Integer getStatus() {
        return this.status;
    }

    public String getTime() {
        return this.time;
    }
}
