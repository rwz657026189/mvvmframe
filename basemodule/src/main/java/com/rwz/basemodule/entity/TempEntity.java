package com.rwz.basemodule.entity;

import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;

import com.rwz.baselist.entity.BaseListEntity;
import com.rwz.basemodule.R;
import com.rwz.basemodule.temp.ITempView;
import com.rwz.basemodule.temp.TempType;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.NetUtils;
import com.rwz.commonmodule.utils.app.ResourceUtil;


/**
 * Created by rwz on 2017/3/13.
 * 空视图基类
 */

public class TempEntity extends BaseListEntity {

    private String nullTipsStr = ResourceUtil.getString(R.string.null_data);
    private String errorTipsStr  = ResourceUtil.getString(R.string.load_error);
    private String nullBtnStr = ResourceUtil.getString(R.string.click_retry);
    private String errorBtnStr = ResourceUtil.getString(R.string.click_retry);
    private boolean isShowNullBtn = false;
    private boolean isShowErrorBtn = true;
    private Drawable nullImgRes;    // = R.mipmap.yemianjiazaishibai;
    private Drawable errorImgRes; // = R.mipmap.yemianjiazaishibai;
    public ObservableInt type = new ObservableInt();

    public TempEntity() {
        this(ITempView.STATUS_LOADING);
    }

    public TempEntity(Drawable nullImgRes, Drawable errorImgRes) {
        this(ITempView.STATUS_LOADING);
        this.nullImgRes = nullImgRes;
        this.errorImgRes = errorImgRes;
    }

    public TempEntity(@TempType int type) {
        this.type.set(type);
        nullImgRes = ResourceUtil.getDrawable(R.mipmap.no_data);
        errorImgRes = ResourceUtil.getDrawable(R.mipmap.no_nerwork);
    }

    public String getNullTipsStr() {
        return nullTipsStr;
    }

    public String getErrorTipsStr() {
        return NetUtils.isConnected(BaseApplication.getInstance()) ? errorTipsStr : ResourceUtil.getString(R.string.no_net_check_retry);
    }

    public boolean isNoNet(int type) {
        return type == ITempView.STATUS_ERROR && !NetUtils.isConnected(BaseApplication.getInstance());
    }

    public String getNullBtnStr() {
        return nullBtnStr;
    }

    public String getErrorBtnStr() {
        return errorBtnStr;
    }

    public void setType(int type) {
        this.type.set(type);
    }

    public boolean getShowNullBtn(int type) {
        return isShowNullBtn && (type == ITempView.STATUS_NULL);
    }

    public boolean getShowErrorBtn(int type) {
//        LogUtil.d("getShowErrorBtn", "isShowErrorBtn = " + isShowErrorBtn, "type = " + type);
        return isShowErrorBtn && (type == ITempView.STATUS_ERROR);
    }

    public Drawable getNullImgRes() {
        return nullImgRes;
    }

    public Drawable getErrorImgRes() {
        return errorImgRes;
    }

    public void setNullTipsStr(String nullTipsStr) {
        this.nullTipsStr = nullTipsStr;
    }

    public void setErrorTipsStr(String errorTipsStr) {
        this.errorTipsStr = errorTipsStr;
    }

    public void setNullBtnStr(String nullBtnStr) {
        this.nullBtnStr = nullBtnStr;
    }

    public void setErrorBtnStr(String errorBtnStr) {
        this.errorBtnStr = errorBtnStr;
    }

    public void setShowNullBtn(boolean showNullBtn) {
        isShowNullBtn = showNullBtn;
    }

    public void setShowErrorlBtn(boolean showErrorBtn) {
        isShowErrorBtn = showErrorBtn;
    }

    public void setNullImgRes(Drawable nullImgRes) {
        this.nullImgRes = nullImgRes;
    }

    public void setErrorImgRes(Drawable errorImgRes) {
        this.errorImgRes = errorImgRes;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.layout_temp;
    }
}
