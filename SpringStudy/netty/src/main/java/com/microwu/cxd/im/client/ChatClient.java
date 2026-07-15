package com.microwu.cxd.im.client;

import com.microwu.cxd.im.handler.ChatMessageCodec;
import com.microwu.cxd.im.handler.HeartbeatClientHandler;
import com.microwu.cxd.im.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/23   10:17
 * Update History:
 * Author        Time            Content
 */
public class ChatClient {

    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ChatMessageCodec chatMessageCodec = new ChatMessageCodec();


        CountDownLatch countDownLatch = new CountDownLatch(1);
        AtomicBoolean login = new AtomicBoolean(false);
        AtomicBoolean exit = new AtomicBoolean(false);
        Scanner scanner = new Scanner(System.in);

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            // 打开长连接配置
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(chatMessageCodec);
                    ch.pipeline().addLast(new IdleStateHandler(0, 3, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast(new HeartbeatClientHandler());
                    ch.pipeline().addLast("client handler", new ChannelInboundHandlerAdapter() {

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println("msg:" + msg);
                            if (msg instanceof LoginResponseMessage) {
                                LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
                                if (loginResponseMessage.isSuccess()) {
                                    login.set(true);
                                }
                                countDownLatch.countDown();
                            }
                        }

                        // 连接建立后，触发 active
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            System.out.println("client channel active...");
                            // 负责接收用户在控制台的输入，负责向服务器发送各种消息
                            new Thread(() -> {
                                System.out.println("请输入用户名：");
                                String username = scanner.nextLine();
                                if (exit.get()) {
                                    return;
                                }
                                System.out.println("请输入密码：");
                                String password = scanner.nextLine();
                                if (exit.get()) {
                                    return;
                                }
                                // 构造消息对象
                                LoginRequestMessage message = new LoginRequestMessage(username, password);
                                System.out.println(message);
                                // 发送消息
                                ctx.writeAndFlush(message);
                                System.out.println("等待后续操作。。。");
                                try {
                                    countDownLatch.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!login.get()) {
                                    ctx.channel().close();
                                    return;
                                }
                                while (true) {
                                    System.out.println("==================================");
                                    System.out.println("\t1、发送单聊消息");
                                    System.out.println("\t2、发送群聊消息");
                                    System.out.println("\t3、创建一个群聊");
                                    System.out.println("\t4、获取群聊成员");
                                    System.out.println("\t5、加入一个群聊");
                                    System.out.println("\t6、退出一个群聊");
                                    System.out.println("\t7、退出聊天系统");
                                    System.out.println("==================================");
                                    String command = null;
                                    try {
                                        command = scanner.nextLine();
                                    } catch (Exception e) {
                                        break;
                                    }
                                    if (exit.get()) {
                                        return;
                                    }
                                    switch (command) {
                                        case "1":
                                            System.out.println("请选择你要发送消息给谁：");
                                            String toUsername = scanner.nextLine();
                                            System.out.println("请输入你要发送的消息内容：");
                                            String content = scanner.nextLine();
                                            ctx.writeAndFlush(new ChatRequestMessage(username, toUsername, content));
                                            break;
                                        case "2":
                                            System.out.println("请选择你要发送消息的群聊：");
                                            String groupName = scanner.nextLine();
                                            System.out.println("请输入你要发送的消息内容：");
                                            String groupContent = scanner.nextLine();
                                            ctx.writeAndFlush(new GroupChatRequestMessage(username, groupName, groupContent));
                                            break;
                                        case "3":
                                            System.out.println("请输入你要创建的群聊昵称：");
                                            String newGroupName = scanner.nextLine();
                                            System.out.print("请选择你要邀请的群成员（不同成员用、分割）：");
                                            String members = scanner.nextLine();
                                            Set<String> memberSet = new HashSet<>(Arrays.asList(members.split("、")));
                                            memberSet.add(username);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(newGroupName, memberSet));
                                            break;
                                    }
                                }
                            }).start();
                        }
                    });
                }
            });

            Channel channel = bootstrap.connect("localhost", 8888).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            System.out.println("客户端出现错误：" + e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
