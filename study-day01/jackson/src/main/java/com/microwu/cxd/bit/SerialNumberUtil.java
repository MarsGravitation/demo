package com.microwu.cxd.bit;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Description: 通过位移运算符 生成流水号
 *  1. 当前时间的毫秒值。最大 999，占 10 位
 *  2. serviceType 表示业务类型。比如订单号、操作流水号。最大值定为 30，占 5 位
 *  3. shortParam 用户自定义短参数。比如订单类型，最大值 30， 5 位
 *  4. longParam 同上，一般放置 id 参数，占 30 位
 *  5. 随机数，15 位
 *  6. 在前面加上日期时间
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/31   13:56
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SuppressWarnings("Duplicates")
public class SerialNumberUtil {

    /**
     * 采用 long 值存储，一共 63 位
     */
    private static final int BIT_COUNT = 63;

    /**
     * 各个部分的最大位数，为了减轻负担，时分秒放到前面去，不占用 long 的位数了
     * 毫秒隐藏起来，方便查问题
     *
     * 毫秒值最大 999 - 1111100111，占 10 位
     */
    private static final int SHIFTS_FOR_MILLS = 10;

    private static final int SHIFTS_FOR_SERVICE_TYPE = 5;

    private static final int SHIFTS_FOR_SHORT_PARAM = 5;
    private static final int SHIFTS_FOR_LONG_PARAM = 30;

    /**
     * 最后的随机数，占满剩余位数
     */
    private static final int SHIFTS_FOR_RANDOM_NUM = BIT_COUNT - SHIFTS_FOR_MILLS
            - SHIFTS_FOR_SERVICE_TYPE - SHIFTS_FOR_SHORT_PARAM - SHIFTS_FOR_LONG_PARAM;

    /**
     * 掩码，做按位与操作
     */
    private static final long MASK_FOR_MILLS = (1 << SHIFTS_FOR_MILLS) - 1;
    private static final long MASK_FOR_SERVICE_TYPE = (1 << SHIFTS_FOR_SERVICE_TYPE) - 1;
    private static final long MASK_FOR_SHORT_PARAM = (1 << SHIFTS_FOR_SHORT_PARAM) - 1;
    private static final long MASK_FOR_LONG_PARAM = (1 << SHIFTS_FOR_LONG_PARAM) - 1;

    /**
     * 时间模版
     */
    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    public static String genSerialNum(long serviceType, long shortParam, long longParam) {
        if (serviceType > 30) {
            throw new RuntimeException("the max value of 'serviceType' is 30");
        }
        if (shortParam > 30) {
            throw new RuntimeException("the max value of 'shortParam' is 30");
        }
        if (longParam > 99999999) {
            throw new RuntimeException("the max value of 'longParam' is 99999999");
        }

        // 放置毫秒值
        // 一定要是 Long 类型，否则会按照 int 的 32 位去移位
        long mills = LocalTime.now().getNano();
        long millsShift = mills << (BIT_COUNT - SHIFTS_FOR_MILLS);

        // serviceType
        long serviceTypeShift = serviceType << (BIT_COUNT - SHIFTS_FOR_MILLS - SHIFTS_FOR_SERVICE_TYPE);

        //放置shortParam
        long shortParamShift = shortParam << (BIT_COUNT - SHIFTS_FOR_MILLS - SHIFTS_FOR_SERVICE_TYPE - SHIFTS_FOR_SHORT_PARAM);

        //放置longParam
        long longParamShift = longParam << (BIT_COUNT - SHIFTS_FOR_MILLS - SHIFTS_FOR_SERVICE_TYPE - SHIFTS_FOR_SHORT_PARAM - SHIFTS_FOR_LONG_PARAM);

        // 生成一个指定位数（二进制位数）的随机数
        long randomShift = getBinaryRandom(SHIFTS_FOR_RANDOM_NUM);

        // 拼接各个部分
        long finalNum = millsShift | serviceTypeShift | shortParamShift | longParamShift | randomShift;

        // 最后前面拼接上年月日时分秒 返回出去
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_PATTERN)) + finalNum;
    }

    /**
     * 拿到指定位数 首位数字不为 0 的位数，最终以十进制数返回
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  15:25
     *
     * @param   	count
     * @return  long
     */
    private static long getBinaryRandom(int count) {
        StringBuilder sb = new StringBuilder();
        String str = "01";

        // 采用 ThreadLocalRandom 生成随机数，避免多线程问题
        ThreadLocalRandom r = ThreadLocalRandom.current();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(str.length());
            char c = str.charAt(num);
            while (c == '0') {
                // 确保第一个是不为 0 数字
                if (i != 0) {
                    break;
                } else {
                    num = r.nextInt(str.length());
                    c = str.charAt(num);
                }
            }
            sb.append(c);
        }
        return Long.valueOf(sb.toString(), 2);
    }

    /**
     * 拿到毫秒数
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  15:32
     *
     * @param   	serialNumber
     * @return  long
     */
    public static long getMills(String serialNumber) {
        long number = getLongSerialNumber(serialNumber);
        System.out.println(Long.toBinaryString(number));
        int n = BIT_COUNT - SHIFTS_FOR_MILLS;
        System.out.println(n);
        System.out.println(Long.toBinaryString(number >> n));
        System.out.println(Long.toBinaryString(MASK_FOR_MILLS));
        return  number >> n & MASK_FOR_MILLS;
    }

    /**
     * 获取 serviceType
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  15:52
     *
     * @param   	serialNumber
     * @return  long
     */
    public static long getServiceType(String serialNumber) {
        long longSerialNumber = getLongSerialNumber(serialNumber);
        System.out.println(Long.toBinaryString(longSerialNumber));
        int n = BIT_COUNT - SHIFTS_FOR_MILLS - SHIFTS_FOR_SERVICE_TYPE;
        System.out.println(Long.toBinaryString(longSerialNumber >> n));
        System.out.println(Long.toBinaryString(MASK_FOR_SERVICE_TYPE));
        longSerialNumber = longSerialNumber >> n & MASK_FOR_SERVICE_TYPE;
        return longSerialNumber;
    }

    /**
     * 把日期前缀去掉
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/31  15:33
     *
     * @param   	serialNumber
     * @return  long
     */
    private static long getLongSerialNumber(String serialNumber) {
        return Long.parseLong(serialNumber.substring(DATE_PATTERN.length()));
    }

    public static void main(String[] args) {
        String serialNum = genSerialNum(1, 2, 300);
        long mills = getServiceType(serialNum);
        System.out.println(mills);
    }

}