package com.microwu.concurrent.sync;

import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * 中断线程：
 *  线程的 thread.interrupt() 方法是中断线程，将会设置该线程的中断状态位，及设置为 true，
 *  中断的结果是线程是死亡，还是等待新的任务或是继续运行至下一步，就取决于这个程序本省。线程
 *  会不时地检测这个中断标志位，以判断线程是否应该被中断（中断标示值是否为 true）。它并不像
 *  stop 方法那样会中断一个正在运行的线程。
 *
 * 判断县城是否被中断
 *  isInterrupted - 不会立刻清楚中断标示位
 *  interrupted - 该方法调用后会将中断标示位清除
 *
 *  while(!Thread.currentThread().isInterrupted() && more work to do) {
 *      do more work
 *  }
 *
 * 如何中断线程
 *  如果一个线程处于阻塞状态（sleep，join，wait，condition.await），则在线程在检查中断标识时，
 *  如果发现中断标识为 true，则会在这些阻塞调用处抛出 InterruptedException，并且在抛出异常后
 *  立即将该线程的中断标示位清除，即重新设置为 false。抛出异常是为了线程从阻塞状态醒过来，并在结束
 *  线程钱让程序员有足够的时间来处理中断请求。
 *
 *  没有任何语言方便的需求一个被中断的线程应该终止。中断一个线程只是为了引起该线程的注意，被中断线程
 *  可以决定如何应对中断。某些线程非常重要，以至于他们应该不理会中断，而是在处理完抛出异常之后继续执行，
 *  但是更普遍的情况是，一个线程把中断看作一个终止请求
 *
 *  public void run() {
 *      try {
 *          ...
 *          while (!Thread.currentThread().isInterrupted() && more work to do) {
 *              do more work
 *          }
 *      } catch (InterruptedException e) {
 *          // 线程在 wait 活 sleep 期间被中断了
 *      } finally {
 *          // 线程结束前做一些清理工作
 *      }
 *  }
 *
 * 底层中断异常处理方式
 *  1. catch 字句中，设置中断状态，让外界判断是否终止线程还是继续下去
 *     try {
 *         sleep(delay);
 *     } catch (InterruptedException e) {
 *         Thread.currentThread().isInterrupted();
 *     }
 *  2. 直接抛出异常
 *      void mySubTask() throws InterruptedException {
 *      ...
 *      sleep(delay);
 *      ...
 *      }
 *
 * 中断应用
 *  1. 使用中断信号量中断非阻塞状态的线程
 *      volatile boolean stop = false;
 *
 *      public void run() {
 *          while (!stop) {
 *
 *          }
 *      }
 *
 *      stop = true;
 *  2. 使用 thread.interrupt 中断非阻塞状态线程
 *      public void run() {
 *          while (!Thread.currentThread().isInterrupted()) {
 *
 *          }
 *      }
 *
 *      thread.interrupt();
 *  3. 使用 thread.interrupt() 中断阻塞状态线程
 *      public void run() {
 *          while (!Thread.currentThread().isInterrupted()) {
 *              // 线程阻塞，如果线程收到中断操作信号将抛出异常
 *              Thread.sleep(1000);
 *          } catch (InterruptedException e) {
 *              // 中不中断由自己决定，如果真需要中断线程，需要重新设置
 *              // 否则可以不调用
 *              Thread.currentThread().interrupt();
 *          }
 *      }
 *
 * https://www.cnblogs.com/onlywujun/p/3565082.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/2   17:13
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class InterruptedThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while(true) {
                    if(this.isInterrupted()) {
                        System.out.println("线程中断...");
                        break;
                    }
                }
                System.out.println("跳出循环");
            }
        };
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
    }
}