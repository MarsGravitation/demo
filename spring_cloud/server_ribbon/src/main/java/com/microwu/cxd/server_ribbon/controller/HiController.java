package com.microwu.cxd.server_ribbon.controller;

import com.microwu.cxd.server_ribbon.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiController {
    @Autowired
    private HelloService helloService;

    @RequestMapping("hi")
    @ResponseBody
    public String sayHello(@RequestParam String name){
        return helloService.hiService(name);
    }


}
