package parser;

/**
 * CLass Var
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения переменных
 */

public class Var {
    private final String arg;

    /**
     * Purpose: Проверка аргумента на валидность
     *
     * @param var
     * @return создает новый объект из входной строки  если она валидная
     * иначе выкидывает ошибку
     * @throws IllegalArgumentException
     */
    public Var(String var) {
        if (!isValidVariable(var)) {
            throw new IllegalArgumentException("unsupported characters in variables");
        }
        this.arg = var;
    }

    /**
     * Purpose: Проверка аргумента на валидность
     *
     * @param var
     */
    private boolean isValidVariable(String var) {
        for (char c : var.toCharArray()) {
            if (!isValidSymbol(c))
                return false;
        }
        return true;
    }


    /**
     * Purpose: Проверка символа на валидность
     *
     * @param c
     */
    private boolean isValidSymbol(char c) {
        return (c == '\'' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    public String toString() {
        return arg;
    }
}
