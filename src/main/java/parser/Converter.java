package parser;


import static parser.Token.*;

import operation.FromOperationArgument;
import operation.SelectOperationArguments;
import operation.where.WhereOperationArgument;


import java.util.HashMap;

/**
 * CLass Converter
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для конвертации  SQL выражение в MongoDB выражение
 */

public class Converter {
    private String sqlExpression;
    HashMap<Token, String> mongoDB = new HashMap<>();

    /**
     * Purpose: Создаёт новый объект конвертер из SQL выражения
     *
     * @param sqlExpression - SQL выражение
     */
    public Converter(String sqlExpression) {
        this.sqlExpression = sqlExpression;
    }

    /**
     * Purpose: разделяет выражение на SQL оператор и его аргумент
     *
     * @param sql- SQL выражение
     *
     * @throws IllegalArgumentException - выкидывает ошибку если выражение не соответствует стандарту
     */
    private void addToSQLHashMap(String sql) throws IllegalArgumentException {
        boolean firstRound = true;
        Token preToken = Token.SELECT;
        for (Token token : Token.values()) {
            if (sql.contains(token.toString())) {
                String[] arg = sql.split(token.toString() + " ");
                if (arg.length != 2) {
                    throw new IllegalArgumentException();
                } else if (!firstRound) {
                    addToMongoDBHashMap(preToken, arg[0].trim());
                }
                sql = arg[1];
                firstRound = false;
                preToken = token;
            }
        }

        addToMongoDBHashMap(preToken, sql.trim());
    }

    /**
     * purpose: сопоставляет каждому оператору ее аналог  в MongoDB формате
     * и добавляем в HashMap
     *
     * @param curToken - текущий  SQL оператор
     * @param tokenArgument - аргумент текущего оператора
     * @throws IllegalArgumentException - выкидывает ошибку если выражение не соответствует стандарту
     */
    private void addToMongoDBHashMap(Token curToken, String tokenArgument) throws IllegalArgumentException {
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
     *
     * @return MongoDB выражение в виде строки
     * @throws IllegalArgumentException - выкидовает ошибку если SQL вырадение не соответсвует стандарту
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
