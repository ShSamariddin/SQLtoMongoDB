package where.arguments;

import Parser.Var;

import java.util.ArrayList;

public class ToleranceRange {
    private ArrayList<Statement> argumentList = new ArrayList<>();

    public ToleranceRange(String row) {
        StringBuilder statement = new StringBuilder();
        ArrayList<String> arg = new ArrayList<String>();
        for (char c : row.toCharArray()) {
            if (!isBlank(c)) {
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
        } else{
            throw  new IllegalArgumentException("Not Enough argument in WHERE");
        }
    }

    public String toString(){
        StringBuilder ans = new StringBuilder();
        ans.append('{');
        for(Statement arg: argumentList){
            ans.append(arg.toString());
            ans.append(", ");
        }
        ans.deleteCharAt(ans.length() - 1);
        ans.deleteCharAt(ans.length() - 1);
        ans.append('}');
        return ans.toString();
    }

    private boolean isBlank(char c) {
        return (c == ' ' || c == '\r' || c == '\n' || c == '\t');
    }
}