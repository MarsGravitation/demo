package com.microwu.cxd;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * Description: mock 测试
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/22   9:58
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class MockTest {

    /**
     * 验证行为是否发生
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  9:59
     *
     * @param
     * @return  void
     */
    private static void test() {
        // 模拟创建一个List对象
        List<Integer> mock = Mockito.mock(List.class);
        // 调用mock 方法
        mock.add(1);
        mock.clear();
        // 验证行为是否发生
        Mockito.verify(mock).add(1);
        Mockito.verify(mock).clear();
    }

    /**
     * 多次触发返回不同值
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  10:04
     *
     * @param
     * @return  void
     */
    private static void test02() {
        // mock 一个Iterator 类
        Iterator iterator = Mockito.mock(Iterator.class);
        // 预设当iterator调用next时第一次返回hello，第n次都返回world
        Mockito.when(iterator.next()).thenReturn("hello").thenReturn("world");
        // 使用mock对象
        String s = iterator.next() + " " + iterator.next() + " " + iterator.next();
        // 验证结果
        Assert.assertEquals("hello world world", s);
    }

    /**
     * 模拟抛出异常
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  10:10
     *
     * @param
     * @return  void
     */
    @Test(expected = IOException.class)
    public static void test03() throws IOException {
        OutputStream mock = Mockito.mock(OutputStream.class);
        // 预设当流关闭时抛出异常
        Mockito.doThrow(new IOException()).when(mock).close();
        mock.close();
    }

    /**
     * 使用默认的Answer 模拟对象
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  10:13
     *
     * @param
     * @return  void
     */
    public void test04() {

    }

    public static void main(String[] args) throws IOException {
        test03();
    }

    class A {

    }
}