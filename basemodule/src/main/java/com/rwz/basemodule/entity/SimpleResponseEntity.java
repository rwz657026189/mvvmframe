package com.rwz.basemodule.entity;

/**
 * Created by rwz on 2017/7/19.
 * 用户操作简单回调
 */

public class SimpleResponseEntity {

    boolean result; //结果
    String message; //信息

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public SimpleResponseEntity() {
    }

    public SimpleResponseEntity(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "SimpleResponseEntity{" +
                "result=" + result +
                ", message='" + message + '\'' +
                '}';
    }
}
