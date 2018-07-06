package com.carey.rx;

/**
 * Created by carey on 2018/7/5 20:36:06.
 */
public class MapDataSource<T, R> implements Observable.DataSource<R> {
    final Observable<T> source;
    final Transformer<? super T, ? extends R> transformer;

    public MapDataSource(Observable<T> source, Transformer<? super T, ? extends R> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public void bind(Subscriber<? super R> subscriber) {
        /**
         * 最终还是会调用MapSubscribe的onNext方法
         * 装饰器
         */
        source.subscribe(new MapSubscriber<R, T>(subscriber, transformer));
    }
}
