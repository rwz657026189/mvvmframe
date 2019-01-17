package com.rwz.basemodule.entity;

/**
 * Created by rwz on 2017/7/19.
 */

public class TabEntity{

    int id;//决定请求哪页数据（推荐、最热、评论最多或其他……）
    String title;
    int position;//viewpager中的位置
    String imgUrl;//图标
    int type;

    public TabEntity(String title) {
        this.title = title;
    }

    public TabEntity(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public TabEntity(int id, String title, int position, String imgUrl, int type) {
        this.id = id;
        this.title = title;
        this.position = position;
        this.imgUrl = imgUrl;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getType() {
        return type;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
