package com.rwz.basemodule.entity.turnentity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.rwz.basemodule.R;
import com.rwz.basemodule.inf.CommBiConsumer;
import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 * Created by rwz on 2017/3/13.
 * 消息对话框传递实体类
 */

public class MsgDialogTurnEntity implements Parcelable {

    private String title;
    private String msg;
    private String hint;
    private int requestCode;
    private CommBiConsumer<MsgDialogTurnEntity, Boolean> listener;//dialog点击监听
    private String enterText;  //确认文字
    private String cancelText; //取消文字(为空 则 只显示单按钮)
    private boolean cancelable;//设置外部区域是否可以取消
    private Bundle params;     //参数

    public void setListener(CommBiConsumer<MsgDialogTurnEntity, Boolean> listener) {
        this.listener = listener;
    }

    public CommBiConsumer<MsgDialogTurnEntity, Boolean> getListener() {
        return listener;
    }

    public MsgDialogTurnEntity(String title, String msg, int requestCode) {
        this.title = title;
        this.msg = msg;
        this.requestCode = requestCode;
        enterText = ResourceUtil.getString(R.string.enter);
        cancelText = ResourceUtil.getString(R.string.cancel);
        cancelable = true;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getHint() {
        return hint;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getEnterText() {
        return enterText;
    }

    public void setEnterText(String enterText) {
        this.enterText = enterText;
    }

    public String getCancelText() {
        return cancelText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public String getTitle() {
        return title;
    }

    public String getMsg() {
        return msg;
    }

    public MsgDialogTurnEntity() {
    }

    public static Creator<MsgDialogTurnEntity> getCREATOR() {
        return CREATOR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MsgDialogTurnEntity that = (MsgDialogTurnEntity) o;

        return requestCode == that.requestCode;

    }

    @Override
    public int hashCode() {
        return requestCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.msg);
        dest.writeInt(this.requestCode);
        dest.writeString(this.enterText);
        dest.writeString(this.cancelText);
        dest.writeByte(this.cancelable ? (byte) 1 : (byte) 0);
        dest.writeBundle(this.params);
    }

    protected MsgDialogTurnEntity(Parcel in) {
        this.title = in.readString();
        this.msg = in.readString();
        this.requestCode = in.readInt();
        this.enterText = in.readString();
        this.cancelText = in.readString();
        this.cancelable = in.readByte() != 0;
        this.params = in.readBundle(getClass().getClassLoader());
    }

    public static final Creator<MsgDialogTurnEntity> CREATOR = new Creator<MsgDialogTurnEntity>() {
        @Override
        public MsgDialogTurnEntity createFromParcel(Parcel source) {
            return new MsgDialogTurnEntity(source);
        }

        @Override
        public MsgDialogTurnEntity[] newArray(int size) {
            return new MsgDialogTurnEntity[size];
        }
    };
}
