/**
 * 以十进制，八进制，十六进制打印十进制数100
 */

#include <stdio.h>

int main(void) {
    int x = 100;

    printf("dec = %d; Octal = %o; Hex = %x\n", x, x, x);

    // 可读性可使用#
    printf("dec = %d; Octal = %#o; Hex = %#x\n", x, x, x);

    return 0;
}