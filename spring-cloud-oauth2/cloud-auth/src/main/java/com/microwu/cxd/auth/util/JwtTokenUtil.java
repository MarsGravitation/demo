package com.microwu.cxd.auth.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: JWT的构成
 *  header： {'typ': 'JWT', 'alg': ''HS256}
 *      声明类型 + 声明加密算法
 *
 *  payload： 载荷
 *      > 标准中注册的声明（建议但不强制使用）
 *          iss(jwt签发者), sub(jwt所面向的用户)
 *          aud(接受jwt的一方), exp(过期时间)
 *          nbf(定义在什么时间之前，jwt不可用)
 *          iat(jwt的签发时间)
 *          jti(jwt的唯一身份标识，主要用来作为一次性token，避免重放攻击)
 *      > 公共的声明
 *          一般添加用户的相关信息或业务需要的必要信息，不建议添加敏感信息
 *      > 私有的声明
 *
 * signature：签证信息
 *  header(base64) + payload(base64) + secret
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:                   2020/5/10   12:30
 * Copyright:              北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * <p>
 * Author        Time            Content
 */
public class JwtTokenUtil {

    private static final String SECRET = "chengxudong";
    private static final String MD5_KEY = "randomKey";

    /**
     * 对私钥进行加密
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/10  15:28
     *
     * @param
     * @return  javax.crypto.SecretKey
     */
    private SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(SECRET);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 生成 Token
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/10  12:34
     *
     * @param   	claims
     * @param 		subject
     * @return  java.lang.String
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + 1000 * 60 * 60);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
    }

    /**
     * 用户名 + 随机数 做载荷
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/10  15:31
     *
     * @param   	userName
     * @param 		randomKey
     * @return  java.lang.String
     */
    public String generateToken(String userName, String randomKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(MD5_KEY, randomKey);
        return doGenerateToken(claims, userName);
    }

    /**
     * 获取载荷部分
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/5/10  15:33
     *
     * @param   	token
     * @return  io.jsonwebtoken.Claims
     */
    public Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }
}