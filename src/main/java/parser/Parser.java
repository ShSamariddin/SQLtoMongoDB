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

    private boolean isBlank(char c) {
        return (c == ' ' || c == '\r' || c == '\n' || c == '\t');
    }

    public boolean hasNextToken() {
        skipWhitespace();
        return position != sql.length();
    }

    private void skipWhitespace() {
        while (position < sql.length() && isBlank(sql.charAt(position))) {
            position++;
        }
    }


    private  String whereArg(){
        skipWhitespace();
        StringBuilder ans = new StringBuilder();
        while (position < sql.length()
                && ((position + 5 > sql.length() || !sql.substring(position, position + 5).equals("LIMIT"))
                || (position + 4 > sql.length() || !sql.substring(position, position + 4).equals("SKIP")))) {
                ans.append(sql.charAt(position));
            position++;
        }
        return ans.toString();
    }

    private String strArgument() {
        StringBuilder ans = new StringBuilder();
        while (true) {
            skipWhitespace();
            while (position < sql.length() && !isBlank(sql.charAt(position))
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

    public Token nextToken() {
        skipWhitespace();
        switch (token) {
            case START:
                String strToken = strArgument();
//                String[] splitPart = sql.split("SELECT", 2);
                if (!strToken.equals("SELECT")) {
                    throw new IllegalArgumentException("");
                } else if (!hasNextToken()) {
                    throw new IllegalArgumentException("SELECT has no arguments");
                }
                tokenArg = strArgument();
                token = SELECT;
                break;
            case SELECT:
                strToken = strArgument();
                if (!strToken.equals("FROM")) {
                    throw new IllegalArgumentException();
                } else if (!hasNextToken()) {
                    throw new IllegalArgumentException("FROM has no argument");
                }
                tokenArg = strArgument();
                token = FROM;
                break;
            default:
                strToken = strArgument();
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

    public Token curToken() {
        return token;
    }

    public Integer curPosition() {
        return position;
    }

    public String tokenArgument() {
        return tokenArg;
    }
}
