package com.rwz.mvvmsdk.entity.test;

import com.rwz.baselist.entity.BaseListEntity;
import com.rwz.mvvmsdk.R;

public class TestItemEntity extends BaseListEntity{

    private String title;
    private String url;

    public TestItemEntity(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_main_test;
    }


}
