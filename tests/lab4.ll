; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>
@b = global i32 1
@c = global i32 2

define i32 @main() {
mainEntry:
  %a = load <3 x i32>, <3 x i32>* @a, align 16
  %b = load i32, i32* @b, align 4
  %GT = icmp sgt <3 x i32> %a, i32 %b
  %compare = zext <3 x i1> %GT to i32
  %0 = icmp ne i32 %compare, 0
  br i1 %0, label %If_true, label %Else

If_true:                                          ; preds = %mainEntry
  br label %Out

Else:                                             ; preds = %mainEntry
  br label %Out

Out:                                              ; preds = %Else, %If_true
  ret i32 0
}
