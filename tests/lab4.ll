; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @f(i32 %0) {
fEntry:
  %r = alloca i32, align 4
  store i32 %0, i32* %r, align 4
  ret i32 0
}

define i32 @a(i32* %0) {
aEntry:
  %i32Array = alloca i32*, align 8
  store i32* %0, i32** %i32Array, align 8
  %arr = load i32*, i32** %i32Array, align 8
  %lval = getelementptr i32, i32* %arr, i32 0
  store i32 1, i32* %lval, align 4
  %arr1 = load i32*, i32** %i32Array, align 8
  %arr2 = getelementptr i32, i32* %arr1, i32 1
  %b = load i32, i32* %arr2, align 4
  %name = call i32 @f(i32 %b)
  ret i32 %name
}

define i32 @g(i32* %0) {
gEntry:
  %i32Array = alloca i32*, align 8
  store i32* %0, i32** %i32Array, align 8
  %arr = alloca [3 x i32], align 4
  %pointer = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 0
  store i32 1, i32* %pointer, align 4
  %pointer1 = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 1
  store i32 2, i32* %pointer1, align 4
  %pointer2 = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 2
  store i32 0, i32* %pointer2, align 4
  %arr3 = getelementptr [3 x i32], [3 x i32]* %arr, i32 0, i32 0
  %name = call i32 @a(i32* %arr3)
  %arrPtr = load i32*, i32** %i32Array, align 8
  %name4 = call i32 @a(i32* %arrPtr)
  ret i32 1
}

define i32 @main() {
mainEntry:
  ret i32 0
}
