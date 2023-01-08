; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <2 x i32> <i32 1, i32 0>
@c = global i32 1

define i32 @main() {
mainEntry:
  %b = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4
  %a = load i32, i32* getelementptr (<2 x i32>, <2 x i32>* @a, i32 0, i32 0), align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* @a, i32 0, i32 %a
  %a2 = load i32, i32* %ret, align 4
  %ret3 = getelementptr <2 x i32>, <2 x i32>* %b, i32 0, i32 1
  store i32 %a2, i32* %ret3, align 4
  %a4 = load i32, i32* getelementptr (<2 x i32>, <2 x i32>* @a, i32 0, i32 0), align 4
  ret i32 %a4
}
