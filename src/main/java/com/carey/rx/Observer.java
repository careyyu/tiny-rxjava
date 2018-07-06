package com.carey.rx;

/**
 *
 * @param <T>
 *
 * Created by carey on 2018/7/5 20:36:06.
 */
public interface Observer<T> {
    void onCompleted();

    void onError(Throwable t);

    void onNext(T var1);
}