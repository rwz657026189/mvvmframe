package com.rwz.basemodule.help;

import android.text.TextUtils;

import com.rwz.commonmodule.config.GlobalConfig;

/**
 * Created by rwz on 2017/7/31.
 */

public class ExceptionHelp {

    private static final boolean isChecked = GlobalConfig.isDebug;

    public static void checkNull(Object object) {
        if (isChecked && object == null) {
            throw new NullPointerException(object + "can't be null");
        }
    }

    public static void checkTurnParams(Object object) {
        if (isChecked && object == null) {
            throw new NullPointerException("you can't turn with null params");
        }
    }

    public static void checkTurnParams(Object... object) {
        if (isChecked) {
            String tips = "you can't turn with null params";
            if (object == null) {
                throw new NullPointerException(tips);
            } else {
                for (Object o : object) {
                    checkTurnParams(o);
                    if (o instanceof String && TextUtils.isEmpty((String)o)) {
                        throw new NullPointerException(tips);
                    }
                }
            }
        }
    }

}
