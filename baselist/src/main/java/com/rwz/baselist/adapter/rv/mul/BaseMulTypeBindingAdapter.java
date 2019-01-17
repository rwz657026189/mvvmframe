package com.rwz.baselist.adapter.rv.mul;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.rwz.baselist.adapter.rv.BaseBindingAdapter;
import com.rwz.baselist.adapter.rv.BaseBindingVH;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * 介绍：多种ItemType的Base类
 * 泛型T:多Item多Bean情况可以不传。如果只有一种Bean类型，可以传入Bean，实现IBaseMulInterface接口。
 * 或者传入IBaseMulInterface接口，可以拿到 getItemLayoutId()，
 * 但是通过getItemViewType(int position)，一样。所以多Item多Bean建议不传。
 * <p>
 * 基类的泛型B：不用传，因为多种ItemType 肯定Layout长得不一样，那么Binding类也不一样，传入没有任何意义
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 时间： 16/09/25.
 */

public class BaseMulTypeBindingAdapter<T extends IBaseMulInterface> extends BaseBindingAdapter<T, ViewDataBinding> {

    //单击监听
    private Consumer onClickCommand;
    //长按监听
    private Consumer onLongClickCommand;

    public void setOnClickCommand(Consumer<Integer> onClickCommand) {
        this.onClickCommand = onClickCommand;
    }

    public void setOnLongClickCommand(Consumer onLongClickCommand) {
        this.onLongClickCommand = onLongClickCommand;
    }

    public BaseMulTypeBindingAdapter(Context mContext, List<T> mDatas) {
        super(mContext, mDatas);
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemLayoutId();
    }

    @Override
    public BaseBindingVH<ViewDataBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(mInflater, viewType, parent, false);
        final BaseBindingVH<ViewDataBinding> holder = new BaseBindingVH<>(binding);
        onCreateViewHolder(holder);
        if (onClickCommand != null) {//优化 : 没有监听就不用监听itemView了
            RxView.clicks(binding.getRoot())
                    .map(new Function<Object, Integer>() {
                        @Override
                        public Integer apply(Object object) throws Exception {
                            int layoutPosition = holder.getLayoutPosition();
                            LogUtil.d("onCreateViewHolder", "layoutPosition = " + layoutPosition);
                            return layoutPosition;
                        }
                    })
                    .filter(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer integer) throws Exception {
                            return integer >= 0;
                        }
                    })
                    .subscribe(onClickCommand);
        }
        if (onLongClickCommand != null) {
            RxView.longClicks(binding.getRoot())
                    .map(new Function<Object, Integer>() {
                        @Override
                        public Integer apply(Object object) throws Exception {
                            return holder.getLayoutPosition();
                        }
                    })
                    .filter(new Predicate<Integer>() {
                        @Override
                        public boolean test(Integer integer) throws Exception {
                            return integer >= 0;
                        }
                    })
                    .subscribe(onLongClickCommand);
        }
        return holder;
    }


}
