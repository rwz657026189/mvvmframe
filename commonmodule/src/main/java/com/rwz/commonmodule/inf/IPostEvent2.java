package com.rwz.commonmodule.inf;


/**
 * Created by rwz on 2016/12/14.
 */

public interface IPostEvent2<A , B>{
    void onEvent(A a, B b);
}
