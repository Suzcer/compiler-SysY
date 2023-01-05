int a[3] = { 0,1,1 };
int main() {
    if (a[0]) {
        a[1] = 2;
    }else{
        a[1] = 3;
    }
    return a[1];
}
