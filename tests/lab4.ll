; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 0, i32* %a, align 4
  %count = alloca i32, align 4
  store i32 0, i32* %count, align 4
  br label %whileCond

whileCond:                                        ; preds = %Out, %mainEntry
  %a1 = load i32, i32* %a, align 4
  %LE = icmp sle i32 %a1, 0
  %compare = zext i1 %LE to i32
  %0 = icmp ne i32 %compare, 0
  br i1 %0, label %whileBody, label %exit

whileBody:                                        ; preds = %whileCond
  %a2 = load i32, i32* %a, align 4
  %1 = sub i32 %a2, 1
  store i32 %1, i32* %a, align 4
  %count3 = load i32, i32* %count, align 4
  %2 = add i32 %count3, 1
  store i32 %2, i32* %count, align 4
  %a4 = load i32, i32* %a, align 4
  %LT = icmp slt i32 %a4, -20
  %compare5 = zext i1 %LT to i32
  %3 = icmp ne i32 %compare5, 0
  br i1 %3, label %If_true, label %Else

exit:                                             ; preds = %If_true, %whileCond
  %count6 = load i32, i32* %count, align 4
  ret i32 %count6

If_true:                                          ; preds = %whileBody
  br label %exit
  br label %Out

Else:                                             ; preds = %whileBody
  br label %Out

Out:                                              ; preds = %Else, %If_true
  br label %whileCond
}
