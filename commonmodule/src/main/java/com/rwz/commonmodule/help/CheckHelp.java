package com.rwz.commonmodule.help;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.utils.show.LogUtil;


/**
 * Created by rwz on 2017/3/21.
 */

public class CheckHelp {

    private static final int TIME_INTERVAL_TURN = 500;//界面之间跳转的间隔时间
    private static final int TIME_INTERVAL_CLICK = 500;//按钮点击间隔时间
    private static final int TIME_INTERVAL_REQUEST = 500;//网络请求时间间隔

    private static long mLastTurnTime;//记录最后一次的跳转时间
    private static long mLastClickTime;//记录最后一次的点击时间（有时跳转跟点击同步进行，故分开记录）
    private static long mLastRequestTime;//记录最后一次的网络请求时间

    //检查参数是否有效
    public static boolean checkParamValid(String invalidParam,String... params) {
        if (TextUtils.isEmpty(invalidParam) || params == null) {
            LogUtil.e("invalidParam/params is NULL");
            return false;
        }
        for (String param : params) {
            if (TextUtils.isEmpty(param) || invalidParam.equals(param)) {
                return false;
            }
        }
        return true;
    }

    //检查跳转时间间隔
    public static boolean checkTurnTime() {
        long currTurnTime = System.currentTimeMillis();
        boolean canTurn = currTurnTime - mLastTurnTime > TIME_INTERVAL_TURN;
        mLastTurnTime = currTurnTime;
        return canTurn;
    }
    //检查网络请求时间间隔
    public static boolean checkRequestTime() {
        long currTurnTime = System.currentTimeMillis();
        boolean canTurn = currTurnTime - mLastRequestTime > TIME_INTERVAL_REQUEST;
        mLastRequestTime = currTurnTime;
        return canTurn;
    }
    //检查点击时间间隔
    public static boolean checkClickTime() {
        return checkClickTime(null, null);
    }

    //检查点击时间间隔
    public static boolean checkClickTime(Context context) {
        return checkClickTime(context, context.getString(R.string.tips_double_click));
    }
    //检查点击时间间隔,tips:不能点击的提示消息
    private static boolean checkClickTime(Context context, String tips) {
        long currClickTime = System.currentTimeMillis();
        boolean canClick = currClickTime - mLastClickTime > TIME_INTERVAL_CLICK;
        LogUtil.d("checkClickTime", "currClickTime = " + currClickTime, "mLastClickTime = " + mLastClickTime);
        mLastClickTime = currClickTime;
        if (!canClick && !TextUtils.isEmpty(tips) && context != null) {
            Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
        }
        return canClick;
    }
}
