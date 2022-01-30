/*
13.2.2 fopen 函数
    fopen：
    第一个参数是要打开的文件名
    第二个参数是用于指定文件打开模式的一个字符串
    返回值是一个文件指针

13.2.3 getc() 函数和 putc() 函数
    getc(fp); // 从指针 fp 指定的文件中获取一个字符
    putc(ch, fp); // 将字符 ch 写入到 FILE 指针 fpout 指定的文件中

13.2.4 文件结尾
    int 来控制 EOF

13.2.5 fclose()
    fclose(fp); // 关闭由指针 fp 指定的文件，同时根据需要刷新缓冲区

13.4 

13.4.1 fprinf() 和 fscanf()
    和 prinf()、scanf() 相似，区别在于前两者需要第一个参数来指定合适的文件

13.4.2 fgets() 和 fputs()
    fgets(buf, MAX, fp); 
    第一个参数是用于存储输入的地址（char *类型）
    第二个参数是整数，表示输入字符串的最大长度
    第三个参数是文件指针，指向要读取的文件

    fgets 读取到它所遇到的第一个换行字符的后面，或者读取比字符串的最大长度少
一个的字符，或者读取到文件结尾。然后 fgets 向末尾添加一个空字符已构成一个字符串。
所以，字符串的最大长度代表字符的最大数目再加上一个空字符。
    puts(buf, fp);

*/
#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
    int ch;
    FILE *fp;
    long count = 0;

    if (argc != 2)
    {
        printf("Usage: %s filename\n", argv[0]);
        exit(1);
    }
    if ((fp = fopen(argv[1], "r")) == NULL)
    {
        printf("Can't open %s\n", argv[1]);
        exit(1);
    }
    while ((ch = getc(fp)) != EOF)
    {
        /* code */
        putc(ch, stdout);
        count++;
    }
    fclose(fp);
    printf("File %s has %ld characters\n", argv[1], count);
    return 0;
    
}