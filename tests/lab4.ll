; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  %a = alloca i32, align 4
  store i32 11, i32* %a, align 4
  %a1 = load i32, i32* %a, align 4
  %icmp = icmp ne i32 %a1, 0
  br i1 %icmp, label %IfBody, label %Else

IfBody:                                           ; preds = %mainEntry
  %b = alloca i32, align 4
  store i32 0, i32* %b, align 4
  %b2 = load i32, i32* %b, align 4
  store i32 %b2, i32* %a, align 4
  br label %IfOut

Else:                                             ; preds = %mainEntry
  br label %IfOut

IfOut:                                            ; preds = %Else, %IfBody
  %a3 = load i32, i32* %a, align 4
  ret i32 %a3
}
