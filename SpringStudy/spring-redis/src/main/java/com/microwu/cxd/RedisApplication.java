package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 * 数据一致性：
 *
 * Cache-Aside Pattern，旁路缓存模式
 *  读流程：
 *      先读缓存，缓存命中，直接返回数据
 *      缓存没有命中，读数据库，从数据库中取出数据，放入缓存，同时返回响应
 *  写流程：
 *      更新的时候，先更新数据库，然后再删除缓存
 *
 * Read-Through/Write-Through 读写穿透
 *  Read/Write-Through 模式中，服务端把缓存作为主要数据存储。应用程序跟数据库缓存交互，
 *  都是通过抽象层完成
 *
 *  Read-Through 只是在 Cache-Aside 进行了封装
 *  读流程：
 *      从 Cache Provider 获取，抽象层从缓冲中获取，数据不存在，抽象层从数据库读取数据，
 *      将数据放入缓存，然后返回
 *
 *  写流程：
 *      更新抽象层，抽象层更新数据库，然后更新缓存
 *
 * Write-behind 异步缓存写入
 *  Write-behind 跟 Read-Through/Write-Through 有相似的地方，都是由 Cache Provider
 *  负责缓存和数据库的读写。他们又有个很大的不同：Read/Write-Through 是同步更新缓存和数据的，
 *  Write-Behind 则是只更新缓存，不直接更新数据库，通过批量异步的方式来更新数据库
 *
 *  这种方式下，缓存和数据库的一致性不强，对一致性要求高的系统要谨慎使用。但它适合频繁写的场景
 *
 * 如何操作缓存
 *  平时我们一般使用的是 Cache-Aside
 *  这里我们使用的是删除缓存，而不是更新缓存。因为更新数据库，更新缓存不是原子操作，可能存在并发问题，
 *  出现脏数据
 *
 * 双写
 *  为什么先操作数据库，而不是先操作缓存？
 *
 *  线程 A 删除缓存，线程 B 读，将数据库的老数据读入缓存，线程 A 更新数据，导致数据不一致
 *
 *
 *
 * https://mp.weixin.qq.com/s/vYq9lRS8KeWOIqjCC01fqQ
 *
 */
@SpringBootApplication
public class RedisApplication {
    public static void main( String[] args )
    {
        SpringApplication.run(RedisApplication.class, args);
    }
}
