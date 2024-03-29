#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include <errno.h>

#include <netinet/in.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/epoll.h>
#include <unistd.h>
#include <sys/types.h>

#define IPADDRESS "127.0.0.1"
#define PORT 8080
#define MAXSIZE 1024
#define LISTENQ 5
#define FDSIZE 1000
#define EPOLLEVENT 100

// 函数声明
// 创建套接字并进行绑定
static int socket_bind(const char *ip, int port);
// IO 多路复用 epoll
static void do_epoll(int listenfd);
// 事件处理函数
static void handle_events(int epollfd, struct epoll_event *events, int num, int listenfd, char *buf);
// 处理接收到的连接
static void handle_accpet(int epollfd, int listenfd);
// 读处理
static void do_read(int epollfd, int fd, char *buf);
// 写处理
static void do_write(int epollfd, int fd, char *buf);
// 添加事件
static void add_event(int epollfd, int fd, int state);
// 修改事件
static void modify_event(int epollfd, int fd, int state);
// 删除事件
static void delete_event(int epollfd, int fd, int state);

int main(int argc, char *argv[])
{
    int listenfd;
    listenfd = socket_bind(IPADDRESS, PORT);
    listen(listenfd, LISTENQ);
    do_epoll(listenfd);
    return 0;
}

static int socket_bind(const char *ip, int port)
{
    int listenfd;
}
