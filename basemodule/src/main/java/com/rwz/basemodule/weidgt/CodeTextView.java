package com.rwz.basemodule.weidgt;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.rwz.basemodule.R;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.network.CommonObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;


/**
 * Created by rwz on 2017/3/24.
 * 可重发验证码的EditView
 */

public class CodeTextView extends AppCompatTextView {
    private boolean canClick = true;  //是否可点击
    private boolean isFinishCountDown;//是否结束倒计时

    private final String ENABLE_STR = ResourceUtil.getString(R.string.re_get_code);
    private final String INIT_STR = ResourceUtil.getString(R.string.reg_get_code);
    private final static int MAX_SEND_TIME = 60; //倒计时秒数
    Disposable mDisposable;

    public CodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CodeTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        canClick = true;
        isFinishCountDown = false;
        setText(INIT_STR);
        setEnabled(canClick);
    }

    public void setCanClick(boolean canClick) {
        this.canClick = canClick;
    }

    public boolean isCanClick() {
        return canClick;
    }

    public void startCountDown() {
        canClick = false;
        isFinishCountDown = false;
        setText(String.format(ENABLE_STR,MAX_SEND_TIME));
        setEnabled(canClick);
        if(mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        //倒计时结束、控件销毁、短信发送失败都将结束倒计时
                        return aLong == MAX_SEND_TIME || isFinishCountDown || canClick;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<Long>(){
                    @Override
                    public void onComplete() {
                        LogUtil.d("onCompleted" ,"isFinishCountDown = " + isFinishCountDown);
                        if (!isFinishCountDown) {
                            init();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
//                LogUtil.d("更改文字" + aLong);
                        setText(String.format(ENABLE_STR,MAX_SEND_TIME - aLong));
                    }

                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }
                });
    }



    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isFinishCountDown = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isFinishCountDown = true;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

}


