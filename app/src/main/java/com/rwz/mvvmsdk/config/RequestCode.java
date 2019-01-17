package com.rwz.mvvmsdk.config;

import com.rwz.basemodule.common.VersionUpdateDialog;

public interface RequestCode {

    //基础值, 避免与其他类型重复
    int BASE_VALUE = 6000;

    int SINGLE = BASE_VALUE; //列表页仅有一条请求(不论有其他任何请求，都应该单独新增一个类型)


}
