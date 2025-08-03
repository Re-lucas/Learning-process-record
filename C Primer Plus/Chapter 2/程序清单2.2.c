//单位的转换

#include <stdio.h>

int main(void){
    int feet, fathoms;

    fathoms = 2;
    feet = 6 * fathoms;

    printf("There are %d of feet in %d fathoms.\n", feet, fathoms);
    printf("Yes, I said there are %d of feet.", feet);

    return 0;
}