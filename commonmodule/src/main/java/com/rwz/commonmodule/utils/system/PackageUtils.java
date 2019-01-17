package com.rwz.commonmodule.utils.system;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.rwz.commonmodule.R;
import com.rwz.commonmodule.base.BaseApplication;
import com.rwz.commonmodule.utils.app.CommUtils;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.io.File;
import java.util.List;

/**
 * Created by rwz on 2017/4/7.
 */

public class PackageUtils {
    /**
     * 当前使用databinding时不能直接安装APK,否则坑爹的谷歌会把你坑的不要不要的！！！
     *
     * @param activity
     */

    /**
     * 安装Apk(适配android 7.0)
     */
    public static void installApk(Context context, String apkPath) {
        LogUtil.d("installApk","apkPath = "+apkPath);
        if (context == null || TextUtils.isEmpty(apkPath)) {
            LogUtil.e("apkPath = " + apkPath);
            return;
        }
        File file = new File(apkPath);
        if (!file.exists()) {
            LogUtil.e("安装文件不存在 apkPath = " + apkPath);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            //file:///storage/emulated/0/fjz/update/fanjianzhi.apk
            String packageName = AndroidUtils.getPackageName(context);
            Uri apkUri = FileProvider.getUriForFile(context, packageName + ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        if(CommUtils.canTurn(context, uninstallIntent))
            context.startActivity(uninstallIntent);
//        Environment拥有一些可以获取环境变量的方法
//        package:com.demo.CanavaCancel 这个形式是 package:程序完整的路径 (包名+程序名).
    }

    /**
     * 根据文件获取包名
     * @param context
     * @param apkPath
     * @return
     */
    public static String getPackageName(Context context, String apkPath) {

        if (context == null || TextUtils.isEmpty(apkPath) || !apkPath.endsWith(context.getString(R.string.apk))) {
            LogUtil.e("apkPath = " + apkPath);
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        try{
            info = pm.getPackageArchiveInfo(apkPath , PackageManager.GET_ACTIVITIES);
        }catch(Exception e){
            e.printStackTrace();
        }
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            LogUtil.d("getPackageName = " + appInfo.packageName , " apkPath = " + apkPath);
            return appInfo.packageName;
        }
        LogUtil.d("getPackageName info = " , " apkPath = " + apkPath);
        return null;
    }

    /**
     * 打开apk
     * @param context
     * @param packageName
     */
    public static boolean openApk(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            LogUtil.e("openApk file , packageName = " + packageName);
            return false;
        }
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null && CommUtils.canTurn(context, intent)) {
            try {
                context.startActivity(intent);
                LogUtil.d("openApk : " + packageName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            LogUtil.e("openApk file, intent == null , packageName = " + packageName);
        }
        return false;
    }

    /**
     * 判断apk是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context,String packageName){
        if (context == null || TextUtils.isEmpty(packageName)) {
            LogUtil.e("context = " + context, " packageName = " + packageName);
            return false;
        }
        boolean installed = false;
        PackageManager pm = context.getPackageManager();
        try{
            if (pm != null) {
                pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                installed =true;
            }
        }catch(PackageManager.NameNotFoundException e){
//            e.printStackTrace();
        }
        return installed;
    }

    /**
     * app是否启动
     * packName  应用的包名
     * 返回值
     */
    public static boolean isAppAlive(Context context, String packageName){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null)
            return false;
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch", String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch", String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    /**
     * 判断某个activity是否正在运行
     * @param mContext
     * @param activityClassName
     * @return
     */
    public static boolean isActivityRunning(Context mContext, String activityClassName){
        if (TextUtils.isEmpty(activityClassName)) {
            return false;
        }
        try {
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            if(am == null)
                return false;
            List<ActivityManager.RunningTaskInfo> info = am.getRunningTasks(1);
            if (info != null && info.size() > 0) {
                for (int i = 0; i < info.size(); i++) {
                    ComponentName baseActivity = info.get(i).baseActivity;
                    if (baseActivity != null) {
                        String className = baseActivity.getClassName();
                        if (activityClassName.equals(className)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAppAlive() {
        Context context = BaseApplication.getInstance();
        String packageName = context.getPackageName();
        //TODO 注意Activity限定名与现实实际情况统一
        return PackageUtils.isActivityRunning(context, packageName + ".activity.MainActivity");
    }

    /**
     * 判断Service是否在运行？
     * @param mContext
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context mContext,String className) {
        boolean isRunning = false;
        ActivityManager am = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        if(am == null)
            return false;
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
