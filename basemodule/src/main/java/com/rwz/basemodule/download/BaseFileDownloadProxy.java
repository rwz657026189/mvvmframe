package com.rwz.basemodule.download;

import android.text.TextUtils;

import com.rwz.basemodule.help.PermissionHelp;
import com.rwz.commonmodule.utils.app.FileUtil;
import com.rwz.commonmodule.utils.safety.EncryptionUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;
import java.util.UUID;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 18:39
 */

public abstract class BaseFileDownloadProxy implements IFileDownloadProxy {

//    protected DownloadConfig mConfig;
    private DownloadTaskManager mTaskManager;

    public BaseFileDownloadProxy() {
        mTaskManager = new DownloadTaskManager();
    }

    @Override
    public boolean startDownload(DownloadConfig config) {
        LogUtil.d(TAG, "startDownload", config);
        boolean result = checkParams(config);
        if (!result && config != null) {
            IFileDownloadListener listener = config.getListener();
            if (listener != null) {
                listener.onError();
            }
        }
        return result;
    }

    private boolean checkParams(DownloadConfig config) {
        showLog("load");
        if (config == null) {
            showLog("config != null");
            return false;
        }
        if (TextUtils.isEmpty(config.getUrl())) {
            showLog("url != null");
            return false;
        }
        boolean hasWritePermission = PermissionHelp.hasWritePermission();
        if (!hasWritePermission) {
            showLog("don't has write permission");
            return false;
        }
        File file = FileUtil.createParentFile(config.getSavePath() + config.getFileName(), true);
        if (file == null) {
            showLog("create Parent File error");
            return false;
        }
        return true;
    }

    protected void showLog(String... msg) {
        LogUtil.d(TAG, msg);
    }

    /**
     * 获取文件名
     * 配置了就直接采用，没有则以MD5加密url获取
     * @return
     */
    protected String getFileName(DownloadConfig config) {
        if (config != null) {
            String fileName = config.getFileName();
            if (TextUtils.isEmpty(fileName)) {
                String url = config.getUrl();
                if (!TextUtils.isEmpty(url)) {
                    String fileExtensionName = FileUtil.getFileExtensionName(url);
                    String name = EncryptionUtil.encodeMD5ToString(url);
                    return name + "." + fileExtensionName;
                }
            } else {
                return fileName;
            }
        }
        return UUID.randomUUID().toString();
    }

    public DownloadConfig getConfig(long id) {
        if (mTaskManager != null) {
            return mTaskManager.getTask(id);
        }
        return null;
    }

    /**
     * 任务失败或完成后移除
     * @param config
     */
    public void removeConfig(DownloadConfig config) {
        if (config != null && mTaskManager != null) {
            mTaskManager.removeTask(config);
        }
    }

    /**
     * 任务开始成功后添加
     * @param config
     */
    public void addConfig(DownloadConfig config) {
        if (config != null && mTaskManager != null) {
            mTaskManager.putTask(config);
        }
    }

}
