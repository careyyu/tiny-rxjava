package com.carey.rx;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * 针对不同的应用场景 使用各种不同的线程池
 */
public class Schedulers {
    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());

    public static final Scheduler s1 = new Scheduler(Executors.newFixedThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("s1");
            return t;
        }
    }));

    public static final Scheduler s2 = new Scheduler(Executors.newFixedThreadPool(1, new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("s2");
            return t;
        }
    }));




    public static Scheduler io() {
        return ioScheduler;
//        return  new Scheduler(Executors.newSingleThreadExecutor());
    }

    public static Scheduler newSingle() {
        return new Scheduler(Executors.newSingleThreadExecutor());
    }


}
