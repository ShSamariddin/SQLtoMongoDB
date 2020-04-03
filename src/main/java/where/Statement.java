package where;

import parser.Var;

import static where.Operation.*;


public class Statement {
    private final Var left;
    private final Var right;
    private Operation operation;

    Statement(Var left, String operation, Var right) {
        boolean us = false;
        for(Operation op: values()){
            if(operation.equals(op.toString())){
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


    public String toString() {
        switch (operation) {
            case GREAT_THAN:
                return left.toString() + ": {$gt: " + right.toString() + "}";
            case LESS_THAN:
                return left.toString() + ": {$lt: " + right.toString() + "}";
            case NOT_EQUAL:
                return left.toString() + ": {$ne: " + right.toString() + "}";
            default:
                return left.toString() + ": " + right.toString();
        }
    }
}

