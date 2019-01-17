package com.rwz.basemodule.download.downloadmanager;

import android.database.ContentObserver;
import android.os.Handler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by rwz on 2017/8/29.
 * 下载查询类
 */

public class DownloadChangeObserver extends ContentObserver {

    private final ScheduledExecutorService scheduledExecutorService;
    private final Runnable progressRunnable;

    public DownloadChangeObserver(Handler handler, Runnable progressRunnable) {
        super(handler);
        this.progressRunnable = progressRunnable;
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 当所监听的Uri发生改变时，就会回调此方法 每 2s 更新一次进度
     * @param selfChange 此值意义不大, 一般情况下该回调值false
     */
    @Override
    public void onChange(boolean selfChange) {
        if (scheduledExecutorService != null && progressRunnable != null) {
            scheduledExecutorService.scheduleAtFixedRate(progressRunnable, 0, 2, TimeUnit.SECONDS);
        }
    }

    /**
     * 关闭定时器，线程等操作
     */
    public void close() {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
    }

}