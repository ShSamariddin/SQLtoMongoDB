package where;

import parser.Var;


public class Statement {
    private final Var left;
    private final Var right;
    private final String operation;

    Statement(Var left, String operation, Var right) {
        if (!operation.equals(">") && !operation.equals("<")
                && !operation.equals("=") && !operation.equals("<>")) {
            throw new IllegalArgumentException("unsupported mark in WHERE");
        }
        this.left = left;
        this.right = right;
        this.operation = operation;
    }


    public String toString() {
        switch (operation) {
            case ">":
                return left.toString() + ": {$gt: " + right.toString() + "}";
            case "<":
                return left.toString() + ": {$lt: " + right.toString() + "}";
            case "<>":
                return left.toString() + ": {$ne: " + right.toString() + "}";
            default:
                return left.toString() + ": " + right.toString();
        }
    }
}

