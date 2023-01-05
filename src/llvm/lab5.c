int a[3] = { 0,1,1 };
int main() {
    if (a[2]) {
        if(a[1]){
            a[0]=0;
        }else{
            a[0]=1;
        }
    }
    return a[1];
}
