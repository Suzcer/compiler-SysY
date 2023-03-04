include Makefile.git

export CLASSPATH=/usr/local/lib/antlr-*-complete.jar

DOMAINNAME = 47.122.3.40:3000
ANTLR = java -jar /usr/local/lib/antlr-*-complete.jar -listener -visitor -long-messages
JAVAC = javac -g
JAVA = java


PFILE = $(shell find . -name "SysYParser.g4")
LFILE = $(shell find . -name "SysYLexer.g4")
JAVAFILE = $(shell find . -name "*.java")
ANTLRPATH = $(shell find /usr/local/lib -name "antlr-*-complete.jar")

compile: antlr
	$(call git_commit,"make")
	mkdir -p classes
	#$(JAVAC) $(JAVAFILE) -d classes
	$(JAVAC) -classpath $(ANTLRPATH) $(JAVAFILE) -d classes
	
run: compile
	java -classpath ./classes:$(ANTLRPATH) Main $(FILEPATH)


antlr: $(LFILE) $(PFILE) 
	$(ANTLR) $(PFILE) $(LFILE)


test: compile
	$(call git_commit, "test")
	nohup java -classpath ./classes:$(ANTLRPATH) Main ./tests/test1.sysy &


clean:
	rm -f src/*.tokens
	rm -f src/*.interp
	rm -rf target
	#rm -f src/SysYLexer.java src/SysYParser.java src/SysYParserBaseListener.java src/SysYParserBaseVisitor.java src/SysYParserListener.java src/SysYParserVisitor.java
	rm -rf classes

lab5-std:
	clang -S -emit-llvm src/llvm/lab5.c -o src/llvm/lab5.ll
	llvm-as src/llvm/lab5.ll -o src/llvm/lab5.bc
	lli src/llvm/lab5.bc

lab5-mine:
	llvm-as tests/lab4.ll -o tests/lab4.bc
	lli tests/lab4.bc

submit: clean
	git gc
	#bash -c "$$(curl -s $(DOMAINNAME)/scripts/submit-v2.sh)"
	bash submit.sh

.PHONY: compile antlr test run clean submit


