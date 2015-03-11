package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "servers")
public class Server implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -576571978958265390L;

	@Id
	@Column(name = "server_id")
	private Integer id;

    @Column(name = "server_code")
    private String code;

	@Column(name = "server_name")
	private String name;

	@Column(name = "server_address")
	private String address;

	@Column(name = "appid")
	private String appId;

	@Column(name = "state")
	private Integer state;

	@Column(name = "server_enable")
	private Integer enable;

	@Column(name = "server_time")
	private Long time;

	@Transient
	private boolean havePlayer;

    public Server() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public String getCode() {
        return this.code;
    }

    public Integer getId() {
    	return this.id;
    }

    public String getName() {
    	return this.name;
    }

    public String getAddress() {
    	return this.address;
    }

    public String getAppId() {
    	return this.appId;
    }

    public Integer getState() {
    	return this.state;
    }

    public Integer getEnable() {
    	return this.enable;
    }

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public boolean getHavePlayer() {
		return havePlayer;
	}

	public void setHavePlayer(boolean havePlayer) {
		this.havePlayer = havePlayer;
	}
	
}
