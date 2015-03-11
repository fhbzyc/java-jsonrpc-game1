package com.zhanglong.sg.entity;

import java.io.Serializable;

public class BaseStoryPK implements Serializable {

	private static final long serialVersionUID = -3853279506269984090L;

    private Integer id;

	private Integer type;

	public Integer getId() {
        return this.id;
    }

    public Integer getType() {
    	return this.type;
    }

	public void setId(Integer id) {
        this.id = id;
    }

	public void setType(Integer type) {
        this.type = type;
    }
}
