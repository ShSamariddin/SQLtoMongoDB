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
     * Purpose: Создает новый объект из входной строки  если она валидная
     *
     * @param var - название колонки, таблицы или  переменные
     *
     * иначе выкидывает ошибку
     * @throws IllegalArgumentException - выдает ошибку если название колонки,таблицы или переменные
     * из условии WHERE не соответствует формату
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
     * @param var - аргумент  который надо проверит на валидность
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
     * @param c - символ которое проверяется на валидность
     */
    private boolean isValidSymbol(char c) {
        return (c == '\'' || c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9'));
    }

    public String toString() {
        return arg;
    }
}
