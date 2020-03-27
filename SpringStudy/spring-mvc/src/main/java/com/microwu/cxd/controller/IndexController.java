package com.microwu.cxd.controller;

import com.microwu.cxd.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * @RequestBody 和 @ResponseBody 分别完成请求到对象, 对象到请求的转换
 * 底层是依靠HttpMessageConverter 消息转换机制
 *
 * 在请求时, 我们会在请求头 Content-Type, 表示RequestBody 的内容类型, 这样,
 * SpringMVC可以从自己的HttpMessageConverter 数组中, 通过canRead, 判断是否
 * 可以读取指定的mediaType, 转换成对应的class 对象.
 *
 * 在响应时, 我们会在请求头上Accept上, 表示ResponseBody 的内容类型, 这样, SpringMVC
 * 可以从自己的HttpMessageConverter 数组中, 通过caanWriter方法, 判断是否可以能够将
 * class 序列化成mediaType 内容.
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:           2019/7/31   13:56
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Controller
public class IndexController {

    @RequestMapping("/account")
    public String index(Model model) {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("cxd", "成旭东", "123456", "18435202728"));
        model.addAttribute("accountList", accounts);
        return "account";
    }

    @GetMapping(value = "/hello")
    @ResponseBody
    public String hello() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(20);
        return "hello, SpringMVC!";
    }

    @ResponseBody
    @PostMapping("/map")
    public Map<String, String> map(@RequestParam Map<String, String> map) {
//        throw new ServiceException(ServiceExceptionEnum.SYS_ERROR);
        for(Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
        return map;
    }

    @ResponseBody
    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable Integer id) {
        Account account = new Account("123456", "123456", "1", "18435202728");
        return account;
    }

    @ResponseBody
    @GetMapping("/date")
    public void getDate(@RequestParam LocalDateTime datetime) {
        System.out.println(datetime);
    }

    public void getHeader() {

    }

}