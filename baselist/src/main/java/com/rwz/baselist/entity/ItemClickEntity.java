package com.rwz.baselist.entity;

/**
 * Created by rwz on 2018/7/25.
 */

public class ItemClickEntity {

    private int id;

    private int position;

    public ItemClickEntity(int id, int position) {
        this.id = id;
        this.position = position;
    }


    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}
