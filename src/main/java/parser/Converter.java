import parser.*;

import static parser.Token.*;

import where.WhereOperationArgument;


import java.util.HashMap;

/**
 * CLass Converter
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для конвертации  SQL выражение в MongoDB выражение
 */

public class Converter {
   // private Parser lex;
    private String sqlExpression;
    HashMap<Token, String> mongoDB = new HashMap<>();
    HashMap<Token, String> sqlMap = new HashMap<>();

    /**
     * Purpose: Создаёт новый объект конвертер из SQL выражения
     *
     * @param sqlExpression
     */
    public Converter(String sqlExpression) {
        this.sqlExpression = sqlExpression;
    }

    /**
     * purpose: сопоставляет каждому оператору ее аналог  в MongoDB формате
     * и добавляем в HashMap
     * <p>
     * Returns: none
     */
    private void addToSQLHashMap(String sql){
        boolean firstRound = true;
        Token preToken = Token.SELECT;
        for(Token token: Token.values()){
            if(sql.contains(token.toString())){
                String[] arg = sql.split(token.toString() + " ");
                if(arg.length != 2){
                    throw  new IllegalArgumentException();
                } else if(!firstRound){
                    addToMongoDBHashMap(preToken, removeSpaces(arg[0]));
                }
                sql = arg[1];
                firstRound = false;
                preToken = token;
            }
        }

        addToMongoDBHashMap(preToken, removeSpaces(sql));
    }

    private String removeSpaces(String str){
        StringBuilder sql = new StringBuilder(str);
        while(sql.length() > 0 && sql.charAt(0) == ' '){
            sql.deleteCharAt(0);
        }

        while (sql.length() > 0 && sql.charAt(sql.length() - 1) == ' '){
            sql.deleteCharAt(sql.length() - 1);
        }
        return sql.toString();
    }

    private void addToMongoDBHashMap(Token curToken, String tokenArgument ) {
        tokenArgument = tokenArgument.trim();
        switch (curToken) {
            case SELECT:
                SelectOperationArguments select = new SelectOperationArguments(tokenArgument);
                mongoDB.put(curToken, select.toMongoDB());
                break;
            case FROM:
                FromOperationArgument from = new FromOperationArgument(tokenArgument);
                mongoDB.put(curToken, from.toString());
                break;
            case WHERE:
                WhereOperationArgument where = new WhereOperationArgument(tokenArgument);
                mongoDB.put(curToken, where.toMongoDB());
                break;
            case LIMIT:
                Integer.parseInt(tokenArgument);
                mongoDB.put(curToken, ".limit(" + tokenArgument + ")");
                break;
            case OFFSET:
                Integer.parseInt(tokenArgument);
                mongoDB.put(curToken, ".skip(" + tokenArgument + ')');
                break;
        }
    }

    /**
     * purpose: Конвертирует выражение из SQL в MonogoDB
     * <p>
     * Returns: MongoDB выражение в виде строки
     */

    public String convertToMongoDB() throws IllegalArgumentException {
        if (sqlExpression.length() == 0) {
            throw new IllegalArgumentException("Input is empty");
        }
        for (Token token : Token.values()) {
            mongoDB.put(token, "");
        }
        mongoDB.put(WHERE, "{}");
        addToSQLHashMap(sqlExpression);
        if (mongoDB.get(FROM).equals("")) {
            throw new IllegalArgumentException("ops FROM missing");
        }
        return "db." + mongoDB.get(FROM) + ".find(" + mongoDB.get(WHERE) + mongoDB.get(SELECT) + ')' + mongoDB.get(OFFSET) + mongoDB.get(LIMIT);
    }
}
