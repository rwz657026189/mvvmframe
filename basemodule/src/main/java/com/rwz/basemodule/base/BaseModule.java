package com.rwz.basemodule.base;

import com.rwz.basemodule.R;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.Response;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rwz on 2017/7/19.
 */

public class BaseModule {

    /**
     * 是否登录
     */
    public static boolean isLogin = false;

    /**
     * 判读是否正确结果
     * @param response
     * @return
     */
    private static boolean isRequestSuccess(Response response) {
        return isRequestSuccess(response, true);
    }
    private static boolean isRequestSuccess(Response response, boolean showToastIfFail) {
        boolean isSuccess = false;
        //请求成功
        if (response != null && response.getCode() == BaseKey.CODE_SUCCESS) {
            isSuccess = true;
        } else { // 请求失败
            LogUtil.d("BaseModule", "response = " + response);
            String tips = "";
            //后端要求登出
            tips = response.getMsg();
            if (showToastIfFail) {
                ToastUtil.showShort(tips);
            }
            isSuccess = false;

        }
        return isSuccess;
    }

    /**
     * 获取数据
     * @param response
     * @param <T>
     * @return
     */
    public static <T>T getData(Response<T> response) {
        return getData(response, true);
    }

    public static <T>T getData(Response<T> response, boolean showToast) {
        if (isRequestSuccess(response, showToast) && response != null) {
            return response.getContent();
        }
        return null;
    }

    /**
     * 从子线程切换到主线程
     * 跟compose()配合使用,比如ObservableUtils.wrap(obj).compose(toMain())
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> toMainThread() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果的一般处理
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<Response<T>, T> transformerCommon() {
        return transformerCommon(true, true);
    }

    /**
     * @param showToast 异常情况下， 是否打印toast
     * @param isSwitchThread 是否切换线程 （如果本来在子线程请求， 可以给false）
     */
    public static <T> ObservableTransformer<Response<T>, T> transformerCommon(final boolean showToast, final boolean isSwitchThread) {
        return new ObservableTransformer<Response<T>, T>() {
            @Override
            public ObservableSource<T> apply(Observable<Response<T>> upstream) {
                Observable<T> map = upstream
                        .map(new Function<Response<T>, T>() {
                            @Override
                            public T apply(Response<T> t) throws Exception {
                                return getData(t, showToast);
                            }
                        });
                if (isSwitchThread) {
                    return  map.subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                } else {
                    return map;
                }
            }
        };
    }



}
