package com.rwz.baselist.entity;

/**
 * Created by rwz on 2018/7/25.
 */

public class CommandEntity<T> {

    private int id;

    private T t;

    public CommandEntity(T t) {
        this.t = t;
    }

    public CommandEntity(int id) {
        this.id = id;
    }

    public CommandEntity(int id, T t) {
        this.id = id;
        this.t = t;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
