#include <stdio.h>

int main(void) {
    int ascii_value;

    printf("请输入ASCII码:");
    scanf("%d", &ascii_value);

    //使用%c格式说明符将整数转为字符输出
    printf("对应字符：%c\n", (char)ascii_value);

    return 0;
}