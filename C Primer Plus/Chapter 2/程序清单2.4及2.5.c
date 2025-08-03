/* nogood.c -- 有错误的程序 */
//由于2.4同2.5分别为对程序错误的指出与修正，故而放置于同一个文件中

#include <stdio.h> 
int main(void){ //应当使用花括号"{}"
    //原声明方式错误，应当使用int n; int n2...或int n, n2...
    /*int n, int n2, int n3;*/

    int n, n2, n3;

    /* 该程序有多处错误*/ //对于"*/"的补足
    n = 5;
    n2 = n * n;

    //原计算方式错误，n3代表n的三次方，应当为n * n * n;
    /*n3 = n2 * n2;*/ 

    n3 = n * n * n;

    printf("n = %d, n squared = %d, n cubed = %d\n", n, n2, n3); //原版本缺少";"

    return 0;
}