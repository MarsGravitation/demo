package com.cxd.od.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2024/2/5   11:16
 * Update History:
 * Author        Time            Content
 */
public class QrCodeUtil {
    private QrCodeUtil() { }

    /**
     * @description: 生成一个普通的黑白二维码
     * @param content 二维码内容
     * @param width 生成图片宽度
     * @param height 生成图片高度
     * @return 二维码图片字节流
     **/
    public static byte[] drawQrCode(String content, int width, int height) throws WriterException, IOException {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, new HashMap() {

            private static final long serialVersionUID = 1L;

            {
                put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
                put(EncodeHintType.CHARACTER_SET, "UTF-8");
                put(EncodeHintType.MARGIN, 0);
            }
        });
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 开始利用二维码数据图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        image.flush();

        //分配13Kb
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(13312);
        ImageIO.write(image, "png", outputStream);

        return outputStream.toByteArray();
    }
}
