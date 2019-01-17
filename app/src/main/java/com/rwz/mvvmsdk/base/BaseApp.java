package com.rwz.mvvmsdk.base;

import com.rwz.basemodule.manager.PushManager;
import com.rwz.basemodule.manager.StatisticsManager;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.network.net.RetrofitManager;
import com.rwz.web.tencent.TencentWebViewProxy;

public class BaseApp extends BaseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        //推送
        PushManager.init();
        //腾讯H5内核
        TencentWebViewProxy.init(this);
        //统计初始化
        StatisticsManager.init(this);
        //网络通信组件
        RetrofitManager.init(this, GlobalConfig.MAIN_HOST, null);
    }
}
