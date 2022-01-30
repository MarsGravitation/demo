/*
booksave.c - 把结构内容保存到文件中

fwrite(&orimer, sizeof(struct book), 1, pbooks);

这个语句定位到结构 primer 的开始地址，将该结构的所有自己复制到与 pbooks 相关联的文件中。sizeof
(struct book) 告诉函数要复制的每一块有多大，1 表示只复制一块。
*/
#include <stdio.h>
#include <stdlib.h>

#define MAXTITL 40
#define MAXAUTL 40
#define MAXBKS 10

struct book {
    char title[MAXTITL];
    char author[MAXAUTL];
    float value;
};

int main(void)
{
    struct book library[MAXBKS];
    int count = 0;
    int index, filecount;
    FILE * pbooks;
    int size = sizeof(struct book);

    if ((pbooks = fopen("book.dat", "a+b")) == NULL)
    {
        fputs("Can't open book.dat file\n", stderr);
        exit(1);
    }
    rewind(pbooks);
    while (count < MAXBKS && fread(&library[count], size, 1, pbooks) == 1)
    {
        if (count == 0)
            puts("Current contents of book.dat: ");
        printf("%s by %s: $.2f\n", library[count].title,
            library[count].author, library[count].value);
        count++;
    }
    filecount = count;
    if (count == MAXBKS)
    {
        fputs("The book.dat file is full.", stderr);
        exit(2);
    }

    puts("Please add new book titles.");
    puts("Please [enter] at the start of a line to stop.");
    while (count < MAXBKS && gets(library[count].title) != NULL
                            && library[count].title[0] != '\0')
    {
        puts("Now enter the author.");
        gets(library[count].author);
        puts("Now enter then value.");
        scanf("%f", &library[count++].value);
        while (getchar() != '\n')
            continue;
        if (count < MAXBKS)
            puts("Enter the next title.");
    }

    if (count > 0)
    {
        puts("Here is the list of your books: ");
        for (index = 0; index < count; index++)
            printf("%s by %s: %.2f\n", library[index].title,
                    library[index].author, library[index].value);
        fwrite(&library[filecount], size, count - filecount, pbooks);
    } else
        puts("No books? To bad.\n");

    puts("Bye.\n");
    fclose(pbooks);
}