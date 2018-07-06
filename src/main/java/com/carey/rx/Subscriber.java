package com.carey.rx;


public abstract class Subscriber<T> implements Observer<T> {
    public void onStart() {
        System.out.println("onStart" + Thread.currentThread());
    }
}
