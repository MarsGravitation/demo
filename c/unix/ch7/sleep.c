/*
工作目录：
pwdx 118737
118737: /home/workspace/unix/demo

启动路径：
cwd -> /home/workspace/unix/demo

启动目录就是工作目录
*/
#include <unistd.h>  // 在gcc编译器中，使用的头文件因gcc版本的不同而不同

int main(void)
{
    while (1)
    {
        sleep(1);
    }
}