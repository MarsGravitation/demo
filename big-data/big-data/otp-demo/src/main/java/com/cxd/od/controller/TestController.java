package com.cxd.od.controller;

import com.cxd.od.utils.QrCodeUtil;
import com.cxd.od.utils.TotpUtils;
import com.google.zxing.WriterException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Base64;

/**
 * Description:
 *
 * https://www.cnblogs.com/informatics/p/17625253.html
 * https://blog.csdn.net/wangerrong/article/details/132412282
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/5   11:28
 * Update History:
 * Author        Time            Content
 */
@RestController
public class TestController {

    String secret = TotpUtils.getRandomSecretBase32(64);


    @GetMapping("/otpcode")
    public String bindOtpCode(@RequestParam("username") String username) throws IOException, WriterException {
        /**
         * 生成并保存用户密钥
         */
        System.out.println("secret: " + secret);

        //请求google获取otp code
        final String QRCODE_TEMPLATE = "otpauth://totp/{0}?secret={1}&issuer={2}";
        String content = MessageFormat.format(QRCODE_TEMPLATE, username, secret, "SSO");
        try {
            byte[] contents = QrCodeUtil.drawQrCode(content, 320, 320);
            return Base64.getEncoder().encodeToString(contents);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/verifyOtpCode")
    public String verifyOtpCode(@RequestParam("username") String username,@RequestParam("code") String code) throws IOException, WriterException {
        return String.valueOf(TotpUtils.verify(secret ,code));
    }

}
