package com.microwu.cxd.controller;

import com.microwu.cxd.producer.MsgProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/1   16:16
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class SendMessageController {
    @Autowired
    private MsgProducer msgProducer;

    @GetMapping("/send")
    public String send() {
        msgProducer.sendMsg("Hello World!");
        return "Hello World!";
    }
}