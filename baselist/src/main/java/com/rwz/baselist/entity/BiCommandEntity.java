package com.rwz.baselist.entity;


/**
 * Created by rwz on 2018/7/25.
 */

public class BiCommandEntity<A, B> extends CommandEntity<B>{

    private A a;

    public BiCommandEntity(B b, A bool) {
        super(b);
        this.a = bool;
    }

    public BiCommandEntity(int id, A bool) {
        super(id);
        this.a = bool;
    }

    public BiCommandEntity(int id, A a, B b) {
        super(id, b);
        this.a = a;
    }

    public A getA() {
        return a;
    }
}
