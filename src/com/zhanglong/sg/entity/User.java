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
@Table(name = "users" , indexes = {@Index(columnList="user_name" , unique = false)})
public class User implements Serializable , Cloneable {

	public static int NORMAL_REG = 0;
	public static int QUICK_REG = 1;

    private static final long serialVersionUID = -6832583979566246187L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "user_pwd" , nullable = false , columnDefinition = "varchar(32) default ''")
    private String password;

    @Column(name = "user_imei" , nullable = false , columnDefinition = "varchar(64) default ''")
    private String imei;

    @Column(name = "user_mac" , nullable = false , columnDefinition = "varchar(32) default ''")
    private String mac;

    @Column(name = "platform_id" , nullable = false , columnDefinition = "int default 0")
    private int platformId;

    @Column(name = "user_name" , nullable = false , columnDefinition = "varchar(32) default ''")
    private String userName;

    @Column(name = "user_register_type" , nullable = false , columnDefinition = "smallint default 0")
    private int registerType;

    @Column(name = "user_register_time" , columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private Timestamp time;

    @Transient
    public String token = "";

    public User() {
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getPlatformId() {
		return platformId;
	}

	public void setPlatformId(int platformId) {
		this.platformId = platformId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String name) {
		this.userName = name;
	}

	public int getRegisterType() {
		return registerType;
	}

	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public User clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return (User) super.clone();
	}
}
