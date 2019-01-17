package com.rwz.commonmodule.inf;

/**
 * Created by rwz on 2016/12/14.
 */

public interface IPostEvent4<A , B, C, D> {
    void onEvent(A a, B b, C c, D d);
}
