package com.carey.rx;


/**
 * 事件源 可被订阅者
 * Created by carey on 2018/7/5 20:36:06.
 */
public class Observable<T> {
    /**
     * 事件源
     */
    final DataSource<T> dataSource;

    private Observable(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 创建一个数据源
     * @param dataSource
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(DataSource<T> dataSource) {
        return new Observable<T>(dataSource);
    }

    /**
     * 绑定一个事件处理者，并告诉事件源 开始发送事件
     * @param subscriber
     */
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onStart();
        dataSource.bind(subscriber);
    }

    /**
     * 创建出一个新的Observable传递下去
     * 同时也创建了一个新的数据源(对老的数据源做了装饰)，
     * 在数据发送的时候可以做一些操作
     * @param transformer
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(Transformer<? super T, ? extends R> transformer) {
        return create(new MapDataSource<T, R>(this, transformer));
    }



    public Observable<T> skip(int i) {
        return create(new SkipDataSource<T>(this, i));
    }

    /**
     * 异步切线程 事件源的操作也在异步线程中
     * .subscribeOn()
     * .subscribeOn()
     * 只有第一个会生效，也会创建多个线程池
     * @param scheduler
     * @return
     */
    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new DataSource<T>() {
            @Override
            public void bind(Subscriber<? super T> subscriber) {
//                subscriber.onStart();
                // 将事件生产切换到新的线程
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Observable.this.dataSource.bind(subscriber);
                    }
                });
            }
        });
    }

    /**
     * 异步切线程 只有订阅者的处理事件在异步线程中
     *
     * .observeOn()
     * .observeOn()
     * 会创建多个线程， 但是在最后一个线程中执行
     * @param scheduler
     * @return
     */
    public Observable<T> observeOn(final Scheduler scheduler, int i ) {
        return Observable.create(new DataSource<T>() {
            final int index = i;

            public void bind(final Subscriber<? super T> subscriber) {
//
                System.out.println("i'am bing observeOn");
                final Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.dataSource.bind(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onCompleted();
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onError(t);
                            }
                        });
                    }

                    @Override
                    public void onNext(final T var1) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(Thread.currentThread() + String.valueOf(index));
                                subscriber.onNext(var1);
                            }
                        });
                    }
                });
            }
        });
    }


    /**
     * 数据源
     * @param <T>
     */
    public interface DataSource<T> {
        /**
         * 绑定数据消费者并开始给下游发送消息
         * @param subscriber
         */
        void bind(Subscriber<? super T> subscriber);
    }

}
