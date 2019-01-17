package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.flyco.tablayout.SlidingTabLayout;

/**
 * Created by rwz on 2017/7/24.
 *  https://github.com/H07000223/FlycoTabLayout/blob/master/README_CN.md
 */

public class CommTabLayout extends SlidingTabLayout {
    public CommTabLayout(Context context) {
        super(context);
    }

    public CommTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setupWithViewPager(ViewPager viewPager) {
        setViewPager(viewPager);
    }

    public void setCanScroll(boolean canScroll) {
        //setTabMode(tabs.size() <= 5 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);

    }
}
