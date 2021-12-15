package com.microwu.cxd.websocket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microwu.cxd.websocket.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * WebSocketServer
 *
 *  1. 因为 WebSocket 是类似客户端服务端的形式 - 采用 ws 协议，那么这里的 WebSocketServer 其实
 *  就相当于 ws 协议的 Controller
 *  2. 直接 @ServerEndpoint @Component 启用即可，然后在里面实现 @OnOpen、@onClose、@onMessage
 *  3. 新建一个 ConcurrentHashMap webSocketMap 用于接受当前 userId 的 WebSocket，方便 IM 直接对
 *  userId 进行推送消息。单机版实现到这里就可以了
 *  4. 集群版（多个 ws 节点）还需要借助 MySQL 或者 redis 等进行处理，改造对应的 sendMessage 方法
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/6/5  16:34
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Component
@ServerEndpoint("/imserver/{userId}")
public class WebSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 静态变量，用来记录当前在线连接数，应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * 用来存放每个客户端对应的 WebSocket 对象
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 与某个客户端的连接对话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 接受 userId
     */
    private String userId = "";

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功调用的方法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:52
     *
     * @param session
     * @param userId
     * @return void
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
        } else {
            addOnLineCount();
        }

        webSocketMap.put(userId, this);

        LOGGER.info("用户连接：" + userId + ", 当前在线人数为：" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            LOGGER.error("用户：" + userId + "，网络异常！！！");
        }

    }

    /**
     * 实现服务器主动推送
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     17:03
     *
     * @param message
     * @return void
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:59
     *
     * @param message
     * @return void
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // 可以群发消息
        // 消息保存到数据库、redis
        if (StringUtils.isEmpty(message)) {
            return;
        }
        // 解析发送的报文
        try {
            Message m = objectMapper.readValue(message, Message.class);
            if (webSocketMap.containsKey(m.getToUserId())) {
                // 传送给对应 toUserId 用户的 websocket
                webSocketMap.get(m.getToUserId()).sendMessage(objectMapper.writeValueAsString(m));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送自定义消息
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     17:16
     *
     * @param message
     * @param userId
     * @return void
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 关闭连接调用的方法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:55
     *
     * @param
     * @return void
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            subOnlineCount();
        }

        LOGGER.info("用户退出：" + userId + "，当前在线人数为：" + getOnlineCount());
    }


    /**
     * 获取在线人数
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:58
     *
     * @param
     * @return java.lang.Integer
     */
    public static synchronized Integer getOnlineCount() {
        return onlineCount;
    }

    /**
     * 增加在线人数
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/6/5     16:57
     *
     * @param
     * @return void
     */
    public static synchronized void addOnLineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

}
