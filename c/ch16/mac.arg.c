/*

mac_arg.c -- 带有参数的宏

#define SQUARE (x) x*x

SQUARE 为宏标识符，x 为宏的参数，x*x 为替换列表

出现 SQUARE(x) 的地方都用 x*x 代替。使用这些宏时即可以使用 x，
也可以使用其他符号。宏定义中的 x 有程序调用的宏中的符号代替。   

*/
#include <stdio.h>
#define SQUARE (x) x*x
#define PR (X) printf("The result is %d.\n", X)

int main(void)
{
    int x = 4;
    int z;

    printf("X = %d\n", x);
    z = SQUARE(x);
    printf("Evaluation SQUARE(X): ");
    PR(z);
    z = SQUARE(2);
    printf("Evaluation SQUARE(2): ");


}