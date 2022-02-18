#include <stdio.h>
#include <stdlib.h>

#include "head.h"

int main(int argc, char *argv[])
{
    int a, b;

    if (argc < 3)
    {
        fprintf(stderr, "Usage: %s argv[1] argv[2].\n", argv[0]);
        return -1;
    }

    a = atoi(argv[1]);
    b = atoi(argv[2]);

    printf("a + b = %d\n", add(a, b));
    printf("a - b = %d\n", sub(a, b));
    return 0;
}