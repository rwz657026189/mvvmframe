package com.rwz.baselist.decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.rwz.commonmodule.utils.app.DensityUtils;


/**
 * Created by rwz on 2017/3/21.
 * 分割线
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int mSpanCount;

    public GridItemDecoration(int space) {
        this.space = space;
    }

    public GridItemDecoration() {
        this(DensityUtils.dp2px(10));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.left = space / 2;
        outRect.right = space / 2;
//        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        int currPos = parent.getChildLayoutPosition(view);
        int count = getSpanCount(parent);
        if (currPos % count == 0) {// 表示每行第一个
            outRect.left = space;
        } else if (currPos % count == count - 1) {//表示每行最后一个
            outRect.right = space;
        }
        int childCount = parent.getAdapter().getItemCount();
        // 如果是是第一个(刷新的部分) 或者 最后一行，则不需要绘制底部
        if (!isLastRaw(parent, currPos, count, childCount) && currPos != 0) {
            outRect.bottom = space;
        }
    }

    /**
     * 获取列数
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        if (mSpanCount != 0) {
            return mSpanCount;
        }
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            mSpanCount = ((GridLayoutManager)manager).getSpanCount();
        }else if (manager instanceof StaggeredGridLayoutManager) {
            mSpanCount = ((StaggeredGridLayoutManager)manager).getSpanCount();
        }else{
            mSpanCount = 1;
        }
        return mSpanCount;
    }

    /**
     * 是否是最后一行
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount)
    {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager)
        {
            childCount = childCount - childCount % spanCount;
            return pos >= childCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager)
        {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL)
            {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                return pos >= childCount;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount == 0;
            }
        }
        return false;
    }

}