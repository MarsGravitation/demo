package com.microwu.cxd.controller.date;

import com.microwu.cxd.domain.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/22   10:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/date")
public class DateController {

    /**
     * 如果对日期类不做处理，返回前端的是 class.toString
     *
     * {
     *     "code": 1,
     *     "message": "success",
     *     "data": {
     *         "createTime": "2020-04-22T10:46:12.146",
     *         "updateTime": "2020-04-22T02:46:12.146+0000"
     *     }
     * }
     *
     * 如果想统一格式化时间：
     *  1. 加注解：@JsonFormat
     *      "createTime": "2020-04-22 10:49:30",
     *      "updateTime": "2020-04-22 10:49:30"
     *
     *  2. 全局配置（1）
     *      SpringBoot 为我们提供了日期格式化的全局配置，实体类无需加注解
     *      "createTime": "2020-04-22 10:56:26",
     *      "updateTime": "2020-04-22T02:56:26.138+0000"
     *
     *      这种方式可支持 Date + LocalDateTime 并存，全局格式 yyyy-MM-dd HH:mm:ss
     *      但是如果需要 yyyy-MM-dd格式怎么办？
     *      可以配合@JsonFormat注解使用，因为它的优先级比较高
     *
     *  3. 全局配置（2）
     *      效果与 2 相同， 但是@JsonFormat 注解失效，这里我就不测试了
     *
     *  4. 配置文件 - 将date 转换成时间戳
     *      spring:
     *          jackson:
     *              serialization:
     *                  write-dates-as-timestamps: true
     *
     *      "createTime": [
     *             2020,
     *             4,
     *             22,
     *             11,
     *             27,
     *             6,
     *             507000000
     *         ],
     *         "updateTime": 1587526026507
     *
     *         但是LocalDateTime 格式化错误
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  10:46
     *
     * @param
     * @return  com.microwu.cxd.domain.Order
     */
    @GetMapping(value = "/test")
    public Order order() {
        Order order = new Order(LocalDateTime.now(), new Date(), "");
        return order;
    }

    /**
     * 测试格式转换器 - StringToDateConverter
     *
     * 就是前端传过来 string 类型的 数据 -> 后端使用LocalDateTime 接受,
     * 应该是字段进行对应
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/22  11:32
     *
     * @param   	order
     * @return  java.lang.String
     */
    @PostMapping("/test02")
    public String test02(Order order) {
        System.out.println(order);
        return "success";
    }
}