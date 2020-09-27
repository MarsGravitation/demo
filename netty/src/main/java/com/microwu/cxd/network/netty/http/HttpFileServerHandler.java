package com.microwu.cxd.network.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/29   11:50
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private final String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if (!fullHttpRequest.decoderResult().isSuccess()) {
            sendError(channelHandlerContext, HttpResponseStatus.BAD_REQUEST);
        }

        if (fullHttpRequest.method() != HttpMethod.GET) {
            sendError(channelHandlerContext, HttpResponseStatus.METHOD_NOT_ALLOWED);
        }

        final String uri = fullHttpRequest.uri();
        final String path = sanitizeUri(uri);
        if (path == null) {
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendListing(channelHandlerContext, file);
            } else {
                sendRedirect(channelHandlerContext, uri + "/");
            }
            return;
        }

        if (!file.isFile()) {
            sendError(channelHandlerContext, HttpResponseStatus.FORBIDDEN);
            return;
        }

        RandomAccessFile randomAccessFile = null;
        randomAccessFile = new RandomAccessFile(file, "r");

        long fileLength = randomAccessFile.length();
        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpHeaderUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);

        if (HttpHeaderUtil.isKeepAlive(fullHttpRequest)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        channelHandlerContext.write(response);
        ChannelFuture sendFileFuture = null;
        sendFileFuture = channelHandlerContext.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), channelHandlerContext.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    System.out.println("Transfer progress: " + progress);
                } else {
                    System.out.println("Transfer progress" + progress + "/" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                System.out.println("Transfer complete.");
            }
        });

        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpHeaderUtil.isKeepAlive(fullHttpRequest)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void sendRedirect(ChannelHandlerContext channelHandlerContext, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private void sendListing(ChannelHandlerContext channelHandlerContext, File dir) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");

        String dirPath = dir.getPath();
        StringBuilder buf = new StringBuilder();

        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
        buf.append(dirPath);
        buf.append("目录:");
        buf.append("</title></head><body>\r\n");

        buf.append("<h3>");
        buf.append(dirPath).append(" 目录：");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\" ../\")..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }
            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }
            buf.append("<li>链接：<a href=\"");
            buf.append(name);
            buf.append("\">");
            buf.append(name);
            buf.append("</a></li>\r\n");
        }

        buf.append("</ul></body></html>\r\n");

        ByteBuf byteBuf = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(byteBuf);
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private String sanitizeUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (!uri.startsWith(url)) {
            return null;
        }
        if (!uri.startsWith("/")) {
            return null;
        }

        uri = uri.replace('/', File.separatorChar);
        if (uri.contains(File.separator + ".") || uri.contains('.' + File.separator) || uri.startsWith(".") || uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        return System.getProperty("user.dir") + File.separator + uri;
    }

    private void setContentTypeHeader(DefaultHttpResponse response, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file.getParent()));
    }

    private void sendError(ChannelHandlerContext channelHandlerContext, HttpResponseStatus status) {
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }
}