package com.rwz.commonmodule.utils.show;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.utils.app.DensityUtils;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.commonmodule.utils.system.ScreenUtil;


/**
 * Created by rwz on 2017/4/26.
 * 输出手机配置信息
 */

public class ConfigLog {

    public final static int SCREEN_ORIENTATION_VERTICAL = 1; // 屏幕状态：竖屏
    public final static int SCREEN_ORIENTATION_HORIZONTAL = 2; // 屏幕状态：横屏
    private static final String TAG = "ConfigLog";

    private static final boolean isDebug = GlobalConfig.showLog;

    public static void d(Activity activity) {
        if (isDebug) {
            printTop();
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            String  deviceInfo = AndroidUtils.getDeviceModel() + "(" + AndroidUtils.getSystemVersion()+")";
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            int densityDpi = dm.densityDpi;// 屏幕密度，单位为dpi
            float density = dm.density;
            float heightDP = screenWidth / density;
            float widthDP = screenHeight / density;
            float smallestWidthDP;
            if(widthDP < heightDP) {
                smallestWidthDP = widthDP;
            }else {
                smallestWidthDP = heightDP;
            }
            float scale = dm.density;//缩放系数，值为 densityDpi/160
            float fontScale = dm.scaledDensity;//// 文字缩放系数，同scale
            int statusHeight = ScreenUtil.getInstance().getStatusHeight(activity);//状态栏高度
            int screenOrientation = screenHeight > screenWidth ? SCREEN_ORIENTATION_VERTICAL: SCREEN_ORIENTATION_HORIZONTAL;
            String orientation = screenOrientation != SCREEN_ORIENTATION_VERTICAL ? "横屏" : "竖屏";
            String cpu = "";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String[] abis = Build.SUPPORTED_ABIS;
                for (String abi : abis) {
                    cpu += abi+ "   ";
                }
            } else {
                cpu = Build.CPU_ABI;
            }
            //1080 * 1776
            //1440 * 2392   //h_100 = 288,v_100 = 302,
            Context context = BaseApplication.getInstance();
            Log.d(TAG,
                    "h_100 = " + ResourceUtil.getDimen(R.dimen.h_100)+ "," +
                    "v_100 = " + ResourceUtil.getDimen(R.dimen.v_100));
            Log.d(TAG,"║     设备：       " + deviceInfo +
                    "\n║     屏幕宽度：   " + screenWidth +
                    "\n║     屏幕高度：   " + screenHeight +
                    "\n║     尺寸：   " + ScreenUtil.getInstance().getScreenWidth(context) + "*" + ScreenUtil.getInstance().getScreenHeight(context) + ",  h_100 = " + DensityUtils.px2dp(context,ResourceUtil.getDimen(R.dimen.h_100)) + ",  v_100 = " + DensityUtils.px2dp(context,ResourceUtil.getDimen(R.dimen.v_100)) +
                    "\n║     状态栏高度： " + statusHeight +
                    "\n║     屏幕密度：   " + densityDpi +
                    "\n║     屏幕最小宽度：   " + smallestWidthDP +
                    "\n║     文字缩放系数：" + scale +
                    "\n║     文字缩放系数：" + fontScale +
                    "\n║     屏幕方向：" + orientation +
                    "\n║     cpu：" + cpu
            );
            printBottom();
        }
    }

    private static void printTop() {
        Log.d(TAG,"         ");
        Log.d(TAG, "╔════════════════════════════════════════════════════════════════════════════════════════");
    }

    private static void printBottom() {
        Log.d(TAG, "╚════════════════════════════════════════════════════════════════════════════════════════");
        Log.d(TAG,"         ");
    }


}
