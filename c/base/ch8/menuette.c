#include <stdio.h>

char get_choice();
char get_first();
int get_int();
void count();

int main() {
    int choice;
    
    while((choice = get_choice()) != 'q')
    {
        switch (choice)
        {
        case 'a': printf("Buy low, sell high.\n");
            /* code */
            break;
        case 'b': putchar('\a');
            break;
        case 'c': count();
            break;
        default: printf("Program error!\n");
            break;
        }
    }
    printf("Bye.\n");
    return 0;
}

void count() {
    int n, i;
    printf("Count how far?Enter an interger: \n");
    n = get_int();
    for (i = 1; i <= n; i++)
        printf("%d\n", i);
    while (getchar() != '\n')
        continue;
}

char get_choice()
{
    int ch;
    printf("Enter the letter of your choice:\n");
    printf("a. advice           b. bell\n");
    printf("c. count            q. quit\n");
    ch = get_first();
    while((ch < 'a' || ch > 'c') && ch != 'q')
    {
        printf("Please respond whit a, b, c, or q.\n");
        ch = get_first();
    }
    return ch;
}

char get_first()
{
    int ch;
    ch = getchar();
    while(getchar() != '\n')
        continue;
    return ch;
}

int get_int()
{
    int input;
    char ch;

    while(scanf("%d", &input) != 1)
    {
        while((ch = getchar()) != '\n')
            putchar(ch);
        printf(" is not an integer.\nPlease enter an ");
        printf("integer value, such as 25, -178, or 3: ");
    }
    return input;
}