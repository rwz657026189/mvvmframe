package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.BR;
import com.rwz.basemodule.R;
import com.rwz.basemodule.base.BaseViewModule;
import com.rwz.commonmodule.utils.app.Assert;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by rwz on 2017/3/15.
 * 均分item的Linearlayout
 */

public class MulLinearLayout extends LinearLayout{


    @LayoutRes
    private int DEF_LAYOUT = 0;
    public @LayoutRes int mItemLayout = DEF_LAYOUT;//子布局
    public List mData;         //数据
    public BaseViewModule mViewModule;//业务逻辑
    public boolean isEqual;//是否均分布局
    public int space;   //间距
    public int childCount = -1;//孩子最多个数(-1随数据决定)

    public MulLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public MulLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.mll, 0, 0);
        mItemLayout = array.getResourceId(R.styleable.mll_itemLayout, DEF_LAYOUT);
        isEqual = array.getBoolean(R.styleable.mll_isEqual, false);
        space = array.getDimensionPixelSize(R.styleable.mll_space, 0);
        childCount = array.getInteger(R.styleable.mll_childCount, -1);
        LogUtil.d("MulLinearLayout", "setting", "space = " + space, "mItemLayout = " + mItemLayout, "isEqual = " + isEqual, "childCount = " + childCount);
        array.recycle();
        init(context);
    }

    public MulLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (Assert.isNonEmptyColl(mData) && mItemLayout != DEF_LAYOUT) {
            /*int childCount2 = getChildCount();
            if (childCount2 > 0) {
                return;
            }*/
            removeAllViews();
            setOrientation(LinearLayout.HORIZONTAL);
            LayoutInflater inflater = LayoutInflater.from(context);
            LayoutParams params = new LayoutParams(isEqual ? 0 : ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (isEqual) {
                params.weight = 1;
            }
            int size = mData.size();
            size = childCount != -1 ? Math.min(size, childCount) : size;
            LogUtil.d("MulLinearLayout", "initMulLinearLayout","isEqual = " +  isEqual, "size = " + size);

            for (int i = 0; i < size; i++) {
                final Object entity = mData.get(i);
                ViewDataBinding binding = DataBindingUtil.inflate(inflater, mItemLayout, null, false);
                binding.setVariable(BR.entity, entity);
                if (mViewModule != null) {
                    binding.setVariable(BR.viewModule, mViewModule);
                }
                //间距
//                LogUtil.d("MulLinearLayout", "init","isEqual = " +  isEqual);
                if (!isEqual) {
                    params.leftMargin = i != 0 ? space : 0;
//                    LogUtil.d("MulLinearLayout", "init","leftMargin = " +  params.leftMargin);
                }
                View root = binding.getRoot();
                addView(root,params);
                if (mViewModule != null) {
                    final int id = root.getId();
                    RxView.clicks(root).subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object object) throws Exception {
                            IBaseMulInterface iEntity = entity instanceof IBaseMulInterface ? (IBaseMulInterface)entity : null;
                            mViewModule.onClickEventCommand.accept(new CommandEntity<>(id, iEntity));
                        }
                    });
                }

                LogUtil.d("MulLinearLayout", "initMulLinearLayout_addView","isEqual = " +  isEqual, "size = " + size);
            }

        }
    }

    public void setItemLayout(int itemLayout) {
        LogUtil.d("MulLinearLayout","setting","setItemLayout", "itemLayout = " + itemLayout);
        this.mItemLayout = itemLayout;
    }

    public void setData(List data) {
        LogUtil.d("MulLinearLayout","setting","setData", "data = ");
        this.mData = data;
        init(getContext());
    }

    public void setChildCount(int childCount) {
        LogUtil.d("MulLinearLayout","setting","setChildCount", "childCount = " + childCount);
        this.childCount = childCount;
        init(getContext());
    }

    public void setViewModule(BaseViewModule viewModule) {
        LogUtil.d("MulLinearLayout","setting","setViewModule", "viewModule = " + viewModule);
        this.mViewModule = viewModule;
        init(getContext());
    }


    public void setSpace(int space) {
        LogUtil.d("MulLinearLayout","setting","setSpace", "space = " + space);
        this.space = space;
    }


    /**
     * 是否更新数据
     */
    public void updateData(int updateCount) {
        LogUtil.d("MulLinearLayout, updateData",updateCount);
        if (updateCount > 0) {
            removeAllViews();
            init(getContext());
        }
    }

}
