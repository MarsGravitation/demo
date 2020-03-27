package com.microwu.cxd.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Jwt Util
 *
 * @author chentongwei@bshf360.com 2018-06-07 19:51
 */
@Component
public class JwtTokenUtil {

    /** 默认前面秘钥 */
    private static String secret = "defaultSecret";

    /** token默认有效期时长，1小时 */
    private static Long expiration = 3600L;

    /** token的唯一标记，目前用于redis里存储，解决同账号，同时只能单用户登录的情况 */
    private static String md5Key = "randomKey";

    /** token还剩余多长时间就自动刷新下，默认是60s */
    private static Long autoRefreshTokenExpiration = 60L;

    /**
     * 获取用户名从token中
     */
    public static String getUsernameFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * 获取私有的jwt claim
     */
    public static String getPrivateClaimFromToken(String token, String key) {
        return getClaimFromToken(token).get(key).toString();
    }

    /**
     * 获取md5 key从token中
     */
    public static String getMd5KeyFromToken(String token) {
        return getPrivateClaimFromToken(token, md5Key);
    }

    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public static Boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException expiredJwtException) {
            return true;
        }
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public static String generateToken(String userName, String randomKey) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(md5Key, randomKey);
        return doGenerateToken(claims, userName);
    }

    /**
     * 由字符串生成加密key
     * @return
     */
    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.decodeBase64(secret);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 生成token
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, generalKey())
                .compact();
    }

    /**
     * 获取混淆MD5签名用的随机字符串
     */
    public static String getRandomKey() {
        return getRandomString(6);
    }

    /**
     * 获取随机位数的字符串
     */
    public static String getRandomString(int length) {
        final String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 刷新token
     *
     * @param token：token
     * @return
     */
    public String refreshToken(String token, String randomKey) {
        String refreshedToken;
        try {
            final Claims claims = getClaimFromToken(token);
            refreshedToken = generateToken(claims.getSubject(), randomKey);
        } catch (Exception e1) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

}