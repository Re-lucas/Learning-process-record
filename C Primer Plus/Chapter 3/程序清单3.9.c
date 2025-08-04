/**
 * 参数错误的情况
 */

#include <stdio.h>

int main(void) {
    int n = 4;
    int m = 5;
    float f = 5.2f;
    float g = 6.3f;

    printf("%d", n, m, f, g); // 参数过多
    printf("%d %d %f %f", n, m, f); // 参数过少
    printf("%d, %f", f, n); // 参数类型不匹配

    return 0;
}