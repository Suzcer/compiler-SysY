; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %b = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4

  %a = alloca i32, align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 0
  %b2 = load i32, i32* %ret, align 4
  %add = add i32 %b2, 1
  %ret3 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 %add
  %b4 = load i32, i32* %ret3, align 4
  store i32 %b4, i32* %a, align 4
  %ret5 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 0
  %b6 = load i32, i32* %ret5, align 4
  %add7 = add i32 %b6, 1
  %ret8 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 %add7
  %b9 = load i32, i32* %ret8, align 4
  ret i32 0
}
