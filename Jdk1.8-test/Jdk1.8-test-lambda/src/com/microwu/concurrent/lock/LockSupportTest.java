package com.microwu.concurrent.lock;

/**
 * Description: LockSupport
 *  LockSupport 提供 park 和 unpark 方法实现阻塞线程和解除阻塞线程，实现的阻塞和解除阻塞是基于“许可（permit）”作为关联，permit 相当于一个信号量 (0,1)，默认是0.
 *  线程之间不需要一个 Object 或者其他变量来存储状态，不再需要关心对方的状态。
 *
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/10/20   10:51
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class LockSupportTest {
}