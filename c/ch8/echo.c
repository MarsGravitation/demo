/* 
echo.c -- 重复输入 

缓冲分为两类：完全缓冲 I/O 和行缓冲 I/O。对完全缓冲输入来说，
缓冲区满时被清空。这种类型的缓冲通常出现在文件输入中。
对行缓冲 I/O 来说，遇到一个换行字符时被清空缓冲区。键盘输入是
标准的行缓冲，因此按下回车键将清空缓冲区。
*/
#include <stdio.h>

int main() {
    char ch;

    while((ch = getchar()) != '#') 
        putchar(ch);
    return 0;
}