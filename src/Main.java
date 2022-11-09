import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
//        SysYLexer sysYLexer = new SysYLexer(input);
//
//        //Add Error Listener
//        sysYLexer.removeErrorListeners();
//        sysYLexer.addErrorListener(new VerboseListener());
//
//        //Step4: getAllTokens And Output
//        List<? extends Token> allTokens = sysYLexer.getAllTokens();
//        for (Token token : allTokens) {
//            System.out.println(token.getType() + " " + token.getText() + " at Line " + token.getLine());
//        }
    }
}
