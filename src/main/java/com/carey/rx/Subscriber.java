package com.carey.rx;


/**
 * 消息订阅者
 * 主要逻辑在onNext中处理
 * @param <T>
 */
public interface Subscriber<T> {
    default void onStart() {
        System.out.println("onStart" + Thread.currentThread());
    }

    void onCompleted();

    void onError(Throwable t);

    void onNext(T var1);
}
