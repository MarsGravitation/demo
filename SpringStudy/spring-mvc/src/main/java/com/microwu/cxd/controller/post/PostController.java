package com.microwu.cxd.controller.post;

import com.microwu.cxd.domain.Account;
import com.microwu.cxd.domain.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: 测试 SpringMVC 如何解析 form-data 表单数据
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/31   10:42
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/account")
    public CommonResult account(Account account) {
//        HttpMessageConverter - form-data 不是由它解析的
//        MultipartResolver - 解析 form-data
//        StandardMultipartHttpServletRequest.parseRequest
//        MultipartParser.ParseState.parse 真正解析的流程
        return CommonResult.success(account);
    }

    @PostMapping("/account2")
    public CommonResult account2(@RequestBody Account account) {
//        HttpMessageConverter - form-data 不是由它解析的
//        MultipartResolver - 解析 form-data
//        StandardMultipartHttpServletRequest.parseRequest
//        MultipartParser.ParseState.parse 真正解析的流程
        throw new RuntimeException();
//        return CommonResult.success(account);
    }

}