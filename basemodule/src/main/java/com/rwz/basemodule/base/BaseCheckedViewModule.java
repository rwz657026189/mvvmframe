package com.rwz.basemodule.base;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.BiCommandEntity;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IListView;
import com.rwz.basemodule.abs.IView;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.functions.Consumer;

/**
 * Created by rwz on 2017/11/15.
 * 具有选择的item 列表
 * 注意：
 * 1、数据请求成功后，更新总条目数 {@link BaseCheckedViewModule#setTotalCount(int)}
 * 2、执行onHandleBatch()后 更新条目数 {@link BaseCheckedViewModule#setTotalCount(int)}
 * 3、如果部分item不可选，请求数据后(包含刷新)设置可选集合 {@link BaseCheckedViewModule#setCheckableData(java.util.Set)}
 * 4、全选与否： {@link BaseCheckedViewModule#toggleSelectAll()}
 * 5、需要分页并且全选时， 分页数据请求成功后需要调用 {@link BaseCheckedViewModule#addAllOnSelectAll()}
 *
 */

public abstract class BaseCheckedViewModule<T extends IListView> extends BaseListViewModule<T>{

    protected static final int DIALOG_ENTER_HANDLE_BATCH = 100; //批量操作dialog标识符

    public ObservableBoolean isDelStatus = new ObservableBoolean();//是否删除状态
    public ObservableBoolean isSelectAll = new ObservableBoolean();//是否全选状态
    public ObservableInt checkSize = new ObservableInt(); //选中的条数
    protected HashSet<IBaseMulInterface> mCheckSet;
    //可选择集合(默认全部)
    protected Set<IBaseMulInterface> mCheckableData;
    private int mTotalCount;//所有条目数

    public BaseCheckedViewModule() {
        mCheckSet = new HashSet<>();
    }

    public void toggleStatus() {
        if (mCheckSet != null) {
            mCheckSet.clear();
        }
        isSelectAll.set(false);
        checkSize.set(0);
        boolean isDel = isDelStatus.get();
        isDelStatus.set(!isDel);
        setPullRefreshEnabled(isDel && mTotalCount != 0);
        postEvent(IView.UPLOAD_EDIT_STATE, isDelStatus.get());
        notifyDataSetChanged();
    }

    /**
     * 设置条目总数
     * @param totalCount 条目总数
     */
    public void setTotalCount(int totalCount) {
        this.mTotalCount = totalCount;
        isSelectAll.set(checkSize.get() == totalCount);
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public boolean isChecked(IBaseMulInterface iEntity) {
        if(!isDelStatus.get())
            return false;
        if (isSelectAll.get())
            return true;
        boolean isChecked = false;
        if (mCheckSet != null && iEntity != null) {
            isChecked =  mCheckSet.contains(iEntity);
        }
        LogUtil.d(TAG, "isChecked = " + isChecked, "checkSize = " + checkSize.get(), "mTotalCount = " + mTotalCount, "mCheckSet = " + mCheckSet.size());
        return isChecked;
    }

    @Override
    protected void onItemClick(int position, @NonNull IBaseMulInterface iEntity) {
        if (isDelStatus.get() && mCheckSet != null) {
            boolean contains = mCheckSet.contains(iEntity);
            boolean result;
            if (contains) {
                result = mCheckSet.remove(iEntity);
            } else {
                result = mCheckSet.add(iEntity);
            }
            LogUtil.d(TAG, "onItemClick", "position = " + position, "contains = " + contains, "result = " + result);
            checkSize.set(mCheckSet.size());
            isSelectAll.set(mCheckSet.size() == mTotalCount);
            updateData(IListView.CHANGED, position);
//            notifyDataSetChanged();
        }
    }

    /**
     * 选择按钮改变的监听的监听
     */
    public final Consumer<BiCommandEntity<Boolean, IBaseMulInterface>> onCheckedChangeCommand = new Consumer<BiCommandEntity<Boolean, IBaseMulInterface>>() {
        @Override
        public void accept(BiCommandEntity<Boolean, IBaseMulInterface> biEntity) throws Exception {
            if (isDelStatus.get() && biEntity != null) {
                boolean isChecked = biEntity.getA();
                IBaseMulInterface entity = biEntity.getT();
                boolean contains = mCheckSet.contains(entity);
                if (isChecked && !contains) {
                    mCheckSet.add(entity);
                } else if (!isChecked && contains) {
                    mCheckSet.remove(entity);
                }
                LogUtil.d(TAG, "onItemClick2_onCheckedChangeCommand", mCheckSet.contains(entity), entity);
                checkSize.set(mCheckSet.size());
                isSelectAll.set(mCheckSet.size() == mTotalCount);
                onCheckedChanged(mCheckSet);
            }
        }
    };

    /** 选中状态改变时 **/
    protected void onCheckedChanged(Set<IBaseMulInterface> set){
    }

    /**
     * 反置全选
     */
    public void toggleSelectAll() {
        LogUtil.d(TAG, "toggleSelectAll", "isDelStatus = " + isDelStatus.get(),  "isSelectAll = " + isSelectAll.get());
        if (isDelStatus.get()) {
            if (isSelectAll.get()) {
                isSelectAll.set(false);
                mCheckSet.clear();
                checkSize.set(0);
            } else {
                isSelectAll.set(true);
                mCheckSet.addAll(getCheckableData());
                checkSize.set(mTotalCount);
            }
            notifyDataSetChanged();
            onCheckedChanged(mCheckSet);
        }
    }

    /**
     * 当全选的时候，需要手动加入下一页实体类
     */
    protected void addAllOnSelectAll() {
        if (isDelStatus.get() && isSelectAll.get() && mCheckSet != null) {
            mCheckSet.addAll(getCheckableData());
        }
    }

    /**
     * 设置可选集合
     */
    public void setCheckableData(Set<IBaseMulInterface> checkableData) {
        this.mCheckableData = checkableData;
    }

    /**
     * @return 可选集合
     */
    public Collection<IBaseMulInterface> getCheckableData() {
        return mCheckableData == null ? mData : mCheckableData;
    }

    @Override
    protected void cleanDataOnRefresh(String requestCode) {
        super.cleanDataOnRefresh(requestCode);
        if(mCheckableData != null)
            mCheckableData.clear();
    }

    @Override
    protected void onClickView(int id, @Nullable IBaseMulInterface entity) {
        super.onClickView(id, entity);
        LogUtil.d(TAG, "onClickView", "id = " + id);
        if (id == R.id.enter) {
            onShowBatchDialog(R.string.dialog_enter_batch);
        } else if (id == R.id.all) {
            toggleSelectAll();
        } else if (id == R.id.cancel) {
            toggleStatus();
        }
    }

    protected void onShowBatchDialog(@StringRes int res) {
        if (checkSize.get() == 0) {
            ToastUtil.getInstance().showShortSingle(R.string.checked_one_at_least);
        } else {
            showDialog(res, DIALOG_ENTER_HANDLE_BATCH);
        }
    }

    @Override
    protected void onClickDialogEnter(MsgDialogTurnEntity entity) {
        super.onClickDialogEnter(entity);
        if (entity != null && entity.getRequestCode() == DIALOG_ENTER_HANDLE_BATCH) {
            onHandleBatch(mCheckSet);
            toggleStatus();
        }
    }

    public String getRightBottomText(int type, int checkNum) {
        return "";
    }

    /**
     * 执行批量操作
     */
    protected abstract void onHandleBatch(Set<IBaseMulInterface> checkSet);

    @Override
    public Consumer getOnClickEventCommand() {
        return super.getOnClickEventCommand();
    }
}
