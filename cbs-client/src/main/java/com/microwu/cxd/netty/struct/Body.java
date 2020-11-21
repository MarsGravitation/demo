package com.microwu.cxd.netty.struct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;

import java.io.UnsupportedEncodingException;

/**
 * Description: 消息体
 *  关于字符串我这里准备两种解决方案：
 *      1. 字符串结尾是 0x00(\0) - 这种可能性更大，这个是 SMPP 的规范
 *      2. 字符串结尾是 \\0
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/26   10:57
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Data
public abstract class Body {

    /**
     * 字符串的结尾
     */
    protected static final byte EOF = 0x00;

    /**
     * 该协议对于字符串的编码是以 \\0 结尾的，这里解码时需要根据固定的分隔符将字符串解析出来
     */
    protected static final ByteBuf DELIMIT = Unpooled.copiedBuffer("\\0".getBytes());

    /**
     * 将消息体写入 byteBuf
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/26  11:38
     *
     * @param   	byteBuf
     * @return  void
     */
    public abstract void writeBody(ByteBuf byteBuf);

    /**
     * 将字节转换成 body
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/8/26  15:49
     *
     * @param   	byteBuf
     * @return  void
     */
    public abstract void readBody(ByteBuf byteBuf);

    /**
     * 从字节流中解析字符串，按照 \0 进行切分
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/6  10:38
     *
     * @param   	buffer
     * @return  java.lang.String
     */
    protected String decode(ByteBuf buffer) {
        // 1. 获取 \\0 的位置
        int length = indexOf(buffer, DELIMIT);
        if (length == -1) {
            return null;
        }
        // 2. 获取 \\0 的字节
        ByteBuf frame = buffer.readSlice(length);
        byte[] bytes = new byte[length];
        frame.readBytes(bytes);
        // 3. 转换为字符串
        return new String(bytes).replace("\\0", "");
    }

    /**
     * 从原始的字节流 haystack 中获取 needle 的位置
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/6  10:41
     *
     * @param   	haystack
     * @param 		needle
     * @return  int
     */
    protected int indexOf(ByteBuf haystack, ByteBuf needle) {
        for (int i = haystack.readerIndex(); i < haystack.writerIndex(); i ++) {
            int haystackIndex = i;
            int needleIndex;
            for (needleIndex = 0; needleIndex < needle.capacity(); needleIndex ++) {
                if (haystack.getByte(haystackIndex) != needle.getByte(needleIndex)) {
                    break;
                } else {
                    haystackIndex ++;
                    if (haystackIndex == haystack.writerIndex() &&
                            needleIndex != needle.capacity() - 1) {
                        return -1;
                    }
                }
            }

            if (needleIndex == needle.capacity()) {
                // Found the needle from the haystack!
                return i - haystack.readerIndex() + needle.capacity();
            }
        }
        return -1;
    }

    /**
     * 从 buffer 读取一个 C_String
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/6  15:18
     *
     * @param   	buffer
     * @return  java.lang.String
     */
     protected String readNullTerminatedString(ByteBuf buffer) {
        // buffer 的可读字节数
        int maxLength = buffer.readableBytes();

        // 如果不可读，返回 null
        if (maxLength == 0) {
            return null;
        }

        // 获取 readerIndex
        int offset = buffer.readerIndex();
        int zeroPos = 0;

        // 寻找字符串的结尾，zeroPos 是结尾的下标
        while ((zeroPos < maxLength) && (buffer.getByte(zeroPos+offset) != EOF)) {
            zeroPos++;
        }

        if (zeroPos >= maxLength) {
            // 如果走到最后都没有发现字符串的结尾，那么就证明这个字节数组存在问题
            throw new RuntimeException("Terminating null byte not found after searching [" + maxLength + "] bytes");
        }

        // 当我们找到了字符串结尾时
        String result = null;
        if (zeroPos > 0) {
            // 将字符串读取出来
            byte[] bytes = new byte[zeroPos];
            buffer.readBytes(bytes);
            try {
                result = new String(bytes, "ISO-8859-1");
            } catch (UnsupportedEncodingException e) {
            }
        } else {
            result = "";
        }

        // 此时我们需要跳过一个字节（空字节）
         // 因为前面 buffer.getByte != 0x00
         // 也就是下面的一个字节是 0x00
         // 这里把它消耗掉
        byte b = buffer.readByte();
        if (b != EOF) {
        }

        return result;
    }

}