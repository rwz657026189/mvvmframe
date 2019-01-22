package com.rwz.mvvmsdk.net.api;

import com.rwz.basemodule.entity.UpdateVersionEntity;
import com.rwz.mvvmsdk.net.API;
import com.rwz.network.Response;

import io.reactivex.Observable;
import retrofit2.http.POST;

public interface AppApi {

    /**
     * 更新版本
     */
    @POST(API.UPDATE_VERSION)
    Observable<Response<UpdateVersionEntity>> updateVersion();

}
