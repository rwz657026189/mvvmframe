package com.rwz.basemodule.weidgt;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.bigkoo.convenientbanner.ConvenientBanner;

/**
 * Created by rwz on 2017/7/20.
 */

public class CommonBanner extends ConvenientBanner {

    //开始轮询间隔
    private static final int AUTO_TURNING_TIME = 3000;

    public CommonBanner(Context context) {
        super(context);
    }

    public CommonBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        startTurning(AUTO_TURNING_TIME);
        setCanLoop(true);
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopTurning();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        if (visibility != VISIBLE) {
            stopTurning();
        }
        super.onVisibilityChanged(changedView, visibility);
    }
}
