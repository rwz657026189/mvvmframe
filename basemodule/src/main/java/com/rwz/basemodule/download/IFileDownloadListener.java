package com.rwz.basemodule.download;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 18:16
 */

public interface IFileDownloadListener {

    String TAG = "FileDownload";

    void onStart();

    void onProgress(int progress);

    void onCompleted();

    void onError();

}
