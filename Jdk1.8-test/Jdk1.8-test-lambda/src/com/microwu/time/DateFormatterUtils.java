package com.microwu.time;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DateFormatterUtils {

    // 单例
    SINGLETON;

    // 如果不是单例，就不存在并发问题
    private static final Map<String, DateTimeFormatter> FORMATTER_MAP = new ConcurrentHashMap<>();

    public DateTimeFormatter getOrCreate(String pattern) {
        return FORMATTER_MAP.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    /**
     * LocalDateTime -> String
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/13     14:21
     *
     * @param localDateTime
     * @param pattern
     * @return java.lang.String
     */
    public String format(LocalDateTime localDateTime, String pattern) {
        return getOrCreate(pattern).format(localDateTime);
    }

    /**
     * String -> LocalDateTime
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/13     14:21
     *
     * @param time
     * @param pattern
     * @return java.time.LocalDateTime
     */
    public LocalDateTime parse(String time, String pattern) {
        return LocalDateTime.parse(time, getOrCreate(pattern));
    }

    /**
     * 格式化时间戳
     *
     * Long -> String
     *
     *  DateTimeFormatter formatString = DateTimeFormatter.ofPattern(format);
     *  return formatString.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()));
     *
     * https://www.cnblogs.com/luweiweicode/p/14217505.html
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/23     11:10
     *
     * @param timestamp
     * @return java.lang.String
     */
    public String format(long timestamp, String pattern) {
//        Instant instant = Instant.ofEpochSecond(timestamp);
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return format(localDateTime, pattern);
    }

    /**
     * String -> Long
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/13     14:35
     *
     * @param time
     * @param pattern
     * @return java.lang.Long
     */
    public Long stringToLong(String time, String pattern) {
        LocalDateTime localDateTime = parse(time, pattern);
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    public static void main(String[] args) {
        test02();
    }

    public static void test() {
        Instant now = Instant.now();
        String format = SINGLETON.format(1629166015000L, "yyyy-MM-dd HH:mm:ss");
        System.out.println(format);

        // 获取秒
//        System.out.println("epochSecond: " + now.getEpochSecond());
        // 获取毫秒
//        System.out.println("epochMilli:" + now.toEpochMilli() / 1000);
    }

    /**
     *
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/8/13     14:21
     *
     * @param
     * @return void
     */
    public static void test02() {
        String date = "2021-08-31 10:51:29.003";
        long milli = 1630378287000L;
        LocalDateTime time = SINGLETON.parse(date, "yyyy-MM-dd HH:mm:ss.SSS");
        long epochMilli = time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        System.out.println(epochMilli - milli);
    }

}
