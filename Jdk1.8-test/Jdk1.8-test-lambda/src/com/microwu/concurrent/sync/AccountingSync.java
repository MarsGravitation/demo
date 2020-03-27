package com.microwu.concurrent.sync;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/2   15:37
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AccountingSync implements Runnable {
    /**
     * 贡献资源 - 临界资源
     */
    private static int i = 0;

    /**
     * synchronized 修饰实例方法
     *  当前实例锁
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/2  15:39
     *
     * @param
     * @return  void
     */
    public synchronized void increase() {
        i++;
    }

    @Override
    public void run() {
        for(int i = 0; i < 1000000; i++) {
            increase();
        }
    }

    /**
     * 共享资源变量i, 由于i++ 并不具备原子性, 该操作先读值,
     * 如果第二个线程在第一个线程读取旧值和写入新值期间读取i, 那么两个线程
     * 会看到同一个值, 并且执行相同值的+1 操作, 出现线程安全问题
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/12/2  15:41
     *
     * @param   	args
     * @return  void
     */
    public static void main(String[] args) throws InterruptedException {
//        AccountingSync accountingSync = new AccountingSync();
//        Thread t1 = new Thread(accountingSync);
//        Thread t2 = new Thread(accountingSync);

        Thread t1 = new Thread(new AccountingSync());
        Thread t2 = new Thread(new AccountingSync());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}