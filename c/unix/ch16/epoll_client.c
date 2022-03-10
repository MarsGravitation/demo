//网络编程客户端
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

int main(int argc, char *argv[])
{
    if (argc < 2)
    {
        printf("please print one param!\n");
        return -1;
    }
    int port = atoi(argv[2]);
    char ipaddr[30] = {0};
    strcpy(ipaddr, argv[1]);
    int st = connect_server(ipaddr, port);
    socket_send(st);
    close(st);
    return 0;
}