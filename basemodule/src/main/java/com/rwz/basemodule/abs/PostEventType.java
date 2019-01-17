package com.rwz.basemodule.abs;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rwz on 2017/7/18.
 */

@IntDef({IView.FINISH_ATY, IView.SHOW_DIALOG, IView.UPDATE_DATA,
IView.SHOW_LOADING,IView.DISMISS_LOADING, IView.UPLOAD_EDIT_STATE,  IView.FORBID_TOUCH_SCREEN})
@Retention(RetentionPolicy.SOURCE)
public @interface PostEventType {
}