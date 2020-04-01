package Parser;

public class Parser {
    private final String sql;
    private Integer position = 0;
    private Token token, preToken;
    private boolean hasSkipToken = false;
    private boolean hasWhereToken = false;
    private boolean hasLimitToken = false;
    private String strToken;


    public Parser(final String sql) {
        this.sql = sql;
    }

    private boolean isLegalCharacterForSelect(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '-' || c == '_' || c == '\"' || c == '*' || c == '\'');
    }

    private boolean isLegalCharacterForFrom(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '\"' || c =='\'');
    }

    private boolean isLegalCharacterForSkipOrLimit(char c){
        return (c >= '0' && c <='9');
    }

    private boolean isLegalCharacterForWhere(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                || (c == '\"' || c == '>' || c == '<' || c == '='|| c == '_' || c == '\'');
    }

    private boolean isBlank(char c) {
        return (c == ' ' || c == '\r' || c == '\n' || c == '\t');
    }

    public boolean hasNextToken() {
        return position != sql.length();
    }

    private void skipWhitespace() {
        while (position < sql.length() && isBlank(sql.charAt(position))) {
            position++;
        }
    }

    String newExpression() {
        StringBuilder ans = new StringBuilder();
        skipWhitespace();

        while (position < sql.length() && sql.charAt(position) != ')') {
            ans.append(sql.charAt(position));
            position++;
        }
        return ans.toString();
    }

    private String curStrToken() {
        StringBuilder select = new StringBuilder();
        skipWhitespace();
        while (position < sql.length() && !isBlank(sql.charAt(position))) {
            select.append(sql.charAt(position));
            position++;
        }
        return select.toString();
    }

    private String strArgument(Token preToken) {
        StringBuilder ans = new StringBuilder();

        while (true) {
            skipWhitespace();
            while (position < sql.length() && !isBlank(sql.charAt(position)) && sql.charAt(position) != ',') {
                char c = sql.charAt(position);
                if ((preToken == Token.SELECT && isLegalCharacterForSelect(c))
                        || (preToken == Token.FROM && isLegalCharacterForFrom(c))
                        || ((preToken == Token.LIMIT || preToken == Token.SKIP) && isLegalCharacterForSkipOrLimit(c)) ){
                    ans.append(sql.charAt(position));
                } else {
                    throw new IllegalArgumentException("Illegal character in arguments");
                }
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
        if (token == null) {
            strToken = curStrToken();
            if (!strToken.equals("SELECT")) {
                throw new IllegalArgumentException();
            }
            token = Token.SELECT;
        } else if (token == Token.SELECT) {
            strToken = strArgument(Token.SELECT);
            preToken = Token.SELECT;
            token = Token.ARGUMENT;
        } else if (preToken == Token.SELECT) {
            strToken = curStrToken();
            preToken = Token.ARGUMENT;
            if (!strToken.equals("FROM")) {
                throw new IllegalArgumentException();
            }
            token = Token.FROM;
        } else if (token == Token.FROM) {
            strToken = strArgument(Token.FROM);
            token = Token.ARGUMENT;
            preToken = Token.FROM;
        } else if (token == Token.ARGUMENT) {
            strToken = curStrToken();
            preToken = Token.ARGUMENT;
            if (strToken.equals("WHERE") && !hasLimitToken && !hasSkipToken) {
                token = Token.WHERE;
                hasWhereToken = true;
            } else if (strToken.equals("LIMIT")) {
                token = Token.LIMIT;
                hasLimitToken = true;
            } else if (strToken.equals("SKIP")) {
                token = Token.SKIP;
                hasSkipToken = true;
            }
        } else if (token == Token.WHERE) {
            skipWhitespace();
            StringBuilder ans = new StringBuilder();
            while (position < sql.length()
                    && ((position + 5 > sql.length() || !sql.substring(position, position + 5).equals("LIMIT"))
                    || (position + 4 > sql.length() || !sql.substring(position, position + 4).equals("SKIP")))) {
                if (isBlank(sql.charAt(position)) || isLegalCharacterForWhere(sql.charAt(position))) {
                    ans.append(sql.charAt(position));
                } else {
                    throw  new IllegalArgumentException();
                    }
                position++;
            }
            strToken = ans.toString();
            token = Token.ARGUMENT;
        } else {
            strToken = strArgument(Token.LIMIT);
            token = Token.ARGUMENT;
        }
        return token;
    }

    public Token curToken() {
        return token;
    }

    public Integer curPosition() {
        return position;
    }

    public String tokenStr() {
        return strToken;
    }
}
