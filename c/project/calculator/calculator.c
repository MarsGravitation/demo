/* caculator.c - 计算器 */
#include <stdio.h>

int main(void)
{
    float n1, n2;
    char c;

    printf("Enter the calculation\n");
    while (scanf("%f%c%f", &n1, &c, &n2) == 3) {
        switch (c)
        {
        case '+':
            printf("=%f\n", n1 + n2);
            break;
        case '-':
            printf("=%f\n", n1 - n2);
            break;
        case '*':
            printf("=%f\n", n1 * n2);
            break;
        case '/':
            printf("=%f\n", n1 / n2);
            break;
        default:
            fprintf(stderr, "wrong operator (%c)\n", c);
            break;
        }
        printf("Enter the calculation\n");
    }

    printf("Bye.\n");
    
    return 0;
}