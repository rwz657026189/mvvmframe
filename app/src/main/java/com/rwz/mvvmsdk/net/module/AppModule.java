package com.rwz.mvvmsdk.net.module;

import com.rwz.basemodule.base.BaseModule;
import com.rwz.basemodule.entity.UpdateVersionEntity;
import com.rwz.mvvmsdk.net.api.AppApi;
import com.rwz.network.net.RetrofitManager;

import io.reactivex.Observable;

public class AppModule extends BaseModule{

    private static AppApi getService() {
        return RetrofitManager.getInstance().getService(AppApi.class);
    }

    /** 更新版本 **/
    public static Observable<UpdateVersionEntity> updateVersion() {
        return getService()
                .updateVersion()
                .compose(BaseModule.<UpdateVersionEntity>transformerCommon());
    }

}
