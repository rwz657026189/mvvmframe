package com.rwz.basemodule.proxy;

import com.rwz.basemodule.base.BaseViewModule;
import com.rwz.basemodule.inf.IApiProxy;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.CommonObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by rwz on 2018/7/26.
 *
 *  单个接口requestCode 请给 RequestCode.SINGLE，否则空视图设置无效
 */

public abstract class BaseApiProxy<VM extends BaseViewModule> implements IApiProxy{

    protected VM mViewModule;
    protected String TAG = "BaseApiProxy";

    public BaseApiProxy(VM viewModule) {
        this.mViewModule = viewModule;
        TAG = getClass().getSimpleName();
    }

    @Override
    public void cancelRequest() {
    }

    @Override
    public CommonObserver getObserver(final int requestCode) {
        return new CommonObserver() {
            private boolean isCallOnNext = false;
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if(mViewModule != null)
                    mViewModule.onResponseError(requestCode);
            }

            @Override
            public void onNext(Object data) {
                isCallOnNext = true;
                if(mViewModule != null)
                    mViewModule.onResponseSuccess(requestCode, data);
            }

            @Override
            public void onComplete() {
                LogUtil.d(TAG, "onComplete", "isCallOnNext = " + isCallOnNext);
                if (mViewModule != null && !isCallOnNext) {
                    isCallOnNext = false;
                    mViewModule.onResponseSuccess(requestCode, null);
                }
            }

            @Override
            public void onSubscribe(Disposable d) {
                if (mViewModule != null) {
                    isCallOnNext = false;
                    mViewModule.addDisposable(d);
                }
            }
        };
    }

}
