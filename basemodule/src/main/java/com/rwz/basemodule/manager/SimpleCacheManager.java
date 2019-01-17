package com.rwz.basemodule.manager;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.rwz.basemodule.utils.SharePreUtil;

public class SimpleCacheManager {

    private static SimpleCacheManager instance;

    private SimpleCacheManager() {
    }

    public static SimpleCacheManager getInstance() {
        if(instance == null)
            synchronized (SimpleCacheManager.class) {
                if(instance == null)
                    instance = new SimpleCacheManager();
            }
        return instance;
    }

    public void put(String key, Object object) {
        if (!TextUtils.isEmpty(key) && object != null) {
            String value = new Gson().toJson(object);
            SharePreUtil.putString(key, value);
        }
    }

    public <T>T get(String key, Class<T> cla) {
        if (!TextUtils.isEmpty(key) && cla != null) {
            String json = SharePreUtil.getString(key);
            if (!TextUtils.isEmpty(json)) {
                return new Gson().fromJson(json, cla);
            }
        }
        return null;
    }



}
