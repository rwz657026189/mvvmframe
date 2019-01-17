package com.rwz.basemodule.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.help.StringHelp;
import com.rwz.commonmodule.utils.system.AndroidUtils;

/**
 * Created by rwz on 2017/4/10.
 * 版本更新实体类
 */

public class UpdateVersionEntity implements Parcelable {

    private String description;         //结果消息
    private String title;               //升级标题
    private String url;                 //下载地址
    private int tips;                   //是否强制更新（1-否2-是）
    private String version = "";        //内部版本号

    private String name = "";           //版本名
    private boolean hasNewVersion;      //是否有更新

    public UpdateVersionEntity() {
    }

    public boolean isHasNewVersion() {
        return AndroidUtils.getVersionCode(BaseApplication.getInstance()) < StringHelp.parseInt(version, 0);
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getForcedUpdate() {
        return tips;
    }

    public void setTips(int tips) {
        this.tips = tips;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionMsg() {
        return description;
    }

    public String getVersionTitle() {
        return title;
    }

    public String getDownloadUrl() {
        return url;
    }

    public boolean getTips() {
        return tips == 2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.version);
        dest.writeString(this.name);
        dest.writeByte(this.hasNewVersion ? (byte) 1 : (byte) 0);
        dest.writeInt(this.tips);
    }

    protected UpdateVersionEntity(Parcel in) {
        this.description = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.version = in.readString();
        this.name = in.readString();
        this.hasNewVersion = in.readByte() != 0;
        this.tips = in.readInt();
    }

    public static final Creator<UpdateVersionEntity> CREATOR = new Creator<UpdateVersionEntity>() {
        @Override
        public UpdateVersionEntity createFromParcel(Parcel source) {
            return new UpdateVersionEntity(source);
        }

        @Override
        public UpdateVersionEntity[] newArray(int size) {
            return new UpdateVersionEntity[size];
        }
    };
}
