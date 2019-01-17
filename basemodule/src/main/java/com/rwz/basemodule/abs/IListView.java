package com.rwz.basemodule.abs;

import android.support.annotation.LayoutRes;

import com.rwz.basemodule.annotation.ViewListUpdateType;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 15:38
 */

public interface IListView extends IView{

    int INSERTED = 0; //插入
    int CHANGED = 1;  //改变
    int REMOVE = 2;  //移除
    int SCROLL_TO_BOTTOM = 3;  //滚动到底部
    int SCROLL_TO_TOP = 4;  //滚动到底部
    int SCROLL_TO_POSITION = 5;  //滚动到指定位置

    /**
     * 数据加载完成
     * @param isRefresh
     */
    void loadDataComplete(boolean isRefresh);

    /**
     * 是否能加载更多
     * @param enabled
     */
    void setLoadingMoreEnabled(boolean enabled);

    /**
     * 是否能刷新
     * @param enabled
     */
    void setPullRefreshEnabled(boolean enabled);

    /** 刷新 **/
    void autoRefresh();

    /**
     * 更新列表
     */
    void notifyDataSetChanged();

    /**
     * 更新数据
     * @param type
     * @param position
     */
    void updateData(@ViewListUpdateType int type, int position);

    /**
     * 空视图类型
     */
    void setTempType(int type);


    void addHeaderView(@LayoutRes int layoutId);


}
