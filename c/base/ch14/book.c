/*
技巧：
    建立结构的格式或布局
    声明遵循该布局的变量
    获取对一个结构变量的各个部分的访问

14.2 建立结构声明

struct book {

};

它并没有创建一个实际的数据对象，而是描述了组成这类对象的元素，有时候，
把结构声明叫做模板。首先使用关键字 struct，它表示接下里是一个结构。
后面是一个可选的标记，它是用来引用该结构的快速标记。

struct book library;

它把 library 声明为一个使用 book 结构设计的结构变量。
看到这条指令，编译器会创建一个变量 library。编译器使用 book 模板为该变量
分配空间。

在结构变量的声明中，struct book 所起的作用就像 int 的作用一样。

struct book doyle, panshin, * ptbook;
实际上，book 结构的声明创建了一个名为 struct book 的新类型。

struct book library;
是以下声明的简化：
struct book {

} library; // 定义之后跟变量名

声明结构的过程和定义结构变量的过程可以被合并成一步。

struct { // 无标记

} library;

如果向多次使用一个结构模板，就需要使用带有标记的形式。

14.3.1 初始化结构

struct book library = {
    "",
    "",
    1
};

14.3.2 访问结构成员

用结构成员运算符点 (.)，例如，library.value

14.3.3 结构的指定初始化项目

14.4.1 声明结构数组

struct book library[MAXBKS];

14.4.2 标识结构数组的成员

library[0].value


*/
#include <stdio.h>
#define MAXTITL 41
#define MAXAUTL 31

struct book {
    char title[MAXTITL];
    char author[MAXAUTL];
    float value;
};

int main()
{
    struct book library;
    printf("Please enter the book title.\n");
    gets(library.title);
    printf("Now enter author.\n");
    gets(library.author);
    printf("Noew enter the value.\n");
    scanf("%f", &library.value);
    printf("%s by %s: $%.2f\n", library.author, library.title, library.value);
    printf("Done.\n");

    return 0;
    
}