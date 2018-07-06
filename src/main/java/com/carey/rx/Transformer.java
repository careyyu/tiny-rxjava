package com.carey.rx;

/**
 * 转换器
 * Created by carey on 2018/7/5 20:01:00.
 */
public interface Transformer<T, R> {
    /**
     * 转换
     * @param from
     * @return
     */
    R call(T from);
}
