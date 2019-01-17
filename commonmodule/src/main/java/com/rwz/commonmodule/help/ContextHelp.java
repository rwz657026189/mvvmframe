package com.rwz.commonmodule.help;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

/**
 * Created by rwz on 2017/7/31.
 */

public class ContextHelp {

    /**
     * 获取一个activity 实例
     * @param context
     * @return
     */
    public static AppCompatActivity getAppCompActivity(Context context) {
        if (context == null) return null;
        if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        } else if (context instanceof ContextThemeWrapper) {
            return getAppCompActivity(((ContextThemeWrapper) context).getBaseContext());
        }
        return null;
    }

}
