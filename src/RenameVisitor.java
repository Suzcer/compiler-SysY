import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import symtable.GlobalScope;
import symtable.Scope;

import java.util.HashMap;

public class RenameVisitor<T> extends SysYParserBaseVisitor<T> {


    private GlobalScope globalScope = null;

    private Scope currentScope = null;

    private int localScopeCounter = 0;

    /**
     * 以下属性均与重命名相关
     **/
    private int lineNo;

    private int column;

    private String rename;

    private String[] ruleNames;

    private Vocabulary vocabulary;

    private HashMap<String, String> mp;


    public void setRulesName(String[] ruleNames) {
        this.ruleNames = ruleNames;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    public void setMp(HashMap<String, String> mp) {
        this.mp = mp;
    }

    public void setArgs(int lineNo, int column, String rename) {
        this.lineNo = lineNo;
        this.column = column;
        this.rename = rename;
    }

    private static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    private String baseTrans(String text) {
        if (text.charAt(0) == '0' && text.length() >= 2) {
            int i;
            if (text.charAt(1) == 'x' || text.charAt(1) == 'X') {
                i = Integer.parseInt(text.substring(2), 16);
            } else {
                i = Integer.parseInt(text, 8);
            }
            return String.valueOf(i);
        } else {
            return text;
        }
    }

    //实现语法树的打印
    @Override
    public T visitChildren(RuleNode node) {

        String ruleName = ruleNames[node.getRuleContext().getRuleIndex()];
        for (int i = 0; i + 1 < node.getRuleContext().depth(); i++) System.err.print("  ");
        System.err.println(captureName(ruleName));

        return super.visitChildren(node);
    }

    //打印终结符及终结符的高亮
    @Override
    public T visitTerminal(TerminalNode node) {
        String type = vocabulary.getSymbolicName(node.getSymbol().getType());
        if (mp.containsKey(type)) {
            RuleNode parent;
            if (node.getParent() instanceof RuleNode) {
                parent = (RuleNode) node.getParent();
                for (int i = 0; i < parent.getRuleContext().depth(); i++)
                    System.err.print("  ");
            }

            String oriName = baseTrans(node.getText());
            String str = currentScope.findScope(node.getText()) + "." + oriName;

            System.err.print(oriName + " ");

            System.err.print(type);
            System.err.println("[" + mp.get(type) + "]");
        }
        return super.visitTerminal(node);
    }
}