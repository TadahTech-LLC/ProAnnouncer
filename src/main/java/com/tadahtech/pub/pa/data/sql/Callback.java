package com.tadahtech.pub.pa.data.sql;

/**
 * @author Timothy Andis
 */
public class Callback<T> {

    private T t;

    public T get() {
        return t;
    }

    public void call(T t) {
        this.t = t;
    }
}
