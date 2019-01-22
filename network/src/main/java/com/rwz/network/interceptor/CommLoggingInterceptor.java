package com.rwz.network.interceptor;

import com.rwz.commonmodule.utils.app.TimeUtil;
import com.rwz.commonmodule.utils.show.L;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by rwz on 2017/7/24.
 */

public class CommLoggingInterceptor implements Interceptor {

    private static final String TAG = "CommLoggingInterceptor";

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        Request request = chain.request();
        RequestBody body = request.body();
        LogUtil.ok(TAG, "heads = " + request.headers());
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            int size = formBody.size();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < size; i++) {
                sb.append("║   ").append(formBody.name(i)).append(" = ").append(formBody.value(i)).append("\n");
            }
            LogUtil.ok(TAG, "url : \n║   " + request.url(), "\n║   请求body : \n" + sb.toString());
        }
        long t1 = System.currentTimeMillis();//请求发起的时间
        Response response = chain.proceed(request);
        long t2 = System.currentTimeMillis();//收到响应的时间
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理

        ResponseBody responseBody = response.peekBody(1024 * 1024);

        HttpUrl url = response.request().url();
        LogUtil.ok(TAG, "接收响应: \n║   " +
                 url + "\n║   " +
                "耗时：" + TimeUtil.formatTimePrecise((t2 - t1), TimeUtil.FORMAT_PRECISE2, 2, false) + "\n║   " +
                "响应头：" + response.headers(), "body = " + body);

        L.j(responseBody.string());

        return response;
    }
}