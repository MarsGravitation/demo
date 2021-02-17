package com.microwu.cxd.kafka.controller;

import com.microwu.cxd.kafka.common.Foo1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/2   13:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping("/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        this.template.send("topic1", new Foo1(what));
    }

//    @PostMapping(path = "/send/foo/{what}")
//    public void sendFoo(@PathVariable String what) {
//        this.template.send("foos", new Foo1(what));
//    }
//
//    @PostMapping(path = "/send/bar/{what}")
//    public void sendBar(@PathVariable String what) {
//        this.template.send("bars", new Bar1(what));
//    }
//
//    @PostMapping(path = "/send/unknown/{what}")
//    public void sendUnknown(@PathVariable String what) {
//        this.template.send("bars", what);
//    }

}