package com.rwz.basemodule.download;

import android.os.Parcel;
import android.os.Parcelable;

import com.rwz.basemodule.entity.NotificationEntity;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 18:18
 */

public class DownloadConfig implements Parcelable {
    //无效id
    public static final long INVALID_ID = -1;

    //标识任务的唯一id
    private long id = INVALID_ID;
    //下载后文件名
    private String fileName;
    //下载地址
    private String url;
    //保存目录
    private String savePath;
    //通知栏实体类（null 则不显示）
    private NotificationEntity entity;
    //下载监听
    private IFileDownloadListener listener;

    public DownloadConfig(String fileName, String url, String savePath, NotificationEntity entity, IFileDownloadListener listener) {
        this.fileName = fileName;
        this.url = url;
        this.savePath = savePath;
        this.entity = entity;
        this.listener = listener;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public NotificationEntity getEntity() {
        return entity;
    }

    public void setEntity(NotificationEntity entity) {
        this.entity = entity;
    }

    public IFileDownloadListener getListener() {
        return listener;
    }

    public void setListener(IFileDownloadListener listener) {
        this.listener = listener;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.fileName);
        dest.writeString(this.url);
        dest.writeString(this.savePath);
        dest.writeParcelable(this.entity, flags);
    }

    protected DownloadConfig(Parcel in) {
        this.id = in.readLong();
        this.fileName = in.readString();
        this.url = in.readString();
        this.savePath = in.readString();
        this.entity = in.readParcelable(NotificationEntity.class.getClassLoader());
    }

    public static final Creator<DownloadConfig> CREATOR = new Creator<DownloadConfig>() {
        @Override
        public DownloadConfig createFromParcel(Parcel source) {
            return new DownloadConfig(source);
        }

        @Override
        public DownloadConfig[] newArray(int size) {
            return new DownloadConfig[size];
        }
    };
}
