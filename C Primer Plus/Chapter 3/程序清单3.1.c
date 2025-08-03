/*体重与相同重量白金价值的互换*/
#include <stdio.h>
int main(void){
    float weight;
    float value;

    //通过读取用户输入的浮点数并存储
    printf("Please enter your weight in pounds :");
    scanf("%f", &weight); //& 符号为地址符号

    //假设白金价格为每盎司$1700，同时转换磅到盎司
    value = 1700.0 * weight * 14.5833;

    printf("Your weight in platinum is worth $ %.2f.\n", value);//%.2f 强制输出后两位
    printf("You are easily worth that! If platinum prices drop. \n");
    printf("eat more to maintain your valur.\n");

}