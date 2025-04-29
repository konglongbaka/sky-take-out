package com.sky.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberIdUtil {
    private static final AtomicInteger sequence = new AtomicInteger(0);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static String generate() {
        // 格式: 年月日时分秒 + 3位序列号 (例如: 20231015123045001)
        return LocalDateTime.now().format(FORMATTER) +
                String.format("%03d", sequence.incrementAndGet() % 1000);
    }
}
