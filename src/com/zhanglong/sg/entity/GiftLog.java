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
@Table(name = "gift_logs" , indexes = {@Index(columnList="role_id" , unique = false)})
public class GiftLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2215111408492321064L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "gift_code" , nullable = false , columnDefinition = "varchar(8) default ''")
    private String code;

    @Column(name = "gift_id" , nullable = false , columnDefinition = "int default 0")
    private Integer giftId;

    @Column(name = "role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

    @Column(name = "role_name" , nullable = false , columnDefinition = "varchar(20) default ''")
    private String roleName;

    @Column(name = "gift_time" , nullable = false , columnDefinition = "timestamp")
    private Timestamp time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
}
