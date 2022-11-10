import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;

import java.io.IOException;
import java.util.List;

public class Main
{
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("input path is required");
        }
        String source = args[0];
        CharStream input = CharStreams.fromFileName(source);
        final boolean[] err = {false};

        SysYLexer sysYLexer = new SysYLexer(input){
            @Override
            public void notifyListeners(LexerNoViableAltException e) {
                err[0] = true;
                String text = _input.getText(Interval.of(_tokenStartCharIndex, _input.index()));
                System.err.println("Error type A at Line " + _tokenStartLine + ": Mysterious character \"" + text + "\".");
            }
        };

        Vocabulary vocabulary = sysYLexer.getVocabulary();

        //Add Error Listener
//        sysYLexer.removeErrorListeners();
//        VerboseListener listener = new VerboseListener();
//        sysYLexer.addErrorListener(listener);

        //Step4: getAllTokens And Output
        if(!err[0]){
            List<? extends Token> allTokens = sysYLexer.getAllTokens();
            for (Token token : allTokens) {
                String symbolicName = vocabulary.getSymbolicName(token.getType());
                System.err.println(symbolicName + " " + token.getText() + " at Line " + token.getLine()+".");
            }
        }
    }
}
