package com.rwz.basemodule.manager;

import android.content.Context;

/**
 * Created by rwz on 2017/8/24.
 * 推送管理者
 */

public class PushManager {

    public static final String TAG = "PushManager";

    private static PushManager instance;

    public static String sDeviceToken;//友盟设备唯一id(与服务器账号绑定, 登录注册传递)

    public static void init() {
        instance = new PushManager();
    }

    public static PushManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("must call init() at Application");
        }
        return instance;
    }

    /**
     * 每个activity中需要调用
     * @param context
     */
    public void onCreate(Context context) {
    }

}
