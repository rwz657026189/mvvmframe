package com.rwz.baselist.adapter.rv;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rwz.baselist.BR;

import java.util.ArrayList;
import java.util.List;


/**
 * 介绍：普通Adapter
 * 泛型没有特殊需求可以不传
 * 泛型D:是Bean类型，如果有就传。
 * 泛型B:是对应的xml Layout的Binding类
 * 作者：zhangxutong
 * 邮箱：mcxtzhang@163.com
 * 主页：http://blog.csdn.net/zxt0601
 * 时间： 16/09/25.
 */

public class BaseBindingAdapter<D, B extends ViewDataBinding> extends RecyclerView.Adapter<BaseBindingVH<B>> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<D> mData;
    protected LayoutInflater mInflater;
    //用于设置Item的事件Presenter
    protected Object viewModule;
//    protected Object TempPresenter;//新增一个是空视图的点击事件

    public BaseBindingAdapter(Context mContext, List data, int mLayoutId) {
        this.mContext = mContext;
        this.mLayoutId = mLayoutId;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public BaseBindingAdapter(Context mContext, List data) {
        this.mContext = mContext;
        this.mData = data;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public BaseBindingVH<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseBindingVH<B> holder = new BaseBindingVH<>((B) DataBindingUtil.inflate(mInflater, mLayoutId, parent, false));
        onCreateViewHolder(holder);
        return holder;
    }

    /**
     * 如果需要给Vh设置监听器啥的 可以在这里
     *
     * @param holder
     */
    public void onCreateViewHolder(BaseBindingVH<B> holder) {
    }

    /**
     * 子类除了绑定数据，还要设置监听器等其他操作。
     * 可以重写这个方法，不要删掉super.onBindViewHolder(holder, position);
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(BaseBindingVH<B> holder, int position) {
        D data = mData.get(position);
        B binding = holder.getBinding();
//        LogUtil.e("onBindViewHolder", "position = " + position, "data = " + data);
        binding.setVariable(BR.entity, data);
        if (viewModule != null) {
            binding.setVariable(BR.viewModule, viewModule);
        }
        binding.setVariable(BR.position, position);
        binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    public Object getViewModule() {
        return viewModule;
    }

    /**
     * 用于设置Item的事件Presenter
     *
     * @param viewModule
     * @return
     */
    public BaseBindingAdapter setViewModule(Object viewModule) {
        this.viewModule = viewModule;
        return this;
    }

    /**
     * 用于设置空视图按钮的事件Presenter
     * @param tempPresenter
     */
  /*  public void setTempPresenter(Object tempPresenter) {
        TempPresenter = tempPresenter;
    }*/

    /**
     * 刷新数据，初始化数据
     *
     * @param list
     */
    public void setDatas(List<D> list) {
        if (this.mData != null) {
            if (null != list) {
                List<D> temp = new ArrayList<>(list);
                this.mData.clear();
                this.mData.addAll(temp);
            } else {
                this.mData.clear();
            }
        } else {
            this.mData = list;
        }
        notifyDataSetChanged();
    }

    /**
     * 删除一条数据
     * 会自动定向刷新
     *
     * @param i
     */
    public void remove(int i) {
        if (null != mData && mData.size() > i && i > -1) {
            mData.remove(i);
            notifyItemRemoved(i);
        }
    }

    /**
     * 添加一条数据 至队尾
     * 会自动定向刷新
     *
     * @param data
     */
    public void add(D data) {
        if (data != null && mData != null) {
            mData.add(data);
            notifyItemInserted(mData.size());
        }
    }

    /**
     * 在指定位置添加一条数据
     * 会自动定向刷新
     * <p>
     * 如果指定位置越界，则添加在队尾
     *
     * @param position
     * @param data
     */
    public void add(int position, D data) {
        if (data != null && mData != null) {
            if (mData.size() > position && position > -1) {
                mData.add(position, data);
                notifyItemInserted(position);
            } else {
                add(data);
            }
        }
    }


    /**
     * 加载更多数据
     *
     * @param list
     */
    public void addDatas(List<D> list) {
        if (null != list) {
            List<D> temp = new ArrayList<>(list);
            if (this.mData != null) {
                this.mData.addAll(temp);
            } else {
                this.mData = temp;
            }
            notifyDataSetChanged();
        }

    }


    public List<D> getDatas() {
        return mData;
    }
}
