package where;

import parser.Var;

import static where.Operation.*;

/**
 * CLass Statement
 * <p>
 * Author: Sharipov Samariddin
 * <p>
 * Purpose: Класс для хранения условного выражение из оператора WHERE
 */

public class Statement {
    private final Var left;
    private final Var right;
    private Operation operation;



    Statement(Var left, String operation, Var right) throws IllegalArgumentException {
        boolean us = false;
        for (Operation op : values()) {
            if (operation.equals(op.toString())) {
                this.operation = op;
                us = true;
            }
        }

        if (!us) {
            throw new IllegalArgumentException("unsupported operation in WHERE");
        }
        this.left = left;
        this.right = right;
    }

    /**
     * Purpose: Возвращает условное выражение в MongoDB формате
     */
    public String toMongoDB() {
        return left.toString() + ": {" + operation.toMongoDB() + ": " + right.toString() + "}";
    }
}

