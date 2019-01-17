package com.rwz.basemodule.help;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import com.rwz.basemodule.R;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.commonmodule.utils.app.ResourceUtil;
import com.rwz.network.CommonObserver;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * Created by rwz on 2018/7/26.
 */

public class MsgHelp {

    public static void showToast(String text) {
        ToastUtil.getInstance().showShortSingle(text);
    }

    public static void showSnackBar(View view, @StringRes int text) {
        if(text != 0)
            showSnackBar(view, ResourceUtil.getString(text));
    }

    public static void showSnackBar(final View view, final String text) {
        if(view == null || TextUtils.isEmpty(text))
            return;
        Observable.just(view)
                .filter(new Predicate<View>() {
                    @Override
                    public boolean test(View view) throws Exception {
                        return view != null && !TextUtils.isEmpty(text);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<View>() {
                    @Override
                    public void onNext(View value) {
                        Snackbar.make(value, text, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.has_know, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                });
    }


}
