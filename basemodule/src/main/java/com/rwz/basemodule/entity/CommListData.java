package com.rwz.basemodule.entity;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;

import java.util.List;

/**
 * Created by rwz on 2018/6/20.
 * count + list组合
 */

public class CommListData<T extends IBaseMulInterface> {

    private String count;

    private List<T> arrayList;

    public String getCount() {
        return count;
    }

    public List<T> getListArr() {
        return arrayList;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setArrayList(List<T> arrayList) {
        this.arrayList = arrayList;
    }

    public CommListData() {
    }

    public CommListData(String count, List<T> arrayList) {
        this.count = count;
        this.arrayList = arrayList;
    }
}
