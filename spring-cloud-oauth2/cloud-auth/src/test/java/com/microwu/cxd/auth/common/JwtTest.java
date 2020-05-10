package com.microwu.cxd.auth.common;

import com.alibaba.fastjson.JSON;
import com.microwu.cxd.auth.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *  私钥，公钥，证书的理解
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/10   15:34
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
public class JwtTest {
    private static JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

    private static String test() {

        String chengxudong = jwtTokenUtil.generateToken("chengxudong", "123456");
        return chengxudong;
    }

    private static void test02() {
        String test = test();
        System.out.println(test);
        Claims claimFromToken = jwtTokenUtil.getClaimFromToken(test);
        System.out.println(claimFromToken.getSubject());
    }

    private static String test03() {
        // 证书文件路径
        String key_location = "chengxudong.jks";
        // 私钥库密码
        String key_password = "chengxudong";
        // 私钥密码
        String key_pwd = "chengxudong";
        // 私钥别名
        String alias = "chengxudong";

        // 访问证书路径
        ClassPathResource resource = new ClassPathResource(key_location);

        // 创建密钥工厂
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource, key_password.toCharArray());
        // 读取密钥对
        KeyPair keyPair =
                keyStoreKeyFactory.getKeyPair(alias, key_pwd.toCharArray());

        // 获取私钥
        RSAPrivateCrtKey rsaPrivateCrtKey = (RSAPrivateCrtKey) keyPair.getPrivate();

        // 定义 Payload
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", 1);

        // 生成令牌
        Jwt jwt = JwtHelper.encode(JSON.toJSONString(tokenMap), new RsaSigner(rsaPrivateCrtKey));

        // 取出令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
        return encoded;
    }

    private static void test04() {
        String token = test03();

        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwk5OBJeTnL7moMRNuPLtxcqGKh7diSuw4q7JHqLKbKvGzivJeXxkUsRjPJe85vpJqDLn1XYdMDZb9yJMbyBOYSRLrCZtXIZKOj3UWXNzMxSYRu0tvWaHrIz8ciGugOMEXup2NMgT0g2fsTJNeL47V8wqlUZaULqsGcpLFgSZufdvEleLVvPBKq6GZ0U9xsRkch8Ul8/NN/HIC8qDEDLgwlUdHGBn/qBhc2LrA0iHGC55EIAgvwWlfJvapz0w6A4I0gQ+caqW/nABLmfqyx32oUXrLyiObKQyjgICdeveZlQS6sBODhc9Bp7cIpBfBK9lB6a7M4TsAm8QjJBXSrfP7QIDAQAB-----END PUBLIC KEY-----";
        // 校验 jwt
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(publicKey));

        // 获取原始内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        // jwt 令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

    public static void main(String[] args) {
        test04();
    }
}