package com.microwu.cxd.controller;

import com.alipay.api.AlipayApiException;
import com.microwu.cxd.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   22:36
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class PayController {
    @Autowired
    private PayService payService;

    @GetMapping("pay")
    public String pay() throws AlipayApiException {
        return payService.pay();
    }

}