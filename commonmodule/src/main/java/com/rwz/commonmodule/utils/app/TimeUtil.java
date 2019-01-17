package com.rwz.commonmodule.utils.app;

/**
 * Created by rwz on 2017/8/9.
 */

public class TimeUtil {


    //时、分、秒
    public static final String[] FORMAT1 = {"\"","\"","\""};
    public static final String[] FORMAT2 = {"h","m","s"};
    public static final String[] FORMAT3 = {"时","分","秒"};
    public static final String[] FORMAT4 = {":",":",""};
    public static final String[] FORMAT5 = {":",":",""};//默认显示分、秒，不足时再显示小时

    //分、秒、毫秒
    public static final String[] FORMAT_PRECISE = {":",":",""};
    public static final String[] FORMAT_PRECISE2 = {"m","s","ms"};

    private static final int SYSTEM = 60;//进制
    private static final int MILL_SYSTEM = 1000;//进制

    /**
     * 格式化时间：时、分、秒
     * @param time 时间（s）
     * @param FORMAT
     * @return
     */
    public static String formatTime(long time, String[] FORMAT) {
        return formatTime(time, FORMAT, false);
    }

    /**
     * 格式化时间：时、分、秒
     * @param seconds 秒
     * @param FORMAT
     * @param isComplement 是否补全
     * @return
     */
    public static String formatTime(long seconds, String[] FORMAT, boolean isComplement) {
        if (seconds >= 0 && FORMAT != null && FORMAT.length == 3) {
            long hour = seconds / SYSTEM / SYSTEM;
            long minute =  seconds / SYSTEM % SYSTEM;
            long second = seconds % SYSTEM;
            String hourText = FORMAT == TimeUtil.FORMAT5 && hour <= 0 ? "" : judgeSystem(hour, FORMAT[0], isComplement);
            return  hourText +
                    judgeSystem(minute, FORMAT[1], isComplement) +
                    judgeSystem(second, FORMAT[2], isComplement);
        }
        return "";
    }

    //格式化时间：时、分、秒
    private static String judgeSystem(long value, String unit, boolean isComplement) {
        String nullStr = "";
        String one = "0";
        String one2 = "00";
        if (value == 0) {
            if (isComplement) {
                return one2 + unit;
            } else {
                return nullStr;
            }
        } else if (value < 10) {
            if (isComplement) {
                return one + value + unit;
            } else {
                return String.valueOf(value) + unit;
            }
        } else  {
            return String.valueOf(value) + unit;
        }
    }

    /**
     *格式化时间：时(未完成)、分、秒 、毫秒
     * @param millSeconds
     * @param FORMAT
     * @param millSecondSystem 毫秒保留几位
     * @param isComplement
     * @return
     */
    public static String formatTimePrecise (long millSeconds, String[] FORMAT, int millSecondSystem, boolean isComplement) {
        if (millSeconds >= 0 && FORMAT != null) {
//            long hour = millSeconds /MILL_SYSTEM / SYSTEM / SYSTEM;
            long minute =  millSeconds  /MILL_SYSTEM / SYSTEM % SYSTEM;
            long second = millSeconds  /MILL_SYSTEM % SYSTEM;
            long millSecond = millSeconds % MILL_SYSTEM;
            if (millSecondSystem == 2) {
                millSecond = millSecond * SYSTEM / MILL_SYSTEM;
            }
            if (FORMAT.length == 3) {//分、秒、毫秒
                return  judgeSystem(minute, FORMAT[0], isComplement) +
                        judgeSystem(second, FORMAT[1], isComplement) +
                        judgeSystem(millSecond, FORMAT[2], isComplement);
            } else if (FORMAT.length == 4) {//时、分、秒、毫秒
                //TODO
            }
        }
        return "";
    }

}
