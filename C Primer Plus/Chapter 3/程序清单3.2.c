/**
 * printf() 的一些特性演示
 */

#include <stdio.h>

int main(void) {
    int iTen = 10;
    int iTwo = 2;

    printf("Doing it right: ");
    printf("%d minus %d is %d.", iTen, 2, iTen - iTwo);
    printf("Doing it wrong.");
    //printf("%d minus %d is %d.", iTen); // 后面两个没有指定的数，会随机在内存中抽取垃圾数。

    return 0;
}