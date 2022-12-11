import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);

        SysYLexer sysYLexer = new SysYLexer(input);

        /*
            以下是lab2中语法分析器的内容。
         */
        CommonTokenStream tokens = new CommonTokenStream(sysYLexer);
        SysYParser sysYParser = new SysYParser(tokens);

        sysYParser.removeErrorListeners();
        ParserListener listener = new ParserListener();
        sysYParser.addErrorListener(listener);

        HashMap<String,String> mp=new HashMap<>();
        mp.put("CONST","orange");
        mp.put("INT","orange");
        mp.put("VOID","orange");
        mp.put("IF","orange");
        mp.put("ELSE","orange");
        mp.put("WHILE","orange");
        mp.put("BREAK","orange");
        mp.put("CONTINUE","orange");
        mp.put("RETURN","orange");
        mp.put("PLUS","blue");
        mp.put("MINUS","blue");
        mp.put("MUL","blue");
        mp.put("DIV","blue");
        mp.put("MOD","blue");
        mp.put("ASSIGN","blue");
        mp.put("EQ","blue");
        mp.put("NEQ","blue");
        mp.put("LT","blue");
        mp.put("GT","blue");
        mp.put("LE","blue");
        mp.put("GE","blue");
        mp.put("NOT","blue");
        mp.put("AND","blue");
        mp.put("OR","blue");
        mp.put("IDENT","red");
        mp.put("INTEGR_CONST","green");

        ParseTree tree = sysYParser.program();
//        String[] ruleNames = sysYParser.getRuleNames();
//        Vocabulary vocabulary = sysYParser.getVocabulary();
//        Visitor visitor = new Visitor();
//        visitor.setRulesName(ruleNames);
//        visitor.setVocabulary(vocabulary);
//        visitor.setMp(mp);

//        int lineNo = Integer.parseInt(args[1]);
//        int column = Integer.parseInt(args[2]);
//        String name = args[3];
        ParseTreeWalker walker = new ParseTreeWalker();
        SymbolTableListener symtableListener = new SymbolTableListener();
        walker.walk(symtableListener,tree);
//        if(!listener.getEntered()){
//            visitor.visit(tree);
//        }

    }
}
