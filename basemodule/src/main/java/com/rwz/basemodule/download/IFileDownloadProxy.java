package com.rwz.basemodule.download;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 18:16
 */

public interface IFileDownloadProxy {

    String TAG = "FileDownload";

    /**
     * 开始一个下载
     * @param config 配置参数
     * @return  是否成功下载
     */
    boolean startDownload(DownloadConfig config);

}
