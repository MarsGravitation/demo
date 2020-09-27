package com.microwu.encrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * Description:
 *
 * 前提：
 *  javax.crypto.Cipher: 翻译为密码，叫做密码器更合适。Cipher 是 Java Cryptographic Extension，Java 加密扩展的核心，
 *  提供基于多种加解密算法的加解密功能。
 *
 * Cipher 初始化 transformation - 转换模式
 *  转换模式 transformation 一般有三部分组成，格式是：算法/工作模式/填充模式(algorithm/mode/padding).
 *  算法，就是值具体加密算法的名称，如 SHA-256 等
 *  工作模式，主要是针对分组密码。分组密码是将明文消息编码表示后的数字（明文数字）序列，划分长度位 n 的组（可以看成长度为 n 的矢量），
 *      每组分别在密钥的控制下变换成等成的输出数字（密文数字）序列。工作模式的出现主要基于下面原因：
 *      > 当需要加密的明文产毒十分大，比如文件内容，由于硬件或者性能原因需要分组加密
 *      > 多次使用相同的密钥对多个分组加密，会引发许多安全问题
 *      从本质上来讲，工作模式是一项增强密码算法或使算法适应具体应用的技术
 *  填充模式，块加密算法要求原文数据长度为固定块大小的整数倍，如果原文数据长度大于固定块大小，则需要在固定块填充数据知道整个快是完整的。
 *
 * Cipher 的属性和方法
 *  1. 共有属性
 *      a. ENCRYPT_MODE，加密
 *      b. DECRYPT_MODE，解密
 *      c. WRAP_MODE，包装密钥
 *      d. UNWRAP_MODE，解包装密钥
 *      e. PUBLIC_KEY，解包装密钥模式下指定密钥类型为公钥
 *      f. PRIVATE_KEY，解包装密钥模式下指定密钥类型为私钥
 *      g. SECRET_KEY，解包装密钥模式下指定密钥类型为密钥，主要用于只有一个密钥，不包含私钥和公钥
 *
 *  2. getInstance 方法 - 构建实例
 *      transformation，这里称为转换模式
 *      Provider 的全类名或者实例，因为 Cipher 要从对应的提供商中获取指定转换模式的实现
 *      实际上 Cipher 实例的初始化必须依赖于转换模式和提供商
 *  3. init 方法 - 初始化 Cipher
 *      opmode 操作模式
 *      key 类型
 *  4. warp 和 unwrap
 *      warp 用于包装一个密钥，使用的时候需要注意 Cipher 的 opmode 要初始化为 WARP_MODE
 *      unwrap 解包装一个密钥，Cipher 的 opmode 要初始化为 UNWRAP_MODE，
 *      其实 warp 和 unwarp 是一个互逆操作
 *      wrap 是把原始的密钥通过某种算法包装为加密后的密钥，这样就可以避免在传递密钥的时候泄漏的密钥的明文
 *      unwrap 是把包装后的密钥解包装为原始的密钥，得到密钥的明文
 *  5. doFinal 主要功能是结束单部分或多部分的加密或解密操作
 *
 * Cipher 的工作流程
 *  大多数情况下，加密后的 byte 数组元素取值不在Unicode 码点的范围内，表面上是乱码，因此需要考虑把这种 byte 数组转换为
 *  非乱码的字符串以便传输，常见的方式有 Hex、Base64 等等
 *
 *
 *
 *  https://www.throwx.cn/2019/02/16/java-security-cipher/
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/9/3   13:48
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class CipherDemo {

    private static final String SECRECT = "password";

    public String wrap(String keyString) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 初始化密钥生成器，指定密钥长度位 128，指定随机源的种子为指定的密钥
        keyGenerator.init(128, new SecureRandom(SECRECT.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.WRAP_MODE, secretKeySpec);
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        byte[] bytes = cipher.wrap(key);
//        return Hex.encodeHexString(bytes);
        return new String(bytes);
    }

    public String unwarp(String keyString) throws Exception {
//        byte[] rawKey = Hex.decodeHex(keyString);
        byte[] rawKey = new byte[1024];
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 初始化密钥生成器，指定密钥长度为 128，指定随机源种子
        keyGenerator.init(128, new SecureRandom(SECRECT.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.UNWRAP_MODE, secretKeySpec);
        SecretKey key = (SecretKey) cipher.unwrap(rawKey, "AES", Cipher.SECRET_KEY);
        return new String(key.getEncoded());
    }

    /**
     * SecretKey: 密钥
     * SecretKeySpec：经过算法加密后的密钥
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/9/3  14:55
     *
     * @param   	content
     * @param 		password
     * @return  java.lang.String
     */
    public String encryptByAES(String content, String password) throws Exception {
        // 指定算法为 AES_128
        Cipher cipher = Cipher.getInstance("AES_128/ECB/NoPadding");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // 因为 AES 要求密钥长度位 128，我们需要固定的密码，因此随机源的种子需要设置为我们的密码数组
        keyGenerator.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        // 基于加密模式和密钥初始化 Cipher
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        //加密后的密文由二进制序列转化为十六进制序列，依赖apache-codec包
        byte[] bytes = cipher.doFinal(content.getBytes());
//        return Hex.encodeHexString(bytes);
        return new String(bytes);

    }

    public String decryptByAES(String content, String password) throws Exception {
        // 这里要把十六进制序列化回二进制序列
//        byte[] bytes = Hex.decodeHex(content);
        byte[] bytes = content.getBytes();
        // 指定算法
        Cipher cipher = Cipher.getInstance("AES_128/ECB/NoPadding");
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = keyGenerator.generateKey();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        // 基于解密模式和密钥初始化 Cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(bytes));

    }

    public static void main(String[] args) throws Exception {

    }

}