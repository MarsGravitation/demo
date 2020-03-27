package com.microwu.cxd.manage.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/18   9:45
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class EndPoint {
    /**
     * 商品查询接口, 后续不做控制
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/18  9:47
     *
     * @param   	id
     * @return  java.lang.String
     */
    @GetMapping("/product/{id}")
     public String getProduct(@PathVariable String id) {
         return "product; " + id;
     }

     /**
      * 订单查询接口, 后续进行权限控制
      *
      * @author   chengxudong               chengxudong@microwu.com
      * @date    2019/8/18  9:48
      *
      * @param   	id
      * @return  java.lang.String
      */
     @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        return "order: " + id;
     }

}