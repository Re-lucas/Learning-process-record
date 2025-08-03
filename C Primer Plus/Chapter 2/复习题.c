//1．C语言的基本模块是什么？
//它们都叫作函数（function）。

//2．什么是语法错误？写出一个英语例子和C语言例子。
/*
语法错误违反了组成语句或程序的规则。
这是一个有语法错误的英文例子："Me speak English good．"。
这是一个有语法错误的C语言例子：printf"Where are the parentheses?"; 。
*/

//3．什么是语义错误？写出一个英语例子和C语言例子。
/*
语义错误是指含义错误。
这是一个有语义错误的英文例子：This sentence is excellent Czech．。
这是一个有语义错误的C语言例子：thrice_n = 3 + n; 。
*/

/*
4．Indiana Sloth编写了下面的程序，并征求你的意见。请帮助他评定。

include studio.h
int main{void} / * 该程序打印一年有多少周 / *
(
    int s

    s := 56;
    print(There are s weeks in a year.);
    return 0;

//上述为题型，下述为更改后的内容：

#include <stdio.h>

int main(void){
    
    int s;
    s = 56;
    printf("There are %d weeks in a year", s);

    return 0;
}
*/

/*
5．假设下面的4个例子都是完整程序中的一部分，它们都输出什么结果？

a. printf("Baa Baa Black Sheep.");
   printf("Have you any wool?\n");
b. printf("Begone!\nO creature of lard!\n");
c. printf("What?\nNo/nfish?\n");
d. int num;
   num = 2;
   printf("%d + %d = %d", num, num, num + num);

//输出内容：
Baa Baa Black Sheep.Have you any wool?
Begone!
O creature of lard!
What?
No/nfish
2 + 2 = 4
*/

/*
6．在main 、int 、function 、char 、= 中，哪些是C语言的关键字？
int, char 是关键字，main是函数名，function是函数的意思，=是运算符。
*/

/*
7．如何以下面的格式输出变量words 和lines 的值（这里，3020 和350 代表两个变量的值）？
输出内容：There were 3020 words and 350 lines.
输出此行内容的代码应为：printf("There were %d words and %d lines.", words, lines);
*/

/*
8．考虑下面的程序：

#include <stdio.h>
int main(void)
{
     int a, b;

     a = 5;
     b = 2; //第七行
     b = a; //第八行
     a = b; //第九行
     printf("%d %d\n", b, a);

     return 0;
}
    请问，在执行完第7、第8、第9行后，程序的状态分别是什么？

    执行第七行时候，b被赋值为2，第八行b被复制为5，第九行a被赋值为上一行的b的数值，也就是5。

    输出内容应该为：5 5
*/

/*
9．考虑下面的程序：

#include <stdio.h>
int main(void)
{
     int x, y;

     x = 10;
     y = 5;      //第七行
     y = x + y;  //第八行
     x = x*y;    //第九行
     printf("%d %d\n", x, y);
     return 0;
}
请问，在执行完第7、第8、第9行后，程序的状态分别是什么？

执行第七行时候y被赋值为5，第八行时y被赋值为15，第九行时x被赋值为150。

输出内容应该为：150 15
*/