package com.microwu.encrypt;

import java.util.Base64;

/**
 * Description:  https://blog.csdn.net/weixin_40811410/article/details/81950142
 *      Base64 是网络上用于传输8bit字节码的编码方式之一, Base64 就是一种基于64个可打印字符来表示二进制数据的方法
 *      Base64 编码是二进制到字符的过程, 可用于在HTTP环境下传递较长的标识信息.
 *      Base64 要求把每三个8bit的字节转转换成四个6bit的字节, 然后把6bit高位两位添加0, 组成四个8bit的字节
 *      规则:
 *          1. 把三个字节变成四个字节
 *          2. 每76个字符加一个换行符
 *          3. 最后的结束符也要处理
 * 迅雷下载地址:
 *  1. 在地址的前后分别添加 AA 和 ZZ
 *  2. 对新字符串进行Base64 编码
 *
 *  Base64 编码的应用:
 *      1. 不在于安全性, 而是在于让内容在各个网关间无错的传输
 *      2. 在计算机中任何数据都是按ASCII码存储的, 而128 - 255 之间的值是不可见字符.
 *          而在网络上交换数据时, 往往要经过多个路由设备, 由于不同的设备对字符的处理方式不同, 那些不可见字符
 *          就有可能被处理错误. 所以先把数据做Base64编码
 *      3. Base64 在URL的应用
 *          Base64 编码可用于在HTTP 环境下传递较长的标识信息. Java中的Hibernate, 采用Base64 将一个较长的唯一标识符
 *          编码为一个字符串, 用作HTTP表单和HTTP GET URL中的参数. 在其他程序中, 也常常需要把二进制数据编码为适合放在
 *          URL中的形式
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/10   13:38
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Base64Demo {
    public static void main(String[] args) {
        String str = "我叫成旭东";
        // 加密为字符串, 字符转字节默认使用 ISO-8859-1 字符集
        String encode = Base64.getEncoder().encodeToString(str.getBytes());
        // 加密成字节数组
        byte[] bytes = Base64.getEncoder().encode(str.getBytes());
        // 解密
        byte[] decode = Base64.getDecoder().decode(bytes);
        String decodeStr = new String(decode);
        System.out.println("加密字符串: " + encode);
        System.out.println("解密字符串: " + decodeStr);

    }
}