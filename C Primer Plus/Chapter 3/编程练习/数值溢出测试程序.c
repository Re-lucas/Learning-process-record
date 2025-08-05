/**
 * 数值溢出/下溢测试程序
 * 逻辑分析：
 * 整数上溢：当超过int最大值时，会回绕到最小值
 * 浮点数上溢：超过double最大值时，会得到inf(无穷大)
 * 浮点数下溢：当数值小于最小正规范数时，逐渐损失精度直至0
 */

#include <stdio.h>

// 定义了整数与浮点数两个类型的上下限
#include <limits.h>
#include <float.h>

int main(void) {
    // 整数溢出测试
    int max_int = INT_MAX;
    printf("整数上溢测试:\n");
    printf("INT_MAX = %d\n", max_int);
    printf("INT_MAX + 1 = %d (回绕到最小值)\n\n", max_int + 1);

    // 浮点数上溢测试
    double max_double = DBL_MAX;
    printf("浮点数上溢测试:\n");
    printf("DBL_MAX = %e\n", max_double); // 使用科学计数法输出
    printf("DBL_MAX * 2 = %e (变为inf)\n\n", max_double * 2);

    // 浮点数下溢测试
    double min_dbl = DBL_MIN;
    printf("浮点数下溢测试:\n");
    printf("DBL_MIN = %e\n", min_dbl);

    double underflow = min_dbl;
    printf("DBL_MIN / 2.0 = %e(精度损失)\n", underflow);

    underflow /= 1e300;
    printf("进一步初一1e300: %e(趋近于零)\n", underflow);

    return 0;
}