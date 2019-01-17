package com.rwz.basemodule.download;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rwz on 2017/8/29.
 */

@Deprecated
public class FileDownloadListenerImpl implements IFileDownloadListener, Parcelable {

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public FileDownloadListenerImpl() {
    }

    protected FileDownloadListenerImpl(Parcel in) {
    }

    public static final Parcelable.Creator<FileDownloadListenerImpl> CREATOR = new Parcelable.Creator<FileDownloadListenerImpl>() {
        @Override
        public FileDownloadListenerImpl createFromParcel(Parcel source) {
            return new FileDownloadListenerImpl(source);
        }

        @Override
        public FileDownloadListenerImpl[] newArray(int size) {
            return new FileDownloadListenerImpl[size];
        }
    };
}
