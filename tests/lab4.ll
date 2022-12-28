; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @f(i32 %0) {
fEntry:
  %i = alloca i32, align 4
  store i32 %0, i32* %i, align 4
  %i1 = load i32, i32* %i, align 4
  ret i32 %i1
}

define i32 @main() {
mainEntry:
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 8, i32* %pointer, align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  %a1 = load i32, i32* %ret, align 4
  %returnValue = call i32 @f(i32 %a1)
  ret i32 %returnValue
}

define i32 @fukk() {
fukkEntry:
  ret void
}
