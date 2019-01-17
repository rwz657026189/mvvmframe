package com.rwz.basemodule.manager;

import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.rwz.commonmodule.config.GlobalConfig;
import com.rwz.commonmodule.utils.app.ReflectionUtil;
import com.rwz.commonmodule.utils.show.LogUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DeveloperManager {

    private static DeveloperManager instance;
    private String currIP;
    private String initIP;
    private List<String> ipData;

    public static DeveloperManager getInstance() {
        if(instance == null)
            synchronized (DeveloperManager.class) {
                if(instance == null)
                    instance = new DeveloperManager();
            }
        return instance;
    }

    public void init() {
        initIP = currIP = GlobalConfig.MAIN_HOST;
        ipData = new ArrayList<>();
        ipData.add(GlobalConfig.ONLINE_HTTP);
        ipData.add(GlobalConfig.LOCAL_HTTP);
    }

    public float getFontScale(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.scaledDensity;
    }

    public float getScreenScale(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.densityDpi;
    }

    public float getSmallestWidthDP(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int heightPixels = dm.heightPixels;
        int widthPixels = dm.widthPixels;
        float density = dm.density;
        float heightDP = heightPixels / density;
        float widthDP = widthPixels / density;
        float smallestWidthDP;
        if(widthDP < heightDP) {
            smallestWidthDP = widthDP;
        }else {
            smallestWidthDP = heightDP;
        }
        return smallestWidthDP;
    }

    public String getCPU() {
        StringBuilder cpu = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String[] abiArr = Build.SUPPORTED_ABIS;
            for (String abi : abiArr) {
                cpu.append(abi).append("   ");
            }
        } else {
            cpu = new StringBuilder(Build.CPU_ABI);
        }
        return cpu.toString();
    }

    public String getCurrIP() {
        return currIP;
    }

    public String getNewServerIP() {
        if(ipData == null || ipData.size() == 0)
            return currIP;
        int index = ipData.indexOf(currIP);
        index++;
        if(index < 0 || index >= ipData.size())
            index = 0;
        return currIP = ipData.get(index);
    }

    public boolean switchServerIP() {
        boolean result = isSwitchServerIP();
        if (result) {
            ReflectionUtil.modifyStaticField("com.rwz.commonmodule.config.GlobalConfig", "MAIN_HOST", currIP);
            invokeRetrofit("createRetrofit", 10_000, currIP);
        }
        return result;
    }

    /**
     * 是否切换了服务器ip
     */
    public boolean isSwitchServerIP() {
        return !TextUtils.equals(initIP, currIP);
    }

    /**
     *  反射ServiceManager(单例) 调用 其父类RetrofitHelper 内的方法
     */
    private Object invokeRetrofit(String methodName, Object... args) {
        try {
            String retrofitManagerClass = "com.rwz.network.net.RetrofitManager";
            Class c = Class.forName(retrofitManagerClass);
            Class[] argsClass = new Class[args == null ? 0 : args.length];
            if (argsClass.length > 0) {
                for (int i = 0; i < argsClass.length; i++) {
                    argsClass[i] = args[i].getClass();
                }
            }
            Method getInstance = c.getMethod("getInstance");
            Object serviceManager = getInstance.invoke(null);
            LogUtil.d("invokeRetrofit", "serviceManager = " + serviceManager);
            Method method = c.getMethod(methodName, argsClass);
            method.setAccessible(true);
            LogUtil.d("invokeRetrofit", "method = " + method);
            return method.invoke(serviceManager, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNewIp(String ip) {
        if(ipData != null)
            ipData.add(ip);
        currIP = ip;
    }



}
