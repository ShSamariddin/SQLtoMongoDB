import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Tree {
    private  Parser lex;
    private ArrayList<String> separatedByComma(String str) throws ParseException {
        StringBuilder arg = new StringBuilder();
        ArrayList<String> argsList = new ArrayList<>();
        for(char c: str.toCharArray()){
            if(c == ','){
                argsList.add(arg.toString());
                arg = new StringBuilder();
            } else{
                arg.append(c);
            }
        }
        if(arg.length() != 0 && !arg.toString().equals("*")){
            argsList.add(arg.toString());
        }
        return argsList;
    }

    private String selectToString(ArrayList<String> argsList){
        StringBuilder ans = new StringBuilder();

        if(argsList.size() != 0) {
            ans.append(", {");
            for (String arg : argsList) {
                ans.append(arg).append(": 1, ");
            }
            ans.deleteCharAt(ans.length() - 1);
            ans.deleteCharAt(ans.length() - 1);
            ans.append("}");
        }
        return ans.toString();
    }

    private String selectT() throws  ParseException{
        if(!lex.hasNextToken()) {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        lex.nextToken();
        String  selectArgs = selectToString(separatedByComma(lex.tokenStr()));
        if(!lex.hasNextToken()) {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        lex.nextToken();
        return fromT(selectArgs);
    }

    private String fromT(String selectArgs){
        StringBuilder ans = new StringBuilder();
        if(!lex.hasNextToken()) {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        lex.nextToken();
        String fromArg = lex.tokenStr();
        ans.append("db.").append(fromArg).append('.');
        if(lex.hasNextToken()) {
            Token curToken = lex.nextToken();
            if (curToken == Token.WHERE) {
                ans.append(whereT(selectArgs));
            } else {
                ans.append("find({}").append(selectArgs).append(").");
                if (curToken == Token.LIMIT) {
                    ans.append(limitT());
                } else {
                    ans.append(skipT());
                }
            }
        } else{
            ans.append("find({}").append(selectArgs).append(")");
        }
        return ans.toString();
    }

    private String whereT(String selectArgs){
        return "";
    }

    private String limitT(){
        StringBuilder ans = new StringBuilder();
        if(lex.hasNextToken()){
            lex.nextToken();
            ans.append("limit(").append(lex.tokenStr()).append(')');
            if(lex.hasNextToken()) {
                lex.nextToken();
                if (lex.curToken() == Token.SKIP) {
                    ans.append('.').append(skipT());
                }
            }
        } else {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        return ans.toString();
    }

    private String skipT(){
        StringBuilder ans = new StringBuilder();
        if(lex.hasNextToken()){
            lex.nextToken();
            ans.append("skip(").append(lex.tokenStr()).append(')');
            if(lex.hasNextToken()) {
                lex.nextToken();
                if (lex.curToken() == Token.LIMIT) {
                    ans.append('.').append(limitT());
                }
            }
        } else {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        return ans.toString();
    }

    public String sol(String str) throws ParseException{
        lex = new Parser(str);
        if(!lex.hasNextToken()) {
            throw  new IllegalArgumentException("Wrong Expression");
        }
        lex.nextToken();
        return selectT();

    }
}
