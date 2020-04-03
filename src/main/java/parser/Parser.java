package parser;

import static parser.Token.*;

/**
 * CLass Parser
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: SQL Parser
 */
public class Parser {
    private final String sql;
    private Integer position = 0;
    private Token token = START;
    private String tokenArg;
    private boolean hasOffsetToken = false;
    private boolean hasLimitToken = false;


    public Parser(final String sql) {
        this.sql = sql;
    }


    public boolean hasNextToken() {
        skipWhitespace();
        return position != sql.length();
    }

    private void skipWhitespace() {
        while (position < sql.length() && Character.isWhitespace(sql.charAt(position))) {
            position++;
        }
    }


    /**
     * Purpose: Возвращает условные выражение оператора WHERE
     */
    private String whereArg() {
        skipWhitespace();
        StringBuilder ans = new StringBuilder();
        while (position < sql.length()
                && ((position + 5 > sql.length() || !sql.substring(position, position + 5).equals("LIMIT"))
                && (position + 6 > sql.length() || !sql.substring(position, position + 6).equals("OFFSET")))) {
            ans.append(sql.charAt(position));
            position++;
        }
        return ans.toString();
    }

    /**
     * Purpose: Возвращает аргумент текущего токена
     *
     * @return
     */
    private String argumentParser() {
        StringBuilder ans = new StringBuilder();
        while (true) {
            skipWhitespace();
            while (position < sql.length() && !Character.isWhitespace(sql.charAt(position))
                    && sql.charAt(position) != ',') {
                ans.append(sql.charAt(position));
                position++;
            }
            skipWhitespace();
            if (position == sql.length() || sql.charAt(position) != ',') {
                break;
            } else {
                ans.append(',');
            }
            position++;
        }
        return ans.toString();
    }

    /**
     * Purpose: Проверяет валидность токена
     *
     * @param strToken
     * @param token
     * @throws IllegalArgumentException
     */
    private void check(String strToken, String token) throws IllegalArgumentException {
        if (!strToken.equals(token)) {
            throw new IllegalArgumentException(token);
        } else if (!hasNextToken()) {
            throw new IllegalArgumentException(token + " has no arguments");
        }
    }

    /**
     * Purpose: Возвращает следующий токен и сохраняет его аргумент
     *
     * @throws IllegalArgumentException
     */
    public Token nextToken() throws IllegalArgumentException {
        skipWhitespace();
        switch (token) {
            case START:
                check(argumentParser(), "SELECT");
                tokenArg = argumentParser();
                token = SELECT;
                break;
            case SELECT:
                check(argumentParser(), "FROM");
                tokenArg = argumentParser();
                token = FROM;
                break;
            default:
                String strToken = argumentParser();
                if (strToken.equals("WHERE") && !hasLimitToken && !hasOffsetToken && hasNextToken()) {
                    token = WHERE;
                    tokenArg = whereArg();
                } else if (strToken.equals("LIMIT") && hasNextToken()) {
                    token = LIMIT;
                    tokenArg = argumentParser();
                    hasLimitToken = true;
                } else if (strToken.equals("OFFSET") && hasNextToken()) {
                    token = OFFSET;
                    tokenArg = argumentParser();
                    hasOffsetToken = true;
                } else {
                    throw new IllegalArgumentException();
                }
        }
        return token;
    }

    /**
     * Purpose: Возвращает аргумент текущего токена
     */
    public String tokenArgument() {
        return tokenArg;
    }
}
