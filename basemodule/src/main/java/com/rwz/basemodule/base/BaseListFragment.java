package com.rwz.basemodule.base;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rwz.baselist.adapter.rv.mul.BaseMulTypeBindingAdapter;
import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.decoration.GridItemSpDecoration;
import com.rwz.baselist.entity.BaseListEntity;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IListView;
import com.rwz.basemodule.abs.IView;
import com.rwz.basemodule.annotation.ViewListUpdateType;
import com.rwz.basemodule.weidgt.CommRefreshLayout;
import com.rwz.basemodule.weidgt.CommonRecyclerView;
import com.rwz.basemodule.weidgt.WrapLinearLayoutManager;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.utils.app.Assert;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Created by rwz on 2017/5/3.
 * 默认列表布局跟item布局用的同一个viewModule
 */

public abstract class BaseListFragment<VB extends ViewDataBinding, VM extends BaseListViewModule>
        extends BaseFragment<VB, VM> implements IListView{
    protected CommonRecyclerView mList;
    protected CommRefreshLayout mRefreshLayout;

    protected int SPAN_COUNT = 1; //每行条目数
    protected BaseMulTypeBindingAdapter mAdapter;
    protected boolean isMulSpanCount = false;//列数是否固定

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        mList = mRootView.findViewById(R.id.list);
        mRefreshLayout = mRootView.findViewById(R.id.refresh_layout);
        if (SPAN_COUNT > 1) {
            GridLayoutManager manager = new GridLayoutManager(getContext(), SPAN_COUNT);
            setSpanCount(manager);
            mList.setLayoutManager(manager);
            RecyclerView.ItemDecoration itemDecoration = getItemDecoration();
            if(itemDecoration != null)
                mList.addItemDecoration(itemDecoration);
        }else if(SPAN_COUNT == 1){
            mList.setLayoutManager(new WrapLinearLayoutManager(getContext()));
        }else return;
        if (mViewModule == null) {
            return;
        }
        mAdapter = new BaseMulTypeBindingAdapter(getContext(), mViewModule.getData());
        mList.setAdapter(mAdapter);
        mRefreshLayout.setPrimaryColorsId(R.color.page_bg, R.color.text_art_color);
        mRefreshLayout.setDragRate(0.5f);//显示下拉高度/手指真实下拉高度=阻尼效果
        mRefreshLayout.setReboundDuration(300);//回弹动画时长（毫秒）
        mRefreshLayout.setOnRefreshLoadMoreListener(mViewModule);
        mRefreshLayout.setOnLoadMoreListener(mViewModule);
        mAdapter.setViewModule(getItemViewModule());
        mAdapter.setOnClickCommand(itemClickCommand);
        setPullRefreshEnabled(false);
        setLoadingMoreEnabled(false);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridItemSpDecoration(ResourceUtil.getDimen(R.dimen.activity_horizontal_margin), mViewModule != null ? mViewModule.getItemDecorationList() : null, 0);
    }

    private Consumer<Integer> itemClickCommand = new Consumer<Integer>() {
        @Override
        public void accept(Integer position) throws Exception {
            if(mViewModule != null)
                mViewModule.onItemClick(position);
        }
    };

    private Consumer<Integer> itemLongClickCommand = new Consumer<Integer>() {
        @Override
        public void accept(Integer position) throws Exception {
            if(mViewModule != null)
                mViewModule.onItemClick(position);
        }
    };

    protected void setOnLongClickCommand(Consumer<Integer> itemLongClickCommand) {
        if(mAdapter != null)
            mAdapter.setOnLongClickCommand(itemLongClickCommand);
    }


    /**
     * item 采用的viewModule
     * @return
     */
    protected BaseViewModule<? extends IView> getItemViewModule() {
        return mViewModule;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_recyclerview;
    }

    private void setSpanCount(GridLayoutManager manager){
        final int spanCount = manager.getSpanCount();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
//                LogUtil.d("span_count = " + mData.size() ,position);
                List<? extends IBaseMulInterface> mData = mViewModule.getData();
                if (mData != null && mData.size() > position) {
                    IBaseMulInterface entity = mData.get(position);
//                    LogUtil.d("position = " + position , "realPos = " + realPos , "span_count = " + entity.getSpanCount());
                    if (entity instanceof BaseListEntity) {
                        if (!isMulSpanCount) {
                            int count = ((BaseListEntity) entity).getSpanCount();
                            return count == 0 ? spanCount : spanCount / count;
                        }else{
                            return 1;
                        }
                    }
                    return 1;
                }
                return 1;
            }
        });

    }

    @Override
    public void loadDataComplete(boolean isRefresh) {
        if (mRefreshLayout != null)
            if (isRefresh)
                mRefreshLayout.finishRefresh();
            else
                mRefreshLayout.finishLoadMore();
    }

    @Override
    public void setLoadingMoreEnabled(boolean enabled) {
        LogUtil.d(TAG, "setLoadingMoreEnabled", "enabled = " + enabled);
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableLoadMore(enabled);
    }

    @Override
    public void setPullRefreshEnabled(boolean enabled) {
        if (mRefreshLayout != null)
            mRefreshLayout.setEnableRefresh(enabled);
    }

    @Override
    public void autoRefresh() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnableRefresh(true);
            mRefreshLayout.autoRefresh();
        }
    }

    /** 清空数据, 并展示空视图 **/
    public void clearData() {
        if(mViewModule != null)
            mViewModule.clearData();
    }

    /** 是否能够刷新数据 **/
    private boolean canNotifyData() {
        //解决滑动过程刷新数据crash, 参考：https://www.jianshu.com/p/be89ebfb215e
        return mList.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || !mList.isComputingLayout();
    }

    @Override
    public void notifyDataSetChanged() {
        boolean canNotifyData = canNotifyData();
        LogUtil.d(TAG, "notifyDataSetChanged", "canNotifyData = " + canNotifyData, "isMainThread = " + Assert.isMainThread());
        if (!canNotifyData) return;

        if (Assert.isMainThread()) {
            //必须在主线程调用才有效
            if (mAdapter != null)
                mAdapter.notifyDataSetChanged();
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (mAdapter != null)
                        mAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void updateData(@ViewListUpdateType final int type, final int position) {
        boolean canNotifyData = canNotifyData();
        LogUtil.d(TAG, "updateData", "canNotifyData = " + canNotifyData, "type = " + type, "position = " + position);
        if (!canNotifyData) return;

        if (Assert.isMainThread()) {
            updateDataOnMainThread(type, position);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    updateDataOnMainThread(type, position);
                }
            });
        }
    }

    private void updateDataOnMainThread(int type, int position) {
        if (mAdapter == null) {
            return;
        }
        int count = mAdapter.getItemCount();
        int itemCount = type == IListView.REMOVE ? (count + 1) : (type == IListView.INSERTED ? count - 1 : count);
        LogUtil.d(TAG, "updateDataOnMainThread", "type = " + type, "position = " + position, "itemCount = " + itemCount);
        if (position >= 0 && position < itemCount) {
            if (type == IListView.INSERTED) {
                mAdapter.notifyItemInserted(position);
            } else if (type == IListView.CHANGED) {
                mAdapter.notifyItemChanged(position);
            } else if (type == IListView.REMOVE) {
                mAdapter.notifyItemRemoved(position);
            } else if (type == IListView.SCROLL_TO_TOP) {
                scrollToTop();
            } else if (type == IListView.SCROLL_TO_BOTTOM && mAdapter != null) {
                scrollToPosition(mAdapter.getItemCount() - 1);
            } else if (type == IListView.SCROLL_TO_POSITION) {
                scrollToPosition(position);
            }
        }
    }

    public void scrollToPosition(int position) {
        if (mList != null &&  mAdapter != null && position >= 0 && mAdapter.getItemCount() > position) {
            RecyclerView.LayoutManager layoutManager = mList.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position, 0);
            }
        }
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        if (mList != null &&  mAdapter != null && mAdapter.getItemCount() > 0) {
            if (GlobalConfig.isSmoothScrollList) {
                mList.smoothScrollToPosition(0);
            } else {
                mList.scrollToPosition(0);
            }
        }
    }

    @Override
    public void setTempType(int type) {
    }

    @Override
    public void addHeaderView(@LayoutRes int layoutId) {

    }

    /**
     * 清空数据
     */
    public void cleanData() {
        if (mViewModule != null) {
            mViewModule.cleanData();
        }
    }

}
