package com.rwz.commonmodule.utils.app;

import android.os.Looper;
import android.text.TextUtils;

import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.Collection;

/**
 * Created by rwz on 2017/7/17.
 */

public class Assert {

    private Assert() {
    }

    /**
     * 是否空集合 非空对象为false
     * @param collection
     * @return
     */
    public static boolean isEmptyColl(Object collection) {
        return collection == null || ((collection instanceof Collection) && ((Collection) collection).isEmpty());
    }
    //是否非空集合
    public static boolean isNonEmptyColl(Object coll) {
        return coll != null && ((coll instanceof Collection) && !((Collection) coll).isEmpty());
    }

    /**
     * 所有参数是否存在空对象
     * @param objects
     * @return true 有参数为空
     */
    public static boolean isEmpty(Object... objects) {
        if (objects != null) {
            for (Object o : objects) {
                if (o == null) {
                    LogUtil.e("Assert", "isEmpty" , " is Null");
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 是否存在空字符串
     * @param strings
     * @return
     */
    public static boolean isEmptyStr(String... strings) {
        if (strings != null) {
            for (String o : strings) {
                if (TextUtils.isEmpty(o)) {
                    LogUtil.e("Assert", "isEmptyStr" , o + " is Null");
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 比较字符串是否相等
     * @param s1
     * @param object
     * @return
     */
    public static boolean equal(String s1, Object object) {
        if (s1 == null) {
            return object == null;
        } else {
            return s1.equals(object);
        }
    }

    /**
     * 是否在主线程
     * @return
     */
    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }


    /**
     * 判断一个游标在集合中是否有效
     * @param position
     * @param collection
     * @return
     */
    public static boolean isCursorValid(int position, Collection collection) {
        return collection != null && collection.size() > position && position >= 0;
    }

    public static boolean isCursorValid(int position, int[] ints) {
        return ints != null && ints.length > position && position >= 0;
    }
    public static boolean isCursorValid(int position, String[] strings) {
        return strings != null && strings.length > position && position >= 0;
    }

    /** 是否为空/0 **/
    public static boolean isZero(String num) {
        return TextUtils.isEmpty(num) || "0".equals(num);
    }

}
