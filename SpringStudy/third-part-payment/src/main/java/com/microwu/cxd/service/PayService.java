package com.microwu.cxd.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/23   22:38
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class PayService {

    private static final String APP_ID = "2016101100657449";

    private static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3tRvvaHA6M9rhA05/6QWPiYIJHg1E/M9Ih9+TVNuEU52+KtszF1f6CgUBVhqOBZs2s3ZRfk9AnwjhCMkwG2Nhp14gLQdC25OTi/R0RtXvBNFMuAFQcg+WSLss054I/03xNkOX58copTO8tVAnrIXXD4DdPazedi8PbH9MEc/LUuRq55KgSl3CqHPCTC0ZxX0ECMHZ7BKABeeo8seEtws5WSB4frq+8kjtomknB4RFO3RnRpM9bjnVHEN3QlsMI8AKHRfqtuRKFY4P5SbDH2sYrKJl+EZ0KEj/dPXG0m7Y55/Jcz0FllG88msXJMeR2mEkfNLlUbrsoWVb4mR72NLywIDAQAB";

    private static final String APP_PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDe1G+9ocDoz2uEDTn/pBY+JggkeDUT8z0iH35NU24RTnb4q2zMXV/oKBQFWGo4FmzazdlF+T0CfCOEIyTAbY2GnXiAtB0Lbk5OL9HRG1e8E0Uy4AVByD5ZIuyzTngj/TfE2Q5fnxyilM7y1UCeshdcPgN09rN52Lw9sf0wRz8tS5GrnkqBKXcKoc8JMLRnFfQQIwdnsEoAF56jyx4S3CzlZIHh+ur7ySO2iaScHhEU7dGdGkz1uOdUcQ3dCWwwjwAodF+q25EoVjg/lJsMfaxisomX4RnQoSP909cbSbtjnn8lzPQWWUbzyaxckx5HaYSR80uVRuuyhZVviZHvY0vLAgMBAAECggEAbEEqtCcEUFaMtk+ysKaoA7N4WzdnGfUe6xf/xVNV8uR62h52MebfNCtcZqhomi0S304IU31djXp/yq9EKJRQoGiaZXSbc7U4J7YFqiAthHqcMLYy951v5OAZvJbPuTRIxsJowD3NrJK8466EwVRmEvMxvL4pvXV1Jkf1aGIEqFzY8WHev9yO0da/eB9dpsEFe3SBQL3cjKXt6xbjPUn64yPu9Vc28y+qucBGMmB4iIXqwy4qa8B8i9pDG1Q6/v2ZAy/Cqj9Rk+3ckg5oE4+E9Vo99PUqmh17rnD+2bXOMFTXf6IbFwbeOR9JK8KvjqTTPBb+tJm77CyVwDHJD8QiKQKBgQD5q38YZ7n/xpOrbugPPLgy6J9gTpWbJAd3mj2xXqhmE0LlRxU6eQeL3D4mJDZ3VKlDTxPU8Z/Q0aJT/BRwPUMavy0hGT//Oup2t7hme2TS+1Phs2O/zn3BtOUxmZ8Jw+iHgpo0YaxjhXSArIWDIvc2L0IC7pZ9Osu7hmIZtDwn1QKBgQDkertz67DYalr6rYSSjxVlhYHcr4pKTTZplob7SKre1CKC8A9w9K86v7qvlQ/iTNPPcqBA9KUeOiGjuwcGO3qYVlIbtp43fIV5LvDkcaVkZm95VjJer4WZhf53wCsBFBqq1tyASiXQl5/XCZWyOU70Cy2pyHauRwdm6PDZSn4VHwKBgQDpbP7Z+Xi7+5PqzzllrZZl+nmv2CklsGzO1MVjRKXbMZz0sh3V2nxEtfjhPjAyJ1RNWuymxfVujeDf9qnBOnW31w/Bh1L/NL0rm59rKWjdnR8SwN684LlQ1ukGE6POxHxt3ZyA5uT+8zI2b5YwQguxfEOSbDEFCmiafgzQDWPEtQKBgQCFZR7CfE4A3H+Hsf0f8EgGwN7j8N0lqlTlasweM/wHIwWPOwdY5JLWExC06KugbUiQ6pSjt/IbZsczJ8PfIKTgAi3oHkPbk2j+KqjcR3nP13qn9epsm7+9tZyvX9P7yjgTnGSaxi+zzIK28tKifpEZLNmZMgAQmkm5DLFpu/NG8wKBgQCoybhF7Fw6VbNz6NHxXs5pJS10XCrv4zwFY3mnIq81DgvIpweyJY5nKc4NZ20p8jH5oKlLjwH+AaeGcTPVViVyQAlTQXyPxjrcFaaDATcXUKLgCcVWi9Hk92c+vbc4V2kOFNA8OTTkkXKVXswR+hukgAtwgTxk19luAtcOIuHbNA==";

    private static final String CHARSET = "utf-8";

    public String pay() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do", APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2"); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        Random random = new Random();
        long order_no = random.nextLong();
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\"" + order_no + "\"," +
                " \"total_amount\":\"88.88\"," +
                " \"subject\":\"Iphone6 16G\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        String form = "";
        try {
            AlipayTradeWapPayResponse response = alipayClient.pageExecute(alipayRequest);
            System.out.println(response.getCode() + "\r\n" + response.getMsg() + "\r\n" + response.getBody() + "\r\n" + response.isSuccess());
            form = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return form;
    }
}