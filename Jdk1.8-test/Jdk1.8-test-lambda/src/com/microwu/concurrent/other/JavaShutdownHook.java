package com.microwu.concurrent.other;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Java Shutdown Hook 场景使用和源码分析
 *
 * 如果想在 Java 进程退出时，包括正常和异常，做一些额外处理工作，例如资源清理，对象销毁，内存数据
 * 持久化到磁盘，等待线程池处理完所有任务等等。特别时进程异常挂掉的情况，如果一些重要状态没及时保留下
 * 来，或者线程池的任务没被处理完，有可能会造成严重问题。
 *
 * 我们可以通过 Java.Runtime.addShutdownHook(Thread hook) 方法向 JVM 注册关闭钩子，在 JVM
 * 退出之前会自动调用执行钩子方法，做一些结尾操作，从而让进程平滑优雅的退出，保证了业务的完整性。
 *
 * shutdown hook 就是一个简答的已初始化但是未启动的线程。当虚拟机开始关闭时，它将会调用所有已注册的钩子，
 * 这些钩子执行是并发的，执行顺序是不确定的
 *
 * 关闭钩子被调用场景
 *  1. 程序正常退出
 *  2. 程序调用 System.exit() 退出
 *  3. 终端使用 Ctrl + C 中断程序
 *  4. 程序抛出异常导致程序退出，例如 OOM，数组越界等异常
 *  5. 系统事件，例如用户注销活关闭系统
 *  6. 使用 kill pid
 *
 * Shutdown Hook 在 Spring 中的应用
 *
 *  @Override
 * public void registerShutdownHook() {
 * 	if (this.shutdownHook == null) {
 * 		// No shutdown hook registered yet.
 * 		this.shutdownHook = new Thread() {
 *                        @Override
 *            public void run() {
 * 				synchronized (startupShutdownMonitor) {
 * 				    // 钩子方法
 * 					doClose();
 *                }
 *            }        * 		};
 * 		// 底层还是使用此方法注册钩子
 * 		Runtime.getRuntime().addShutdownHook(this.shutdownHook);
 * 	}
 * }
 *
 * doClose 会做一些虚拟机关闭前处理工作，例如销毁容器里所有单例 bean，关闭 BeanFactory，关闭发布事件等等
 *
 *  protected void doClose() {
 * 	// Check whether an actual close attempt is necessary...
 * 	if (this.active.get() && this.closed.compareAndSet(false, true)) {
 * 		if (logger.isDebugEnabled()) {
 * 			logger.debug("Closing " + this);
 *                }
 *
 * 		LiveBeansView.unregisterApplicationContext(this);
 *
 * 		try {
 * 			// 发布Spring 应用上下文的关闭事件，让监听器在应用关闭之前做出响应处理
 * 			publishEvent(new ContextClosedEvent(this));
 *        }
 * 		catch (Throwable ex) {
 * 			logger.warn("Exception thrown from ApplicationListener handling ContextClosedEvent", ex);
 *        }
 *
 * 		// Stop all Lifecycle beans, to avoid delays during individual destruction.
 * 		if (this.lifecycleProcessor != null) {
 * 			try {
 * 			    // 执行lifecycleProcessor的关闭方法
 * 				this.lifecycleProcessor.onClose();
 *            }
 * 			catch (Throwable ex) {
 * 				logger.warn("Exception thrown from LifecycleProcessor on context close", ex);
 *            }
 *        }
 *
 * 		// 销毁容器里所有单例Bean
 * 		destroyBeans();
 *
 * 		// 关闭BeanFactory
 * 		closeBeanFactory();
 *
 * 		// Let subclasses do some final clean-up if they wish...
 * 		onClose();
 *
 * 		// Reset local application listeners to pre-refresh state.
 * 		if (this.earlyApplicationListeners != null) {
 * 			this.applicationListeners.clear();
 * 			this.applicationListeners.addAll(this.earlyApplicationListeners);
 *        }
 *
 * 		// Switch to inactive.
 * 		this.active.set(false);    * 	}
 * }
 *
 *  bean 实现 DisposableBean 接口，重写 destroy 对象销毁方法。destroy 方法就是在 Spring 注册的关闭钩子
 *  里被调用的。例如 ThreadPoolTaskExecutor 线程池类，它实现了 DisposableBean 接口，重写了 destroy 方法，
 *  从而在程序退出前，进行线程池销毁工作
 *
 * https://www.cnblogs.com/luciochn/p/14878160.html
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/16  14:18
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class JavaShutdownHook {

    /**
     * 线程池
     */
    private static ExecutorService executorService = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<>());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("执行钩子方法。。。");
        }));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("开始执行钩子方法。。。");
            // 关闭线程池
            executorService.shutdown();
            System.out.println("结束执行钩子方法。。。");
        }));
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("程序开始启动。。。");
        // 向线程池添加 10 个任务
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            executorService.execute(() -> {
                System.out.println("Task " + finalI + " execute...");
            });
        }
        Thread.sleep(2000);
        System.out.println("程序即将退出。。。");

        System.exit(0);
    }

}