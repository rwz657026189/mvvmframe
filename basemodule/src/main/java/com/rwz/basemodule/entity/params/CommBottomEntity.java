package com.rwz.basemodule.entity.params;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 * Created by rwz on 2018/7/13.
 *  通用的底部列表item
 */

public class CommBottomEntity implements Parcelable {

    //内容
    private String content;
    //颜色
    private int color;
    //是否可以点击
    private boolean isClickEnable;

    public CommBottomEntity() {
    }

    public CommBottomEntity(String content) {
        this(content, true);
    }

    public CommBottomEntity(String content, boolean isClickEnable) {
        this.content = content;
        this.isClickEnable = isClickEnable;
    }

    public CommBottomEntity(String content, int color, boolean isClickEnable) {
        this.content = content;
        this.color = color;
        this.isClickEnable = isClickEnable;
    }

    public CommBottomEntity(@StringRes int content, @ColorRes int color, boolean isClickEnable) {
        this(ResourceUtil.getString(content), ResourceUtil.getColor(color), isClickEnable);
    }

    public boolean isClickEnable() {
        return isClickEnable;
    }

    public void setClickEnable(boolean clickEnable) {
        isClickEnable = clickEnable;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeInt(this.color);
        dest.writeByte(this.isClickEnable ? (byte) 1 : (byte) 0);
    }

    protected CommBottomEntity(Parcel in) {
        this.content = in.readString();
        this.color = in.readInt();
        this.isClickEnable = in.readByte() != 0;
    }

    public static final Creator<CommBottomEntity> CREATOR = new Creator<CommBottomEntity>() {
        @Override
        public CommBottomEntity createFromParcel(Parcel source) {
            return new CommBottomEntity(source);
        }

        @Override
        public CommBottomEntity[] newArray(int size) {
            return new CommBottomEntity[size];
        }
    };
}
