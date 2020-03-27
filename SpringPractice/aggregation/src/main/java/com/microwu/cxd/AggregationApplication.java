package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 接口的开发往往是从各处捞取数据, 然后组装成结果
 * 案例: 一个接口要实现: 拉取用户基础信息 + 用户的博客列表 + 用户的粉丝数据的整合数据
 *
 * 如果串行执行的话, 可能效率不高; 如果各个业务之间没有联系的话, 可以使用并行
 *      异步线程 + CountDownLatch + Future
 *
 */
@SpringBootApplication
public class AggregationApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(AggregationApplication.class, args);
    }
}
