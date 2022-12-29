; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @f(i32 %0) {
fEntry:
  %i = alloca i32, align 4
  store i32 %0, i32* %i, align 4
  %i1 = load i32, i32* %i, align 4
  ret i32 %i1
  %b = alloca i32, align 4
  store i32 9, i32* %b, align 4
  %q = alloca i32, align 4
  store i32 9, i32* %q, align 4
}

define i32 @main() {
mainEntry:
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 8, i32* %pointer, align 4
  %b = load i32, i32* %b, align 4
  %q = load i32, i32* %q, align 4
  %0 = add i32 %b, %q
  %returnValue = call i32 @f(i32 %0)
  ret i32 %returnValue
}
