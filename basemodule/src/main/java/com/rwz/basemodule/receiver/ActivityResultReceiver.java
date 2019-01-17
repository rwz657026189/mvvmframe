package com.rwz.basemodule.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.rwz.basemodule.config.BaseKey;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.inf.IPostEvent3;
import com.rwz.commonmodule.utils.system.AndroidUtils;

/**
 *  activity onActivityResult() 的广播
 */
public class ActivityResultReceiver extends BroadcastReceiver{

    private static final String ACTION = AndroidUtils.getPackageName(BaseApplication.getInstance()) + ".ACTIVITY_RESULT_ACTION";
    private static final String PERMISSION = AndroidUtils.getPackageName(BaseApplication.getInstance()) + ".ACTIVITY_RESULT_PERMISSION";

    private IPostEvent3<Integer, Integer, Bundle> postEvent;

    public ActivityResultReceiver(IPostEvent3<Integer, Integer, Bundle> postEvent) {
        this.postEvent = postEvent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION.equals(intent.getAction()) && postEvent != null) {
            int requestCode = intent.getIntExtra(BaseKey.ONE, 0);
            int resultCode = intent.getIntExtra(BaseKey.TWO, 0);
            Bundle bundle = intent.getParcelableExtra(BaseKey.THREE);
            postEvent.onEvent(requestCode, resultCode, bundle);
        }
    }

    public static void register(Context context, ActivityResultReceiver receiver) {
        if (context != null && receiver != null) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ACTION);
            context.registerReceiver(receiver, intentFilter, PERMISSION, null);
        }
    }

    public static void unregister(Context context, ActivityResultReceiver receiver) {
        if(context != null)
            context.unregisterReceiver(receiver);
    }

    public static void callback(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(ACTION);
        intent.putExtra(BaseKey.ONE, requestCode);
        intent.putExtra(BaseKey.TWO, resultCode);
        if(data != null)
            intent.putExtra(BaseKey.THREE, data.getExtras());
        BaseApplication.getInstance().sendBroadcast(intent, PERMISSION);
    }



}
