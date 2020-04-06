package com.microwu.time;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Description:     Java8 日期时间API 的重点
 *      1. 提供了ZoneId 获取时区
 *      2. LocalDate 和 LocalTime 类
 *      3. Java8 所有日期和时间API都是不可变类, 并且线程安全
 *      4. 时区代表了地球上某个区域内普遍使用的标准时间. 每个时区都有一个代号, 格式通常由区域/城市构成
 *          再加上与格林威治或UTC的时差. 例如: 东京的时差是 +09:00
 *      5. OffsetDateTime实际上组合了LocalDateTime和ZoneOffset类. 用来包含和格林威治或UTC时差的完整日期
 *      6. DateTimeFormatter 用来格式化或解析时间
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/19   9:29
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TimeDemo {
    private static void test01() {
        // 获取今天的日期
        LocalDate now = LocalDate.now();
        System.out.println(now);
        // 获取年, 月, 日
        int year = now.getYear();
        int monthValue = now.getMonthValue();
        int dayOfMonth = now.getDayOfMonth();
        System.out.printf("year = %d, month = %d, day = %d", year, monthValue, dayOfMonth);
    }

    public static void test02() {
        // 处理特定日期
        LocalDate date = LocalDate.of(2019, 8, 19);
        System.out.println(date);
        // 判断日期是否相等
        System.out.println(date.equals(LocalDate.now()));

    }

    /**
     * 处理类似每月账单, 结婚纪念, EMI日或者保险缴费日.
     * Java8 通过 MonthDay 类, 这个类去掉了年, 组合了月, 日, 这意味着你可以用它
     * 判断每年都会发生的事件. 这个类是不可变并且线程安全的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/19  9:36
     *
     * @param
     * @return  void
     */
    public static void test03() {
        LocalDate now = LocalDate.now();
        LocalDate birthday = LocalDate.of(2019, 10, 8);
        MonthDay birthMonthDay = MonthDay.of(birthday.getMonth(), birthday.getDayOfMonth());
        MonthDay fromMonth = MonthDay.from(now);
        if(fromMonth.equals(birthMonthDay)) {
            System.out.println("Happy Birthday");
        } else {
            System.out.println("Sorry, today is not your birthday");
        }

    }

    public static void test04() {
        // 获取当前时间, 默认格式是hh:mm:ss.nnn
        // 只包含时间信息, 没有日期
        LocalTime now = LocalTime.now();
        System.out.println(now);
        // 在现有的时间上增加小时
        // 由于Java8 提供的时间类是不可变类型, 所以返回后一定要用变量赋值
        LocalTime twoHour = now.plusHours(2);
        System.out.println(twoHour);
    }

    public static void test05() {
        // 计算一周后的日期
        LocalDate now = LocalDate.now();
        System.out.println(now);
        LocalDate oneWeek = now.plusWeeks(1);
        System.out.println(oneWeek);
        // 一年前或者一年后的日期
        LocalDate lastYear = now.minus(1, ChronoUnit.YEARS);
        System.out.println(lastYear);
        LocalDate nextYear = now.plus(1, ChronoUnit.YEARS);
        System.out.println(nextYear);
    }

    public static void test06() {
        // 使用Clock时钟类, 用于获取当前的时间戳, 或者当前是取下的日期时间信息
        Clock clock = Clock.systemUTC();
        System.out.println(clock);
        Clock clock1 = Clock.systemDefaultZone();
        System.out.println(clock1);

    }

    public static void test07() {
        // 判断日期是否早于另一个日期
        LocalDate now = LocalDate.now();
        LocalDate of = LocalDate.of(2019, 06, 19);
        System.out.println(now.isAfter(of));
    }

    public static void test08() {
        // Java8 不仅分离了日期和时间, 把时区也分离出来了
        // 现在有一系列单独的类, 如ZoneId来处理特定时区, ZoneDateTime类来表示某时区下的时间
        ZoneId america = ZoneId.of("America/New_York");
        LocalDateTime now = LocalDateTime.now();
        ZonedDateTime of = ZonedDateTime.of(now, america);
        System.out.println(of);
    }

    public static void test09() {
        YearMonth now = YearMonth.now();
        // 返回当月的天数
        System.out.println(now + "," + now.lengthOfMonth());
        YearMonth of = YearMonth.of(2018, Month.FEBRUARY);
        System.out.println(of);
    }

    public static void test10() {
        // 计算两个日期之间的天数和月数
        LocalDate date = LocalDate.of(2020, Month.JANUARY, 22);
//        LocalDate now = LocalDate.now();
        LocalDate now = LocalDate.of(2020, Month.MARCH, 14);
//        Period between = Period.between(date, now);
//        System.out.println(between.getDays());
        long between1 = ChronoUnit.DAYS.between(date, now);
        System.out.println(between1 - 1);
        // 获取时间戳
        // Instant 包含了日期和时间, 相当于 Date类, 可以进行互相转换
//        Instant now1 = Instant.now();
//        System.out.println(now1);
    }

    public static void test11() {
        // 日期转换成字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMdd yyyy hh:mm a");
        String format = dateTimeFormatter.format(now);
        System.out.println(format);
    }

    public static void main(String[] args) {
        test10();
    }
}