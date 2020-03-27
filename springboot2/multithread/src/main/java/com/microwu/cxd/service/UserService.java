package com.microwu.cxd.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Description:     Spring异步编程:
 *      1. 循环依赖异常
 *          开启@EnableAsync 和 @Async注解所在的Bean被循环依赖
 *      2. 异步失效
 *          在本类中使用了异步是不支持异步的
 *          调用者其实是this, 不是真正的代理对象, Spring无法截获这个方法调用
 *      3. 事务失效异常
 *      4. 异步嵌套异常
 *          在本类中异步嵌套异步是不支持的
 *          调用者其实被代理过一次, 在嵌套会出现二次代理, 达不到代理的效果
 *       5. 基本类型异常
 *          这种异步不支持返回结果, 因为这个异步线程被主线程出发后提交到线程池处理异步任务,
 *          线程之间通信是不能之间返回的
 *       6. 返回异步结果
 *
 * 注意事项:
 *      1. 尽量不要在本类中异步调用
 *      2. 尽量不要有返回值
 *      3. 不能使用本类的私有方法或者非接口化加@Aysnc, 因为代理实效
 *      4. 异步方法不能用static修饰
 *      5. 异步类没有使用@Component修饰, 导致Spring无法扫描异步类
 *      6. 需要自动注入, 不能手动new对象
 *      7. 启动类必须加@EnableAsync
 *      8. 在调用Async方法上标注@Transaction 是管理调用方法的事务的
 *      9. 在Async方法标注@Transaction是管理异步方法的事务, 事务因线程隔离
 *
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/8   11:09
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class UserService {
    // 循环依赖 - 导致项目无法启动
//    @Autowired
//    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private volatile Integer count = 0;

    @Async("executors")
    public void sendMsg() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        logger.info("给第 {} 个用户发送短信...", count++);

    }

    @Async("executors")
    public void sendMail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        logger.info("给第 {} 个用户发送邮件...", count++);
    }

    public void send() throws InterruptedException {
        this.sendMsg();
        this.sendMail();
    }
}