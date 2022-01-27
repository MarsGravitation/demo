// sum_arr1.c -- 对一个数组的所有元素求和
/*
The size of ar is 8 bytes.
The total number ofmarbles is 295.
The size of marbles is 40 bytes.

*/
#include <stdio.h>
#define SIZE 10
int sum(int ar[], int n);
int main() {
    int marbles[SIZE] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    long answer;
    answer = sum(marbles, SIZE);
    printf("The total number ofmarbles is %ld.\n", answer);
    // marbles 是一个数组
    printf("The size of marbles is %zd bytes.\n", sizeof marbles);
    return 0;
}

int sum(int ar[], int n) {
    int i, sum;
    for (i = 0; i < n; i++) {
        sum += ar[i];
    }
    // ar只是一个指针
    printf("The size of ar is %zd bytes.\n", sizeof ar);
    return sum;
}