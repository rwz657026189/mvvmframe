package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.BR;
import com.rwz.basemodule.base.BaseViewModule;
import com.rwz.basemodule.entity.ItemEntity;
import com.rwz.commonmodule.utils.app.Assert;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by rwz on 2017/3/15.
 * 均分item的Linearlayout
 */

public class DivideEqualLinearLayout extends LinearLayout{

    private List<ItemEntity> list;
    private BaseViewModule viewModule;
    @LayoutRes
    private int childResId;
    private boolean isDivideEqual = true; //是否均分

    public DivideEqualLinearLayout(Context context) {
        super(context);
        init();
    }

    public DivideEqualLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DivideEqualLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setDivideEqual(boolean divideEqual) {
        isDivideEqual = divideEqual;
        init();
    }

    private void init() {
        LogUtil.d("DivideEqualLinearLayout", "init", "list = " + list, "mViewModule = " + viewModule);
        if (list != null && viewModule != null) {
            setData(list, viewModule, childResId);
        }
    }

    public void setList(List<? extends ItemEntity> list) {
        if (Assert.isEmptyColl(list)) {
            return;
        }
        if (this.list == null) {
            this.list = new ArrayList<>();
        } else {
            this.list.clear();
        }
        this.list.addAll(list);
        LogUtil.d("DivideEqualLinearLayout", "setList", "list = " + list);
        init();
    }

    public void setChildResId(int childResId) {
        this.childResId = childResId;
        init();
    }

    public void setViewModule(BaseViewModule viewModule) {
        this.viewModule = viewModule;
        LogUtil.d("DivideEqualLinearLayout", "setViewModule", "mViewModule = " + viewModule);
        init();
    }

    public void setData(List<ItemEntity> list) {
        setData(list, LinearLayoutCompat.HORIZONTAL,null,0);
    }

    public void setData(List<ItemEntity> list,BaseViewModule viewModule) {
        setData(list, LinearLayoutCompat.HORIZONTAL,viewModule,0);
    }
    public void setData(List<ItemEntity> list,BaseViewModule viewModule,@LayoutRes int childResId) {
        setData(list, LinearLayoutCompat.HORIZONTAL,viewModule,childResId);
    }
    public void setData(List<ItemEntity> list,BaseViewModule viewModule,@LayoutRes int childResId, boolean divideEqual) {
        this.isDivideEqual = divideEqual;
        setData(list, LinearLayoutCompat.HORIZONTAL,viewModule,childResId);
    }
    public synchronized void setData(List<ItemEntity> list, @LinearLayoutCompat.OrientationMode int orientation ,
                                     final BaseViewModule viewModule , @LayoutRes int childResId) {
        if (list == null || getChildCount() > 0) {
            return;
        }
//        LogUtil.d("list.size = " + list.size());
        if (list != null && list.size() > 0) {

            LogUtil.d("DivideEqualLinearLayout","setData", "size = " + list.size(), "isDivideEqual = " + isDivideEqual);

            LayoutInflater inflate = LayoutInflater.from(getContext());
            setOrientation(orientation);
            LayoutParams params = new LayoutParams(isDivideEqual ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (isDivideEqual) {
                params.weight = 1;
            }
            for (final ItemEntity itemEntity : list) {
                //注意childResId(一般为WrapList的mChildItemLayoutId)优先级高于entity的itemLayoutId,这样数据传递更加方便一点
                int itemLayoutId = childResId == 0 ? itemEntity.getItemLayoutId() : childResId;
                ViewDataBinding binding = DataBindingUtil.inflate(inflate, itemLayoutId, null, false);
//                LogUtil.d("DivideEqualLinearLayout","setData_test", "childResId = " + (childResId == 0),"entity = " + entity);
                binding.setVariable(BR.entity, itemEntity);
                binding.setVariable(BR.viewModule, viewModule);
                View root = binding.getRoot();
                addView(root,params);
                if (viewModule != null) {
                    final int id = root.getId();
                    RxView.clicks(root).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            viewModule.onClickEventCommand.accept(new CommandEntity<>(id, itemEntity));
                        }
                    });
                }
            }
        }
    }



}
