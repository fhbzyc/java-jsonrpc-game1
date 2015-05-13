package com.zhanglong.sg.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gift_code")
public class GiftCode implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4477032417874221256L;

	@Id
    @Column(name = "gift_code")
    private String code;

    @Column(name = "gift_status" , nullable = false , columnDefinition = "smallint default 0")
    private Integer status;

    @Column(name = "gift_id" , nullable = false , columnDefinition = "int default 0")
    private Integer giftId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}
}
