package com.rwz.basedatabinding.conversion;

import android.databinding.BindingConversion;
import android.graphics.drawable.Drawable;

import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * Created by rwz on 2017/7/27.
 */

public class ImageViewConversion {

    /**
     * 将资源文件转化为drawable , 主要用于xml 相关属性的选择上
     * @param mipmapRes
     * @return
     */
    @BindingConversion
    public static Drawable convertColorToDrawable(int mipmapRes) {
        LogUtil.d("ImageViewConversion", mipmapRes);
        return ResourceUtil.getDrawable(mipmapRes);
    }


}
