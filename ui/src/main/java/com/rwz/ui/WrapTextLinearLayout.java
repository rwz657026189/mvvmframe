package com.rwz.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rwz on 2017/9/20.
 * 包含TextView的LinearLayoutCompat (且根据布局的长度对TextView进行处理)
 */

public class WrapTextLinearLayout extends LinearLayoutCompat{

    public WrapTextLinearLayout(Context context) {
        super(context);
    }

    public WrapTextLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapTextLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        if (count > 0) {
            TextView spTextView = null;
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                if (view instanceof TextView) {
                    spTextView = (TextView) view;
                    break;
                }
            }
            if (spTextView != null) {
                int size = MeasureSpec.getSize(widthMeasureSpec);
                spTextView.measure(widthMeasureSpec, heightMeasureSpec);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
