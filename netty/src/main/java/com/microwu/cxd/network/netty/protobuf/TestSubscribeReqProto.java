package com.microwu.cxd.network.netty.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/24   11:59
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TestSubscribeReqProto {
    /**
     * 编码
     * @param req
     * @return
     */
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }

    /**
     * 解码
     * @param body
     * @return
     * @throws InvalidProtocolBufferException
     */
    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    /**
     * 编码需要SubscribeReqProto.SubscribeReq.builder 实例, 通过build 构建对象
     * @return
     */
    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("ChengXuDong");
        builder.setProductName("Netty Book");
        List<String> address = new ArrayList<>();
        address.add("NanJing");
        address.add("BeiJing");
        address.add("ShenZhen");
        builder.addAllAddress(address);
        return builder.build();

    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode: " + req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode: " + req.toString());
        System.out.println("Assert equal: " + req.equals(req2));

    }

}