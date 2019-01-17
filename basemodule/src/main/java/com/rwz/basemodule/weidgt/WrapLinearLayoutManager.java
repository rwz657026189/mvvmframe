package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * Created by rwz on 2018/8/23.
 *  解决 RecyclerView Bug： Inconsistency detected. Invalid view holder adapter
 */

public class WrapLinearLayoutManager extends LinearLayoutManager{
    public WrapLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        }catch (IndexOutOfBoundsException e){
            //手动catch住
            e.printStackTrace();
            LogUtil.e("WrapLinearLayoutManager, onLayoutChildren", e.getMessage());
        }
    }

}
