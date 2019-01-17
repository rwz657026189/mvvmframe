package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by rwz on 2017/7/19.
 * 可禁止滑动的ViewPager
 */

public class CommonViewPager extends ViewPager {

    private boolean canScroll = true;//是否可以滑动


    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public CommonViewPager(Context context) {
        super(context);
    }

    public CommonViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return canScroll && super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return canScroll && super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

}
