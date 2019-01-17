package com.rwz.network.net;

import android.util.SparseArray;

import org.reactivestreams.Subscription;

/**
 * Created by rwz on 2017/7/13.
 * 所有请求集合
 */

public class RxManagerImpl implements IRxManager {

    private static RxManagerImpl sInstance = null;

    private SparseArray<Subscription> mSubscriptions;

    public static RxManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (RxManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new RxManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private RxManagerImpl() {
        mSubscriptions = new SparseArray<>();
    }

    @Override
    public void add(int tag, Subscription subscription) {
        if (subscription != null) {
            mSubscriptions.put(tag, subscription);
        }
    }

    @Override
    public void remove(int tag) {
        mSubscriptions.remove(tag);
    }

    public void removeAll() {
        mSubscriptions.clear();
    }

    @Override
    public void cancel(int tag) {
        Subscription subscription = mSubscriptions.get(tag);
        if (subscription == null) {
            return;
        }
        subscription.cancel();
    }

    @Override public void cancelAll() {
        int size = mSubscriptions.size();
        for (int i = 0; i < size; i++) {
            cancel(mSubscriptions.keyAt(i));
        }
    }
}