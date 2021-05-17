package com.microwu.string;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/11   10:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class StringDemo {
    public static void main(String[] args) {
        // String的占位符
        Date date = new Date();
        String content = "%tF %tT: dsp报警 活动activityId = %d, 导入用户数据 = %s 失败";
        System.out.println(String.format(content, date, date, 1, "a.csv"));

        // MessageFormat
        content = "{0}: dsp报警 活动activityId = {1}, 导入用户数据 = {2} 失败";
        System.out.println(MessageFormat.format(content, LocalDateTime.now(), 1, "a.csv"));
    }
}