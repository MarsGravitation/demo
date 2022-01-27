/*

从 scanf 角度看输入：
假定使用了一个 %d 说明符来读取一个整数。scanf() 函数开始每次读取
一个输入字符，它跳过空白字符直到遇到一个非空白字符。因为它试图读取
一个整数，所以 scanf() 期望发现一个数字字符或者一个符号。如果它发现
了一个数字或一个符号，那么它就保存之并读取下一个字符；如果接下来的
字符，它保存这个数字，并读取下一个字符。就这样，scanf() 持续读取和
保存直到它遇到一个非数字的字符。如果遇到了一个非数字的字符，它就得出
结论：已经读到了证书的尾部。scanf 把这个非数字字符放入输入。这就意味
着当程序下一次开始读取输入时，它将从前面被放弃的哪个非数字字符开始。
最后，scanf() 计算它读取到的数字的相应数值，并将该值放到指定的变量中。

如果使用了字段宽度，那么 scanf() 在字段结尾或在第一个空白字符处终止。

如果第一个非空白字符不是数字，scanf() 会停在哪里，并把字符放回输入。

如果使用 %s 说明符，那么空白字符以外的所有字符都是可接受的，所以 scanf
跳过空白字符知道遇到第一个非空白字符，然后保存再次遇到空白之前的所有字符。
这就意味着 %s 使 scanf 读取一个单词。

如果使用 %c 说明符，那么所有的输入字符都是平等的。如果下一个输入字符是空格
或者换行符，不会跳过空白字符。
*/
#include <stdio.h>
#include <string.h>

#define DENSITY 62.4
int main()
{
    float weight, volume;
    int size, letters;
    char name[40];

    printf("Hi! What's your first name?\n");
    scanf("%s", name);
    printf("%s, what's your weight in pounds?\n", name);
    scanf("%f", &weight);
    size = sizeof name;
    letters = strlen(name);
    volume = weight / DENSITY;
    printf("Well, %s, your volumme is %2.2f cubic feet.\n", name, volume);
    printf("Also, your first name has %d letters, \n", letters);
    printf("and we have %d bytes to store it in.\n", size);
    return 0;
}