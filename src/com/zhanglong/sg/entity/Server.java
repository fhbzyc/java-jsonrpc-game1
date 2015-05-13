package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "servers")
public class Server implements Serializable {

	private static final long serialVersionUID = -576571978958265390L;

	@Id
	@Column(name = "server_id" , nullable = false , columnDefinition = "int default 0")
	private Integer id;

	@Column(name = "server_name" , nullable = false , columnDefinition = "varchar(255) default ''" , length = 255)
	private String name;

	@Column(name = "server_address" , nullable = false , columnDefinition = "varchar(255) default ''" , length = 255)
	private String address;

	@Column(name = "server_state" , nullable = false , columnDefinition = "smallint default 0")
	private Integer state;

	@Column(name = "server_enable" , nullable = false , columnDefinition = "boolean default FALSE")
	private Boolean enable;

	@Transient
	private boolean havePlayer;

    public Server() {
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

    public void setState(Integer state) {
        this.state = state;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public Integer getState() {
    	return this.state;
    }

    @JsonIgnore
    public Boolean getEnable() {
    	return this.enable;
    }
}
