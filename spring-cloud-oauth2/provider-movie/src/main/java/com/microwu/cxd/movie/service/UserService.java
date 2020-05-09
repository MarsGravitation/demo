package com.microwu.cxd.movie.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/5/8   15:01
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@FeignClient(name = "PROVIDER-USER")
public interface UserService {

    @GetMapping("/user/{id}")
    String hello(@PathVariable Long id);
}
