package com.rwz.basemodule.entity.turnentity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rwz on 2017/4/12.
 */

public class ShareTurnEntity implements Parcelable {

    public int id;
    public int type;

    public ShareTurnEntity(int id, int type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
    }

    public ShareTurnEntity() {
    }

    protected ShareTurnEntity(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<ShareTurnEntity> CREATOR = new Creator<ShareTurnEntity>() {
        @Override
        public ShareTurnEntity createFromParcel(Parcel source) {
            return new ShareTurnEntity(source);
        }

        @Override
        public ShareTurnEntity[] newArray(int size) {
            return new ShareTurnEntity[size];
        }
    };
}
