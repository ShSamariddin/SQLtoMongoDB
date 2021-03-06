package operation.where;

import parser.Var;

import java.util.ArrayList;

/**
 * CLass FromOperationArgument
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения  условных выражении  оператора WHERE
 */

public class WhereOperationArgument {
    private final ArrayList<Statement> argumentList = new ArrayList<>();


    /**
     * Purpose: Создает объект условного выражения из sql строки
     *
     * @param row - строка содержащая  аргументы оператора WHERE
     * @throws IllegalArgumentException - выкидывает  если условии записано в неправильном формате
     */
    public WhereOperationArgument(String row) throws IllegalArgumentException {
        StringBuilder statement = new StringBuilder();
        ArrayList<String> arg = new ArrayList<>();
        for (char c : row.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                statement.append(c);
            } else {
                if (statement.length() != 0) {
                    arg.add(statement.toString());
                }
                if (arg.size() == 4) {
                    if (arg.get(3).equals("AND")) {
                        argumentList.add(new Statement(new Var(arg.get(0)), arg.get(1), new Var(arg.get(2))));
                    } else {
                        throw new IllegalArgumentException();
                    }
                    arg.clear();
                }
                statement = new StringBuilder();
            }

        }
        if (statement.length() != 0) {
            arg.add(statement.toString());
        }
        if (arg.size() == 3) {
            argumentList.add(new Statement(new Var(arg.get(0)), arg.get(1), new Var(arg.get(2))));

            arg.clear();
        } else {
            throw new IllegalArgumentException("Not Enough argument in WHERE");
        }
    }


    /**
     * Purpose: переводит условные sql выражение в MongoDB формат
     *
     * @return условные MongoDB выражение
     */
    public String toMongoDB() {
        StringBuilder ans = new StringBuilder();
        ans.append('{');
        for (Statement arg : argumentList) {
            ans.append(arg.toMongoDB());
            ans.append(", ");
        }
        ans.deleteCharAt(ans.length() - 1);
        ans.deleteCharAt(ans.length() - 1);
        ans.append('}');
        return ans.toString();
    }


}
