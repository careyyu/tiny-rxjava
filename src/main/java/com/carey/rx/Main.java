package com.carey.rx;

/**
 * 核心概念
 * Observable 与 Observer
 * 装饰器
 * Created by carey on 2018/7/5 20:36:06.
 */
public class Main {
    public static void main(String[] args) {
//        simple();
//        transform();
        observeOnThentransform();
//    asySubscribeOn();
//    asyObserveOn();
//        Scheduler scheduler = Schedulers.io();
//        Scheduler.Worker worker = scheduler.createWorker();
//        worker.schedule(()->{
//            System.out.println(Thread.currentThread());
//        });
    }

    /**
     * 最基础的rx模式 单纯的观察者模式
     */
    private static void simple() {
        Observable<Integer> observable = Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        });

        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onStart() {
                System.out.println("i'm starting");
            }

            @Override
            public void onCompleted() {
                System.out.println("i'm completed");
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onNext(Integer var1) {
                System.out.println(var1);
            }
        });

    }


    /**
     * 转换
     */
    private static void transform() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).map(new Transformer<Integer, String>() {
            @Override
            public String call(Integer from) {
                return "maping " + from;
            }
        }).subscribe(new SimpleSubscriber<String>() {
            @Override
            public void onNext(String var1) {
                System.out.println(var1);
            }
        });
    }


    /**
     * 转换
     */
    private static void observeOnThentransform() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        })
                .observeOn(Schedulers.io(),1)
                .observeOn(Schedulers.io(),2)
                .map(new Transformer<Integer, String>() {
                    @Override
                    public String call(Integer from) {
                        System.out.println( Thread.currentThread() + "mapping ");
                        return "maping " + from;
                    }
                })
                .subscribe(new SimpleSubscriber<String>() {
                    @Override
                    public void onNext(String var1) {
                        System.out.println(Thread.currentThread() + var1);
                    }
                });
    }

    /**
     * 异步
     */
    private static void asySubscribeOn() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                System.out.println("send a data @ " + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer var1) {
                        System.out.println("Subscriber handle data @ " + Thread.currentThread().getName());
                        System.out.println(var1);
                    }
                });
    }


    private static void asyObserveOn() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                System.out.println("send a data@ " + Thread.currentThread().getName());
                subscriber.onNext(1);
            }
        })
                .observeOn(Schedulers.io(),1)
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer var1) {
                        System.out.println("Subscriber handle data @ " + Thread.currentThread().getName());
                        System.out.println(var1);
                    }
                });
    }


    private abstract static class SimpleSubscriber<T> extends Subscriber<T> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable t) {

        }
    }
}
