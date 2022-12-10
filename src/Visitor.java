import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;

public class Visitor extends SysYParserBaseVisitor<Void>{

    public String[] ruleNames;

    public Vocabulary vocabulary;

    public HashMap<String,String> mp;

    public void setRulesName(String[] ruleNames){
        this.ruleNames=ruleNames;
    }

    public void setVocabulary(Vocabulary vocabulary){
        this.vocabulary=vocabulary;
    }

    public void setMp(HashMap<String,String> mp){
        this.mp=mp;
    }


    public static String captureName(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }
        //实现语法树的打印
    @Override
    public Void visitChildren(RuleNode node) {
        String ruleName = ruleNames[node.getRuleContext().getRuleIndex()];
        for(int i=0;i+1<node.getRuleContext().depth();i++) System.err.print("  ");
        System.err.println(captureName(ruleName));
        return super.visitChildren(node);
    }

    public String baseTrans(String text){
        if(text.charAt(0)=='0'&& text.length()>=2){
            int i;
            if(text.charAt(1)=='x'||text.charAt(1)=='X'){
                i=Integer.parseInt(text.substring(2),16 );
            }else {
                i=Integer.parseInt(text,8);
            }
            return String.valueOf(i);
        }else{
            return text;
        }
    }
    //打印终结符及终结符的高亮
    @Override
    public Void visitTerminal(TerminalNode node) {

        String type = vocabulary.getSymbolicName(node.getSymbol().getType());
        if(mp.containsKey(type)){
            RuleNode parent;
            if(node.getParent() instanceof RuleNode){
                parent = (RuleNode) node.getParent();
                for(int i=0;i<parent.getRuleContext().depth();i++)
                    System.err.print("  ");
            }

            System.err.print(baseTrans(node.getText())+" ");
            System.err.print(type);
            System.err.println("["+mp.get(type)+"]");
        }

        return super.visitTerminal(node);
    }
}
