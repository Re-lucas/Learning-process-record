/**
 * 以两种方式显示float类型的值
 */

#include <stdio.h>

int main(void) {
    // 将双精度浮点数赋值给单精度可能有精度损失
    // 但在这里我们使用 float 和 double 来演示两种不同的浮

    float aboat = 32000.0f; //单精度浮点数
    double abet = 2.14e9; //双精度浮点数
    long double dip = 5.32e-5L; //Long double为C99的扩展类型

    printf("%f can be written %e\n", aboat, aboat);
    
    // 要求编译器支持 C99 或更高版本以使用 %a 格式符
    // %a 格式符用于显示浮点数的十六进制表示
    printf("And it's %a in hexadeciaml, powers of 2 notation.\n", aboat);

    printf("%f can be written %e\n", abet, abet);
    // 注意 long double 的格式符为 %Lf
    // 使用 %Le 来显示 long double 的科学计数法
    printf("%Lf can be written %Le\n", dip, dip);

    return 0;
}