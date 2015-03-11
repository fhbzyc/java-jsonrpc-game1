package com.zhanglong.sg.result;

/**
 * Created by Speed on 2015/1/12.
 */
public class ErrorResult {

    public static ErrorResult NotEnoughGold = new ErrorResult(new Error(Error.ERROR_BUY_OVER_NUM, "元宝不足，前去充值？"));

    public ErrorResult(Error err) {
        this.setError(err);
    }

    public Error error;

    public void setError(Error err) {
        this.error = err;
    }

    public Error getError() {
        return this.error;
    }

    public static Object returnError(int code, String msg) {
    	return null;
    }
}
