#include <stdio.h>
#include <stdbool.h>

int get_int();

bool bad_limits(int begin, int end, int low, int high);

double sum_squares(int a, int b);

int main() {
    const int MIN = -1000;
    const int MAX = +1000;
    int start;
    int stop;
    double answer;
    start = get_int();
    printf("upper limit: ");
    stop = get_int();
    while (start != 0 || stop != 0)
    {
        /* code */
        if (bad_limits(start , stop, MIN, MAX))
            printf("Please try again.\n");
        else
        { 
            answer = sum_squares(start, stop);
            printf("The sum of the squartes of the interger from ");
            printf("from %d to %d is %g\n", start, stop, answer);
        }
        printf("Enter the limits(enter 0 for both limits to quit);\n");
        printf("lower limit: ");
        start = get_int();
        printf("upper limit: ");
        stop = get_int();
    }
    printf("Done.\n");

    return 0;
}

int get_int()
{
    int input;
    char ch;

    while(scanf("%d", &input) != 1)
    {
        while((ch = getchar()) != '\n')
            putchar(ch);
        printf(" is not an interge.\nPlease enter an ");
        printf("interger value, such as 25, -178, or 3: ");
    }
    return input;
}

double sum_squares(int a, int b)
{
    double total = 0;
    int i;

    for (i = a; i <= b; i++)
        total += i * i;
    
    return total;
}

bool bad_limits(int begin, int end, int low, int high)
{
    bool not_good = false;

    if (begin > end)
    {
        printf("%d isn't smaller than %d.\n", begin, end);
        not_good = true;
    }
    if (begin < low || end < low)
    {
        printf("Values must be %d or greater.\n", low);
        not_good = true;
    }
    if (begin > high || end > high)
    {
        printf("Value must be %d or less.\n", high);
        not_good = true;
    }
    return not_good;
}