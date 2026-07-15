/**
 * @file hello.c
 * @author your name (you@domain.com)
 * @brief
 * @version 0.1
 * @date 2023-07-11
 *
 * @copyright Copyright (c) 2023
 *
 * 1. VSCode 配置 C 语言开发环境
 *
 * https://blog.csdn.net/qq_31716541/article/details/130804201
 * https://blog.csdn.net/weixin_61370021/article/details/123480478
 */
#include <stdio.h>
int main(void)
{
    int dogs;

    printf("How many dogs do you have?\n");
    scanf("%d", &dogs);
    printf("So you have %d dog(s)!\n", dogs);
    return 0;
}