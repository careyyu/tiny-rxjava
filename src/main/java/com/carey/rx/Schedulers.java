package com.carey.rx;

import java.util.concurrent.Executors;

/**
 * 针对不同的应用场景 使用各种不同的线程池
 *
 */
public class Schedulers {
    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());

    public static Scheduler io() {
//        return ioScheduler;
        return  new Scheduler(Executors.newSingleThreadExecutor());
    }
}
