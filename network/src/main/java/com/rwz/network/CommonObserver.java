package com.rwz.network;

import com.rwz.commonmodule.utils.show.LogUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by rwz on 2017/7/17.
 *  特别注意, 建议订阅者都是独立对象, 因在onSubscribe()调用了destroy(), 用同一对象可能引起一些潜在问题
 */

public abstract class CommonObserver<T> implements Observer<T> {

    private static final String TAG = "OkHttp";
    private Disposable mDisposable;

    public CommonObserver() {
    }

    @Override
    public void onSubscribe(Disposable d) {
//        destroy();
        mDisposable = d;
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.d(TAG, "CommonObserver", "onError", e);
        if (e != null) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        LogUtil.d(TAG, "CommonObserver", "onComplete");
    }

    public void destroy() {
        if (mDisposable != null) {
            LogUtil.d(TAG, "CommonObserver,RecordViewModule", "destroy", "mDisposable = " + mDisposable.isDisposed());
            mDisposable.dispose();
            mDisposable = null;
        } else {
            LogUtil.d(TAG, "CommonObserver,RecordViewModule", "destroy", "mDisposable = " + mDisposable);
        }
    }

}
