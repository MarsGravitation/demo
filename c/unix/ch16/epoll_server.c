//网络编程服务端
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <arpa/inet.h>  //htons()函数头文件
#include <netinet/in.h> //inet_addr()头文件
#include <fcntl.h>
#include <sys/epoll.h>
#include "pub.h"

#define MAXSOCKET 20

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        printf("please print one param!\n");
        return -1;
    }
    int listen_st = server_socket(atoi(argv[1]));
    if (listen_st < 0)
    {
        return -1;
    }
    struct epoll_event ev, events[100];
    int epfd = epoll_create(MAXSOCKET);
    setnonblock(listen_st);
    ev.data.fd = listen_st;
    ev.events = EPOLLIN | EPOLLERR | EPOLLHUP;
    epoll_ctl(epfd, EPOLL_CTL_ADD, listen_st, &ev);

    while (1)
    {
        int nfds = epoll_wait(epfd, events, MAXSOCKET, -1);
        if (nfds == -1)
        {
            printf("epoll_wait failed! error message: %s\n", strerror(errno));
            break;
        }
        int i;
        for (i = 0; i < nfds; i++)
        {
            if (events[i].data.fd < 0)
                continue;
            if (events[i].data.fd == listen_st)
            {
                int client_st = server_accept(listen_st);
                if (client_st < 0)
                {
                    continue;
                }
                setnonblock(client_st);
                struct epoll_event client_ev;
                client_ev.data.fd = client_st;
                client_ev.events = EPOLLIN | EPOLLERR | EPOLLHUP;
                epoll_ctl(epfd, EPOLL_CTL_ADD, client_st, &client_ev);
                continue;
            }
            if (events[i].events & EPOLLIN)
            {
                if (socket_recv(events[i].data.fd) < 0)
                {
                    close_socket(events[i].data.fd);
                    events[i].data.fd = -1;
                    continue;
                }
            }
            if (events[i].events & EPOLLERR)
            {
                printf("EPOLLERR\n");
                close_socket(events[i].data.fd);
                events[i].data.fd = -1;
                continue;
            }
            if (events[i].events & EPOLLHUP)
            {
                printf("EPOLLHUP\n");
                close_socket(events[i].data.fd);
                events[i].data.fd = -1;
                continue;
            }
        }
    }
    close(epfd);
    close_socket(listen_st);
    return 0;
}