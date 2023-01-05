; ModuleID = 'moudle'
source_filename = "moudle"

@a = global <3 x i32> <i32 0, i32 1, i32 1>
@b = global i32 1
@c = global i32 2

define i32 @main() {
mainEntry:
  %a = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 0), align 4
  %b = load i32, i32* @b, align 4
  %GT = icmp sgt i32 %a, %b
  %c = load i32, i32* @c, align 4
  %0 = zext i1 %GT to i32
  %GT1 = icmp sgt i32 %0, %c
  br i1 %GT1, label %If_true, label %Else

If_true:                                          ; preds = %mainEntry
  br label %Out

Else:                                             ; preds = %mainEntry
  br label %Out

Out:                                              ; preds = %Else, %If_true
  %a2 = load i32, i32* getelementptr (<3 x i32>, <3 x i32>* @a, i32 0, i32 1), align 4
  ret i32 %a2
}
