; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @g(i32 %0) {
gEntry:
  %w = alloca i32, align 4
  store i32 %0, i32* %w, align 4
  %a = alloca <2 x i32>, align 8
  %pointer = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 0
  store i32 -2, i32* %pointer, align 4
  %pointer1 = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  store i32 0, i32* %pointer1, align 4
  %ret = getelementptr <2 x i32>, <2 x i32>* %a, i32 0, i32 1
  %a2 = load i32, i32* %ret, align 4
  %w3 = load i32, i32* %w, align 4
  %1 = mul i32 %a2, %w3
  ret i32 %1
}

define i32 @main() {
mainEntry:
  %q = alloca <3 x i32>, align 16
  %pointer = getelementptr <3 x i32>, <3 x i32>* %q, i32 0, i32 0
  store i32 -1, i32* %pointer, align 4
  %pointer1 = getelementptr <3 x i32>, <3 x i32>* %q, i32 0, i32 1
  store i32 -1, i32* %pointer1, align 4
  %pointer2 = getelementptr <3 x i32>, <3 x i32>* %q, i32 0, i32 2
  store i32 0, i32* %pointer2, align 4
  %ret = getelementptr <3 x i32>, <3 x i32>* %q, i32 0, i32 2
  %q3 = load i32, i32* %ret, align 4
  %ret4 = getelementptr <3 x i32>, <3 x i32>* %q, i32 0, i32 1
  %q5 = load i32, i32* %ret4, align 4
  %returnValue = call i32 @g(i32 %q5)
  %0 = add i32 %q3, %returnValue
  ret i32 %0
}
