package com.rwz.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.rwz.commonmodule.utils.app.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager的指示器
 */
public class IndicatorLinearLayout extends LinearLayout {
    public final int mBgResId;
    private int mPageIndex;
    private int mPageNum;
    private final List<View> views;

    public IndicatorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.views = new ArrayList();
        this.mBgResId = 0;
    }

    public void setPageNum(int pageNum, int pageIndex) {
        /*if (pageNum == 1) {
            pageNum = 0;
        }*/
        int i;
        if (this.mPageNum > pageNum) {
            for (i = this.mPageNum; i > pageNum; i--) {
                removeView(this.views.get(i - 1));
                this.views.remove(i - 1);
            }
        } else if (this.mPageNum < pageNum) {
            int width = DensityUtils.dp2px(5);
            for (i = this.mPageNum; i < pageNum; i++) {
                View view = new View(getContext());
                LayoutParams params = new LayoutParams(width, width);
                view.setLayoutParams(params);
                params.setMargins(width / 2, 0, width / 2, 0);
                view.setBackgroundResource(this.mBgResId != 0 ? this.mBgResId : R.drawable.shape_indicator_point);
                addView(view);
                views.add(view);
            }
        }
        this.mPageNum = pageNum;
        if (this.mPageIndex < this.mPageNum) {
            (views.get(this.mPageIndex)).setSelected(false);
        }
        if (pageIndex < this.mPageNum) {
            (views.get(pageIndex)).setSelected(true);
        }
        this.mPageIndex = pageIndex;
    }

}