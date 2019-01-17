package com.rwz.basemodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.rwz.baselist.entity.BaseListEntity;
import com.rwz.basemodule.R;

/**
 * Created by rwz on 2017/7/27.
 * 兴趣、 声音标签选择界面传递参数
 */

public class ItemEntity extends BaseListEntity implements Parcelable {

    String imgUrl;      //图片地址
    String avatar;      //图片地址
    String name;       //图片名称
    int id;             //跳转id

    private boolean isChecked; //是否选中【自定义】

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.layout_item;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void toggle() {
        isChecked = !isChecked;
    }

    public ItemEntity() {
    }

    public String getAvatar() {
        return avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    protected ItemEntity(Parcel in) {
        this.imgUrl = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
        this.isChecked = in.readByte() != 0;
    }

    public static final Creator<ItemEntity> CREATOR = new Creator<ItemEntity>() {
        @Override
        public ItemEntity createFromParcel(Parcel source) {
            return new ItemEntity(source);
        }

        @Override
        public ItemEntity[] newArray(int size) {
            return new ItemEntity[size];
        }
    };

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        ItemEntity that = (ItemEntity) object;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
