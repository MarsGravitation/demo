package com.microwu.encrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Description:     https://www.cnblogs.com/pcheng/p/9629621.html
 *  RSA 加密算法是一种非对称加密算法
 *          对极大整数做因数分解的难度决定了RSA算法的可靠性. 换言之, 对一个极大整数做因数分解越困难
 *          RSA算法愈可靠
 *
 *          MD5加密: 同样的参数, 加密结果是一样的. 因为它是不可解密的, 只能作为验签使用,不能对数据加密传输
 *          BASE64: 可以反解密, 不安全
 *          AES: 对称加密, 要求key是一样的, 容易造成key的泄漏, 没有RES加密安全
 *          RSA: 非对称加密算法, 也就是加密的公钥和解密的私钥是不一样的. 并且相同的参数每次加密的结果都不一样.
 *               在对接的过程中, 暴露的永远的是公钥, 所以数据是绝对安全的
 *
 *  RSA 加密的公钥和私钥的长度是可以定义的, 目前没有上限. 主流密钥长度都是1024bit 以上的, 也就是最少128个字节
 *  RSA加密的明文长度不能大于密钥的长度,但是由于明文长度小于密钥长度的时候, 需要进行填充占用11字节, 所以
 *  0 < 加密明文长度 < 密钥长度 - 11
 *
 * RSA加密, 签名区别:
 *      加密和签名都是为了安全性考虑. 简单地说, 加密是为了放置信息被泄露, 而签名是为了防止信息被篡改
 *      RSA加密: B -> A 传递消息
 *          1. A 生成一对密钥(公钥和私钥), 私钥不公开, A自己保留, 公钥为公开, 任何人可以获取
 *          2. A传递自己的公钥给B, B用A的公钥对消息进行加密
 *          3. A接受到B加密的消息, 利用自己的私钥进行解密
 *          这个过程中, 只有2次传递过程, 第一次是A传递公钥给B, 第二次B传递加密消息给A, 即使都被地方截获, 也没有危险
 *          因为只有A的私钥才能对消息进行解密,防止了消息内容的泄漏
 *      RSA签名: A -> B 回复
 *          1. A用自己的私钥对消息加签, 形成签名, 并将加签的消息和消息本身一起传递给B
 *          2. B收到消息后, 在获取A的公钥进行验签
 *          在这个过程中, 只有2次传递过程中, 第一次是A传递加签的消息和消息本身给B, 第二次是B 获取A的公钥, 即使都被敌方截获,
 *          也没有关系, 因为只有A的私钥消息才能对消息进行签名,即使知道消息, 也无法伪造带签名的回复给B, 防止了消息内容的篡改
 *
 *          公钥加密, 私钥加密, 私钥签名, 公钥验签
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/10   10:36
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class EncryptDemo {
    /**
     *  RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  10:52
     *
     * @return  java.security.PrivateKey
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        // 定义密钥长度为1024
        generator.initialize(1024);
        return generator.generateKeyPair();

    }

    /**
     * 获取私钥
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  10:58
     *
     * @param   	privateKey
     * @return  java.security.PrivateKey
     */
    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeKey = Base64.getDecoder().decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  11:00
     *
     * @param   	publicKey
     * @return  java.security.PublicKey
     */
    public static PublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeKey = Base64.getDecoder().decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  11:13
     *
     * @param   	data
     * @param 		publicKey
     * @return  java.lang.String
     */
    public static String encrypt(String data, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while(inputLen - offset > 0) {
            if(inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64编码, 并以utf-8为标准转换成字符串
        // 加密后的字符串
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    /**
     * RSA解密
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  11:23
     *
     * @param   	data
     * @param 		privateKey
     * @return  java.lang.String
     */
    public static String decrypt(String data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.getDecoder().decode(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while(inputLen - offset > 0) {
            if(inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;

        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * A -> B: A通过私钥对数据加密, 形成签名, 把签名和数据给B
     * B 获取签名和数据, 通过公钥验签
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  11:25
     *
     * @param   	data
     * @param 		privateKey
     * @return  java.lang.String
     */
    public static String sign(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.getEncoder().encodeToString(signature.sign()));
    }

    /**
     * 验签
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/9/10  11:33
     *
     * @param   	srcData
     * @param 		publicKey
     * @param 		sign
     * @return  boolean
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.getDecoder().decode(sign.getBytes()));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IOException, SignatureException {
        // 生成密钥对
        KeyPair keyPair = getKeyPair();
        String privateKey = new String(Base64.getEncoder().encode(keyPair.getPrivate().getEncoded()));
        System.out.println("私钥: " + privateKey);
        String publicKey = new String(Base64.getEncoder().encode(keyPair.getPublic().getEncoded()));
        System.out.println("公钥: " + publicKey);
        // RSA 加密
        String data = "小明登录密码";
        String encryptData = encrypt(data, getPublicKey(publicKey));
        System.out.println("加密后的内容: " + encryptData);
        // RSA 解密
        String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
        System.out.println("解密后的内容: " + decryptData);

        // RSA 签名
        String sign = sign(data, getPrivateKey(privateKey));
        // RSA 验签
        boolean result = verify(data, getPublicKey(publicKey), sign);
        System.out.println("验签结果: " + result);

    }

}