package com.rwz.basemodule.download;

import java.util.HashMap;
import java.util.Map;

/**
 * @function:
 * @author: rwz
 * @date: 2017-08-19 19:11
 */

public class DownloadTaskManager {

    private static DownloadTaskManager instance;

    public static DownloadTaskManager getInstance() {
        if (instance == null) {
            synchronized (DownloadTaskManager.class) {
                if (instance == null) {
                    instance = new DownloadTaskManager();
                }
            }
        }
        return instance;
    }

    public DownloadTaskManager() {
        mData = new HashMap<>();
    }

    private Map<Long, DownloadConfig> mData;

    public synchronized void putTask(DownloadConfig config) {
        if (config != null && mData != null && (!mData.containsValue(config))) {
            mData.put(config.getId(), config);
        }
    }

    public boolean containerTask(DownloadConfig config) {
        return mData != null && mData.containsValue(config);
    }

    public DownloadConfig getTask(long id) {
        if (mData != null) {
            return mData.get(Long.valueOf(id));
        }
        return null;
    }

    public synchronized void removeTask(DownloadConfig config) {
        if (config != null && mData != null && mData.containsValue(config)) {
            mData.remove(config.getId());
        }
    }

    public synchronized void removeTask(long id) {
        if (mData != null && mData.containsKey(id)) {
            mData.remove(id);
        }
    }

    public synchronized void clear() {
        if (mData != null) {
            mData.clear();
        }
    }

}
