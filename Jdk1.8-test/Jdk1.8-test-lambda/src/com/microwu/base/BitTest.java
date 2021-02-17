package com.microwu.base;

/**
 * Description: 位运算
 *  1. 原码、反码、补码
 *      a. 原码：
 *      最高位为符号位，0 代表正数，1 代表负数
 *      127 原码：0111 1111
 *      -127 原码：1111 1111
 *      b. 反码
 *      正数的反码和原码一致
 *      负数的反码是原码按位取反，最高位不变
 *      127 反码：0111 1111
 *      -127 反码：1000 0000
 *      c. 补码
 *      正数的补码和原码一致
 *      负数的补码是反码 + 1
 *      127 的补码：0111 1111
 *      -127 的补码：1000 0001
 *
 *      总结：
 *          |- 正数的原码、反码、补码是一致的
 *          |- 负数的补码是反码 + 1，反码是按位取反，最高位不变
 *          |- 计算机数字运算是基于补码的
 *
 *  2. 补码有啥好？
 *      将减法转换为加法，计算机中的模表示时钟的一圈，本质上是将溢出的部分舍去而不改变结果
 *      以 8 位运算为例，8 位的模是 2^8 = 256
 *      在无符号的情况下：127 + 2 = 129
 *      0111 1111
 *      0000 0010
 *      ---------
 *      1000 0001
 *
 *      我们将最高位作为符号位，计算机均以补码来表示，1000 0001 的原码为减一按位取反 1111 1111，也就是 -127
 *      负数的补码为模减去该数的绝对值
 *      如 -5 = 156 - 251 = 1111 1011
 *
 *      化简为加：16 - 5 = 16 + (-5) = 11
 *      0001 0000
 *      1111 1011
 *      ————
 *    1 0000 1011   - 最高位超出范围舍去，得到 11
 *
 *    https://www.jianshu.com/p/36ec7a047f29
 *
 *  2. 位运算
 *      异或：与 0 异或，值不变；交换
 *      +11：原码 = 反码 = 补码 = 0000 1011
 *      -11：原码 = 1000 1011，反码 = 1111 0100，补码 = 1111 0101
 *
 *      a = a^b
 *      0000 1011
 *      1111 0101
 *      ---------
 *    a 1111 1110
 *      b = b^a
 *    b 1111 0101
 *      ----------
 *    b 0000 1011
 *      a = a^b
 *      1111 1110
 *      ----------
 *    a 1111 0101
 *
 *      按位与：清零，把所有位按位与；取其中一个位，如 1111 0110 & 0000 1111 = 0000 0110，取低四位
 *      按位或：置一
 *      带符号左移：
 *          a. 补码左移 n 位
 *          b. 左边移出的部分直接舍弃，右边移入部分全部补零
 *          c. 移位结果等于 M 乘以 2 的 n 次方，0，正数，负数通用
 *          d. 如果位数超过了该类型的最大位数，编译器会对移动的位数取模，int 移动了 33 位，实际上只移动了 33 % 2 = 1
 *
 *      组合技巧：
 *          a. 计算 n 个 bit 能表示的最大数值
 *          -1 ^　(-1 << n)
 *            1111 1111
 *  左移 3 位 1111 1000
 *            1111 1111
 *            ----------
 *            0000 0111 = 7
 *
 *          b. 用固定位的最大值作为 Mask 避免溢出
 *          n = 3，最大能表示的数值 = -1 ^ (-1 << 3) = 7
 *          mask = 7
 *          如 0000 0111 & 0000 0111 = 7，没溢出的话数值保持不变
 *          0000 1000
 *          0000 0111
 *          ----------
 *          0000 0000
 *          溢出后就归零，s = (s + 1) & mask 保证不会超过 mask
 *
 *  https://www.throwable.club/2020/08/10/snowflake-source-code/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/28   10:02
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class BitTest {

    /**
     * 起始的时间戳
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastStmp = -1L;//上一次时间戳

    public BitTest(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        // 获取系统时间戳
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        // 高并发场景下，同一个毫秒生成多个 ID
        if (currStmp == lastStmp) {
            // 相同毫秒内，序列号自增
            // 确保sequence 不会溢出
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                // 如果超出序列号，死循环等待下一个毫秒值
                currStmp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //时间戳部分
                | datacenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
//        BitTest bitTest = new BitTest(1, 1);
//        long l = bitTest.nextId();
//        System.out.println(l);
//        System.out.println(String.valueOf(l).length());

        System.out.println(TIMESTMP_LEFT);
    }

}