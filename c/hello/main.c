#include <stdio.h>
#include <stdlib.h>

int main()
{
    printf("Hello world!\n");
    return 0;
}

struct student /* ������һ���ṹ������ */
// student �ṹ���ǩ
// �ṹ��ģ�� - �ṹ������
{
    long studentID; /* ѧ�� */
    char studentName[10];
    char studentSex;
    int yearOfBirth;
//    int scoreMath;
//    int socreEnglish;
//    int scoreComputer;
//    int scoreProgramming;
    int score[4];
};
// �ֺŽ�β��˵��һ�����Ľ���

// �ȶ���ṹ�������ڶ��������
// �ͻ�������һ�£���������ǰ���������ں�
// struct student stu1;

// �������͵�ͬʱ�������
struct student2
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];
} stu1;

// ֱ�Ӷ���ṹ�������������ṹ���ǩ
// ���Ƽ�
struct
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];

} stu2;

// typedef Ϊһ���Ѵ��ڵ����Ͷ���һ����������δ����������
typedef struct student STUDENT;

typedef struct student
{
    long studentID;
    char studentName[10];
    char studentSex;
    int yearOfBirth;
    int score[4];
} STUDENT;

// �ýṹ�� student ���Ͷ�����������
// ����ʡ�� struct
struct student stu1, stu2;

// STUDENT = struct student
STUDENT stu1, stu2;

// ��ʼ���ṹ��
STUDENT stu1 = {1, "cxd", 'M', 1991, {1, 2, 3, 4}};

typedef struct date
{
    int year;
    int month;
    int day;
}DATE;

// �ṹ��Ƕ��
typedef struct student
{
    long studentID;
    char studentName[10];
    char studentSex;
    DATE birthday;
    int score[4];
} STUDENT;

// ��Աѡ�������
// �ṹ�������.��Ա��
stu1.studentID = 1;

// �����ֽṹ��Ƕ��ʱ�������Լ�����ʽ���ʽṹ���Ա
stu1.birthday.day = 1991;


int main()
{
    STUDENT stu1 = {1, "cxd", 'M', {1, 2, 3}, {1, 2, 3, 4}};
    STUDENT stu2;
    // �ṹ�帳ֵ
    // ���ṹ��ĳ�Ա˳����һ����Ӧ��Ա��ֵ
    // �ṹ�����ͱ���һ��
    stu2 = stu1;
}

// �ṹ������ĵ�ַ &stu2 �Ǹñ�����ռ�ڴ�ռ���׵�ַ

typedef struct sample
{
    char m1;
    int m2;
    char m3;
} SAMPLE;

// sizeof(��������ʽ)
// sizeof(����)

sizeof(struct sample);
sizeof(SAMPLE);

// �ṹ���ڴ��ǰ����ֳ����ж����
// 32 λ��ϵ�ṹ�У�int ֵ�������� 4 �ֽڵ�ַ�߽磬��֤��һ�� int
// ��������ͨ��һ���ڴ���������ʵ���ÿ���ڴ�������� 4 �ֽڶ����
// ��ַ����ȡ����� 32 Ϊ����


// ��ζ���ָ��ṹ�������ָ��
STUDENT stu1;
STUDENT *pt;
pt = &stu1;

STUDENT *pt = &stu1;


// ͨ����Աѡ����������ʽṹ��ָ�������ָ��Ľṹ���Ա
stu1.studentID = 1;
(*pt).studentID = 1;

// ͨ��ָ����������ʽṹ��ָ�������ָ��Ľṹ���Ա
pt->studentID = 1;

// �ṹ��Ƕ�׷��ʳ�Ա����
stu1.birthday.year = 2021;
(*pt).birthday.month = 7;
pt->birthday.day = 2;

// ��ζ���ָ��ṹ�������ָ��
STUDENT stu[1];
STUDENT *pt;
pt = stu;

STUDENT *pt = stu;

STUDENT *pt = &stu[0];

// ��η��ʽṹ������ָ��ָ��Ľṹ���Ա

// stu[1]
pt++;
