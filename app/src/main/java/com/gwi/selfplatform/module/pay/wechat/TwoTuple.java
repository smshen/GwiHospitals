package com.gwi.selfplatform.module.pay.wechat;

/**
 * Created by Administrator on 2016-5-10.
 */

/**
 * 两个元素的元组，用于在一个方法里返回两种类型的值
 *
 * @param <A>
 * @param <B>
 */
public class TwoTuple<A, B> {
    public final A first;
    public final B second;

    public TwoTuple(A a, B b) {
        first = a;
        second = b;
    }
}