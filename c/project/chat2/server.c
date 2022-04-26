/*
https://www.jb51.net/article/216379.htm
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <pthread.h>
#include <semaphore.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#ifndef DEBUG
#define debug(format, ...) \
    {                      \
    }
#else
#define debug(format, ...)                                          \
    {                                                               \
        fprintf(stdout, "%s:%d:%s ", __func__, __LINE__, __TIME__); \
        fprintf(stdout, format, ##__VA_ARGS__);                     \
        fprintf(stdout, "\n");                                      \
    }
#endif // DEBUG

#define error(format, ...)                                          \
    {                                                               \
        fprintf(stdout, "%s:%d:%s ", __func__, __LINE__, __TIME__); \
        fprintf(stdout, format, ##__VA_ARGS__);                     \
        fprintf(stdout, ":%m\n");                                   \
        exit(EXIT_FAILURE);                                         \
    }

// 客户端最大连接数
#define CLIENT_MAX 50

// 服务器端口号
#define PORT 5566

// 缓冲区大小
#define BUF_SIZE 4096

// 重新定义 socket 地址类型
typedef struct sockaddr* SP;

// 客户端结构体
typedef struct Client
{
    int sock;      // socket 标识符
    pthread_t tid; // 线程 ID
    char name[20];
    struct sockaddr_in addr;
} Client;

// 定义 50 个存储客户端的结构变量
Client clients[50];

// 定义信号量用于限制客户端的数量
sem_t sem;

// 信号处理函数
void sigint(int num)
{
    for (int i = 0; i < 10; i++)
    {
        if (clients[i].sock)
        {
            pthread_cancel(clients[i].tid); // 销毁线程
        }
    }
    debug("server exit!");
    exit(EXIT_SUCCESS);
}

void client_exit(Client* client)
{
    sem_post(&sem);
    close(client->sock);
    client->sock = 0;
}

void client_send(Client* client, char* buf)
{
    size_t len = strlen(buf) + 1;
    for (int i = 0; i < CLIENT_MAX; i++)
    {
        if (clients[i].sock && clients[i].sock != client->sock)
        {
            send(clients[i].sock, buf, len, 0);
        }
    }
}

void* run(void* arg)
{
    Client* client = arg;
    char buf[BUF_SIZE] = {};

    // 接受昵称
    int ret_size = recv(client->sock, client->name, 20, 0);
    if (0 >= ret_size)
    {
        client_exit(client);
        return NULL;
    }

    // 通知其他客户端新人上线
    sprintf(buf, "!!!欢迎%s进入聊天室!!!", client->name);
    client_send(client, buf);
    for (;;)
    {
        // 接受消息
        ret_size = recv(client->sock, buf, BUF_SIZE, 0);
        if (0 >= ret_size || 0 == strcmp("quit", buf))
        {
            // 通知其他客户端退出
            sprintf(buf, "!!!%s退出聊天室!!!", client->name);
            client_send(client, buf);
            client_exit(client);
            return NULL;
        }
        strcat(buf, ":");
        strcat(buf, client->name);
        client_send(client, buf);
        debug(buf);
    }
}

int main(int argc, const char* argv[])
{
    signal(SIGINT, sigint);
    debug("注册信号处理函数成功！");

    sem_int(&sem, 0, CLIENT_MAX);
    debug("初始化信号量成功！");

    int svr_sock = socket(AF_INET, SOCK_STREAM, 0);
    if (0 > svr_sock)
    {
        error("socket");
    }
    debug("创建 socket 对象成功！");

    
}