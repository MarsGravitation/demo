package com.microwu.other;

/**
 * Description:
 *
 * 1. 由于使用 128 bit 去存储，使用两个 long 存储
 *
 * https://juejin.cn/post/7155119516853207077
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2022/12/28   9:59
 * Update History:
 * Author        Time            Content
 */
public class ULID {

    private static final long TIMESTAMP_MASK = 0xffff000000000000L;

    public static final int RANDOMNESS_BYTE_LEN = 10;

    private static final char[] DEFAULT_ALPHABET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
            'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};

    private static final byte[] DECODING_TABLE = new byte[]{
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, 0x00, 0x01,
            0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, -1, -1,
            -1, -1, -1, -1, -1, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
            0x0f, 0x10, 0x11, -1, 0x12, 0x13, -1, 0x14, 0x15, -1,
            0x16, 0x17, 0x18, 0x19, 0x1a, -1, 0x1b, 0x1c, 0x1d, 0x1e,
            0x1f, -1, -1, -1, -1, -1, -1, 0x0a, 0x0b, 0x0c,
            0x0d, 0x0e, 0x0f, 0x10, 0x11, -1, 0x12, 0x13, -1, 0x14,
            0x15, -1, 0x16, 0x17, 0x18, 0x19, 0x1a, -1, 0x1b, 0x1c,
            0x1d, 0x1e, 0x1f, -1, -1, -1, -1, -1
    };

    private static final int DEFAULT_ALPHABET_MASK = 0b11111;

    private static final int ULID_BYTE_LEN = 0x1a;

    private static final long OVERFLOW = 0x0000000000000000L;

    private static final byte TIMESTAMP_OVERFLOW_FLAG = 0b11000;

    /**
     * The most significant 64 bits of this ULID
     */
    private final long msb;

    /**
     * The least significant 64 bits of this ULID
     */
    private final long lsb;

    public ULID(long msb, long lsb) {
        this.msb = msb;
        this.lsb = lsb;
    }

//    public ULID(long timestamp, byte[] randomness) {
//        if ()
//    }

}
