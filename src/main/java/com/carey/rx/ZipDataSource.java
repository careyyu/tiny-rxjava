package com.carey.rx;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 将原有的数据源的数据全部提取出来，分别保存到一个queue中，然后依次从queue中将数据取出来
 * Created by carey on 2018/7/6 14:34:09.
 */
public class ZipDataSource<T, P, R> implements Observable.DataSource<R> {
    private Observable[] origins;
    private final Func<T, P, R> func;
    private ZipSourceAdaptor[] zipSourceAdaptors;
    private R[] convertResult;


    public ZipDataSource(Observable[] observables, Func<T, P, R> func) {
        this.origins= observables;
        this.func = func;
        this.convertResult = (R[]) new Object[origins.length];
    }

    @Override
    public void bind(Subscriber<? super R> subscriber) {
        for (ZipSourceAdaptor zipSourceAdaptor : zipSourceAdaptors) {
            zipSourceAdaptor.bind(null);
        }
    }

    private void drain(){

    }



    private class ZipSourceAdaptor<T> implements Observable.DataSource<T>{

        private Queue<T> queue;
        private ZipDataSource parent;
        private Observable observable;

        public ZipSourceAdaptor(ZipDataSource zipDataSource, Observable observable){
            this.parent = zipDataSource;
            this.observable = observable;
            this.queue = new ConcurrentLinkedDeque<>();
        }

        @Override
        public void bind(Subscriber<? super T> subscriber) {
            observable.subscribe(new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onNext(Object var1) {
                    queue.offer((T) var1);
                    parent.drain();

                }
            });
        }
    }
}
