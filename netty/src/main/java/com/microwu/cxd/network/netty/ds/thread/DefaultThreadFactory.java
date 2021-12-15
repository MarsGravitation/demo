package com.microwu.cxd.network.netty.ds.thread;

import io.netty.util.internal.ObjectUtil;
import io.netty.util.internal.StringUtil;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 如何安全高效的自定义有意义的线程名
 *
 * 补充：
 *  1. LinkedBlockingQueue 无界阻塞队列，高负载很容易把队列打爆，导致 OOM
 *  2. 默认策略会抛出 RejectedExecutedExecutionException，是一个运行时异常，很容易被忽略
 *  3. 建议实现 JDK 的线程池工厂，自定义有意义的线程池和线程的名字
 *      |- 自定义线程池的命名规则，配置线程池的队列界限，自定义拒绝策略，并且稳妥的处理异常
 *      |- 除了配置合适的拒绝策略外，还要配合服务降级策略协同处理
 *
 *  推荐的线程池异常捕获写法：
 *      try {
 *          // 业务逻辑
 *      } catch (RuntimeException runtimeException) {
 *          // 运行时异常
 *      } catch (Throwable throwable) {
 *          // 异常
 *      }
 *
 * https://mp.weixin.qq.com/s?__biz=MzU1NjY0NzI3OQ==&mid=2247485240&idx=1&sn=cf70c90d768c066467a0b2c1fbb3b97a&chksm=fbc09138ccb7182e7159098348fe789b71dfc79768e3ad770f5706aa9b22824d16608938b7de&scene=21#wechat_redirect
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/16  19:28
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolId = new AtomicInteger();

    private final AtomicInteger nextId = new AtomicInteger();
    private final String prefix;
    private final boolean daemon;
    private final int priority;
    protected final ThreadGroup threadGroup;

//    public DefaultThreadFactory(Class<?> poolType, int priority) {
//        this(poolType, false, priority);
//    }
//
//    public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
//        this(toPoolName(poolType), daemon, priority);
//    }

    public DefaultThreadFactory(String poolName, boolean daemon, int priority, ThreadGroup threadGroup) {
        ObjectUtil.checkNotNull(poolName, "poolName");

        if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
            throw new IllegalArgumentException(
                    "priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
        }

        // 主要是构造一个前缀，属于线程池名字，以 - 连接，后续是递增的池编号
        // 原子类包装，保证线程安全
        prefix = poolName + '-' + poolId.incrementAndGet() + "-thread-";
        this.daemon = daemon;
        this.priority = priority;

//        SecurityManager s = System.getSecurityManager();
//        group = (s != null) ? s.getThreadGroup() :
//                Thread.currentThread().getThreadGroup();
        this.threadGroup = threadGroup;
    }

    private static String toPoolName(Class<?> poolType) {
        if (poolType == null) {
            throw new NullPointerException("poolType");
        }
        // 拿到 NioEventLoop 的简单类名
        String poolName = StringUtil.simpleClassName(poolType);
        // 做字符串转换
        switch (poolName.length()) {
            case 0:
                return "unknown";
            case 1:
                return poolName.toLowerCase(Locale.US);
            default:
                // 如果第一个字符是大写，第二个字符是小写，就做一个转换
                if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
                    // NioEventLoop -> nioEventLoop
                    return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
                } else {
                    return poolName;
                }
        }
    }

//    public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
//
//    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r,
                prefix + nextId.incrementAndGet(),
                0);
        try {
            if (t.isDaemon() != daemon) {
                t.setDaemon(daemon);
            }

            if (t.getPriority() != priority) {
                t.setPriority(priority);
            }
        } catch (Exception ignored) {
            // Doesn't matter even if failed to set.
        }
        return t;
    }
}
