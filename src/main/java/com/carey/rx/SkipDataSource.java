package com.carey.rx;

/**
 * Created by carey on 2018/7/6 09:04:48.
 */
public class SkipDataSource<T> implements Observable.DataSource {
    private Observable origin;
    private int remaining;

    public SkipDataSource(Observable source, int skipCnt) {
        this.origin = source;
        this.remaining = skipCnt;
    }

    @Override
    public void bind(Subscriber subscriber) {

        origin.subscribe(new Subscriber() {
            @Override
            public void onCompleted() {
                subscriber.onCompleted();
            }

            @Override
            public void onError(Throwable t) {
                subscriber.onError(t);
            }

            @Override
            public void onNext(Object var1) {
                if (remaining > 0) {
                    remaining--;
                } else {
                    subscriber.onNext(var1);
                }

            }
        });
    }
}
