/*
book.c --- 仅包含一本书的图书目录

技巧：
    - 建立结构的格式或布局
    - 声明遵循该布局的变量
    - 获取对一个结构变量的各个部件的访问

14.2 建立结构声明
    
    struct book {
        float value;
    };

    它并没有创建一个实际的数据对象，而是描述了组成这类对象的元素（结构声明也叫做模板）

    struct: 表示接下来是一个结构
    book: 可选的标记，他是用来引用该结构的快速标记
    结构成员列表：

14.3 定义结构变量
    结构声明并没有让计算机为数据分配空间
    创建结构变量的时候会分配空间

    struct book library;

    这些存储空间是以一个名字 library 被结合在一起的

    在结构变量的声明中，struct book 所起的作用就像 int 一样。

    实际上，book 结构的声明创建了一个名为 struct book 的新类型。

    struct book library;

    struct book {

    } library;

    声明结构的过程和定义结构变量的过程可以被合并成一步。

    struct {

    } library;

    如果向多次使用一个结构模板，就需要使用带有标记的形式

14.3.1 初始化结构
    struct book library = {
        "",
        "",
        1.95
    };

14.3.2 访问结构成员
    结构成员运算符 .
    library.value

14.3.3 结构的指定初始化项目
    struct book surprise = { .value = 10.99 };

14.4.1 声明结构数组
    struct book library[MAXBKS];

14.4.2 标识结构数组的成员
    library[0].value;

14.5 嵌套结构
    fellow.handle.first;

14.6 指向结构的指针

14.6.1 声明和初始化结构指针
    struct guy * him;
    关键字 + 结构标记 + * + 指针名

14.6.2 使用指针访问成员
    him->income

14.7 向函数传递结构信息

14.7.1 传递结构成员

14.7.2 使用结构地址
    double sum (const struct funds *)；

14.7.4 把结构作为参数传递
    double sum(struct funds moolath);

14.7.4 其他结构特性

14.7.5 结构，还是指向结构的指针
    通常，程序员为了追求效率而使用结构指针作为函数参数；当需要保护数据、防止意外改变数据时对
指针使用 const 限定词。传递结构值是处理小型结构最常用的方法。

14.7.6 在结构中使用字符数组还是字符指针
    使用字符数组成员存储字符串

14.7.7 结构、指针和 malloc

14.12 typedef
    能够为某一类型创建自己的名字

    typedef unsigned char BYTE;

    该定义的作用域取决于 typedef 语句所在的位置。如果定义是在一个函数内部，它的作用域就是局部的，
限定在那个函数里。如果定义是在函数外部，它将具有全局作用域。
    通常，这些定义使用大写字母


*/
#include <stdio.h>
#define MAXTITL 41
#define MAXAUTL 31
struct book /* 结构模板：标记为 book */
{
    char title[MAXTITL];
    char author[MAXAUTL];
    float value;
};

int main(void)
{
    struct book library; /* 把 library 声明为 book 类型的变量 */
    printf("Please enter the book title.\n");
    gets(library.title);
    printf("Now enter the author.\n");
    gets(library.author);
    printf("Now enter the value.\n");
    scanf("%f", &library.value);
    printf("%s by %s: $%.2f\n", library.title, library.author, library.value);
    printf("Done.\n");

    return 0;
}