; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @a(i32 %0) {
aEntry:
  %i32Array = alloca i32*, align 8
  store i32 %0, i32** %i32Array, align 4
  %ret = getelementptr i32*, i32** %i32Array, i32 0
  %arr = load i32*, i32** %ret, align 8
  ret i32* %arr
}

define i32 @main() {
mainEntry:
  %arr = alloca [3 x i32], align 4
  %pointer = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4
  %pointer2 = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 2
  store i32 0, i32* %pointer2, align 4
  %arr3 = load [3 x i32], [3 x i32]* %arr, align 4
  %name = call i32 @a([3 x i32] %arr3)
  ret i32 %name
}
