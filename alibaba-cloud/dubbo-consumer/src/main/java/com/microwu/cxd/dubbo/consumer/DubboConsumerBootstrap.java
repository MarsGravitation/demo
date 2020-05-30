package com.microwu.cxd.dubbo.consumer;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import com.microwu.cxd.dubbo.service.RestService;
import com.microwu.cxd.dubbo.service.User;
import com.microwu.cxd.dubbo.service.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/28   14:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableAutoConfiguration
public class DubboConsumerBootstrap {

    @Reference
    private UserService userService;

    @Reference
    private RestService restService;

//    @Autowired
//    @Lazy
//    private DubboFeignRestService dubboFeignRestService;

    public static void main(String[] args) {
        SpringApplication.run(DubboConsumerBootstrap.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            User user = new User();
            user.setId(1L);
            user.setName("cxd");
            user.setAge(25);

            System.out.printf("UserService.save(%s), %s\n", user, userService.save(user));
            System.out.printf("UserService.findAll = (%s)\n", userService.findAll());

        };
    }

    @Bean
    public ApplicationRunner feignRunner() {
        return args -> callAll();
    }

    public void callAll() {
        System.out.println(restService.pathVariables("a", "b", "c"));

        System.out.println(restService.headers("b", "a", 10));

        System.out.println(restService.param("cxd"));

        System.out.println(restService.params(1, "1"));

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "cxd");
        map.put("age", 25);
        System.out.println(restService.requestBodyMap(map, "Hello, World"));
    }

//    @FeignClient("dubbo-provider")
//    @DubboTransported(protocol = "dubbo")
//    interface DubboFeignRestService {
//
//        @GetMapping("/param")
//        String param(@RequestParam("param") String param);
//
//        @PostMapping("/params")
//        String params(@RequestParam("a") String paramA, @RequestParam("b") String paramB);
//
//        @PostMapping(value = "/request/body/map", produces = APPLICATION_JSON_UTF8_VALUE)
//        User RequestBody(@RequestParam("param") String param, @RequestBody Map<String, Object> data);
//
//        @GetMapping("/headers")
//        String headers(@RequestHeader("h2") String header2, @RequestParam("v") Integer value, @RequestHeader("h") String header);
//
//        @GetMapping("/path-variables/{p1}/{p2}")
//        String pathVariables(@RequestParam("v") String param, @PathVariable("p2") String path2, @PathVariable("p1") String path1);
//
//    }
}