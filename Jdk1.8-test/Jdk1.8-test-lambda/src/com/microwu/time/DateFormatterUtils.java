package com.microwu.time;

import java.time.LocalDateTime;
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

    public String format(LocalDateTime localDateTime, String pattern) {
        return getOrCreate(pattern).format(localDateTime);
    }

}
