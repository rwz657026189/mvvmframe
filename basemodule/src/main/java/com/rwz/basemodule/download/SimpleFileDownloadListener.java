package com.rwz.basemodule.download;

import com.rwz.commonmodule.utils.show.LogUtil;

/**
 * Created by rwz on 2017/9/5.
 */

public class SimpleFileDownloadListener implements IFileDownloadListener{

    @Override
    public void onStart() {
        LogUtil.d(TAG, "onStart");
    }

    @Override
    public void onProgress(int progress) {
        LogUtil.d(TAG, "onProgress" , "progress = " + progress);
    }

    @Override
    public void onCompleted() {
        LogUtil.d(TAG, "onCompleted");
    }

    @Override
    public void onError() {
        LogUtil.d(TAG, "onError");
    }
}
