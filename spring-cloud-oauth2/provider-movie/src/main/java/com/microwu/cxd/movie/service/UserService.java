package com.microwu.cxd.movie.service;

import com.microwu.cxd.movie.domain.User;
import feign.Logger;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/5/8   15:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
//@FeignClient(name = "PROVIDER-USER", configuration = UserFeignConfig.class, fallback = UserFeignClientFallback.class)
//@FeignClient(name = "PROVIDER-USER", configuration = UserFeignConfig.class, fallbackFactory = UserFeignClientFallbackFactory.class)
@FeignClient(name = "PROVIDER-USER", configuration = UserFeignConfig.class)
public interface UserService {

    /**
     * 如果使用@PathVariable 必须指定value 属性，
     * 但是 G版本好像也可以不指定
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/13  10:40
     *
     * @param   	id
     * @return  java.lang.String
     */
    @GetMapping("/user/{id}")
    String hello(@PathVariable Long id);

    /**
     * 如果使用get 发送多参数，我推荐使用 map
     *  @RequestMapping(value = "/get", method = RequestMethod.GET)
     *  public User get0(User user);
     *  feign.FeignException: status 405 reading UserFeignClient#get0(User); content:
     * {"timestamp":1482676142940,"status":405,"error":"Method Not Allowed","exception":"org.springframework.web.HttpRequestMethodNotSupportedException","message":"Request method 'POST' not supported","path":"/get"}
     *
     * 这种异常情况我没有测试
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/13  10:41
     *
     * @param   	map
     * @return  com.microwu.cxd.movie.domain.User
     */
    @GetMapping("/test/get")
    User get(@RequestParam Map<String, Object> map);

    @PostMapping("/test/post")
    User postUser(User user);
}

/**
 * 默认 Feign 是不打印日志的
 *  NONE：性能最佳，适用于生产，不记录任何日志，默认值
 *  BASIC：适用于生产环境追踪问题，仅记录请求方法，URL，响应状态码以及执行时间
 *  HEADERS：在上一个级别基础上，记录请求和响应的header
 *  FULL：比较适用于开发及测试环境定位问题，记录请求和响应的header，body和元数据
 *
 *  该Feign Client 的配置类，注意：
 *      1. 该类可以独立出去
 *      2. 该类上也可以添加@Configuration声明是一个配置类
 *          但是千万别放在竹应用程序上下文@ComponentScan 扫描的包中，
 *          否则，该配置将会被所有Feign Client 共享，无法实现细粒度配置
 *   建议：不加@Configuration注解
 *
 *   这个测试失败，没有debug 日志
 */
class UserFeignConfig {
    @Bean
    public Logger.Level logger() {
        return Logger.Level.FULL;
    }
}

/**
 * 熔断器是一个延迟和容错库，用于隔离访问远程系统，服务或者第三方库，
 * 防止级联失败，从而提升系统的可用性和容错性
 *
 *  1. 包括请求，使用HystrixCommand 包括对依赖的的调用逻辑，每个命令在独立的线程中执行
 *      用到了设计模式中的 命令模式
 *
 *  2. 资源隔离，当某服务的错误率超过一定阈值时，Hystrix 可以自动或手动跳闸，停止请求该服务一段时间
 *  3. 资源隔离，Hystrix 为每个依赖都维护了一个小型线程池（或者信号量）。如果该线程池已满，发往该
 *      依赖的请求就被立即拒绝，而不是排队等待，从而加速失败判定
 *   4. 监控，Hystrix 可以近乎实时的监控运行指标的配置的变化，例如成功，失败，超时以及被拒绝的请求等
 *   5. 回退机制，当请求失败，超时，被拒绝时，或者当断路器打开时，执行回退逻辑。回退逻辑可有开发人员
 *      自行提供
 *   6. 自我修复，断路器打开一段时间后，会进入半开状态。
 * 服务降级
 */
@Component
class UserFeignClientFallback implements UserService {

    @Override
    public String hello(Long id) {
        return "默认用户，id：" + id;
    }

    @Override
    public User get(Map<String, Object> map) {
        return null;
    }

    @Override
    public User postUser(User user) {
        return null;
    }
}

@Component
@Slf4j
class UserFeignClientFallbackFactory implements FallbackFactory<UserService> {

    /**
     * 打印的日志看不懂，这个我在研究研究
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/12  16:30
     *
     * @param   	throwable
     * @return  com.microwu.cxd.movie.service.UserService
     */
    @Override
    public UserService create(Throwable throwable) {
        return new UserService() {
            @Override
            public String hello(Long id) {
                log.error("进入回退逻辑：", throwable);
                return "默认用户，id：" + id;
            }

            @Override
            public User get(Map<String, Object> map) {
                return null;
            }

            @Override
            public User postUser(User user) {
                return null;
            }
        };
    }
}