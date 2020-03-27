package com.microwu.cxd.server_feign.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-hi")
public interface HelloService {

    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String helloService(@RequestParam("name") String name);
}
