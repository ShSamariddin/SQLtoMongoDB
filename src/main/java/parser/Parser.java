package parser;

import static parser.Token.*;

public class Parser {
    private final String sql;
    private Integer position = 0;
    private Token token = START;
    private String tokenArg;
    private boolean hasSkipToken = false;
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


    private String whereArg() {
        skipWhitespace();
        StringBuilder ans = new StringBuilder();
        while (position < sql.length()
                && ((position + 5 > sql.length() || !sql.substring(position, position + 5).equals("LIMIT"))
                && (position + 4 > sql.length() || !sql.substring(position, position + 4).equals("SKIP")))) {
            ans.append(sql.charAt(position));
            position++;
        }
        return ans.toString();
    }


    private String strArgument() {
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

    private void check(String strToken, String token) {
        if (!strToken.equals(token)) {
            throw new IllegalArgumentException(token);
        } else if (!hasNextToken()) {
            throw new IllegalArgumentException(token + " has no arguments");
        }
    }

    public Token nextToken() {
        skipWhitespace();
        switch (token) {
            case START:
                check(strArgument(), "SELECT");
                tokenArg = strArgument();
                token = SELECT;
                break;
            case SELECT:
                check(strArgument(), "FROM");
                tokenArg = strArgument();
                token = FROM;
                break;
            default:
                String strToken = strArgument();
                if (strToken.equals("WHERE") && !hasLimitToken && !hasSkipToken && hasNextToken()) {
                    token = WHERE;
                    tokenArg = whereArg();
                } else if (strToken.equals("LIMIT") && hasNextToken()) {
                    token = LIMIT;
                    tokenArg = strArgument();
                    hasLimitToken = true;
                } else if (strToken.equals("SKIP") && hasNextToken()) {
                    token = SKIP;
                    tokenArg = strArgument();
                    hasSkipToken = true;
                } else {
                    throw new IllegalArgumentException();
                }
        }
        return token;
    }

    public String tokenArgument() {
        return tokenArg;
    }
}
