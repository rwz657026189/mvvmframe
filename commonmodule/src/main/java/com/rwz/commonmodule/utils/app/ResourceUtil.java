package com.rwz.commonmodule.utils.app;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.rwz.commonmodule.base.BaseApplication;

/**
 * Created by rwz on 2017/3/9.
 * @function 资源获取工具类
 */

public class ResourceUtil {

    private ResourceUtil() {
        throw new RuntimeException("不能被实例化");
    }

    /**
     * 获取字符串
     * @param stringRes
     * @return
     */
    public static String getString(@StringRes int stringRes) {
        return stringRes == 0 ? null : BaseApplication.getInstance().getString(stringRes);
    }

    /**
     * 获取颜色值
     * @param colorRes
     * @return
     */
    public static int getColor(@ColorRes int colorRes) {
        return  colorRes == 0 ? Color.WHITE : ContextCompat.getColor(BaseApplication.getInstance(), colorRes);
    }
    /**
     * 获取尺寸值
     * @param dimenRes
     * @return
     */
    public static int getDimen(@DimenRes int dimenRes) {
        return dimenRes == 0 ? 0 : (int) BaseApplication.getInstance().getResources().getDimension(dimenRes);
    }

    /**
     * 获取数值
     * @param value
     * @return
     */
    public static int getInteger(@IntegerRes int value) {
        return value == 0 ? 0 : BaseApplication.getInstance().getResources().getInteger(value);
    }

    /**
     * 获取数组
     * @param intArr
     * @return
     */
    public static int[] getIntArr(@ArrayRes int intArr) {
        return intArr == 0 ? null : BaseApplication.getInstance().getResources().getIntArray(intArr);
    }
    /**
     * 获取数组
     * @param stringArr
     * @return
     */
    public static String[] getStringArr(@ArrayRes int stringArr) {
        return stringArr == 0 ? null : BaseApplication.getInstance().getResources().getStringArray(stringArr);
    }

    public static Drawable getDrawable(@DrawableRes int drawableRes) {
        return drawableRes == 0 ? null : ContextCompat.getDrawable(BaseApplication.getInstance(), drawableRes);
    }

    public static Drawable getDrawable(@DrawableRes int unCheckDrawableRes,@DrawableRes int checkDrawableRes, boolean isChecked) {
        return getDrawable(isChecked ? checkDrawableRes : unCheckDrawableRes);
    }

    /**
     * 根据资源文件获取其尺寸
     * @param drawableRes
     * @return
     */
    public static int[] getImgResSize(@DrawableRes int drawableRes) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), drawableRes, opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }



}
