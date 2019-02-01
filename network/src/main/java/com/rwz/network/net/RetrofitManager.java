package com.rwz.network.net;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.utils.show.LogUtil;
import com.rwz.commonmodule.utils.system.AndroidUtils;
import com.rwz.commonmodule.utils.system.DeviceInfoUtil;
import com.rwz.network.gson.GsonConverterFactory;
import com.rwz.network.interceptor.CommLoggingInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by rwz on 2017/7/12.
 */

public class RetrofitManager {

    public static final int TIME_OUT = 10_000;
    public static final int TIME_OUT_LONG = 5_000_000;
    private static final String TAG = "RetrofitManager";
    private Retrofit mRetrofit;
    //没有采用gson解析
    private Retrofit mRetrofitExceptGson;
    private Context mContext;
    private static RetrofitManager INSTANCE;
    //头信息
    private Map<String, String> mHeaderMap;
    //设备信息
    private String mDeviceInfo;

    public static void init(Context context, String host,@Nullable Map<String, String> headerMap) {
        if (INSTANCE == null)
            synchronized (RetrofitManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new RetrofitManager(context, host ,headerMap);
            }
    }

    private RetrofitManager(Context mContext, String host, Map<String, String> headerMap) {
        this.mContext = mContext.getApplicationContext();
        this.mHeaderMap = headerMap;
        createRetrofit(TIME_OUT, host);
    }

    public static RetrofitManager getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("must call init() at Application");
        }
        return INSTANCE;
    }

    @NonNull
    private void createRetrofit(int timeOutMillSeconds, String host) {
        Gson gson = new GsonBuilder().setLenient().create();
        LogUtil.d(TAG, "createRetrofit", "baseUrl = " + host);
        mRetrofit =  new Retrofit.Builder()
                .baseUrl(host)
                .client(getClient(timeOutMillSeconds))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit getRetrofitBuildExceptGson(String host ,int timeOutMillSeconds) {
        return new Retrofit.Builder()
                .baseUrl(host)
                .client(getClient(timeOutMillSeconds))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /** 没有采用gson解析 **/
    public <T>T getServiceExceptGson(Class<T> c, String host, int time) {
        if(mRetrofitExceptGson == null)
            mRetrofitExceptGson = getRetrofitBuildExceptGson(host, time);
        if (c != null && mRetrofitExceptGson != null) {
            return mRetrofitExceptGson.create(c);
        }
        return null;
    }

    public <T>T getService(Class<T> c) {
        if (c != null && mRetrofit != null) {
            return mRetrofit.create(c);
        }
        return null;
    }

    String mUUID;
    public String getUUID(){
        if (TextUtils.isEmpty(mUUID)) {
            synchronized (RetrofitManager.class) {
                mUUID = DeviceInfoUtil.getInstance().getUUID(mContext);
            }
        }
        return mUUID;
    }

    public String getDeviceInfo() {
        if (TextUtils.isEmpty(mDeviceInfo)) {
            mDeviceInfo = AndroidUtils.getDeviceModel() + "(" + AndroidUtils.getSystemVersion()+")";
            //清除空格
            if (mDeviceInfo.contains(" ")) {
                mDeviceInfo.replace(" ", "");
            }
        }
        return mDeviceInfo;
    }

    public OkHttpClient getClient(int timeOutMillSeconds) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Request.Builder builder1 = request.newBuilder();
                        Map<String, String> header = mHeaderMap;
                        if (header != null && header.size() > 0) {
                            Set<String> keys = header.keySet();
                            for (String key : keys) {
                                builder1.addHeader(key, header.get(key)).build();
                            }
                        }
                        Request build = builder1.build();
                        return chain.proceed(build);
                    }
                })
                .retryOnConnectionFailure(true) //失败后重连
                .connectTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS)
                .readTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS);

        if (GlobalConfig.SHOW_NET_ERROR) {
            builder = builder.addInterceptor(new CommLoggingInterceptor());
        }
        return builder.build();
    }

    public void addHeaderParams(Map<String, String> headerMap) {
        if(this.mHeaderMap == null)
            this.mHeaderMap = new HashMap<>();
        this.mHeaderMap.putAll(headerMap);
    }

    public RetrofitManager addHeaderParam(String key, String value) {
        if(this.mHeaderMap == null)
            this.mHeaderMap = new HashMap<>();
        this.mHeaderMap.put(key, value);
        return this;
    }

}


