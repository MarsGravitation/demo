package com.microwu.cxd.network.netty.blog.rpc.kernel.netty.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/7   15:14
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HessianSerializerUtils {

    public static <T> byte[] serialize(T obj) {
        byte[] bytes = null;
        // 1. 创建字节输出流
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        // 2. 对字节数组流进行再次封装
        HessianOutput hessianOutput = new HessianOutput(bos);

        try {
            hessianOutput.writeObject(obj);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public static <T> T deserialize(byte[] data) {
        if (data == null) {
            return null;
        }

        // 1. 将字节数组转换成字节输出流
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        HessianInput hessianInput = new HessianInput(bis);

        Object object = null;

        try {
            object = hessianInput.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (T) object;
    }
}