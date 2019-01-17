package com.rwz.basemodule.base;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IListView;
import com.rwz.basemodule.annotation.ViewListUpdateType;
import com.rwz.basemodule.entity.CommListData;
import com.rwz.basemodule.entity.SimpleResponseEntity;
import com.rwz.basemodule.entity.TempEntity;
import com.rwz.basemodule.temp.ITempView;
import com.rwz.basemodule.temp.TempType;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.Assert;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.CommonObserver;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 15:49
 * 所有列表页应该继承或使用它
 *
 * 注意：占位图作为第一个item，第一次请求后会清空数据，将不会存在。若需要重新设置空视图，需要调用assertNullData()。
 *
 */

public abstract class BaseListViewModule<T extends IListView> extends BaseViewModule<T> implements OnRefreshLoadMoreListener,
        OnLoadMoreListener{
    //第一页下标
    protected final static int FIRST_PAGE = 1;
    // 页数
    protected int mPage = FIRST_PAGE;
    //该次请求是否刷新
    protected boolean isRefresh = true;
    //第一次是否请求到数据（主要用于界面初始化）
    public ObservableBoolean hasRequestData = new ObservableBoolean(false);
    //数据源
    protected List<IBaseMulInterface> mData;
    //占位图
    protected TempEntity mTempEntity;
    //分隔条位置
    protected List<Integer> mDecorationList;
    //标记需要采用空视图的请求;一般是第一条, 非特殊情况禁止修改（一般只用于刷新的适合会更改）
    protected int mTempRequestCode = 0;
    //加载完成后是否允许下拉刷新、上拉加载更多
    protected boolean isRefreshEnable = true;
    protected boolean isLoadingMoreEnable = true;

    public BaseListViewModule() {
        this.mData = new ArrayList<>();
        mTempEntity = getTempEntity();
    }

    @Override
    public void initCompleted() {
        //初始化完成开始显示占位图, 防止部分页面传递有列表数据
        if (isAutoLoadingData) {
            mData.add(getTempEntity());
            notifyDataSetChanged();
        }
        super.initCompleted();
    }

    /**
     * 设置某处位置的空视图
     * 在init()方法后调用,否则无效
     * @param type
     */
    protected void setTemp(@TempType int type) {
        LogUtil.d(TAG, "setTemp", "type = " + type);
        if (mView != null) {
            mView.setTempType(type);
        }
        if (mTempEntity != null) {
            mTempEntity.type.set(type);
        }
    }

    public TempEntity getTempEntity() {
        if (mTempEntity == null) {
            Context context = BaseApplication.getInstance();
            Drawable nullImg = ResourceUtil.getDrawable(R.mipmap.no_data);
            Drawable errorImg = ResourceUtil.getDrawable(R.mipmap.no_nerwork);
            mTempEntity =  new TempEntity(nullImg, errorImg);
        }
        return mTempEntity;
    }

    protected void loadDataComplete() {
        if (mView != null)
            mView.loadDataComplete(isRefresh);
    }

    protected void notifyDataSetChanged() {
        LogUtil.d(TAG, "MainListViewModule, notifyDataSetChanged", "mView = " + mView);
        if (mView != null)
            mView.notifyDataSetChanged();
    }

    protected void updateData(@ViewListUpdateType int type, int position) {
        if (mView != null)
            mView.updateData(type, position);
    }

    /** 移除数据 **/
    protected synchronized void removeData(IBaseMulInterface iEntity) {
        if (mView != null && iEntity != null && mData != null) {
            int position = mData.indexOf(iEntity);
            LogUtil.d(TAG, "removeData", "position = " + position, "iEntity = " + iEntity);
            if (position >= 0) {
                mData.remove(iEntity);
                mView.updateData(IListView.REMOVE, position);
            }
        }
    }

    /** 滚动到顶部 **/
    protected void scrollToTop() {
        if (mView != null)
            mView.updateData(IListView.SCROLL_TO_TOP, 0);
    }

    /** 滚动到指定位置 **/
    protected void scrollToPosition(int position) {
        if (mView != null)
            mView.updateData(IListView.SCROLL_TO_POSITION, position);
    }

    @Override
    protected CommonObserver getObserver(int requestCode) {
        //若未修改, 标记第一条请求, 会根据该请求的结果决定是否显示占位图
        if(mTempRequestCode == 0)
            mTempRequestCode = requestCode;
        return super.getObserver(requestCode);
    }

    @Override
    public void onResponseError(int requestCode) {
        LogUtil.d(TAG, "BaseListViewModule","onResponseError" , "requestCode = " + requestCode, "mPage = " + mPage, "isRefresh = "  + isRefresh);
        if (mTempRequestCode == requestCode && mPage == FIRST_PAGE && isRefresh) {
            //先清空一次数据(比如再次搜索的时候加载失败,就需要清空数据在显示)
            cleanDataOnRefresh(requestCode);
            if(mData != null && !mData.contains(getTempEntity()))
                mData.add(mTempEntity);
            setTemp(ITempView.STATUS_ERROR);
            notifyDataSetChanged();
            setRefreshLoadingMoreEnable(true, false);
        }
        hasRequestData.set(true);
        //加载完成，改变状态
        loadDataComplete();
    }

    /**
     * 多条请求逻辑：
     * 1. 第一条请求必然判断是否为空 是：空视图； 否 下一个请求：无论是否为空均不判断
     * 2. 刷新时会重新第一条请求
     * @param requestCode  请求码
     * @param data  实体类
     * @param <T>
     */
    @Override
    public synchronized <T> void onResponseSuccess(int requestCode, T data) {
        LogUtil.d(TAG, "BaseListViewModule","onResponseSuccess" , "mTempRequestCode = " + mTempRequestCode, "requestCode = " + requestCode, "mPage = " + mPage, "isRefresh = " + isRefresh);
        if (mTempRequestCode == requestCode) {
            //判断是否为空集合
            if (isEmptyData(requestCode, data)) {
                if (mPage == FIRST_PAGE && isRefresh) {
                    //先清空一次数据(比如再次搜索的时候加载失败,就需要清空数据在显示)
                    cleanDataOnRefresh(requestCode);
                    assertNullData(null);
                    notifyDataSetChanged();
                    setRefreshLoadingMoreEnable(true, false);
                }
            } else {
                if (mPage == FIRST_PAGE && isRefresh) {
                    cleanDataOnRefresh(requestCode);
                }
                performRequestData(requestCode, data);
            }
        } else {
            performRequestData(requestCode, data);
        }
        hasRequestData.set(true);
        //加载完成，改变状态
        loadDataComplete();
    }

    /** 判断请求的数据是否为空 **/
    protected <T> boolean isEmptyData(int requestCode, T data) {
        if (Assert.isEmptyColl(data)) {
            return true;
        } else if (data instanceof CommListData) {
            List list = ((CommListData) data).getListArr();
            return list == null || list.size() == 0;
        }
        return false;
    }

    private <T> void performRequestData(int requestCode, T data) {
        if (mTempEntity != null) {
            mData.remove(mTempEntity);
            mTempEntity = null;
        }
        handlerData(requestCode, data);
        LogUtil.d(TAG, "performRequestData", "isRefreshEnable = " + isRefreshEnable, "isLoadingMoreEnable = " + isLoadingMoreEnable);
        //启动上拉加载更多、下拉刷新
        setRefreshLoadingMoreEnable(isRefreshEnable, isLoadingMoreEnable);
    }

    /** 当刷新的时候清空数据 **/
    protected void cleanDataOnRefresh(int requestCode) {
        mData.clear();
    }

    /**
     * recyclerView item 点击事件
     * @param position
     */
    public void onItemClick(int position) {
        if (position >= 0 && position < mData.size()) {
            IBaseMulInterface entity = mData.get(position);
            if (entity != null) {
                onItemClick(position, entity);
            }
        }
    }

    protected abstract void onItemClick(int position, @NonNull IBaseMulInterface iEntity);

    /**
     * 服务器请求到的实体类
     * @param requestCode 请求码
     * @param data  实体类
     * @param <T>
     */
    protected  <T> void handlerData(int requestCode, T data) {
        LogUtil.d(TAG,"handlerData", "requestCode = " + requestCode, "data = " + data);
        if (Assert.isNonEmptyColl(data) && mData != null) {
            mData.addAll((Collection<? extends IBaseMulInterface>) data);
            notifyDataSetChanged();
        } else if (data instanceof SimpleResponseEntity) {
            String message = ((SimpleResponseEntity) data).getMessage();
            ToastUtil.showShort(message);
        } else if (data instanceof CommListData) {
            LogUtil.d(TAG,"handlerData", "requestCode = " + requestCode, ((CommListData) data).getCount());
            mData.addAll(((CommListData) data).getListArr());
            notifyDataSetChanged();
        }
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        onRefresh();
    }

    public void onRefresh() {
        LogUtil.d(TAG, "onRefresh");
        isRefresh = true;
        mPage = FIRST_PAGE;
        requestData();
    }

    /** 主动刷新， 最终会修改状态 并 调用onRefresh()**/
    public void autoRefresh() {
        if(mView != null)
            mView.autoRefresh();
    }

    /** 清空数据, 并展示空视图 **/
    public void clearData() {
        LogUtil.d(TAG, "clearData");
        if (mData != null) {
            mData.clear();
            assertNullData(null);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        isRefresh = false;
        mPage++;
        requestData();
        LogUtil.d("onLoadMore", "mPage = " + mPage);
    }

    protected void setRefreshLoadingMoreEnable(boolean isRefreshEnable, boolean isLoadingMoreEnable) {
        if (mPage == FIRST_PAGE && mView != null) { //避免多处触发
            mView.setPullRefreshEnabled(isRefreshEnable);
            mView.setLoadingMoreEnabled(isLoadingMoreEnable);
        }
    }

    protected void setLoadingMoreEnabled(boolean enable) {
        if (mView != null) {
            mView.setLoadingMoreEnabled(enable);
        }
    }

    protected void setPullRefreshEnabled(boolean enable) {
        if (mView != null) {
            mView.setPullRefreshEnabled(enable);
        }
    }

    @Override
    protected void onClickView(int id, @Nullable IBaseMulInterface iEntity) {
        super.onClickView(id, iEntity);
        if (id == R.id.reload) {//空视图重新加载数据
            setTemp(ITempView.STATUS_LOADING);
            onRefresh();
        }
    }

    public void addHeaderView(@LayoutRes int layoutId) {
        if (mView != null) {
            mView.addHeaderView(layoutId);
        }
    }

    public int getPage() {
        return mPage;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public List<IBaseMulInterface> getData() {
        return mData;
    }

    /**
     * 获取需要设置分割线的位置
     * @return
     */
    public List<Integer> getItemDecorationList() {
        return mDecorationList;
    }

    protected void addDecorationPos(int pos) {
        if (pos >= 0) {
            if (mDecorationList != null) {
                if (!mDecorationList.contains(Integer.valueOf(pos))) {
                    mDecorationList.add(pos);
                }
            }else{
                LogUtil.e(TAG, "请在子类实例化");
            }
        }
    }

    /**
     * 是否请求到列表数据
     * @return
     */
    public boolean isResponseSuccess() {
        return hasRequestData != null && hasRequestData.get();
    }

    public boolean isResponseSuccess(@StringRes int tips) {
        if (hasRequestData != null && hasRequestData.get()) {
            return true;
        } else {
            ToastUtil.getInstance().showShort(tips);
            return false;
        }
    }

    /**
     * 判断数据是否为空，若是则显示空视图
     */
    public void assertNullData(List data) {
        if (Assert.isEmptyColl(data)) {
            if (mTempEntity == null) {
                mTempEntity = getTempEntity();
            }
            if (mData != null && !mData.contains(mTempEntity)) {
                mData.add(mTempEntity);
            }
            setTemp(ITempView.STATUS_NULL);
        }
    }


    /**
     * 清空数据
     */
    public void cleanData() {
        if (mData != null) {
            mData.clear();
            mData.add(getTempEntity());
            setTemp(ITempView.STATUS_NULL);
            notifyDataSetChanged();
            LogUtil.d(TAG, "cleanData = " + mData.size());
            setPullRefreshEnabled(false);
        }
    }


    public Consumer getOnClickEventCommand() {
        return onClickEventCommand;
    }



}
