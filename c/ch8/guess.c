/*

n
Well, then, is it 2?
Well, then, is it 3?

每次输入都进行了两次猜测。
程序读取了两次，'n', 换行符

*/
#include <stdio.h>

int main() {
    int guess = 1;
    while(getchar() != 'y')
        printf("Well, then, is it %d?\n", guess++);
    printf("I know I could do it!\n");
    return 0;
}