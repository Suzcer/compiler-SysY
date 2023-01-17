; ModuleID = 'moudle'
source_filename = "moudle"

@arr = global [3 x i32] [i32 1, i32 2, i32 0]

define i32 @a(i32* %0) {
aEntry:
  %i32Array = alloca i32*, align 8
  store i32* %0, i32** %i32Array, align 8
  %arr = load i32*, i32** %i32Array, align 8
  %arr1 = getelementptr i32, i32* %arr, i32 0
  %arr2 = load i32, i32* %arr1, align 4
  ret i32 %arr2
}

define i32 @main() {
mainEntry:
  %name = call i32 @a(i32* getelementptr inbounds ([3 x i32], [3 x i32]* @arr, i32 0, i32 0))
  ret i32 %name
}
