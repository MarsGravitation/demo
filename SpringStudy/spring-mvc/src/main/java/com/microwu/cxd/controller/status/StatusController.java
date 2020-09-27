package com.microwu.cxd.controller.status;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/28   10:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class StatusController {

    /**
     * 返回状态码
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/7/28  10:13
     *
     * @param
     * @return  org.springframework.http.ResponseEntity
     */
    @GetMapping("/status")
    public ResponseEntity status() {
        try {

//            throw new RuntimeException();
             return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}