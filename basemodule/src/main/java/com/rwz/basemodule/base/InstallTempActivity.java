package com.rwz.basemodule.base;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rwz.basemodule.R;
import com.rwz.basemodule.config.BaseKey;
import com.rwz.basemodule.help.InstallHelp;
import com.rwz.basemodule.service.DownloadService;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.show.LogUtil;

/**
 */
public class InstallTempActivity extends AppCompatActivity {


    public static void turn(String apkPath) {
        Context context = BaseApplication.getInstance();
        Intent intent = new Intent(context, InstallTempActivity.class);
        intent.putExtra(BaseKey.STRING, apkPath);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_temp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        try {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(DownloadService.VERSION_APK_ID);
        } catch (Exception e) {
        }
        String path = getIntent().getStringExtra(BaseKey.STRING);
        LogUtil.d("InstallTempActivity", "path = " + path);
//        PackageUtils.installApk(this,path);
        InstallHelp.installApk(this, path);
        finish();
    }
}
