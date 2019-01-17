package com.rwz.basemodule.base;

import android.databinding.Observable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.baselist.entity.CommandEntity;
import com.rwz.basemodule.R;
import com.rwz.basemodule.abs.IView;
import com.rwz.basemodule.abs.RxViewModule;
import com.rwz.basemodule.entity.SimpleResponseEntity;
import com.rwz.basemodule.entity.turnentity.LoadingDialogTurnEntity;
import com.rwz.basemodule.entity.turnentity.MsgDialogTurnEntity;
import com.rwz.basemodule.inf.CommBiConsumer;
import com.rwz.basemodule.inf.IApiProxy;
import com.rwz.basemodule.proxy.CommApiProxy;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.CommonObserver;

import io.reactivex.functions.Consumer;

/**
 * @function:
 * @author: rwz
 * @date: 2017-07-16 16:07
 */

public class BaseViewModule<T extends IView> extends RxViewModule<T> {

    protected String TAG = "BaseViewModule";
    protected boolean isAutoLoadingData = true;//是否自动加载数据
    protected IApiProxy mApiProxy;  //接口代理类

    public BaseViewModule() {
        TAG = getClass().getSimpleName();
        LogUtil.d(TAG, "BaseViewModule", "isRefreshEnable = ");
    }

    public BaseViewModule(Consumer onClickEventCommand) {
        this();
        this.onClickEventCommand = onClickEventCommand;
    }

    protected IApiProxy setApiProxy() {
        return new CommApiProxy(this);
    }

    /**
     * 初始化完成, 最后调用，仅调用一次
     */
    @Override
    public void initCompleted() {
        mApiProxy = setApiProxy();
        LogUtil.d("BaseViewModule", "initCompleted = " + isAutoLoadingData);
        if (isAutoLoadingData)
            requestData();
    }

    /**
     * 加载网络数据
     * 多条请求时，分页加载请求放在该方法，并且禁用自动加载（isAutoLoadingData = false）,手动加载第一条请求
     */
    protected void requestData() {
        if (mApiProxy != null && !(mApiProxy instanceof CommApiProxy)) {
            mApiProxy.submitRequest();
        }
    }

    protected CommonObserver getObserver(final int requestCode) {
        return mApiProxy != null ? mApiProxy.getObserver(requestCode) : null;
    }

    /**
     * 请求失败的回调
     */
    public  void onResponseError(int requestCode) {
    }

    /**
     * 请求成功的回调
     * @param requestCode 请求码
     * @param data        实体类
     * @param <T>
     */
    public synchronized <T> void onResponseSuccess(int requestCode, T data) {
        if (data != null && data instanceof SimpleResponseEntity) {
            String message = ((SimpleResponseEntity) data).getMessage();
            ToastUtil.showShort(message);
        }
    }

    public Consumer onClickEventCommand = new Consumer<CommandEntity<IBaseMulInterface>>() {

        @Override
        public void accept(CommandEntity<IBaseMulInterface> commandEntity) throws Exception {
            onClickView(commandEntity.getId(), commandEntity.getT());
        }
    };

    protected void onClickView(int id, @Nullable IBaseMulInterface iEntity) {
        LogUtil.d("BaseViewModule", "onClickView", id);
    }

    protected void showDialog(@StringRes int msg, int requestCode) {
        if (msg != 0) {
            String title = ResourceUtil.getString(R.string.dialog_def_title);
            String message = ResourceUtil.getString(msg);
            showDialog(new MsgDialogTurnEntity(title, message, requestCode));
        }
    }

    protected void showDialog(MsgDialogTurnEntity entity) {
        if (entity != null && mView != null) {
            entity.setListener(mDialogListener);
            mView.onPostEvent(IView.SHOW_DIALOG, entity);
        }
    }

    /**
     * 显示加载中对话框
     * @param res
     */
    protected void showLoadingDialog(@StringRes int res) {
        showLoadingDialog(ResourceUtil.getString(res));
    }

    protected void showLoadingDialog(String text) {
        showLoadingDialog(new LoadingDialogTurnEntity(text));
    }

    protected void showLoadingDialog(LoadingDialogTurnEntity entity) {
        if (entity != null) {
            postEvent(IView.SHOW_LOADING, entity);
        }
    }

    protected void dismissLoadingDialog() {
        postEvent(IView.DISMISS_LOADING);
    }

    private CommBiConsumer mDialogListener = new CommBiConsumer<MsgDialogTurnEntity, Boolean>() {
        @Override
        public void accept(MsgDialogTurnEntity entity, Boolean isClickEnter) throws Exception {
            if (isClickEnter) {
                onClickDialogEnter(entity);
            } else {
                onClickDialogCancel(entity);
            }
        }
    };

    /**
     * 点击对话框确定
     *
     * @param entity
     */
    protected void onClickDialogEnter(MsgDialogTurnEntity entity) {
    }

    /**
     * 点击对话框取消
     * @param entity
     */
    protected void onClickDialogCancel(MsgDialogTurnEntity entity) {
    }

    /**
     * @param isForbidTouchScreen 是否禁用触摸屏幕（禁用一切事件）
     */
    public void setForbidTouchScreen(boolean isForbidTouchScreen) {
        postEvent(IView.FORBID_TOUCH_SCREEN, isForbidTouchScreen);
    }

    Observable.OnPropertyChangedCallback callback;

    protected Observable.OnPropertyChangedCallback getPropertyChangedCallback() {
        if (callback == null) {
            callback = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable observable, int i) {
                    onPropertyChanged(observable, i);
                }
            };
        }
        return callback;
    }

    protected void onPropertyChanged(Observable observable, int value) {

    }

    public FragmentManager getFragmentManager() {
        if (mContext != null && mContext instanceof AppCompatActivity) {
            AppCompatActivity aty = (AppCompatActivity) mContext;
            return aty.getSupportFragmentManager();
        }
        return null;
    }

    /**
     * @return 页面类型
     */
    public int getType() {
        return 0;
    }

}
