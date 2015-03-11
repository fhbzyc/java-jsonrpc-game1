package com.zhanglong.sg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "apk_version")
public class Version {

	public static int TYPE_ZIP = 1;
	public static int TYPE_APK = 2;

	public static int ENABLE = 1;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY) //
	@Column(name = "version_code")
	private Integer versionCode;

	@Column(name = "version_type" , nullable = false , columnDefinition = "smallint default 0")
	private Integer versionType;

	@Column(name = "version_name" , nullable = false) 
	private String versionName;

	@Column(name = "version_url" , nullable = false)
	private String versionUrl;

	@Column(name = "version_enable" , nullable = false , columnDefinition = "smallint default 0")
	private Integer versionEnable;

	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}

	public void setVersionType(Integer versionType) {
		this.versionType = versionType;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public void setVersionUrl(String versionUrl) {
		this.versionUrl = versionUrl;
	}

	public void setVersionEnable(Integer versionEnable) {
		this.versionEnable = versionEnable;
	}

	public Integer getVersionCode() {
		return this.versionCode;
	}

	public Integer getVersionType() {
		return this.versionType;
	}

	public String getVersionName() {
		return this.versionName;
	}

	public String getVersionUrl() {
		return this.versionUrl;
	}

	public Integer getVersionEnable() {
		return this.versionEnable;
	}
}
