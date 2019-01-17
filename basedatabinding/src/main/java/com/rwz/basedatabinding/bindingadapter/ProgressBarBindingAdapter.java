package com.rwz.basedatabinding.bindingadapter;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.widget.SeekBar;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;

/**
 * Created by rwz on 2018/8/23.
 */

public class ProgressBarBindingAdapter {

    @BindingAdapter({"android:thumb"})
    public static void setImageViewResource(SeekBar progressBar, @DrawableRes int resource) {
        if(resource != 0) //thumb
            progressBar.setThumb(ResourceUtil.getDrawable(resource));

    }

}
