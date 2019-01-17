package com.rwz.basemodule.entity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Web实体
 */
public class WebEntity implements Parcelable {

    private int type;       //类型（部分H5页面需要特殊处理）
    private String url;     //链接
    private String title;   //标题
    private Bundle params;  //传递的参数

    public WebEntity(String url) {
        this(0, null, url);
    }

    public WebEntity(String title, String url) {
        this(0, title, url);
    }

    public WebEntity(int type, String title, String url) {
        this.type = type;
        this.title = title;
        this.url = url;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.type);
        dest.writeBundle(this.params);
    }

    protected WebEntity(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.type = in.readInt();
        this.params = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<WebEntity> CREATOR = new Creator<WebEntity>() {
        @Override
        public WebEntity createFromParcel(Parcel source) {
            return new WebEntity(source);
        }

        @Override
        public WebEntity[] newArray(int size) {
            return new WebEntity[size];
        }
    };
}
