package com.zhanglong.sg.protocol;

import java.io.Serializable;

public class Success implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4248827531057484976L;

	private int id;

	private Object result;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	
}
