package com.microwu.cxd.nio.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.time.Instant;
import java.util.*;

import static com.microwu.cxd.nio.http.StatusCodeEnum.OK;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/20   14:23
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class TinyHttpd {
    private static final int DEFAULT_PORT = 8080;

    private static final int DEFAULT_BUFFER_SIZE = 4096;

    private static final String INDEX_PAGE = "index.html";

    private static final String STATIC_RESOURCE_DIR = "static";

    private static final String META_RESOURCE_DIR_PREFIX = "/meta/";

    private static final String KEY_VALUE_SEPARATOR = ":";

    private static final String CRLF = "\r\n";

    private int port;

    public TinyHttpd() {
        this(DEFAULT_PORT);
    }

    public TinyHttpd(int port) {
        this.port = port;
    }

    /**
     * 启动
     *
     * ServerSocketChannel 是服务端
     * SocketChannel 是客户端
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/20  14:35
     *
     * @param
     * @return  void
     */
    public void start() throws IOException {
        // epoll_create
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", port));
        serverSocketChannel.configureBlocking(false);

        System.out.println(String.format("TinyHttpd 已启动, 正在监听 %d 端口...", port));

        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while(true) {
            // epoll_ctl - 添加或删除所要监听的socket 到epoll等待队列
            // 中间过程是 中断程序添加rdlist(就绪列表), 并指向socket
            // epoll_wait - 看rdlist是否有socket
            int readyNum = selector.select();
            if(readyNum == 0) {
                continue;
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()) {
                // SelectionKey 代表了就绪事件
                SelectionKey next = iterator.next();
                iterator.remove();

                if(next.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                } else if(next.isReadable()) {
                    request(next);
                    next.interestOps(SelectionKey.OP_WRITE);
                } else if(next.isWritable()) {
                    response(next);
                }
            }
        }
    }

    /**
     * 处理请求
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/20  14:35
     *
     * @param   	selectionKey
     * @return  void
     */
    public void request(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        socketChannel.read(byteBuffer);

        byteBuffer.flip();
        byte[] bytes = new byte[byteBuffer.limit()];
        byteBuffer.get(bytes);
        String headerString = new String(bytes);
        request(selectionKey);
        try{
            Headers headers = parseHeader(headerString);
            selectionKey.attach(Optional.of(headers));
        }catch (Exception e) {
            selectionKey.attach(Optional.empty());
        }
    }

    /**
     * 解析消息头
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/20  14:41
     *
     * @param   	headerStr
     * @return  com.microwu.cxd.nio.http.Headers
     */
    public Headers parseHeader(String headerStr) {
        if(Objects.isNull(headerStr) || headerStr.isEmpty()) {
            throw new RuntimeException();
        }
        int index = headerStr.indexOf(CRLF);
        if(index == -1) {
            throw new RuntimeException();
        }

        Headers headers = new Headers();
        String firstLine = headerStr.substring(0, index);
        String[] parts = firstLine.split(" ");

        /**
         * 请求头格式: method path version
         * 比如:
         *  GET /index.html HTTP/1.1
         */
        if(parts.length < 3) {
            throw new RuntimeException();
        }

        headers.setMethod(parts[0]);
        headers.setPath(parts[1]);
        headers.setVersion(parts[2]);

        parts = headerStr.split(CRLF);
        for(String part : parts) {
            index = part.indexOf(KEY_VALUE_SEPARATOR);
            if(index == -1) {
                continue;
            }
            String key = part.substring(0, index);
            if(index == -1 || index + 1 >= part.length()) {
                headers.set(key, "");
                continue;
            }
            String value = part.substring(index + 1);
            headers.set(key, value);
        }

        return headers;
    }

    private void response(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        Optional<Headers> op = (Optional<Headers>) selectionKey.attachment();

//        // 处理无效请求, 返回404
//        if(!op.isPresent()) {
//            handleBadRequest(socketChannel);
//            socketChannel.close();
//            return;
//        }
//
//        String ip = socketChannel.getRemoteAddress().toString().replace("/", "");
//        Headers headers = op.get();
//        // 处理 403
//        if(headers.getPath().startsWith(META_RESOURCE_DIR_PREFIX)) {
//            handlerForbidden(socketChannel);
//            socketChannel.close();
//            log(ip, headers, FORBIDDEN.getCode());
//            return;
//        }

        String ip = socketChannel.getRemoteAddress().toString().replace("/", "");
        Headers headers = op.get();
        try {
            handleOk(socketChannel, headers.getPath());
            log(ip, headers, OK.getCode());
        }catch (FileNotFoundException e) {

        }finally {
            socketChannel.close();
        }
    }

    /**
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/11/20  15:10
     *
     * @param   	channel
     * @param 		path
     * @return  void
     */
    private void handleOk(SocketChannel channel, String path) throws IOException {
        ResponseHeaders headers = new ResponseHeaders(OK.getCode());

        ByteBuffer bodyBuffer = readFile(path);
        headers.setContentLength(bodyBuffer.capacity());
        headers.setContentType("");
        ByteBuffer headerBuffer = ByteBuffer.wrap(headers.toString().getBytes());

        channel.write(new ByteBuffer[]{headerBuffer, bodyBuffer});
    }

    private ByteBuffer readFile(String path) throws IOException {
        path = STATIC_RESOURCE_DIR + (path.endsWith("/") ? path + INDEX_PAGE : path);
        RandomAccessFile file = new RandomAccessFile(path, "r");
        FileChannel channel = file.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int) channel.size());
        channel.read(buffer);

        buffer.flip();
        return buffer;
    }

    private void log(String ip, Headers headers, int code) {
        String dateStr = Date.from(Instant.now()).toString();
        String msg = String.format("%s [%s] \"%s %s %s\" %d %s",
                ip, dateStr, headers.getMethod(), headers.getPath(), headers.getVersion(), code, headers.get("User-Agent"));
        System.out.println(msg);
    }

    public static void main(String[] args) throws IOException {
        new TinyHttpd().start();
    }
}