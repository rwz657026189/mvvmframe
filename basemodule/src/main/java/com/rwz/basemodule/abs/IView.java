package com.rwz.basemodule.abs;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 15:38
 */

public interface IView {

    //结束当前activity
    int FINISH_ATY = 0;
    //显示dialog
    int SHOW_DIALOG = 1;
    //请求完成更新数据
    int UPDATE_DATA = 2;
    //显示正在加载中dialog
    int SHOW_LOADING = 3;
    //消失正在加载中dialog
    int DISMISS_LOADING = 4;
    //更新状态
    int UPLOAD_EDIT_STATE = 5;
    //是否禁用触摸屏幕
    int FORBID_TOUCH_SCREEN = 6;
    /**
     * 通信
     */
    void onPostEvent(@PostEventType int type, Object params);


}
