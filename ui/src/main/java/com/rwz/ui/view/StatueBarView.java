package com.rwz.ui.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.rwz.commonmodule.utils.system.ScreenUtil;


/**
 * Created by rwz on 2017/2/24.
    同状态栏高度的色块 注意4.4以下高度为0
 */

public class StatueBarView extends View {

    public StatueBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ? ScreenUtil.getInstance().getStatusHeight(getContext()): 0;
        invalidate();
    }
}
