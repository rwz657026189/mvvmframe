package com.rwz.network.net;

import org.reactivestreams.Subscription;

/**
 * Created by rwz on 2017/7/13.
 */

public interface IRxManager {

    void add(int tag, Subscription subscription);

    void remove(int tag);

    void cancel(int tag);

    void cancelAll();

}
