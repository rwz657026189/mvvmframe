package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 15:56
 */

public class CommonRecyclerView extends RecyclerView{

    public CommonRecyclerView(Context context) {
        super(context);
        init();
    }

    public CommonRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
    }

    /**
     * 获取第一个可见item位置
     * @return
     */
    public int findFirstVisibleItemPosition() {
        int firstVisibleItemPosition = -1;
        LayoutManager manager = getLayoutManager();
        if (manager == null) {
        }else if (manager instanceof LinearLayoutManager) {
            firstVisibleItemPosition = ((LinearLayoutManager) manager).findFirstVisibleItemPosition();
        } else if (manager instanceof GridLayoutManager) {
            firstVisibleItemPosition =  ((GridLayoutManager) manager).findFirstVisibleItemPosition();
        }
        LogUtil.d("CommonRecyclerView", "firstVisibleItemPosition = " + firstVisibleItemPosition);
        return firstVisibleItemPosition;
    }

    public int findLastVisibleItemPosition() {
        int lastVisibleItemPosition = -1;
        LayoutManager manager = getLayoutManager();
        if (manager == null) {
        }else if (manager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
        } else if (manager instanceof GridLayoutManager) {
            lastVisibleItemPosition =  ((GridLayoutManager) manager).findLastVisibleItemPosition();
        }
        LogUtil.d("CommonRecyclerView", "lastVisibleItemPosition = " + lastVisibleItemPosition);
        return lastVisibleItemPosition;
    }

    /**
     * 根据位置获取该View
     * @param position
     * @return
     */
    public View getItemView(int position) {
        int firstVisibleItemPosition = findFirstVisibleItemPosition();
        int lastVisibleItemPosition = findLastVisibleItemPosition();
        if (firstVisibleItemPosition != -1 && lastVisibleItemPosition != -1 && position >= firstVisibleItemPosition && position <= lastVisibleItemPosition) {
            int index = position - firstVisibleItemPosition;
            LogUtil.d("CommonRecyclerView","getItemView", "index = " + index, "position = " + position,
                    "firstVisibleItemPosition = " + firstVisibleItemPosition, getChildAt(0), getChildAt(1));
            return getChildAt(index);
        }
        return null;
    }


}
