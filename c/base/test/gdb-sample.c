/*
https://blog.csdn.net/liigo/article/details/582231

file <filename>: 加载被调试的可执行程序文件
r: Run，运行被调试的程序。如果此前没有下过断点，则执行完整个程序；如果有断点，则程序暂停在第一个可用断点处
c: Continue，继续执行被调试程序，直至下一个断点或程序结束
b <line number>:
b <func name>: Breakpoint，设置断点
d [编号]: Delete breakpoint，删除指定编号的某个断点。断点编号从 1 开始递增
s: 执行一行源程序代码，如果此行代码中有函数调用，则进入该函数
n: 执行一行源程序代码，此行代码中的函数调用也一并执行
（s 相当于 “Step Info” 单步跟踪进入，n 相当于 “Step Over”  单步跟踪）
p <var name>: Print，显示指定变量的值
q: Quit，退出 GDB 调试环境

*/
#include <stdio.h>

int nGlobalVar = 0;

int tempFunction(int a, int b)
{
    printf("tempFunction is called, a = %d, b = %d\n", a, b);
    return a + b;
}

int main(void)
{
    int n;

    n = 1;
    n++;
    n--;

    nGlobalVar += 100;
    nGlobalVar -= 12;

    printf("n = %d, nGlobalVar = %d\n", n, nGlobalVar);

    n = tempFunction(1, 2);
    printf("n = %d", n);
    return 0;
}
