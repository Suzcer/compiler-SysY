; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca <5 x i32>, align 32
  %pointer = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 1
  store i32 3, i32* %pointer1, align 4
  %pointer2 = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 2
  store i32 2, i32* %pointer2, align 4
  %pointer3 = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 3
  store i32 0, i32* %pointer3, align 4
  %pointer4 = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 4
  store i32 0, i32* %pointer4, align 4
  %ret = getelementptr <5 x i32>, <5 x i32>* %a, i32 0, i32 0
  %a5 = load i32, i32* %ret, align 4
  ret i32 %a5
}
