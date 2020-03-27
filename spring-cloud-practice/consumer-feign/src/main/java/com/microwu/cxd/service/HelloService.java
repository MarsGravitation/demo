package com.microwu.cxd.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/3/21   11:14
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@FeignClient(name = "provider-user", fallback = Fallback.class)
public interface HelloService {

    @GetMapping("/hello")
    String hello();

}
