package com.rwz.ui.float_view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.system.ScreenUtil;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowManager {
    private static final String TAG = "GSYFloatWindowManager";

    private static volatile FloatWindowManager instance;

    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private FloatView floatView = null;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    private void showWindow(Context context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }
        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        int screenWidth = ScreenUtil.getInstance().getScreenWidth(BaseApplication.getInstance());
        int screenHeight = ScreenUtil.getInstance().getScreenHeight(BaseApplication.getInstance());
        mParams = getLayoutParams(context, screenWidth, screenHeight);

//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(R.drawable.app_icon);
        floatView = new FloatView(context);
        floatView.initParams(windowManager, mParams);
        windowManager.addView(floatView, mParams);
    }

    private WindowManager.LayoutParams getLayoutParams(Context context, int screenWidth, int screenHeight) {
        WindowManager.LayoutParams  params = new WindowManager.LayoutParams();
        params.packageName = context.getPackageName();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.START | Gravity.TOP;
        params.x = screenWidth - FloatView.MENU_MIN_WIDTH;
        params.y = screenHeight / 2;
        return params;
    }

    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }
        isWindowDismiss = true;
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

}

