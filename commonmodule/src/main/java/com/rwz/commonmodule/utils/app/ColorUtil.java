package com.rwz.commonmodule.utils.app;

import android.animation.ArgbEvaluator;

/**
 * Created by rwz on 2018/7/23.
 */

public class ColorUtil {

    /**
     * 根据首尾色值、进度获取一个中间过渡色值 参考{@link ArgbEvaluator#evaluate(float, Object, Object)}
     * @param startColor    开始颜色
     * @param endColor      结束颜色
     * @param progress      进度 （0f ~ 1f）
     */
    public static int getTransitionColor(int startColor, int endColor, float progress) {
        int startA = (startColor >> 24) & 0xff;
        int startR = (startColor >> 16) & 0xff;
        int startG = (startColor >> 8) & 0xff;
        int startB = startColor & 0xff;

        int endA = (endColor >> 24) & 0xff;
        int endR = (endColor >> 16) & 0xff;
        int endG = (endColor >> 8) & 0xff;
        int endB = endColor & 0xff;

        return ((startA + (int)(progress * (endA - startA))) << 24) |
                ((startR + (int)(progress * (endR - startR))) << 16) |
                ((startG + (int)(progress * (endG - startG))) << 8) |
                ((startB + (int)(progress * (endB - startB))));
    }

}
