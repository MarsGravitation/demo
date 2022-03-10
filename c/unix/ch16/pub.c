// 辅助方法 -- pub.c
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <netinet/in.h>
#include <fcntl.h>
#include "pub.h"

#define MAXBUF 1024

int socket_create()
{
    int st = socket(AF_INET, SOCK_STREAM, 0);
    if (st == -1)
    {
        printf("create socket failed! error message: %s\n", strerror(errno));
        return -1;
    }
    return st;
}

// 设置服务端 socket 地址重用
int socket_reuseaddr(int st)
{
    int on = 1;
    if (setsockopt(st, SOL_SOCKET, SO_REUSEADDR, &on, sizeof(on)) == -1)
    {
        printf("setsockopt reuseaddr failed! error message: %s\n", strerror(errno));
        close_socket(st);
        return -1;
    }
    return 0;
}

int socket_bind(int st, int port)
{
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    if (bind(st, (struct sockaddr *)&addr, sizeof(addr)) == -1)
    {
        printf("bind failed! error message: %s\n", strerror(errno));
        close_socket(st);
        return -1;
    }
    if (listen(st, 20) == -1)
    {
        printf("listen failed! error message: %s\n", strerror(errno));
        close_socket(st);
        return -1;
    }
    return 0;
}

int server_socket(int port)
{
    if (port < 0)
    {
        printf("function server_socket param not correct!\n");
        return -1;
    }
    int st = socket_create();
    if (st < 0)
    {
        return -1;
    }
    if (socket_reuseaddr(st) < 0)
    {
        return -1;
    }
    if (socket_bind(st, port) < 0)
    {
        return -1;
    }
    return st;
}

int connect_server(char *ipaddr, int port)
{
    if (port < 0 || ipaddr == NULL)
    {
        printf("function connect_server param not correct!\n");
        return -1;
    }
    int st = socket_create();
    if (st < 0)
    {
        return -1;
    }
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = inet_addr(ipaddr);
    if (connect(st, (struct sockaddr *)&addr, sizeof(addr)) == -1)
    {
        printf("connect failed! error message: %s\n", strerror(errno));
        return -1;
    }
    return st;
}

int setnonblock(int st)
{
    if (st < 0)
    {
        printf("function setnonblock param not corrrect!\n");
        close_socket(st);
        return -1;
    }
    int opts = fcntl(st, F_GETFL);
    if (opts < 0)
    {
        printf("func fcntl failed! error message: %s\n", strerror(errno));
        return -1;
    }
    opts = opts | O_NONBLOCK;
    if (fcntl(st, F_SETFL, opts) < 0)
    {
        printf("func fcntl failed! error message: %s\n", strerror(errno));
        return -1;
    }
    return opts;
}

int server_accept(int st)
{
    if (st < 0)
    {
        printf("function accept_client socket param not correct!\n");
        return -1;
    }
    struct sockaddr_in addr;
    memset(&addr, 0, sizeof(addr));
    socklen_t len = sizeof(addr);
    int client_st = accept(st, (struct sockaddr *)&addr, &len);
    if (client_st < 0)
    {
        printf("accept client failed! error message: %s\n", strerror(errno));
        return -1;
    }
    else
    {
        char ipaddr[20] = {0};
        sockaddr_toa(&addr, ipaddr);
        printf("accept by %s\n", ipaddr);
    }
    return client_st;
}

int close_socket(int st)
{
    if (st < 0)
    {
        printf("function close_socket param not correct!\n");
        return -1;
    }
    close(st);
    return 0;
}

int sockaddr_toa(const struct sockaddr_in *addr, char *ipaddr)
{
    if (addr == NULL || ipaddr == NULL)
    {
        return -1;
    }
    unsigned char *p = (unsigned char *)&(addr->sin_addr.s_addr);
    sprintf(ipaddr, "%u.%u.%u.%u", p[0], p[1], p[2], p[3]);
    return 0;
}

int socket_recv(int st)
{
    if (st < 0)
    {
        printf("function socket_recv param not correct!\n");
        return -1;
    }
    char buf[MAXBUF] = {0};
    int rc = 0;
    rc = recv(st, buf, sizeof(buf), 0);
    if (rc == 0)
    {
        // client close
        printf("client is close!\n");
        return -1;
    }
    else if (rc < 0)
    {
        printf("recv failed! error message: %s\n", strerror(errno));
    }
    printf("%s", buf);
    return 0;
}

int socket_send(int st)
{
    char buf[MAXBUF] = {0};
    while (1)
    {
        read(STDIN_FILENO, buf, sizeof(buf));
        if (buf[0] == '0')
        {
            break;
        }
        if (send(st, buf, strlen(buf), 0) < 0)
        {
            printf("send failed! error message: %s\n", strerror(errno));
            return -1;
        }
        memset(buf, 0, sizeof(buf));
    }
    return 0;
}