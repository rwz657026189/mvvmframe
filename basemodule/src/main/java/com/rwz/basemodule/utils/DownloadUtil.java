package com.rwz.basemodule.utils;

import android.text.TextUtils;

import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DownloadUtil {

    private static final String TAG = "DownloadUtil";
    //重连时间20s
    private final static int timeOutMillSeconds = 20_000;
    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil getInstance() {
        if (downloadUtil == null)
            synchronized (DownloadUtil.class) {
                if(downloadUtil == null)
                    downloadUtil = new DownloadUtil();
            }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //失败后重连
                .connectTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS)
                .writeTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS)
                .readTimeout(timeOutMillSeconds, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * @param url 下载连接
     * @param filePath 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String filePath, final DownloadListener listener) {
        final File newFile = createNewFile(filePath);
        LogUtil.d("DownloadUtil", "url = " + url, "\n filePath = " + filePath, "\n newFile = " + newFile);
        if (TextUtils.isEmpty(url) || newFile == null || !newFile.exists()) {
            if(listener != null)
                listener.onFailed();
            return;
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                if(listener != null)
                    listener.onFailed();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[1048 * 4];
                FileOutputStream fos = null;
                try {
                    ResponseBody body = response.body();
                    if(body == null)
                        return;
                    is = body.byteStream();
                    long total = body.contentLength();
                    fos = new FileOutputStream(newFile);
                    int len;
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        if (total > 0) {
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
                            if(listener != null)
                                listener.onProgress(progress);
                        }
                    }
                    fos.flush();
                    // 下载完成
                    if(listener != null)
                        listener.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    if(listener != null)
                        listener.onFailed();
                } finally {
                    if (is != null)
                        is.close();
                    if (fos != null)
                        fos.close();
                }
            }
        });
    }

    private File createNewFile(String path) {
        if(TextUtils.isEmpty(path))
            return null;
        File file = new File(path);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean result = parentFile.mkdirs();
            LogUtil.d(TAG, "mkdirs", "result = " + result);
            if(!result)
                return null;
        }
        if (!file.exists()) {
            try {
                boolean result = file.createNewFile();
                LogUtil.d(TAG, "createNewFile", "result = " + result);
                if(result)
                    return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return file;
        }
        return null;
    }

    public  interface DownloadListener {
        /**
         * 下载成功
         */
        void onCompleted();

        /**
         * @param progress
         * 下载进度
         */
        void onProgress(int progress);

        /**
         * 下载失败
         */
        void onFailed();
    }

    public static abstract class SimpleListener implements DownloadListener{
        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onFailed() {

        }
    }

}
