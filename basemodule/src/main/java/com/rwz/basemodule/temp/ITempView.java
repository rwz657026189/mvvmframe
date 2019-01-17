package com.rwz.basemodule.temp;

/**
 * Created by rwz on 2017/7/17.
 */

public interface ITempView {

    /**
     * 正在加载中
     */
    int STATUS_LOADING = 3538;
    /**
     * 显示空数据
     */
    int STATUS_NULL = 3539;
    /**
     * 显示错误视图
     */
    int STATUS_ERROR = 3540;
    /**
     * 移除空视图
     */
    int STATUS_DISMISS = 3541;


    void setTempType(@TempType int type);

}
