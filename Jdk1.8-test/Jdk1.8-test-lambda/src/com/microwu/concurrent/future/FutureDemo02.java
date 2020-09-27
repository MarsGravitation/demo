package com.microwu.concurrent.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Description:
 *  1. 新建一个 Callable 匿名函数实现类对象，我们的业务逻辑在 Callable 的 call 方法中实现，
 *      其中 Callable 的泛型是返回结果类型
 *  2. 然后把 Callable 匿名函数对象作为 FutureTask 的构造函数传入，构建一个 futureTask 对象
 *  3. 然后把 futureTask 对象作为 Thread 构造参数传入并开启这个线程执行去执行业务逻辑
 *  4. 最后调用 futureTask get 方法得到业务逻辑执行结果
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/2   10:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FutureDemo02 {

    /**
     * 情景：假如我们要打火锅，首先我们要准备两样东西，把水烧开和准备食材。
     *  因为烧开水是一个比较漫长的过程（相当于耗时的业务逻辑），因此我们
     *  可以一边烧开水（相当于另起一个线程），一边准备火锅食材（主线程），
     *  等两者都准备好了，我们就可以开始打火锅
     *
     *  还有一种是使用线程池执行，不过原理都差不多
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/2  10:37
     *
     * @param   	args
     * @return  void
     */
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName() + ": 开始烧开水。。。");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + ": 开水烧好了。。。");
                return "开水";
            }
        });

        // 起一个线程烧开水
        new Thread(futureTask).start();

        // 主线程准备食材
        System.out.println(Thread.currentThread().getName() + ": 此时开启一个新线程开始烧开水，现在我们准备食材。。。");
        Thread.sleep(4000);
        System.out.println(Thread.currentThread().getName() + " 火锅食材准备好了。。。");
        String food = "火锅食材";

        // 开水已经烧好
        String boilWater = futureTask.get();

        System.out.println(Thread.currentThread().getName() + ": " + boilWater + ", " + food);

    }
}