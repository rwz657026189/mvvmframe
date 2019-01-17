package com.rwz.commonmodule.help;

import android.text.TextUtils;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lyy on 2015/7/29.
 * 正则表达式类型判断帮助类
 */
public class RegexHelp {

    /**
     * 判断是不是Html链接
     */
    public static boolean isHtml(String str) {
        if(str == null)
            return false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(str);
        return matcher.matches();
    }

    /**
     * 游戏二维码ID获取
     */
    public static String getQrCodeId(String str) {
        //http://www.gaoshouyou.com/youxiku/qrcode/4828.html
        int start = str.indexOf("qrcode/");
        int end = str.indexOf(".html");
        if (str.contains("gaoshouyou.com")) {
            if (start != -1 && end != -1) {
                Log.d("TAG", "id ==> " + str.substring(start + 7, end));
                return str.substring(start + 7, end);
            }
        }
        return "";
    }

    /**
     * 验证邮箱
     */
    public static boolean isEmail(String string) {
        if(string == null)
            return false;
        Pattern p = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        Matcher matcher = p.matcher(string);
        return matcher.matches();
    }

    /**
     * 验证手机号码
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        if(phoneNumber == null)
            return false;
        //应用到国际市场
        return !TextUtils.isEmpty(phoneNumber);
        /*Pattern p = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher matcher = p.matcher(phoneNumber);
        return matcher.matches();*/
    }

    /**
     * 验证Apk
     */
    public static boolean isApk(String fileName) {
        if(fileName == null)
            return false;
        Pattern p = Pattern.compile("^(apk)");
        Matcher m = p.matcher(fileName);
        return m.matches();
    }
}
