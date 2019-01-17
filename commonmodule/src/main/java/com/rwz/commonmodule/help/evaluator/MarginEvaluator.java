package com.rwz.commonmodule.help.evaluator;

import android.animation.IntEvaluator;
import android.view.View;
import android.view.ViewGroup;

import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * Created by rwz on 2018/7/5.
 */

public class MarginEvaluator extends IntEvaluator {

    public final static int LEFT = 0;
    public final static int TOP = 1;
    public final static int RIGHT = 2;
    public final static int BOTTOM = 3;

    private final ViewGroup.MarginLayoutParams params;
    private final View targetView;
    private final int direction;

    public MarginEvaluator(View targetView, ViewGroup.MarginLayoutParams params, int direction) {
        this.targetView = targetView;
        this.params = params;
        this.direction = direction;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        Integer currValue = super.evaluate(fraction, startValue, endValue);
        if (params != null && targetView != null) {
            switch (direction) {
                case LEFT:
                    params.leftMargin = currValue;
                    break;
                case TOP:
                    params.topMargin = currValue;
                    break;
                case RIGHT:
                    params.rightMargin = currValue;
                    break;
                case BOTTOM:
                    params.bottomMargin = currValue;
                    break;
            }
            targetView.setLayoutParams(params);
            LogUtil.d("MarginEvaluator", "currValue = " + currValue, "direction = " + direction);
        }
        return currValue;
    }
}
