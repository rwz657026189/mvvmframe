package com.rwz.basemodule.base;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by rwz on 2018/6/25.
 *  支持与activity 绑定/解绑
 */

public class BaseBindService extends Service{

    private BaseBindBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new BaseBindService.BaseBindBinder();
    }

    public class BaseBindBinder extends Binder {
        public Service getService() {
            return BaseBindService.this;
        }
    }


}
