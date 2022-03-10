
// https://www.cnblogs.com/zhanggaofeng/p/5901316.html
// pub.h
#ifndef _vsucess

#define _vsucess

#ifdef _cplusplus
extern "C"
{

#endif
    // 服务器创建 socket
    int server_socket(int port);

    // 设置非阻塞
    int setnonblock(int st);

    // 接受客户端 socket
    int server_accept(int st);

    // 关闭 socket
    int close_socket(int st);

    // 接受消息
    int socket_recv(int st);

    // 连接服务器
    int connect_server(char *ipaddr, int port);

    // 发送消息
    int socket_send(int st);

    // 将 sockadd_in 转换成 IP 地址
    int sockaddr_toa(const struct sockaddr_in *addr, char *ipaddr);

#ifdef _cplusplus
}
#endif

#endif