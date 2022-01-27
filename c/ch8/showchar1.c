/*

getchar() 读取每个字符
scanf() 在读取数字时会跳过空格、制表符和换行符

*/
#include <stdio.h>

void display(char cr, int lines, int width);

int main() {
    int ch;
    int rows, cols;
    printf("Enter a character and tow intergers: \n");
    while((ch = getchar()) != '\n') {
        if (scanf("%d %d", &rows, &cols) != 2)
            break;
        display(ch, rows, cols);
        while(getchar() != '\n')
            continue;
        printf("Enter another character and two interges: \n");
        printf("Enter a newline to quit.\n");
    }
    printf("Bye.\n");
    return 0;
}

void display(char cr, int lines, int width) {
    int row, col;

    for (row = 1; row <= lines; row++) {
        for (col = 1; col <= width; col++) {
            putchar(cr);
        }
        putchar('\n');
    }
}