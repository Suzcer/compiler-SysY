import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.Vocabulary;

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

        SysYLexer sysYLexer = new SysYLexer(input)
//        {
//            @Override
//            public void notifyListeners(LexerNoViableAltException e) {
//                err[0] = true;
//                String text = _input.getText(Interval.of(_tokenStartCharIndex, _input.index()));
//                System.err.println("Error type A at Line " + _tokenStartLine + ": Mysterious character \"" + text + "\".");
//            }
//        }
        ;

        Vocabulary vocabulary = sysYLexer.getVocabulary();

        //Add Error Listener
        sysYLexer.removeErrorListeners();
        VerboseListener listener = new VerboseListener();
        sysYLexer.addErrorListener(listener);

        //Step4: getAllTokens And Output
//        if(!err[0]){
        List<? extends Token> allTokens = sysYLexer.getAllTokens();
        if(!listener.getEntered()){
            for (Token token : allTokens) {
                String symbolicName = vocabulary.getSymbolicName(token.getType());
                String text = token.getText();
                if(text.charAt(0)=='0'&& text.length()>=2){
                    int i;
                    if(text.charAt(1)=='x'||text.charAt(1)=='X'){
                        i = Integer.parseInt(text.substring(2), 16);
                    }else{
                        i = Integer.parseInt(text, 8);
                    }
                    System.err.println(symbolicName+ " " + i+ " at Line " + token.getLine()+".");
                }else{
                    System.err.println(symbolicName + " " + token.getText() + " at Line " + token.getLine()+".");
                }
            }
        }
    }
}
