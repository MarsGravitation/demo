package com.microwu.design.adapter;

import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Description: 适配器模式
 *  适配器模式做了一个中间转换
 *
 *  适配器使用场景：如果一个类已经稳定存在，不想修改它，但是还要为其增加新需求，且这个需求和累的现有接口不能完全匹配，
 *      那么就可以使用适配器模式实现接口的转换以便满足新需求
 *  或者是为两个已有的接口提供一种协同工作方式
 *  或者是为了软件升级，向后兼容
 *
 *  JDK 适配器：InputStreamReader，OutputStreamWriter，Arrays.asList
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/11/26   13:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AdaptationTest {

    public static void main(String[] args) {
        new InputStreamReader(null);
        Arrays.asList(null);
    }

}