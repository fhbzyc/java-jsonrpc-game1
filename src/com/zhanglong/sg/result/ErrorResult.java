package com.zhanglong.sg.result;

/**
 * Created by Speed on 2015/1/12.
 */
public class ErrorResult {
	
    public static String NotEnoughGold = "元宝不足，前去充值？";

	public int id;

    public Error error;

    public ErrorResult() {
    }

    public ErrorResult(Error err) {
        this.setError(err);
    }

    public ErrorResult(int id, int code, String message) {

    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setError(Error err) {
        this.error = err;
    }

    public Error getError() {
        return this.error;
    }
}
