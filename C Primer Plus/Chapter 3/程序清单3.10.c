/**
 * 使用转义序列输出特殊字符
 */

#include <stdio.h>

int main(void) {
    float salary;

    // \a - 响铃字符
    // \b - 退格符 
    // \r - 回车符
    printf("\aEnter your desired monthly salary: ");
    printf("$___________\b\b\b\b\b\b\b\b\b");
    scanf("%f", &salary);
    printf("\n\t$%.2f a month is $%.2f a year.", salary, salary * 12.0);
    printf("\rGee!\n");
    

    // 实际输出 ： Gee!    $456789.00 a month is $5481468.00 a year.
    // Gee! 覆盖了前面的内容
    return 0;
}