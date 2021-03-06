package com.carey.rx;

/**
 * 核心概念
 * Observable 与 Observer(Subscribe)
 * 装饰器
 * 所有的操作都是在Observable和Subcribe上做装饰
 * Created by carey on 2018/7/5 20:36:06.
 */
public class Example {
    public static void main(String[] args) {
//        simple();
//        transform();
//        observeOnThentransform();
        asySubscribeOn();
//    asyObserveOn();
//        Scheduler scheduler = Schedulers.io();
//        Scheduler.Worker worker = scheduler.createWorker();
//        worker.schedule(()->{
//            System.out.println(Thread.currentThread());
//        });
//        testZip();
//        skip();
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
                .observeOn(Schedulers.io(), 1)
                .observeOn(Schedulers.io(), 2)
                .map(new Transformer<Integer, String>() {
                    @Override
                    public String call(Integer from) {
                        System.out.println(Thread.currentThread() + "mapping ");
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
                .subscribeOn(Schedulers.s1)
                .subscribeOn(Schedulers.s2)
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(var1 + "Subscriber handle data @ " + Thread.currentThread());
                    }
                });

        //由于异步，这段会先输出
        System.out.println("ending");
    }


    private static void asyObserveOn() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                System.out.println("send a data@ " + Thread.currentThread().getName());
                subscriber.onNext(1);
//                subscriber.onNext(2);
            }
        })
                .observeOn(Schedulers.s1, 1)
                .observeOn(Schedulers.s2, 1)
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(var1 + "Subscriber handle data @ " + Thread.currentThread());
                    }
                })
        ;
    }


    private static void skip() {
        Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        })
                .skip(2)
                .subscribe(new SimpleSubscriber<Integer>() {
                    @Override
                    public void onNext(Integer var1) {
                        System.out.println(var1);
                    }
                });
    }


    public static void testZip() {
        Observable<Integer> o1=  Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        });
        Observable<Integer> o2=  Observable.create(new Observable.DataSource<Integer>() {
            @Override
            public void bind(Subscriber<? super Integer> subscriber) {
                for (int i = 10; i < 20; i++) {
                    subscriber.onNext(i);
                }
            }
        });
        Observable.zip(o1, o2, new Func<Integer, Integer, Object>() {

            @Override
            public Object apply(Integer integer, Integer integer2) {
                return integer + integer2;
            }
        }).subscribe(new SimpleSubscriber<Object>() {
            @Override
            public void onNext(Object var1) {
                System.out.println("Subscriber handle data @ " + Thread.currentThread().getName());
                System.out.println(var1);
            }
        });
    }

    private abstract static class SimpleSubscriber<T> implements Subscriber<T> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable t) {

        }
    }
}
