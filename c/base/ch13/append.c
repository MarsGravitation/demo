/*
append.c -- 把多个文件的内容追加到一个文件中

13.7.3 int setvbuf()
    int setvbuf(FILE * restrict fp, char * restrict buf, int mod, size_t size);
    setvbuf 建立了一个供标准 I/O 函数使用的替换缓冲区。
    打开文件以后，在没有对流进行任何操作以前，可以调用这个函数。由指针 fp 来指定流，buf
指向将使用的存储区。如果 buf 的值不是 NULL，就必须创建这个缓冲区。例如，可以声明一个 1024 
个字符的数组，然后传递该数组的地址。但是，如果 buf 的值为 NULL，函数会自动为自己分配一个
缓冲区。size 变量为 setvbuf() 函数指定数组的大小。mod 将从下列选项中选取：
    _IOFBF: 完全缓冲
    _IOLBF: 行缓冲
    _IONBF: 无缓冲

    如果成功执行，函数会返回零值，否则返回一个非零值

13.7.5 size_t fwrite()
    size_t fwrite(const void * restrict ptr, size_t size, size_t nmemb, FILE * restrict fp);

    ptr: 要写入的数据块的地址
    size: 要写入数据块的大小
    nmemb: 数据块的数目
    fp: 指定要写入的文件

    注释：
    参数 size 是指单个元素的大小（其单位是字节而不是位，例如，读取一个 int 型数据就是 4 字节）；
    参数 count 指出要读或写的元素个数，这些元素在 buf 所指的内存空间中连续存放，共占“size*count”个字节。

    return: 成功写入的项目数。正常情况下，它与 nmemb 相等

13.7.6 size_t fread()
    size_t fread(void * restrict prt, size_t size, size_t nememb, FIFE * restrict fp);

13.7.7 int feof(FILE * fp) 和 int ferror(FILE *ｆｐ)
    当标准输入函数返回 EOF 时，通常表示已经到达了文件结尾。可是，这也有可能表示发生了读取错误。
使用 feof() 和 ferror() 区分。正常返回零值，错误返回非零值。

*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFSIZE 1024
#define SLEN 81

void append(FILE *source, FILE * dest);

int main(void)
{
    FILE *fa, *fs;
    int files = 0;
    char file_app[SLEN];
    char file_src[SLEN];
    puts("Enter name of destination file: ");
    gets(file_app);
    if ((fa = fopen(file_app, "a")) == NULL)
    {
        fprintf(stderr, "Can't open %s\n", file_app);
        exit(2);
    }
    if (setvbuf(fa, NULL, _IOFBF, BUFSIZ) != 0)
    {
        fputs("Can't create output buffer\n", stderr);
        exit(3);
    }
    puts("Enter name of first source file (empty lien to quit): ");
    while (gets(file_src) && file_src[0] != '\0')
    {
        if (strcmp(file_src, file_app) == 0)
            fputs("Can't append file to itself\n", stderr);
        else if ((fs = fopen(file_src, "r")) == NULL)
            fprintf(stderr, "Can't open %s\n", file_src);
        else
        {
            if (setvbuf(fs, NULL, _IOFBF, BUFSIZ) != 0)
            {
                fputs("Can't create input buffer\n", stderr);
                continue;
            }
            append(fs, fa);
            if (ferror(fs) != 0)
                fprintf(stderr, "Error in reading file %s.\n", file_src);
            if (ferror(fa) != 0)
                fprintf(stderr, "Error in writing file %s.\n", file_app);
            fclose(fs);
            files++;
            printf("File %s appended.\n", file_src);
            puts("Next file (empty line to quit): ");
        }
    }
    printf("Done. %d file appended.\n", files);
    fclose(fa);
    return 0;
}

void append(FILE *source, FILE *dest)
{
    size_t bytes;
    static char temp[BUFSIZ];
    while((bytes == fread(temp, sizeof(char), BUFSIZ, source)) > 0)
        fwrite(temp, sizeof(char), bytes, dest);
}