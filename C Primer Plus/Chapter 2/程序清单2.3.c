//一个文件包含两个函数
#include <stdio.h>

void bulter(void); //定义一个函数原型

int main(void){

    printf("I will summon the bulter function.\n");
    bulter();
    printf("Yes, bring me some tea and writeable");
    
    return 0;
}

void bulter(void)/*函数定义的开始*/
{
    printf("You rang sir? \n");
}