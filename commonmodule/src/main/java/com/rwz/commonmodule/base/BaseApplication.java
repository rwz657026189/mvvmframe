package com.rwz.commonmodule.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 16:13
 * 自定义Application 需要继承BaseApplication 或者调用init()初始化
 */
public class BaseApplication extends Application{

    private static Context INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

    public static void init(Application context) {
        INSTANCE = context;
    }

    /**
     * 分割 Dex 支持
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getInstance() {
        return INSTANCE;
    }


}
