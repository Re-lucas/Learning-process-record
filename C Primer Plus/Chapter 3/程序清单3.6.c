/**
 * altnames.c 可以移植整数类型名
 */

#include <stdio.h>
#include <inttypes.h> // 引入 inttypes.h 以使用可移植的整数类型

int main(void) {
    // me32 是一个32位整数类型的别名
    // me64 是一个64位整数类型的别名


    int32_t me32; // 32位整数
    int64_t me64; // 64位整数

    me32 = 2147483647; // 最大32位整数
    me64 = 9223372036854775807; // 最大64位整数

    printf("First, auuume int32_t is int:");
    printf(me32 = "%d\n", me32);
    printf("Next, let's not make any assumptions.\n");
    printf("Instead, use a \"macro\" from inttypes.h:");
    printf("me32 = %" "d" "\n", me32);

    return 0;

}