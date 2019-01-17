package com.rwz.baselist.entity;

import android.support.annotation.LayoutRes;

/**
 * Created by rwz on 2016/12/16.
 * 简单实体类, 实用无逻辑的基本布局
 */

public class SimpleEntity extends BaseListEntity{

    @LayoutRes int mLayoutRes; //默认0,将作为空item

    private Object data;        //数据
    private boolean bool;

    public SimpleEntity(@LayoutRes int mLayoutRes) {
        this.mLayoutRes = mLayoutRes;
    }

    public SimpleEntity(@LayoutRes int mLayoutRes, Object data) {
        this.mLayoutRes = mLayoutRes;
        this.data = data;
    }

    public SimpleEntity(int mLayoutRes, Object data, boolean bool) {
        this.mLayoutRes = mLayoutRes;
        this.data = data;
        this.bool = bool;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public boolean isBool() {
        return bool;
    }

    @Override
    public int getItemLayoutId() {
        return mLayoutRes;
    }

    public Object getData() {
        return data;
    }




}
