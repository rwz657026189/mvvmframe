package com.rwz.commonmodule.utils.show;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.Assert;
import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 * Created by rwz on 2017/3/9.
 */

public class ToastUtil {

    private static ToastUtil INSTANCE;
    private Toast mToast;

    private ToastUtil() {
    }

    public static ToastUtil getInstance() {
        if (INSTANCE == null) {
            synchronized (ToastUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ToastUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void showShortSingle(final @StringRes int stringRes) {
        showShortSingle(ResourceUtil.getString(stringRes));
    }

    public void showShortSingle(final String string) {
        if (TextUtils.isEmpty(string)) {
            return;
        }
        if (Assert.isMainThread()) {
            getToast().setText(string);
            mToast.show();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    getToast().setText(string);
                    //已在主线程中，可以更新UI
                    mToast.show();
                }
            });
        }
    }

    private Toast getToast() {
        if (mToast == null) {
            mToast = Toast.makeText(BaseApplication.getInstance(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    /**
     * 一定在主线程显示
     * @param stringRes
     */
    public static void showShort(@StringRes int stringRes) {
        if (Assert.isMainThread()) {
            showText(ResourceUtil.getString(stringRes), false);
        } else {
            showTextOnBackgroundThread(ResourceUtil.getString(stringRes), false);
        }
    }
    public static void showLong(@StringRes int stringRes) {
        if (Assert.isMainThread()) {
            showText(ResourceUtil.getString(stringRes), true);
        } else {
            showTextOnBackgroundThread(ResourceUtil.getString(stringRes), true);
        }
    }

    public static void showShort(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (Assert.isMainThread()) {
            showText(text, false);
        } else {
            showTextOnBackgroundThread(text, false);
        }
    }

    public static void showLong(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (Assert.isMainThread()) {
            showText(text, true);
        } else {
            showTextOnBackgroundThread(text, true);
        }
    }



    private static void showTextOnBackgroundThread(final String text, final boolean isLong) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                //已在主线程中，可以更新UI
                showText(text, isLong);
            }
        });
    }

    private static void showText(String text, boolean isLong) {
        if (isLong) {
            Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        }
    }
}
