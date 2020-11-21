package com.microwu.concurrent.future;

/**
 * Description: Promise, Future 和 Callback
 *  异步操作有两个经典接口：Future 和 Promise，其中 Future 表示一个可能还没有实际完成的异步任务的结果，针对这个结果可以添加 Callback 以便在任务执行成功
 *  或者失败后做出响应的操作，而 Promise 交由任务执行者，任务执行者通过 Promise 可以标记任务完成或者失败。可以说这一套模型是很多异步非阻塞架构的基础。
 *
 *  1. Future 将来式 - JDK
 *      future.get
 *  2. Future 回调式 - Guava
 *      future.addCallback();
 *  3. Future 回调式 - Netty4
 *      future.addCallback()
 *  4. 回调式 - JDK
 *      completableFuture
 *  5. Promise 模式
 *      回调地狱 - 回调的嵌套，ES6 提出 Promise 模式来解决
 *      在 Netty 中，
 *      Future 定义了 isSuccess(), isCancellable(), cause() 判断异步执行状态方法（read-only）。
 *      Promise 增加了 setSuccess(), setFailure() 来标记任务完成或者失败（writable）。
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   10:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class PromiseTest {
    public static void main(String[] args) {

    }
}