/*
echo_eof.c -- 重复输入，直到文件的结尾

int ch; 因为 EOF 为 -1，getchar() 本身的类型实际上是 int

echo_eof < words

< 是重定向运算符。该运算符把 words 文件与 stdin 流关联起来，将该文
件的内容引导至 echo_eof 程序。

echo_eof > myworkds
> 是另一个重定向运算符。该运算符会导致建立一个名为 mywords 的新文件，然后将 
echo_eof 的输出重定向到该文件。

组合重定向：
echo_eof < mywords > savewords

重定向运算符的顺序无关紧要
*/
#include <stdio.h>
int main() {
    int ch;

    while ((ch = getchar()) != EOF)
        putchar(ch);
    return 0;
}