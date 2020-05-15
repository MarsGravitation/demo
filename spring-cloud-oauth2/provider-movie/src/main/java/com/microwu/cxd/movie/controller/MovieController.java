package com.microwu.cxd.movie.controller;

import com.microwu.cxd.movie.domain.User;
import com.microwu.cxd.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/5/7   15:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class MovieController {

//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    /**
     * 测试权限控制
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/13  10:09
     *
     * @param   	id
     * @return  java.lang.String
     */
    @GetMapping("/movie/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String movie(@PathVariable Long id) {
//        ResponseEntity<String> entity = restTemplate.getForEntity("http://127.0.0.1:9001/user/1", String.class, 1L);
//        String body = entity.getBody();
        String body = userService.hello(id);
        return body + ", " + "i see " + id + " movie";
//        return "i am " + id + " movie";
    }

    /**
     * 测试无权限，且get 发送多参数案例
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/13  10:09
     *
     * @param   	map
     * @return  com.microwu.cxd.movie.domain.User
     */
    @GetMapping("/test/get")
    public User user(@RequestParam Map<String, Object> map) {
        return userService.get(map);
    }

    @PostMapping("/test/post")
    public User user(@RequestBody User user) {
        return userService.postUser(user);
    }

}