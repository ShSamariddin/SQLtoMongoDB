import parser.*;
import where.WhereOperationArgument;


import java.util.HashMap;

public class Converter {
    private  Parser lex;
    HashMap<Token, String> ma = new HashMap<>();

    private void addToHashMap(){
        while (lex.hasNextToken()) {
            Token curToken = lex.nextToken();
            String tokenArgument = lex.tokenArgument();
            switch (curToken) {
                case SELECT:
                    SelectOperationArguments select = new SelectOperationArguments(tokenArgument);
                    ma.put(curToken, select.toString());
                    break;
                case FROM:
                    FromOperationArgument from = new FromOperationArgument(tokenArgument);
                    ma.put(curToken, from.toString());
                    break;
                case WHERE:
                    WhereOperationArgument where = new WhereOperationArgument(tokenArgument);
                    ma.put(curToken, where.toString());
                    break;
                case LIMIT:
                    Integer.parseInt(tokenArgument);
                    ma.put(curToken, ".limit(" + tokenArgument + ")");
                    break;
                case SKIP:
                    Integer.parseInt(tokenArgument);
                    ma.put(curToken, ".skip(" + tokenArgument + ')');
                    break;
            }
        }
    }

    public String convertToMongoDB(String str) throws IllegalArgumentException {
        if(str.length() == 0){
            throw new IllegalArgumentException("Input is empty");
        }
        for (Token token : Token.values()) {
            ma.put(token, "");
        }
        ma.put(Token.WHERE, "{}");
        lex = new Parser(str);
        addToHashMap();
        if(ma.get(Token.FROM).equals("")){
            throw  new IllegalArgumentException("ops FROM missing");
        }
        return "db." + ma.get(Token.FROM) + ".find(" + ma.get(Token.WHERE) + ma.get(Token.SELECT) + ')' + ma.get(Token.SKIP) + ma.get(Token.LIMIT);
    }
}
