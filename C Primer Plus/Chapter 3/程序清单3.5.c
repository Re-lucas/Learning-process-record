/**
 * 显示字符的代码编号
 */
#include <stdio.h>

int main(void) {
    char ch ;

    printf("Please enter a character.\n");

    // 使用 %c 格式符读取字符
    // 注意 scanf 读取字符时不需要 &，因为 ch 已经是一个指针
    // 但为了保持一致性，仍然使用 &ch
    scanf("%c", &ch);
    printf("The code number of '%c' is %d.\n", ch, ch);

    return 0;
}