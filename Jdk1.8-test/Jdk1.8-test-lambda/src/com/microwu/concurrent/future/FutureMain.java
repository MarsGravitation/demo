package com.microwu.concurrent.future;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *  1. ExecutorService 会执行 run 方法
 *      |- FutureTask run：执行 call 方法，如果成功设置返回值，如果失败，设置异常
 *  2. get
 *      |- 判断 state 是否完成，进行自旋 - awaitDone，根据 state 返回结果
 *  3. awaitDone
 *      1) 支持线程中断
 *          a. 中断，退出自旋，在线程队列中移除对应的节点
 *          b. 不中断，继续下面的步骤
 *      2) cas 将当前线程构造一个 WatierNode 节点
 *      3) 是否设置超时时间
 *          a. 有，调用 LockSupport.parkNano;阻塞结束后，继续自旋
 *          b. 没有，调用 LockSupport.park
 *
 *
 *
 *  https://www.jianshu.com/p/b765c0d0165d
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/24   10:35
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class FutureMain {
    public static void main(String[] args) throws Exception{

        LocalCacheConnection localCacheConnection = new LocalCacheConnection();
        Future<?> future = localCacheConnection.getResult("connection");

        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("future.get() : " + future.get(2 , TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("future.get() over");
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("future.get() : " + future.get(4, TimeUnit.SECONDS));
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("future.get() over");
                }
            }
        }.start();
    }
}