package com.microwu.cxd.spring.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/17   15:16
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

    @NacosInjected
    private ConfigService configService;

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @RequestMapping("/get")
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> publish(String dataId, String group, String content) throws NacosException {
        boolean result = configService.publishConfig(dataId, group, content);
        if (result) {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
        return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}