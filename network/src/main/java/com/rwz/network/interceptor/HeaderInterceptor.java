package com.rwz.network.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rwz on 2017/9/12.
 */

/**
 * 头部 拦截器
 */
public class HeaderInterceptor implements Interceptor {

    private Map<String, String> header;

    public HeaderInterceptor(Map<String, String> header) {
        this.header = header;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        if (header != null && header.size() > 0) {
            Set<String> keys = header.keySet();
            for (String key : keys) {
                newBuilder.addHeader(key, header.get(key)).build();
            }
        }
        Request build = newBuilder.build();
        return chain.proceed(build);
    }
}
