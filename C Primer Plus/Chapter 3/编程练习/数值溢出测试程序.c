/**
 * 数值溢出/下溢测试程序
 * 逻辑分析：
 * 整数上溢：当超过int最大值时，会回绕到最小值
 * 浮点数上溢：超过double最大值时，会得到inf(无穷大)
 * 浮点数下溢：当数值小于最小正规范数时，逐渐损失精度直至0
 */

#include <stdio.h>
#include <limits.h>
#include <float.h>

int main(void) {
    // 整数溢出测试
    int max_int = INT_MAX;
    printf("整数上溢测试:\n");
    printf("INT_MAX = %d\n", max_int);
    printf("INT_MAX + 1 = %d (回绕到最小值)、\n\n", max_int + 1);

    // 浮点数上溢测试
    double max_double = DBL_MAX;
    printf("浮点数上溢测试:\n");
    printf("DBL_MAX = %e\n", max_double); // 使用科学计数法输出
    printf("DBL_MAX * 2 = %e (变为inf)\n\n", max_double * 2);


    return 0;
}