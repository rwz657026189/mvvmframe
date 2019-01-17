package com.rwz.basemodule.bindingadapter;

import android.databinding.BindingAdapter;

import com.rwz.basemodule.base.BaseViewModule;
import com.rwz.basemodule.entity.ItemEntity;
import com.rwz.basemodule.entity.wrap.WrapList;
import com.rwz.basemodule.weidgt.DivideEqualLinearLayout;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.List;


/**
 * Created by rwz on 2017/3/15.
 */

public final class LinearLayoutAdapter {

    //    @BindingAdapter(value = {"url","scale","isCenterCrop","viewWidth","placeHolder","errorDrawable","circle","rounded","blur","square"}, requireAll=false)
    @BindingAdapter(value = {"setLinearLayoutData","viewModule", "divideEqual","list","childResId"}, requireAll=false)
    public static void setLinearLayoutData(DivideEqualLinearLayout view, WrapList wrapList , BaseViewModule viewModule, boolean divideEqual,List<ItemEntity> list ,int childResId) {
        LogUtil.d("setLinearLayoutData","mViewModule = " + viewModule, "divideEqual = " + divideEqual);
        if (wrapList != null && wrapList.getList() != null) {
            view.setData(wrapList.getList(),viewModule,wrapList.getChildItemLayoutId(), divideEqual);
        } else if (list != null) {
            view.setData(list,viewModule,childResId, divideEqual);
        }
    }

   /* @BindingAdapter(value = {"list","viewModule", "divideEqual", "childResId"}, requireAll=false)
    public static void setLinearLayoutData(DivideEqualLinearLayout view, List<ItemEntity> list , BaseViewModule viewModule, boolean divideEqual, int childResId) {
        LogUtil.d("setLinearLayoutData2","mViewModule = " + viewModule, "divideEqual = " + divideEqual);
        if (list != null) {
            view.setData(list,viewModule,childResId, divideEqual);
        }
    }*/

}
