package com.rwz.basemodule.proxy;

import android.support.annotation.NonNull;

import com.rwz.baselist.adapter.rv.mul.IBaseMulInterface;
import com.rwz.basemodule.base.BaseViewModule;

/**
 * Created by rwz on 2018/7/26.
 */

public class CommApiProxy extends BaseApiProxy<BaseViewModule> {

    public CommApiProxy(BaseViewModule viewModule) {
        super(viewModule);
    }

    @Override
    public void onItemClick(int position, @NonNull IBaseMulInterface iEntity) {
    }

    @Override
    public void onClickView(int id, IBaseMulInterface iEntity) {
    }

    @Override
    public void submitRequest() {
    }



}
