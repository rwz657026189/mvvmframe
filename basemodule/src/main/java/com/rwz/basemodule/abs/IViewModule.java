package com.rwz.basemodule.abs;

import android.content.Context;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 15:38
 */

public interface IViewModule<T extends IView> {

    /** 绑定context **/
    void bindContext(Context context);

    void attachWindow(T t);

    void detachWindow();

    void initCompleted();

    void onResume();

}
