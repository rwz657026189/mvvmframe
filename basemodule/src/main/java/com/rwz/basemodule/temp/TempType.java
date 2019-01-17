package com.rwz.basemodule.temp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rwz on 2017/7/17.
 */

@IntDef({ITempView.STATUS_LOADING, ITempView.STATUS_NULL, ITempView.STATUS_ERROR, ITempView.STATUS_DISMISS})
@Retention(RetentionPolicy.SOURCE)
public @interface TempType {
}