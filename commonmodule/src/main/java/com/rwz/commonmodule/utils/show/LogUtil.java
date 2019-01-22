package com.rwz.commonmodule.utils.show;

import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.rwz.commonmodule.config.GlobalConfig;

/**
 * Created by rwz on 2016/12/9.
 */

public class LogUtil {

    public static boolean isDebug = GlobalConfig.showLog;

    private static final String TAG = "rwz";
    private static final String OkHttp = "OkHttp";
    private static final String SPACE = "   ";

    static {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(1)         // (Optional) How many method line to show. Default 2
                .methodOffset(1)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TAG)               // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    /**
     * 打印网络请求日志
     */
    public static void ok(Object... msg) {
        if (isDebug) {
            Logger.t(OkHttp).d(getText(msg));
        }
    }

    /**
     * 打印普通日志
     */
    public static void d(Object... msg) {
        if (isDebug) {
            Logger.t(TAG).d(getText(msg));
        }
    }

    /**
     * 打印错误日志
     */
    public static void e(Object... msg) {
        if (isDebug) {
            Logger.t(TAG).e(getText(msg));
        }
    }

    /**
     * 打印json
     */
    public static void j(String jsonStr) {
        if (isDebug) {
            if(TextUtils.isEmpty(jsonStr))
                return;
            Logger.t(TAG).json(jsonStr);
        }
    }

    private static String getText(Object... msg) {
        if(msg == null)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object o : msg) {
            sb.append(",").append(o);
        }
        return sb.toString().substring(1);
    }

    public static void stackTraces(){
        stackTraces(15, 3);
    }

    public static void stackTraces(int methodCount, int methodOffset) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        String level = "";
        Log.d(TAG, SPACE + "--------- logStackTraces start ----------");
        for (int i = methodCount; i > 0; i--) {
            int stackIndex = i + methodOffset;
            if (stackIndex >= trace.length) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("|")
                    .append(' ')
                    .append(level)
                    .append(trace[stackIndex].getClassName())
                    .append(".")
                    .append(trace[stackIndex].getMethodName())
                    .append(" ")
                    .append(" (")
                    .append(trace[stackIndex].getFileName())
                    .append(":")
                    .append(trace[stackIndex].getLineNumber())
                    .append(")");
            level += "   ";
            Log.d(TAG, SPACE + builder.toString());
        }
        Log.d(TAG, SPACE + "--------- logStackTraces end ----------");
    }

}
