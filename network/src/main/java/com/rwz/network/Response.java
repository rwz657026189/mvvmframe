package com.rwz.network;

/**
 * Created by rwz on 2017/2/11.
 */
public class Response<T> {
    private T data;
    private int code;
    private String msg;

    public T getContent() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "Response{" +
                "data=" + data +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
