package com.carey.rx;

/**
 * Created by carey on 2018/7/6 14:28:22.
 */
public interface Func<T , P , R > {
    /**
     * 转换
     * @param t
     * @param p
     * @return
     */
    R apply(T t, P p);
}
