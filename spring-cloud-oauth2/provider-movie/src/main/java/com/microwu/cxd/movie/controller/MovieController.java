package com.microwu.cxd.movie.controller;

import com.microwu.cxd.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/movie")
public class MovieController {

//    @Autowired
//    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String movie(@PathVariable Long id) {
//        ResponseEntity<String> entity = restTemplate.getForEntity("http://127.0.0.1:9001/user/1", String.class, 1L);
//        String body = entity.getBody();
        String body = userService.hello(id);
        return body + ", " + "i see " + id + " movie";
//        return "i am " + id + " movie";
    }

}