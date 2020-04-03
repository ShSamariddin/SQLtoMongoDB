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
    private Parser lex;
    private String sqlExpression;
    HashMap<Token, String> ma = new HashMap<>();

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
    private void addToHashMap() {
        while (lex.hasNextToken()) {
            Token curToken = lex.nextToken();
            String tokenArgument = lex.tokenArgument();
            switch (curToken) {
                case SELECT:
                    SelectOperationArguments select = new SelectOperationArguments(tokenArgument);
                    ma.put(curToken, select.toMongoDB());
                    break;
                case FROM:
                    FromOperationArgument from = new FromOperationArgument(tokenArgument);
                    ma.put(curToken, from.toString());
                    break;
                case WHERE:
                    WhereOperationArgument where = new WhereOperationArgument(tokenArgument);
                    ma.put(curToken, where.toMongoDB());
                    break;
                case LIMIT:
                    Integer.parseInt(tokenArgument);
                    ma.put(curToken, ".limit(" + tokenArgument + ")");
                    break;
                case OFFSET:
                    Integer.parseInt(tokenArgument);
                    ma.put(curToken, ".skip(" + tokenArgument + ')');
                    break;
            }
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
            ma.put(token, "");
        }
        ma.put(WHERE, "{}");
        lex = new Parser(sqlExpression);
        addToHashMap();
        if (ma.get(FROM).equals("")) {
            throw new IllegalArgumentException("ops FROM missing");
        }
        return "db." + ma.get(FROM) + ".find(" + ma.get(WHERE) + ma.get(SELECT) + ')' + ma.get(OFFSET) + ma.get(LIMIT);
    }
}
