#include <stdio.h>
#include <stdlib.h>

int main()
{
    printf("Hello world!\n");
    return 0;
}

struct student /* 声明了一个结构体类型 */
// student 结构体标签
// 结构体模板 - 结构体类型
{
    long studentID; /* 学号 */
    char studentName[10];
    char studentSex;
    int yearOfBirth;
//    int scoreMath;
//    int socreEnglish;
//    int scoreComputer;
//    int scoreProgramming;
    int score[4];
};
// 分号结尾，说明一条语句的结束

// 先定义结构体类型在定义变量名
// 和基本类型一致，类型名在前，变量名在后
// struct student stu1;

// 定义类型的同时定义变量
struct student2
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];
} stu1;

// 直接定义结构体变量，不定义结构体标签
// 不推荐
struct
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];

} stu2;

// typedef 为一种已存在的类型定义一个别名，并未定义新类型
typedef struct student STUDENT;

typedef struct student
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];
} STUDENT;

// 用结构体 student 类型定义两个变量
// 不能省略 struct
struct student stu1, stu2;

// STUDENT = struct student
STUDENT stu1, stu2;

// 初始化结构体
STUDENT stu1 = {1, "cxd", 'M', 1991, {1, 2, 3, 4}};

typedef struct date
{
    int year;
    int month;
    int day;
}DATE;

// 结构体嵌套
typedef struct student
{
    long studentID;
    char studentName[10];
    char studentSex;
    DATE birthday;
    int score[4];
} STUDENT;

// 成员选择运算符
// 结构体变量名.成员名
stu1.studentID = 1;

// 当出现结构体嵌套时，必须以级联方式访问结构体成员
stu1.birthday.day = 1991;


int main()
{
    STUDENT stu1 = {1, "cxd", 'M', {1, 2, 3}, {1, 2, 3, 4}};
    STUDENT stu2;
    // 结构体赋值
    // 按结构体的成员顺序逐一对相应成员赋值
    // 结构体类型必须一致
    stu2 = stu1;
}

// 结构体变量的地址 &stu2 是该变量所占内存空间的首地址

typedef struct sample
{
    char m1;
    int m2;
    char m3;
} SAMPLE;

// sizeof(变量或表达式)
// sizeof(类型)

sizeof(struct sample);
sizeof(SAMPLE);

// 结构体内存是按照字长进行对齐的
// 32 位体系结构中，int 值被对齐在 4 字节地址边界，保证了一个 int
// 型数总能通过一次内存操作被访问到，每次内存访问是在 4 字节对齐的
// 地址出读取或存入 32 为整数


// 如何定义指向结构体变量的指针
STUDENT stu1;
STUDENT *pt;
pt = &stu1;

STUDENT *pt = &stu1;


// 通过成员选择运算符访问结构体指针变量所指向的结构体成员
stu1.studentID = 1;
(*pt).studentID = 1;

// 通过指向运算符访问结构体指针变量所指向的结构体成员
pt->studentID = 1;

// 结构体嵌套访问成员变量
stu1.birthday.year = 2021;
(*pt).birthday.month = 7;
pt->birthday.day = 2;

// 如何定义指向结构体数组的指针
STUDENT stu[1];
STUDENT *pt;
pt = stu;

STUDENT *pt = stu;

STUDENT *pt = &stu[0];

// 如何访问结构体数组指针指向的结构体成员

// stu[1]
pt++;
