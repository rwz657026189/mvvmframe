package com.rwz.commonmodule.utils.app;

import android.app.Application;
import android.content.Context;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.base.BaseApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rwz on 2018/8/8.
 *
 * 日期格式化
 *
 */
public class DateUtil {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String CURR_DAY = "HH:mm";
    public static final String CURR_YEAR = "MM-dd";
    public static final String CURR_YEAR_DAY = "MM-dd HH:mm";
    public static final String LONG_LONG_AGO = "yyyy-MM-dd";

    public static String parseDate(long time) {
        long minuteDx = (System.currentTimeMillis() - time) / 1000 / 60;
        Context context = BaseApplication.getInstance();
        if (minuteDx < 3) { //3分钟
            return ResourceUtil.getString(R.string.at_once);
        } else if (minuteDx < 60) { //1小时内
            return String.format(ResourceUtil.getString(R.string.before_minute_default), minuteDx);
        } else if(minuteDx < 60 * 24){ //一天之内
            SimpleDateFormat bartDateFormat = new SimpleDateFormat(CURR_YEAR_DAY, Locale.US);
            Date date = new Date(time);
            return bartDateFormat.format(date);
        } else if (minuteDx < 60 * 24 * 365) {//一年之内
            SimpleDateFormat bartDateFormat = new SimpleDateFormat(CURR_YEAR_DAY, Locale.US);
            Date date = new Date(time);
            return bartDateFormat.format(date);
        } else { //一年之前
            SimpleDateFormat bartDateFormat = new SimpleDateFormat(LONG_LONG_AGO, Locale.US);
            Date date = new Date(time);
            return bartDateFormat.format(date);
        }
    }

    /** 格式化时间 **/
    public static String formatDate(long time) {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN, Locale.US);
        Date date = new Date(time);
        return bartDateFormat.format(date);
    }

}
