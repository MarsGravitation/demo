/*
preproc.c - 简单的预处理器的例子

编译时代入法，明显常量

第一部分为指令 #define
第二部分为所选择的缩略语，这些缩略语称为宏
第三部分（其余部分）称为替换列表或主体

预处理器在程序中发现了宏的实例后，总会用实体代替该宏。
从宏编程最终的替换文本的过程称为宏展开

X is 2.
X is 4.
Consistency is the last refuge of the unimaginative. -Oscar Wild      
TWO: OW
*/
#include <stdio.h>

#define TWO 2
#define OW "Consistency is the last refuge of the unimagina\
tive. -Oscar Wild"
#define FOUR TWO*TWO
#define PX printf("X is %d.\n", x)
#define FMT "X is %d.\n"

int main(void)
{
    int x = TWO;

    PX;
    x = FOUR;
    printf(FMT, x);
    printf("%s\n", OW);
    printf("TWO: OW\n");

    return 0;
}