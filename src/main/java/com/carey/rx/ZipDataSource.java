package com.carey.rx;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 将原有的数据源的数据全部提取出来，分别保存到一个queue中，然后依次从queue中将数据取出来
 * Created by carey on 2018/7/6 14:34:09.
 */
public class ZipDataSource<T, P, R> implements Observable.DataSource<R> {
    private Observable[] origins;
    private final Func<T, P, R> func;
    //    private ZipSourceAdaptor[] zipSourceAdaptors;
    private final Queue[] queues;
    private R[] convertResult;


    public ZipDataSource(Observable[] observables, Func<T, P, R> func) {
        this.origins = observables;
        this.func = func;
        this.convertResult = (R[]) new Object[origins.length];
        this.queues = new ArrayDeque[origins.length];
        for (int i =origins.length -1; i>=0;i--) {
            queues[i] = new ArrayDeque();
        }
    }

    @Override
    public void bind(Subscriber<? super R> subscriber) {
        int i = 0;
        for (Observable o : origins) {

            final Queue q = queues[i];
            o.subscribe(new Subscriber() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onNext(Object var1) {
                    q.offer(var1);
                }
            });
            i++;
        }

        while (true) {
            boolean end = false;
            ArrayList list = new ArrayList();
            for (Queue q : queues) {
                Object o = q.poll();
                if (o == null) {
                    end = true;
                    break;
                }
                list.add(o);
            }
            if (end) {
                break;
            }
            R r = func.apply((T) list.get(0), (P) list.get(1));
            subscriber.onNext(r);
        }
    }

    private void drain() {

    }


//    private class ZipSourceAdaptor<T> implements Observable.DataSource<T>{
//
//        private Queue<T> queue;
//        private ZipDataSource parent;
//        private Observable observable;
//
//        public ZipSourceAdaptor(ZipDataSource zipDataSource, Observable observable){
//            this.parent = zipDataSource;
//            this.observable = observable;
//            this.queue = new ConcurrentLinkedDeque<>();
//        }
//
//        @Override
//        public void bind(Subscriber<? super T> subscriber) {
//            observable.subscribe(new Subscriber() {
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onError(Throwable t) {
//
//                }
//
//                @Override
//                public void onNext(Object var1) {
//                    queue.offer((T) var1);
//                    parent.drain();
//
//                }
//            });
//        }
//    }
}
