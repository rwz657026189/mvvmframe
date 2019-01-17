package com.rwz.baselist.entity;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 14:33
 */

public abstract class BaseListEntity implements IBaseMulInterface{

    public int spanCount = 1;

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }
}
