package com.rwz.basemodule.annotation;

import android.support.annotation.IntDef;

import com.rwz.basemodule.abs.IListView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rwz on 2017/7/19.
 */

@IntDef({IListView.INSERTED, IListView.CHANGED,IListView.REMOVE, IListView.SCROLL_TO_BOTTOM,
        IListView.SCROLL_TO_TOP, IListView.SCROLL_TO_POSITION})
@Retention(RetentionPolicy.SOURCE)
public @interface ViewListUpdateType {
}