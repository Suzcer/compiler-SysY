import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class VerboseListener extends BaseErrorListener {

    private boolean entered=false;

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line, int charPositionInLine,
                            String msg,
                            RecognitionException e)
    {
//        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
//        Collections.reverse(stack);
//        System.err.println("rule stack: "+stack);
//        System.err.println("line "+line+":"+charPositionInLine+" at "+
//                offendingSymbol+": "+msg);
        CommonToken token = (CommonToken) offendingSymbol;
        entered=true;
        System.err.println("Error type A at Line "+line+":Mysterious character \""+token.getText()+"\".");
    }

    public boolean getEntered(){
        return entered;
    }
}
