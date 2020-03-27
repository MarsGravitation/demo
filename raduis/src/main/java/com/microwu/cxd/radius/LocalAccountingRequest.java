package com.microwu.cxd.radius;

import org.springframework.stereotype.Component;
import org.tinyradius.attribute.RadiusAttribute;
import org.tinyradius.dictionary.DefaultDictionary;
import org.tinyradius.dictionary.Dictionary;
import org.tinyradius.packet.AccessRequest;
import org.tinyradius.packet.AccountingRequest;
import org.tinyradius.packet.RadiusPacket;
import org.tinyradius.util.RadiusException;

import java.io.IOException;
import java.io.InputStream;

/**
 * RadiusService 解析
 * @author fulei             fulei@microwu.com
 * @date 2019/11/8
 * CopyRight    北京小悟科技有限公司    http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Component
public class LocalAccountingRequest extends AccountingRequest {

    @Override
    protected void checkRequestAuthenticator(String sharedSecret, int packetLength, byte[] attributes) throws RadiusException {
//        byte[] expectedAuthenticator = this.updateRequestAuthenticator(sharedSecret, packetLength, attributes);
//        byte[] receivedAuth = this.getAuthenticator();
//
//        for(int i = 0; i < 16; ++i) {
//            if (expectedAuthenticator[i] != receivedAuth[i]) {
//                throw new RadiusException("request authenticator invalid");
//            }
//        }

    }

    public static RadiusPacket decodeRequestPacket(InputStream in, String sharedSecret, int forceType) throws IOException, RadiusException {
        return decodePacket(DefaultDictionary.getDefaultDictionary(), in, sharedSecret, (RadiusPacket)null, forceType);
    }

    public static RadiusPacket decodePacket(Dictionary dictionary, InputStream in, String sharedSecret, RadiusPacket request, int forceType) throws IOException, RadiusException {
        if (sharedSecret != null && sharedSecret.length() != 0) {
            if (request != null && request.getAuthenticator() == null) {
                throw new RuntimeException("request authenticator not set");
            } else {
                int type = in.read() & 255;
                int identifier = in.read() & 255;
                int length = (in.read() & 255) << 8 | in.read() & 255;
                if (request != null && request.getPacketIdentifier() != identifier) {
                    throw new RadiusException("bad packet: invalid packet identifier (request: " + request.getPacketIdentifier() + ", response: " + identifier);
                } else {
                    byte[] authenticator = new byte[16];
                    byte[] attributeData = new byte[length - 20];
                    in.read(authenticator);
                    in.read(attributeData);
                    int pos = 0;

                    for(int var11 = 0; pos < attributeData.length; ++var11) {
                        if (pos + 1 >= attributeData.length) {
                            throw new RadiusException("bad packet: attribute length mismatch");
                        }

                        int attributeLength = attributeData[pos + 1] & 255;
                        if (attributeLength < 2) {
                            throw new RadiusException("bad packet: invalid attribute length");
                        }

                        pos += attributeLength;
                    }

                    if (pos != attributeData.length) {
                        throw new RadiusException("bad packet: attribute length mismatch");
                    } else {
                        LocalAccountingRequest rp = createRadiusPacket(forceType == 0 ? type : forceType);
                        rp.setDictionary(dictionary);
                        rp.setPacketType(type);
                        rp.setPacketIdentifier(identifier);

                        int attributeLength;
                        for(pos = 0; pos < attributeData.length; pos += attributeLength) {
                            int attributeType = attributeData[pos] & 255;
                            attributeLength = attributeData[pos + 1] & 255;
                            RadiusAttribute a = RadiusAttribute.createRadiusAttribute(dictionary, -1, attributeType);
                            a.readAttribute(attributeData, pos, attributeLength);
                            rp.addAttribute(a);
                        }

                        if (request == null) {
                            rp.decodeRequestAttributes(sharedSecret);
                            rp.checkRequestAuthenticator(sharedSecret, length, attributeData);
                        } else {
                            rp.checkResponseAuthenticator(sharedSecret, length, attributeData, request.getAuthenticator());
                        }

                        return rp;
                    }
                }
            }
        } else {
            throw new RuntimeException("no shared secret has been set");
        }
    }


//    public static RadiusPacket decodePacket(Dictionary dictionary, InputStream in, String sharedSecret, RadiusPacket request, int forceType) throws IOException, RadiusException {
//        if (sharedSecret != null && sharedSecret.length() != 0) {
//            if (request != null && request.getAuthenticator() == null) {
//                throw new RuntimeException("request authenticator not set");
//            } else {
//                int type = in.read() & 255;
//                int identifier = in.read() & 255;
//                int length = (in.read() & 255) << 8 | in.read() & 255;
//                if (request != null && request.getPacketIdentifier() != identifier) {
//                    throw new RadiusException("bad packet: invalid packet identifier (request: " + request.getPacketIdentifier() + ", response: " + identifier);
//                } else if (length < 20) {
//                    throw new RadiusException("bad packet: packet too short (" + length + " bytes)");
//                } else if (length > 4096) {
//                    throw new RadiusException("bad packet: packet too long (" + length + " bytes)");
//                } else {
//                    byte[] authenticator = new byte[16];
//                    byte[] attributeData = new byte[length - 20];
//                    in.read(authenticator);
//                    in.read(attributeData);
//                    int pos = 0;
//
//                    for(int var11 = 0; pos < attributeData.length; ++var11) {
//                        if (pos + 1 >= attributeData.length) {
//                            throw new RadiusException("bad packet: attribute length mismatch");
//                        }
//
//                        int attributeLength = attributeData[pos + 1] & 255;
//                        if (attributeLength < 2) {
//                            throw new RadiusException("bad packet: invalid attribute length");
//                        }
//
//                        pos += attributeLength;
//                    }
//
//                    if (pos != attributeData.length) {
//                        throw new RadiusException("bad packet: attribute length mismatch");
//                    } else {
//                        LocalAccountingRequest rp = createRadiusPacket(forceType == 0 ? type : forceType);
//                        rp.setDictionary(dictionary);
//                        rp.setPacketType(type);
//                        rp.setPacketIdentifier(identifier);
//
//                        int attributeLength;
//                        for(pos = 0; pos < attributeData.length; pos += attributeLength) {
//                            int attributeType = attributeData[pos] & 255;
//                            attributeLength = attributeData[pos + 1] & 255;
//                            RadiusAttribute a = RadiusAttribute.createRadiusAttribute(dictionary, -1, attributeType);
//                            a.readAttribute(attributeData, pos, attributeLength);
//                            rp.addAttribute(a);
//                        }
//
//                        if (request == null) {
//                            rp.decodeRequestAttributes(sharedSecret);
//                            rp.checkRequestAuthenticator(sharedSecret, length, attributeData);
//                        } else {
//                            rp.checkResponseAuthenticator(sharedSecret, length, attributeData, request.getAuthenticator());
//                        }
//
//                        return rp;
//                    }
//                }
//            }
//        } else {
//            throw new RuntimeException("no shared secret has been set");
//        }
//    }


    public static LocalAccountingRequest createRadiusPacket(int type) {
        Object rp;
        switch(type) {
            case 1:
                rp = new AccessRequest();
                break;
            case 2:
            case 3:
            case 5:
            default:
                rp = new RadiusPacket();
                break;
            case 4:
                rp = new LocalAccountingRequest();
        }

        ((LocalAccountingRequest)rp).setPacketType(type);
        return (LocalAccountingRequest)rp;
    }
}
