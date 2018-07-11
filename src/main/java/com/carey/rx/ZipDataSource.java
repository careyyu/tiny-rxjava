package com.carey.rx;

import java.util.Queue;

/**
 * 将原有的数据源的数据全部提取出来，分别保存到一个queue中，然后依次从queue中将数据取出来
 * Created by carey on 2018/7/6 14:34:09.
 */
public class ZipDataSource<T, P, R> implements Observable.DataSource<R> {
    private Observable[] origins;
    private final Func<T, P, R> func;
    private ZipSourceAdaptor[] zipSourceAdaptors;


    public ZipDataSource(Observable[] observables, Func<T, P, R> func) {
        this.origins= observables;
        this.func = func;

    }

    @Override
    public void bind(Subscriber<? super R> subscriber) {
        zip().bind(subscriber);


    }

    /**
     * 执行实际的数据融合
     * @return
     */
    private Observable.DataSource<R> zip() {
        return null;
    }

    private class ZipSourceAdaptor<T> {

        private Queue<T> queue;
        private ZipDataSource parent;
        private Observable observable;

        public ZipSourceAdaptor(ZipDataSource zipDataSource, Observable observable){
            this.parent = zipDataSource;
            this.observable = observable;
        }

    }
}
