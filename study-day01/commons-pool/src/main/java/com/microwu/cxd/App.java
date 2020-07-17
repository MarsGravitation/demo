package com.microwu.cxd;

import com.microwu.cxd.factory.MyPoolObjectFactory;
import com.microwu.cxd.pojo.Resource;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *  Common-pool2 由三大模块组成：ObjectPool、PooledObject 和 PooledObjectFactory
 *  ObjectPool：提供所有对象的存储管理
 *  PooledObject：池化的对象，是对对象的一个包装，加上了对象的一些其他信息，包括对象的状态、对象的创建时间等
 *  PooledObjectFactory：工厂类，负责池化对象的创建，对象的初始化，对象状态的销毁和对象状态的验证
 *  ObjectPool 会持有 PooledObjectFactory，将具体的对象的创建、初始化、销毁等任务交给它处理，其操作对象是 PooledObject，即 Object 的包装类
 *
 *  GenericObjectPool：数据结构 = ConcurrentHashMap + LinkedBlockingDeque
 *  前者用于存储所有的对象，后者用于存储空闲的对象
 *
 *  borrowObject 大体思路：
 *      1. 从 Deque 中 pollFirst
 *      2. 若空，检查对象池是否达到上限，达到上限，重复1，没有达到上限，调用 PooledObjectFactory 的 makeObject 去创建一个对象
 *      3. 得到对象之后，对对象进行初始化和一些配置的计数处理，同时将对象放入到 ConcurrentHashMap
 *
 *  returnObject 大体思路：
 *      1. 根据 obj 从 ConcurrentHashMap 拿到对应的 PooledObject p
 *      2. 判空，将p 状态置为 RETURN
 *      3. 若 getTEstOnReturn 参数为 true，进行 validateObject
 *      4. 第p 进行 passivateObject，与初始化相反
 *      5. 更新p 状态为 IDLE
 *      6. 归还 Pool。Pool 的 idle 实例达到上限或者 Pool 已经关闭，销毁，否则加入 Deque
 *
 *
 *  案例： https://www.jianshu.com/p/f403f1782d1c?utm_campaign
 *  原理： https://www.cnblogs.com/jinzhiming/p/5120623.html
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/30   15:00
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class App {
    public static void main(String[] args) {
        // 创建池对象工厂
        MyPoolObjectFactory myPoolObjectFactory = new MyPoolObjectFactory();

        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig<>();
        // 最大空闲数
        poolConfig.setMaxIdle(5);
        // 最小空闲数，池中只有一个空闲对象时，池会再创建一个对象，并借出一个对象，从而保证最小空闲数
        poolConfig.setMinIdle(1);
        // 最大池对象总数
        poolConfig.setMaxTotal(20);
        // 逐出连接的最小空闲时间，默认30 分钟
        poolConfig.setMinEvictableIdleTimeMillis(1800000);
        // 逐出扫描的时间间隔 - 毫秒，如果为负数，则不运行逐出线程，默认 -1
        poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
        // 在获取对象的时候检查有效性，默认 false
        poolConfig.setTestOnBorrow(false);
        // 在空闲时检查有效性，默认 false
        poolConfig.setTestWhileIdle(false);
        // 最大等待时间，默认 -1，无限等待
        poolConfig.setMaxWaitMillis(5000);
        // 是否启用后进先出，默认 true
        poolConfig.setLifo(true);
        // 连接耗尽时是否阻塞， false 报异常，true 阻塞直到超时，默认 true
        poolConfig.setBlockWhenExhausted(true);
        // 每次逐出检查时，逐出的最大数目 默认 3
        poolConfig.setNumTestsPerEvictionRun(3);

        // 创建对象池
        GenericObjectPool<Resource> pool = new GenericObjectPool<Resource>(myPoolObjectFactory, poolConfig);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(40, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(30));
        for (int i = 0; i < 40; i++) {
            executor.execute(() -> {
                try {
                    // 注意：如果对象池没有空余的对象，这里会 block，可以设置 block 的超时时间
                    Resource resource = pool.borrowObject();
                    System.out.println(resource);
                    Thread.sleep(1000);
                    // 申请的资源用完了记得归还，不然其他人要申请时可能就没有资源用了
                    pool.returnObject(resource);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
    }
}