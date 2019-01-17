package com.rwz.basemodule.abs;

import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @function: 控制订阅的生命周期
 * @author: rwz
 * @date: 2017-07-16 15:41
 */

public abstract class RxViewModule<T extends IView> implements IViewModule<T>{

    protected T mView;
    protected CompositeDisposable mCompositeSubscription;
    //特别注意，初始化需要用到该参数时，请在attachWindow()调用
    protected Context mContext;

    protected void dispose() {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.dispose();
        }
    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }

    @Override
    public void bindContext(Context context) {
        this.mContext = context;
    }

    @Override
    public void attachWindow(T t) {
        this.mView = t;
    }

    @Override
    public void detachWindow() {
        this.mView = null;
        this.mContext = null;
        dispose();
    }

    @Override
    public void onResume() {
    }

    protected void postEvent(@PostEventType int type) {
        postEvent(type, null);
    }

    protected void postEvent(@PostEventType int type, Object params) {
        if (mView != null) {
            mView.onPostEvent(type, params);
        }
    }

    protected boolean isAlive() {
        return mView != null;
    }

}
