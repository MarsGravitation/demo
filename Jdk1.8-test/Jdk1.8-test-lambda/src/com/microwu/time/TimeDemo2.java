package com.microwu.time;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.microwu.time.DateFormatterUtils.SINGLETON;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/3   11:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimeDemo2 {

    /**
     * GMT：格里尼治时间，也叫世界时 UT
     * UTC：协调世界时，通常 GMT = UTC
     * ISO-8601：国际标准化组织的日期和时间表示方法 2004-05-03T17:30:08+08:00
     *
     * ZoneId： 时区 ID，抽象类，对应的子类 ZoneOffset，ZoneRegion
     * 静态方法：ZoneId#of(String zoneId) 支持固定时间偏移量和地理区域
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  13:55
     *
     * @param
     * @return  void
     */
    public static void test() {
        ZoneId zoneId = ZoneId.systemDefault();
        System.out.println(zoneId);

        ZoneId utc = ZoneId.of("UTC");
        System.out.println(utc);

        System.out.println(ZoneOffset.UTC);

    }

    /**
     * Clock 代表时钟
     *
     * 不知道为什么这几个时间的时间戳都一样
     * 我认为应该是任何时间戳都是固定的，
     * 只是转换成时间格式后存在时区问题
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  11:09
     *
     * @param
     * @return  void
     */
    public static void test01() {
        Clock clock = Clock.systemDefaultZone();
        System.out.println(clock.millis());

        Clock clock1 = Clock.systemUTC();
        System.out.println(clock1.millis());

        System.out.println(System.currentTimeMillis());

        Clock system = Clock.system(ZoneId.of("Asia/Shanghai"));
        System.out.println(system.millis());
    }

    /**
     * 瞬时时间
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:05
     *
     * @param
     * @return  void
     */
    public static void test02() {
        Instant now = Instant.now();
        // 获取毫秒值
        now.toEpochMilli();

        // 验证以上的结论
        // 确实，时间戳是固定的，不会随时区的变化而变化
        System.out.println(Instant.now(Clock.systemDefaultZone()).toEpochMilli());

        System.out.println(Instant.now(Clock.systemUTC()).toEpochMilli());

        System.out.println(System.currentTimeMillis());
    }

    /**
     * LocalDate + LocalDateTime + LocalTime
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:11
     *
     * @param
     * @return  void
     */
    public static void test03() {
        LocalDate now = LocalDate.now();
        System.out.println(now);

        LocalDate date = LocalDate.of(2020, 4, 3);
        System.out.println(date);

        // 默认是使用系统的默认时区
        System.out.println(LocalDateTime.now());

        System.out.println(LocalDateTime.now(Clock.systemDefaultZone()));

        System.out.println(LocalDateTime.now(Clock.systemUTC()));

    }

    /**
     * OffsetTime 基于 UTC/Greenwich 时间偏移量的时间，例如：10:15:30+01:--
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:14
     *
     * @param
     * @return  void
     */
    public static void test04() {
        OffsetTime now = OffsetTime.now();
        System.out.println(now);

        OffsetDateTime offsetDateTime = OffsetDateTime.now();
        System.out.println(offsetDateTime);

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);

    }

    /**
     * Instant 可以和任何时间进行转换
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:27
     *
     * @param
     * @return  void
     */
    public static void test05() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        System.out.println(localDateTime);
    }

    /**
     * 相互转换
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:30
     *
     * @param
     * @return  void
     */
    public static void test06() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime.toLocalDate();
        localDateTime.toLocalTime();
    }

    /**
     * 与旧日期的转换 - 不做了
     * LocalDateTime 转换到 Instant 或者 OffsetDatetime，
     * 都需要 ZoneOffset 指定时区偏移量
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:32
     *
     * @param
     * @return  void
     */
    public static void test07() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.toInstant(ZoneOffset.of("+08:00"));

        System.out.println(instant);
        System.out.println(Instant.now());
    }

    /**
     * 格式化时间
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/4/3  14:57
     *
     * @param
     * @return  void
     */
    public static void test08() {
        DateTimeFormatter formatter = SINGLETON.getOrCreate("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(formatter.format(now));

        System.out.println(SINGLETON.format(now, "yyyy-MM-dd HH:mm:ss"));
    }

    public static void main(String[] args) {
        test08();
    }
}