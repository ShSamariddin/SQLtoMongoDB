package operation;

import parser.Var;

/**
 * CLass FromOperationArgument
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения  аргумента оператора FROM
 */
public class FromOperationArgument extends OperationArguments {
    Var arg;

    /**
     * Purpose:  Удаляет обвертку из аргумента если она есть, проверяет на валидность
     * все символы и создаёт новый объект хранящий аргумент операции FROM
     *
     * @param str - строка содержащее  название таблицы
     * @throws IllegalArgumentException - выкидывает когда название таблицы не соответствует стандарту
     */
    public FromOperationArgument(String str) throws IllegalArgumentException {
        this.arg = new Var(removeWrapper(str));
    }

    public String toString() {
        return arg.toString();
    }
}
