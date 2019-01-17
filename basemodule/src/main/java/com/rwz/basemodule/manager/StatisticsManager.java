package com.rwz.basemodule.manager;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.meituan.android.walle.WalleChannelReader;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 * Created by rwz on 2017/8/30.
 * 统计管理类
 * http://doc.talkingdata.com/posts/21
 */

public class StatisticsManager {

    public static void init(Context context) {
        if (GlobalConfig.OPEN_STATISTICS) {
        }
    }

    /**
     * 统计自定义事件
     * @param name
     */
    public static void onEvent(@StringRes int name) {
        if (GlobalConfig.OPEN_STATISTICS && name != 0) {
            onEvent(ResourceUtil.getString(name));
        }
    }
    public static void onEvent(String name) {
        if (GlobalConfig.OPEN_STATISTICS && !TextUtils.isEmpty(name)) {
            onEvent(name, null);
        }
    }

    public static void onEvent(String name, String label) {
    }

    /**
     * 统计页面停留时间
     * @param name
     */
    public static void onPageStart(@StringRes int name) {
        if (name != 0) {
            onPageStart(ResourceUtil.getString(name));
        }
    }
    public static void onPageStart(String name) {
    }

    public static void onPageEnd(@StringRes int name) {
        if (name != 0) {
            onPageEnd(ResourceUtil.getString(name));
        }
    }
    public static void onPageEnd(String name) {
    }

    /**
     * 获取渠道标识
     */
    public static String getChannel() {
        String channel = WalleChannelReader.getChannel(BaseApplication.getInstance());
        if(TextUtils.isEmpty(channel))//默认值
            channel = GlobalConfig.DEFAULT_CHANNEL;
        return channel;
    }


}
