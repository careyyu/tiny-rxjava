package com.carey.rx;

/**
 *
 * @param <T>
 * @param <R>
 *
 * Created by carey on 2018/7/5 20:36:06.
 */
public class MapSubscriber<T, R> extends Subscriber<R> {
    final Subscriber<? super T> actual;
    final Transformer<? super R, ? extends T> transformer;

    public MapSubscriber(Subscriber<? super T> actual, Transformer<? super R, ? extends T> transformer) {
        this.actual = actual;
        this.transformer = transformer;
    }

    @Override
    public void onCompleted() {
        actual.onCompleted();
    }

    @Override
    public void onError(Throwable t) {
        actual.onError(t);
    }

    @Override
    public void onNext(R var1) {
        actual.onNext(transformer.call(var1));
    }
}