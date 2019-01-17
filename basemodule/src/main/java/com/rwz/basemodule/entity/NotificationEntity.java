package com.rwz.basemodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Notification实体
 */
public class NotificationEntity implements Parcelable {

    private String title;
    private String msg;
    private int icon;
    private boolean isAlwaysShow; //是否一直显示

    public NotificationEntity() {
    }

    public NotificationEntity(int icon) {
        this.icon = icon;
    }

    public NotificationEntity(String title, String msg, int icon) {
        this.title = title;
        this.msg = msg;
        this.icon = icon;
    }

    public boolean isAlwaysShow() {
        return isAlwaysShow;
    }

    public void setAlwaysShow(boolean alwaysShow) {
        isAlwaysShow = alwaysShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.msg);
        dest.writeInt(this.icon);
    }

    protected NotificationEntity(Parcel in) {
        this.title = in.readString();
        this.msg = in.readString();
        this.icon = in.readInt();
    }

    public static final Parcelable.Creator<NotificationEntity> CREATOR = new Parcelable.Creator<NotificationEntity>() {
        @Override
        public NotificationEntity createFromParcel(Parcel source) {
            return new NotificationEntity(source);
        }

        @Override
        public NotificationEntity[] newArray(int size) {
            return new NotificationEntity[size];
        }
    };

    @Override
    public String toString() {
        return "NotificationEntity{" +
                " \n title='" + title + '\'' +
                ",\n msg='" + msg + '\'' +
                ",\n icon=" + icon +
                ",\n isAlwaysShow=" + isAlwaysShow +
                '}';
    }
}
