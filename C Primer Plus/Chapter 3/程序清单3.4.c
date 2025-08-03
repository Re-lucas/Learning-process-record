/**
 * 格式符必须匹配数据类型：
 * %d → int（有符号）
 * %u → unsigned int
 * %hd → short
 * %ld → long
 * %lld → long long
 */
#include <stdio.h>

int main(void){
    unsigned int un = 3000000000; /*int 为32位和short为16位的系统*/
    short end = 200;
    long big = 65537;
    long long verybig = 12345678908642;

    /**
     * 正确格式符 %u：输出无符号整数 3000000000。
     * 错误格式符 %d：尝试解释为有符号整数，
     * 但 3000000000 超过 int 正数范围（通常为 2147483647），
     * 输出负数结果（具体值依赖系统，如 -1294967296）。
     */
    printf("un = %u and not %d\n", un, un);

    printf("end = %hd and %d\n", end, end);
    printf("big = %ld and not %hd\n", big, big);
    printf("berybig = %lld and not %ld\n", verybig, verybig);

    return 0;
}