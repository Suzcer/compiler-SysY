#include<stdio.h>
int g(int w){
    const int a[2]={-2};
    return a[0] * w ;
}

int main(){
    int q[3]={-1,-1};
    const int d = 4;
    q[1] = d;
    printf("num: %d",q[2] + g(q[1]) * 4 );
    return 5;
}
