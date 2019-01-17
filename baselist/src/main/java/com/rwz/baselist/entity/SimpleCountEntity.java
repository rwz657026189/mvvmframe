package com.rwz.baselist.entity;

import android.databinding.ObservableField;
import android.support.annotation.LayoutRes;

/**
 * Created by rwz on 2016/12/16.
 * 简单实体类, 实用无逻辑的基本布局
 */

public class SimpleCountEntity extends BaseListEntity{

    @LayoutRes
    private int mLayoutRes; //默认0,将作为空item
    private ObservableField data;

    public SimpleCountEntity(@LayoutRes int mLayoutRes) {
        this.mLayoutRes = mLayoutRes;
    }

    public SimpleCountEntity(int mLayoutRes, ObservableField data) {
        this.mLayoutRes = mLayoutRes;
        this.data = data;
    }

    @Override
    public int getItemLayoutId() {
        return mLayoutRes;
    }

    public ObservableField getData() {
        return data;
    }

    public void setData(ObservableField data) {
        this.data = data;
    }
}
