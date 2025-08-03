#include <stdio.h>

void NamePrintf(void);
void LocalSet(void);
void AgeToDay(void);
void jolly(void);
void deny(void);
void br(void);
void IntToes(void);
void PrintSmile(void);
void one_three(void);
void two(void);

int main(void){
    NamePrintf();
    LocalSet();
    AgeToDay();
    jolly();
    jolly();
    jolly();
    deny();
    deny();
    br();
    IntToes();

    for(int i = 3; i > 0; i--){
        for(int j = 0; j < i; j++){
            PrintSmile();
        }
        printf("\n");
    }

    printf("Starting now: \n");
    one_three();
    printf("done!\n");

    return 0;
}

/*
1．编写一个程序，调用一次printf()函数，把你的名和姓打印在一行。
再调用一次printf()函数，把你的名和姓分别打印在两行。
然后，再调用两次printf()函数，把你的名和姓打印在一行。
*/
void NamePrintf(void){
    printf("Gustav Mahler\n");
    printf("Gustav \nMahler\n");
    printf("Gustav\n");
    printf("Mahler\n");
}

/*
2．编写一个程序，打印你的姓名和地址。
*/
void LocalSet(void){
    printf("Gustav, Home information: ******.\n");
}

/*
3．编写一个程序把你的年龄转换成天数，并显示这两个值。这里不用考虑闰年的问题。
*/
void AgeToDay(void){
    int age = 17;
    int AgeDay = age * 365;
    
    printf("I am %d years old, which can be convert to %d days.\n", age, AgeDay);
}

/*
4．编写一个程序，生成以下输出：

For he's a jolly good fellow!
For he's a jolly good fellow!
For he's a jolly good fellow!
Which nobody can deny!

除了main() 函数以外，该程序还要调用两个自定义函数：一个名为jolly() ，用于打印前3条消息，调用一次打印一条；
另一个函数名为deny() ，打印最后一条消息。
*/
void jolly(void){
    printf("For he's a jolly good fellow!\n");
}

void deny(void){
    printf("Which nobody can deny!\n");
}

/*
5．编写一个程序，生成以下输出：

Brazil, Russia, India, China
India, China,
Brazil, Russia

除了main() 以外，该程序还要调用两个自定义函数：一个名为br() ，调用一次打印一次“Brazil, Russia ”；
另一个名为ic() ，调用一次打印一次“India, China ”。其他内容在main() 函数中完成。
*/
void br(void){
    printf("Brazil, Russia.\n");
}
void ic(void){
    printf("India, China.\n");
}

/*
6．编写一个程序，创建一个整型变量toes ，并将toes 设置为10 。
程序中还要计算toes 的两倍和toes 的平方。
该程序应打印3个值，并分别描述以示区分。
*/
void IntToes(void){
    int toes = 10;
    int TwiceToes = toes * 2;
    int PowerToes = toes * toes;

    printf("The toes : %d, the toes multiply of toes : %d, and the toes ^ 2 : %d.\n", toes, TwiceToes, PowerToes);
}

/*
7．许多研究表明，微笑益处多多。编写一个程序，生成以下格式的输出：
Smile!Smile!Smile!
Smile!Smile!
Smile!

该程序要定义一个函数，该函数被调用一次打印一次“Smile! ”，根据程序的需要使用该函数。
*/
void PrintSmile(void){
    printf("Smile!");
}

/*
8．在C语言中，函数可以调用另一个函数。
编写一个程序，调用一个名为one_three() 的函数。
该函数在一行打印单词“one ”，再调用第2个函数two() ，然后在另一行打印单词“three ”。
two() 函数在一行显示单词“two ”。
main() 函数在调用one_three() 函数前要打印短语“starting now: ”，并在调用完毕后显示短语“done! ”。
因此，该程序的输出应如下所示：

starting now:
one
two
three
done!
*/
void one_three(void){
    printf("one\n");
    two();
    printf("three\n");
}

void two(void){
    printf("two\n");
}