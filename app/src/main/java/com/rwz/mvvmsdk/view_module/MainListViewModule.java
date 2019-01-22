package com.rwz.mvvmsdk.view_module;

import android.support.annotation.NonNull;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.basemodule.base.BaseListViewModule;
import com.rwz.commonmodule.utils.show.ToastUtil;
import com.rwz.mvvmsdk.entity.test.TestItemEntity;
import com.rwz.network.CommonObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainListViewModule extends BaseListViewModule{

    private int mType;

    public MainListViewModule(int type) {
        this.mType = type;
    }

    @Override
    protected void requestData() {
        Observable.just(mPage)
                .delay(1000, TimeUnit.MILLISECONDS)
                .map(new Function<Integer, List<TestItemEntity>>() {
                    @Override
                    public List<TestItemEntity> apply(Integer page) throws Exception {
                        List<TestItemEntity> list = new ArrayList<>();
                        int curr = (page - FIRST_PAGE) * 20;
                        for (int i = curr; i < curr + 20; i++) {
                            String url = "http://p2.qhimgs4.com/t01272a9ad72768c9f5.jpg";
                            String url2 = "https://p0.ssl.qhimgs4.com/t010963137bc82c6dbc.jpg";
                            TestItemEntity entity = new TestItemEntity("测试" + i, mType == 1 ? url2 : url);
                            entity.setSpanCount(mType == 1 ? 2 : 1);
                            list.add(entity);
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<List<TestItemEntity>>() {
                    @Override
                    public void onNext(List<TestItemEntity> list) {
                        onResponseSuccess(null, list);
                    }
                });
    }

    @Override
    protected void handlerData(String requestCode, Object data) {
        super.handlerData(requestCode, data);
    }

    @Override
    protected void onItemClick(int position, @NonNull IBaseMulInterface iEntity) {
        if (iEntity instanceof TestItemEntity) {
            ToastUtil.getInstance().showShortSingle("click : " + position + "," + ((TestItemEntity) iEntity).getTitle());
        }
    }



}
