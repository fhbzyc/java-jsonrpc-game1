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
@Table(name = "role_mail" , indexes = {@Index(columnList = "role_id",name = "role_id")})
public class Mail implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 6893856762898416180L;

//    private static final long serialVersionUID = 7765994647828819791L;
//    public static final int UNREAD = 0;
    public static final int READ = 1;
    public static final int DELETED = -1;
//    public static final int SYSTEM = 1;
//    public static final int NORMAL = 0;
//    public static final int STATUS_ATTACH_PICKED = 1;
//    public static final int STATUS_ATTACH_NO_PICK = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Integer id;

    @Column(name = "mail_from_name" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String fromName;

    @Column(name = "role_id" , nullable = false , columnDefinition = "int default 0")
    private Integer roleId;

    @Column(name = "mail_title" , nullable = false , columnDefinition = "varchar(255) default ''")
    private String title;

    @Column(name = "mail_content" , nullable = false , columnDefinition = "text")
    private String content;

    @Column(name = "mail_attchment" , nullable = false , columnDefinition = "text")
    private String attachment;

    @Column(name = "mail_status" , nullable = false , columnDefinition = "smallint default 0")
    private Integer status;

    @Column(name = "mail_time" , nullable = false , columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    private Timestamp sendTime;

    public Mail() {
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
}
