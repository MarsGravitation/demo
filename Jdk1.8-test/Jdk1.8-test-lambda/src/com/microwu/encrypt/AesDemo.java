package com.microwu.encrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/10   15:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AesDemo {
    /**
     * 获得一个 密钥长度为256位的AES 密钥
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  15:36
     *
     * @param
     * @return  java.lang.String
     */
    public static String getStrKeyAES() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = new SecureRandom((String.valueOf(System.currentTimeMillis()).getBytes("UTF-8")));
        generator.init(256, secureRandom);
        SecretKey secretKey = generator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * 使用Base64 加密后的字符串类型 的secretKey 转为 SecretKey
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  15:39
     *
     * @param   	strKey
     * @return  javax.crypto.SecretKey
     */
    public static SecretKey strKey2SecretKey(String strKey) {
        byte[] decode = Base64.getDecoder().decode(strKey);
        SecretKeySpec secretKey = new SecretKeySpec(decode, "AES");
        return secretKey;
    }

    /**
     * 加密
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  15:41
     *
     * @param   	content
     * @param 		secretKey
     * @return  byte[]
     */
    public static byte[] encryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    /**
     * 解密
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  15:43
     *
     * @param   	content
     * @param 		secretKey
     * @return  byte[]
     */
    public static byte[] decryptAES(byte[] content, SecretKey secretKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(content);
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {
        String content = "我叫成旭东";
        // 获取密钥
        String strKeyAES = getStrKeyAES();
        System.out.println("密钥: " + strKeyAES);
        // 将字符串转换成密钥
        SecretKey secretKey = strKey2SecretKey(strKeyAES);
        // 加密数据
        byte[] bytes = encryptAES(content.getBytes(), secretKey);
        System.out.println("加密后Base64 编码: " + Base64.getEncoder().encode(bytes));
        // 解密数据
        String decryptStr = new String(decryptAES(bytes, secretKey));
        System.out.println("解密数据: " + decryptStr);

    }
}