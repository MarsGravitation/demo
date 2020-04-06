package com.microwu.time;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * Description:
 *  Duration：TemporalAmount 实现类。表示秒和纳秒级别的时间量
 *  Period：TemporalAmount 实现类。表示年月日的时间量
 *  TemporalUnit：主要的枚举类型ChronoPeriod , 日期时间的基本单位
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/3   15:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimeDemo3 {

    /**
     * Duration 和 Period
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  15:16
     *
     * @param
     * @return  void
     */
    public static void test() {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        Duration duration = Duration.between(now, time);
        System.out.println(duration.getSeconds());

        Period period = Period.between(now.toLocalDate(), time.toLocalDate());
        System.out.println(period.getDays());
    }

    /**
     * 计算相关操作
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  15:18
     *
     * @param
     * @return  void
     */
    public static void test02() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime time = LocalDateTime.of(2020, 1, 1, 0, 0, 0);

        // 早于某个日期
        // 如果不是LocalDateTime，底层是转换成天，进行比较的
        boolean before = now.isBefore(time);
        System.out.println(before);

        // 计算间隔 可以使用上面的方法, 底层都用的是这个方法
        long until = time.until(now, ChronoUnit.DAYS);
        System.out.println(until);

    }

    /**
     * 日期校准器 TemporalAdjuster
     *      最后一天，第一天
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  15:29
     *
     * @param
     * @return  void
     */
    public static void test03() {
        LocalDateTime now = LocalDateTime.now();
        OffsetDateTime of = OffsetDateTime.of(now, ZoneOffset.of("+08:00"));
        OffsetDateTime time = OffsetDateTime.now();

        OffsetDateTime offsetDateTime = time.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(offsetDateTime);
        System.out.println(of.with(TemporalAdjusters.lastDayOfMonth()));

        LocalDateTime with = now.with(TemporalAdjusters.lastDayOfMonth());
        System.out.println(LocalDateTime.of(with.getYear(), with.getMonthValue(), with.getDayOfMonth(), 23, 59, 59));
        System.out.println(now);
    }

    public static void main(String[] args) {
        test03();
    }

}