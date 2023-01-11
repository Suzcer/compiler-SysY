; ModuleID = 'moudle'
source_filename = "moudle"

define i32 @main() {
mainEntry:
  br label %whileCond

whileCond:                                        ; preds = %whileBody, %mainEntry
  br i1 true, label %whileBody, label %whileExit

whileBody:                                        ; preds = %whileCond
  br label %whileCond

whileExit:                                        ; preds = %whileCond
  ret i32 0
}
