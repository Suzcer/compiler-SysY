import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ParserListener extends BaseErrorListener {

    boolean entered=false;

    boolean getEntered(){
        return entered;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {

//        entered=true;
//        System.err.println("Error type B at Line "+line);

    }
}
