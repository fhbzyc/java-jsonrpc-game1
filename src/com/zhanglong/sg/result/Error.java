package com.zhanglong.sg.result;

/**
 * Created by Speed on 2015/1/12.
 */

public class Error {

    public static int ERROR_BUY_OVER_NUM = 2; // 购买体力次数用尽

    public Error() {
    }

    public Error(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    private int code = 0;

    private String message = "";

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
