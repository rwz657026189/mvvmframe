package com.rwz.basemodule.entity.turnentity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rwz on 2017/3/13.
 * 消息对话框传递实体类
 */

public class LoadingDialogTurnEntity implements Parcelable {

    private String tips;

    private boolean canDismissOutSide;//点击外面是否可以关闭

    public LoadingDialogTurnEntity(String tips) {
        this.tips = tips;
        canDismissOutSide = false;
    }

    public LoadingDialogTurnEntity(String tips, boolean canDismissOutSide) {
        this.tips = tips;
        this.canDismissOutSide = canDismissOutSide;
    }

    public void setCanDismissOutSide(boolean canDismissOutSide) {
        this.canDismissOutSide = canDismissOutSide;
    }

    public boolean isCanDismissOutSide() {
        return canDismissOutSide;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public LoadingDialogTurnEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tips);
        dest.writeByte(this.canDismissOutSide ? (byte) 1 : (byte) 0);
    }

    protected LoadingDialogTurnEntity(Parcel in) {
        this.tips = in.readString();
        this.canDismissOutSide = in.readByte() != 0;
    }

    public static final Creator<LoadingDialogTurnEntity> CREATOR = new Creator<LoadingDialogTurnEntity>() {
        @Override
        public LoadingDialogTurnEntity createFromParcel(Parcel source) {
            return new LoadingDialogTurnEntity(source);
        }

        @Override
        public LoadingDialogTurnEntity[] newArray(int size) {
            return new LoadingDialogTurnEntity[size];
        }
    };
}
